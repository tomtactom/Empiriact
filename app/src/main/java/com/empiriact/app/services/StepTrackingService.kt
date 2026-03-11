package com.empiriact.app.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.repo.PassiveMarkerRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.time.temporal.ChronoUnit
import java.time.ZonedDateTime
import kotlin.coroutines.resume
import kotlin.math.roundToLong

class StepTrackingService(
    private val settingsRepository: SettingsRepository,
    private val passiveMarkerRepository: PassiveMarkerRepository,
    private val stepCounterSource: StepCounterSource,
    private val hourlyStepHistorySource: HourlyStepHistorySource = NoOpHourlyStepHistorySource()
) {
    companion object {
        private const val MAX_HOURLY_BACKFILL_HOURS = 24
    }


    suspend fun captureHourlySnapshot(now: ZonedDateTime = ZonedDateTime.now()): Boolean {
        val optIn = settingsRepository.passiveMarkersOptInEnabled()
        val stepsEnabled = settingsRepository.passiveStepsCollectionEnabled()
        if (!optIn || !stepsEnabled) {
            settingsRepository.clearPassiveStepsReadError()
            settingsRepository.clearPassiveStepsTrackingState()
            return false
        }

        val currentHour = now.withMinute(0).withSecond(0).withNano(0)

        val previousTotalSteps = settingsRepository.getPassiveStepsLastCounterTotal()
        val previousHour = settingsRepository.getPassiveStepsLastCounterHour()

        if (previousHour != null && currentHour.isAfter(previousHour)) {
            val historyStart = previousHour.plusHours(1)
            val historyEnd = currentHour.plusHours(1)

            when (val historyResult = hourlyStepHistorySource.readHourlySteps(historyStart, historyEnd)) {
                is HourlyStepHistoryReadResult.Success -> {
                    if (historyResult.buckets.isNotEmpty()) {
                        settingsRepository.clearPassiveStepsReadError()
                        settingsRepository.setPassiveStepsBaselineHourPending(false)
                        historyResult.buckets.forEach { bucket ->
                            val bucketHour = bucket.start.withMinute(0).withSecond(0).withNano(0)
                            if (!bucketHour.isBefore(historyStart) && bucketHour.isBefore(historyEnd)) {
                                passiveMarkerRepository.upsertHour(
                                    date = bucketHour.toLocalDate(),
                                    hour = bucketHour.hour,
                                    stepCount = bucket.stepCount,
                                    isEstimated = bucket.isEstimated
                                )
                            }
                        }
                    } else {
                        settingsRepository.setPassiveStepsReadError(
                            reason = SettingsRepository.PassiveStepsReadErrorReason.SOURCE_EMPTY,
                            capturedAt = now
                        )
                        return captureFromLiveDelta(previousHour, previousTotalSteps, currentHour, now)
                    }
                }

                HourlyStepHistoryReadResult.PermissionMissing -> {
                    settingsRepository.setPassiveStepsEnabled(false)
                    settingsRepository.clearPassiveStepsTrackingState()
                    settingsRepository.setPassiveStepsReadError(
                        reason = SettingsRepository.PassiveStepsReadErrorReason.PERMISSION_MISSING,
                        capturedAt = now
                    )
                    return false
                }

                HourlyStepHistoryReadResult.SourceEmpty -> {
                    settingsRepository.setPassiveStepsReadError(
                        reason = SettingsRepository.PassiveStepsReadErrorReason.SOURCE_EMPTY,
                        capturedAt = now
                    )
                    return captureFromLiveDelta(previousHour, previousTotalSteps, currentHour, now)
                }

                HourlyStepHistoryReadResult.SyncError -> {
                    settingsRepository.setPassiveStepsReadError(
                        reason = SettingsRepository.PassiveStepsReadErrorReason.SYNC_FAILED,
                        capturedAt = now
                    )
                    return captureFromLiveDelta(previousHour, previousTotalSteps, currentHour, now)
                }
            }
        }

        val liveRead = stepCounterSource.readCurrentTotalSteps()
        if (liveRead is StepReadResult.Success) {
            settingsRepository.setPassiveStepsLastSnapshot(
                totalSteps = liveRead.total,
                hour = currentHour
            )
        }

        if (previousTotalSteps == null || previousHour == null) {
            if (liveRead is StepReadResult.Success) {
                settingsRepository.setPassiveStepsBaselineHourPending(true)
                settingsRepository.clearPassiveStepsReadError()
                return true
            }
            return handleLiveReadFailure(now, liveRead)
        }

        return true
    }

    private suspend fun captureFromLiveDelta(
        previousHour: ZonedDateTime?,
        previousTotalSteps: Long?,
        currentHour: ZonedDateTime,
        now: ZonedDateTime
    ): Boolean {
        if (!stepCounterSource.hasRequiredPermission()) {
            settingsRepository.setPassiveStepsEnabled(false)
            settingsRepository.clearPassiveStepsTrackingState()
            settingsRepository.setPassiveStepsReadError(
                reason = SettingsRepository.PassiveStepsReadErrorReason.PERMISSION_MISSING,
                capturedAt = now
            )
            return false
        }

        if (!stepCounterSource.isSensorAvailable()) {
            settingsRepository.setPassiveStepsEnabled(false)
            settingsRepository.clearPassiveStepsTrackingState()
            settingsRepository.setPassiveStepsReadError(
                reason = SettingsRepository.PassiveStepsReadErrorReason.SENSOR_UNAVAILABLE,
                capturedAt = now
            )
            return false
        }

        val currentTotalSteps = when (val readResult = stepCounterSource.readCurrentTotalSteps()) {
            is StepReadResult.Success -> readResult.total
            else -> return handleLiveReadFailure(now, readResult)
        }

        if (previousTotalSteps == null || previousHour == null) {
            settingsRepository.setPassiveStepsLastSnapshot(
                totalSteps = currentTotalSteps,
                hour = currentHour
            )
            settingsRepository.setPassiveStepsBaselineHourPending(true)
            return true
        }

        if (currentHour.isAfter(previousHour)) {
            val delta = (currentTotalSteps - previousTotalSteps)
                .coerceAtLeast(0)
                .coerceAtMost(Int.MAX_VALUE.toLong())
                .toInt()

            val missingHours = ChronoUnit.HOURS.between(previousHour, currentHour)
                .coerceAtLeast(0)
                .coerceAtMost(Int.MAX_VALUE.toLong())
                .toInt()

            if (missingHours > 0) {
                settingsRepository.setPassiveStepsBaselineHourPending(false)

                val hoursToDistribute = missingHours.coerceAtMost(MAX_HOURLY_BACKFILL_HOURS)
                val baseDistribution = delta / hoursToDistribute
                val remainder = delta % hoursToDistribute
                val startsWithTruncatedGap = missingHours > MAX_HOURLY_BACKFILL_HOURS
                val firstHourToFill = if (startsWithTruncatedGap) {
                    currentHour.minusHours(hoursToDistribute.toLong()).plusHours(1)
                } else {
                    previousHour.plusHours(1)
                }
                val isBackfillEstimate = missingHours > 1

                repeat(hoursToDistribute) { index ->
                    val hourToFill = firstHourToFill.plusHours(index.toLong())
                    val distributedSteps = baseDistribution + if (index >= hoursToDistribute - remainder) 1 else 0

                    passiveMarkerRepository.upsertHour(
                        date = hourToFill.toLocalDate(),
                        hour = hourToFill.hour,
                        stepCount = distributedSteps,
                        isEstimated = isBackfillEstimate
                    )
                }
            }
        }

        val shouldUpdateSnapshot = when {
            previousHour == null || previousTotalSteps == null -> true
            currentHour.isAfter(previousHour) -> true
            currentHour.isEqual(previousHour) -> currentTotalSteps != previousTotalSteps
            else -> true
        }

        if (shouldUpdateSnapshot) {
            settingsRepository.setPassiveStepsLastSnapshot(totalSteps = currentTotalSteps, hour = currentHour)
        }
        settingsRepository.clearPassiveStepsReadError()
        return true
    }

    private suspend fun handleLiveReadFailure(now: ZonedDateTime, readResult: StepReadResult): Boolean {
        when (readResult) {
            StepReadResult.Timeout -> {
                settingsRepository.setPassiveStepsReadError(
                    reason = SettingsRepository.PassiveStepsReadErrorReason.TIMEOUT,
                    capturedAt = now
                )
            }

            StepReadResult.SensorUnavailable -> {
                settingsRepository.setPassiveStepsEnabled(false)
                settingsRepository.clearPassiveStepsTrackingState()
                settingsRepository.setPassiveStepsReadError(
                    reason = SettingsRepository.PassiveStepsReadErrorReason.SENSOR_UNAVAILABLE,
                    capturedAt = now
                )
            }

            StepReadResult.PermissionMissing -> {
                settingsRepository.setPassiveStepsEnabled(false)
                settingsRepository.clearPassiveStepsTrackingState()
                settingsRepository.setPassiveStepsReadError(
                    reason = SettingsRepository.PassiveStepsReadErrorReason.PERMISSION_MISSING,
                    capturedAt = now
                )
            }

            is StepReadResult.Success -> {
                settingsRepository.clearPassiveStepsReadError()
                return true
            }
        }
        return false
    }
}

