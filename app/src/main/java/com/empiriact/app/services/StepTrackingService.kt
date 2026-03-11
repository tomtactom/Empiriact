package com.empiriact.app.services

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.repo.PassiveMarkerRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
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

        val currentTotalSteps = stepCounterSource.readCurrentTotalSteps() ?: return false
        val currentHour = now.withMinute(0).withSecond(0).withNano(0)

        val previousTotalSteps = settingsRepository.getPassiveStepsLastCounterTotal()
        val previousHour = settingsRepository.getPassiveStepsLastCounterHour()

        if (previousTotalSteps != null && previousHour != null && currentHour.isAfter(previousHour)) {
            val delta = (currentTotalSteps - previousTotalSteps)
                .coerceAtLeast(0)
                .coerceAtMost(Int.MAX_VALUE.toLong())
                .toInt()

            val completedHour = currentHour.minusHours(1)
            passiveMarkerRepository.upsertHour(
                date = completedHour.toLocalDate(),
                hour = completedHour.hour,
                stepCount = delta
            )
        }

        settingsRepository.setPassiveStepsLastSnapshot(
            totalSteps = currentTotalSteps,
            hour = currentHour
        )
        return true
    }
}

interface StepCounterSource {
    suspend fun readCurrentTotalSteps(): Long?
}

class SensorStepCounterSource(
    private val context: Context
) : StepCounterSource {

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
