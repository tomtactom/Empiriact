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
            settingsRepository.clearPassiveStepsTrackingState()
            return false
        }

        if (!stepCounterSource.hasRequiredPermission()) {
            settingsRepository.setPassiveStepsEnabled(false)
            settingsRepository.clearPassiveStepsTrackingState()
            return false
        }

        val currentTotalSteps = stepCounterSource.readCurrentTotalSteps() ?: return false
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
                val baseDistribution = delta / missingHours
                val remainder = delta % missingHours

                repeat(missingHours) { index ->
                    val hourToFill = previousHour.plusHours(index.toLong() + 1)
                    val distributedSteps = baseDistribution + if (index >= missingHours - remainder) 1 else 0

                    passiveMarkerRepository.upsertHour(
                        date = hourToFill.toLocalDate(),
                        hour = hourToFill.hour,
                        stepCount = distributedSteps
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
            settingsRepository.setPassiveStepsLastSnapshot(
                totalSteps = currentTotalSteps,
                hour = currentHour
            )
        }
        return true
    }
}

interface StepCounterSource {
    suspend fun readCurrentTotalSteps(): Long?

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

    override suspend fun readCurrentTotalSteps(): Long? {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager ?: return null
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) ?: return null

        return withTimeoutOrNull(3_000) {
            suspendCancellableCoroutine { continuation ->
                val listener = object : SensorEventListener {
                    override fun onSensorChanged(event: SensorEvent) {
                        val value = event.values.firstOrNull()?.roundToLong()
                        sensorManager.unregisterListener(this)
                        if (continuation.isActive) {
                            continuation.resume(value)
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
                        continuation.resume(null)
                    }
                    return@suspendCancellableCoroutine
                }

                continuation.invokeOnCancellation {
                    sensorManager.unregisterListener(listener)
                }
            }
        }
    }
}