sealed class StepReadResult {
    data class Success(val total: Long) : StepReadResult()
    data object Timeout : StepReadResult()
    data object SensorUnavailable : StepReadResult()
    data object PermissionMissing : StepReadResult()
}

interface StepCounterSource {
    suspend fun readCurrentTotalSteps(): StepReadResult

    fun isSensorAvailable(): Boolean = true

    fun hasRequiredPermission(): Boolean = true
}

class SensorStepCounterSource(
    private val context: Context
) : StepCounterSource {

    override fun hasRequiredPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun isSensorAvailable(): Boolean {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        return sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null
    }

    override suspend fun readCurrentTotalSteps(): StepReadResult {
        if (!hasRequiredPermission()) {
            return StepReadResult.PermissionMissing
        }

        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
            ?: return StepReadResult.SensorUnavailable
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            ?: return StepReadResult.SensorUnavailable

        val reading = withTimeoutOrNull(3_000) {
            suspendCancellableCoroutine { continuation ->
                val listener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent) {
                        val value = event.values.firstOrNull()?.roundToLong()
                        sensorManager.unregisterListener(this)
                        if (continuation.isActive) {
                            continuation.resume(value?.let { StepReadResult.Success(it) } ?: StepReadResult.Timeout)
                        }
                    }

                    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
                }

                val registered = sensorManager.registerListener(
                    listener,
                    stepSensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )

                if (!registered) {
                    sensorManager.unregisterListener(listener)
                    if (continuation.isActive) {
                        continuation.resume(StepReadResult.SensorUnavailable)
                    }
                    return@suspendCancellableCoroutine
                }

                continuation.invokeOnCancellation {
                    sensorManager.unregisterListener(listener)
                }
            }
        }

        return reading ?: StepReadResult.Timeout
    }
}
