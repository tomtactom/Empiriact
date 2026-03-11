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
    private val stepCounterSource: StepCounterSource
) {

    suspend fun captureHourlySnapshot(now: ZonedDateTime = ZonedDateTime.now()): Boolean {
        val optIn = settingsRepository.passiveMarkersOptInEnabled()
        val stepsEnabled = settingsRepository.passiveStepsCollectionEnabled()
        if (!optIn || !stepsEnabled) {
            settingsRepository.clearPassiveStepsReadError()
            settingsRepository.clearPassiveStepsTrackingState()
            return false
        }

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
            is StepReadResult.Success -> {
                settingsRepository.clearPassiveStepsReadError()
                readResult.total
            }
            StepReadResult.Timeout -> {
                settingsRepository.setPassiveStepsReadError(
                    reason = SettingsRepository.PassiveStepsReadErrorReason.TIMEOUT,
                    capturedAt = now
                )
                return false
            }
            StepReadResult.SensorUnavailable -> {
                settingsRepository.setPassiveStepsEnabled(false)
                settingsRepository.clearPassiveStepsTrackingState()
                settingsRepository.setPassiveStepsReadError(
                    reason = SettingsRepository.PassiveStepsReadErrorReason.SENSOR_UNAVAILABLE,
                    capturedAt = now
                )
                return false
            }
            StepReadResult.PermissionMissing -> {
                settingsRepository.setPassiveStepsEnabled(false)
                settingsRepository.clearPassiveStepsTrackingState()
                settingsRepository.setPassiveStepsReadError(
                    reason = SettingsRepository.PassiveStepsReadErrorReason.PERMISSION_MISSING,
                    capturedAt = now
                )
                return false
            }
        }
        val currentHour = now.withMinute(0).withSecond(0).withNano(0)

        val previousTotalSteps = settingsRepository.getPassiveStepsLastCounterTotal()
        val previousHour = settingsRepository.getPassiveStepsLastCounterHour()

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

                if (missingHours > 3) {
                    passiveMarkerRepository.upsertHour(
                        date = currentHour.toLocalDate(),
                        hour = currentHour.hour,
                        stepCount = delta,
                        isEstimated = true
                    )
                } else {
                    val baseDistribution = delta / missingHours
                    val remainder = delta % missingHours
                    val isBackfillEstimate = missingHours > 1

                    repeat(missingHours) { index ->
                        val hourToFill = previousHour.plusHours(index.toLong() + 1)
                        val distributedSteps = baseDistribution + if (index >= missingHours - remainder) 1 else 0

                        passiveMarkerRepository.upsertHour(
                            date = hourToFill.toLocalDate(),
                            hour = hourToFill.hour,
                            stepCount = distributedSteps,
                            isEstimated = isBackfillEstimate
                        )
                    }
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
            settingsRepository.setPassiveStepsLastSnapshot(
                totalSteps = currentTotalSteps,
                hour = currentHour
            )
        }
        return true
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
