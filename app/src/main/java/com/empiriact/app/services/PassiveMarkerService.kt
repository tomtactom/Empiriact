package com.empiriact.app.services

import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.data.repo.PassiveMarkerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Passive Marker werden ausschließlich lokal verarbeitet und nur als transparenter Kontext
 * für aktiv erfasste Check-ins genutzt.
 */
class PassiveMarkerService(
    private val settingsRepository: SettingsRepository,
    private val passiveMarkerRepository: PassiveMarkerRepository
) {

    fun contextForCheckins(checkins: List<ActivityLogEntity>): Flow<Map<String, PassiveMarkerContext>> {
        val localDate = checkins.firstOrNull()?.localDate?.toLocalDate() ?: return flowOf(emptyMap())

        return combine(
            passiveMarkerRepository.observeDay(localDate),
            settingsRepository.passiveMarkersOptIn,
            settingsRepository.passiveStepsEnabled,
            settingsRepository.passiveSleepEnabled,
            settingsRepository.passiveScreenTimeProximityEnabled
        ) { passiveByHour, optIn, stepsEnabled, sleepEnabled, screenEnabled ->
            if (!optIn) return@combine emptyMap()

            val passiveMap = passiveByHour.associateBy { it.hour }
            checkins.associate { checkin ->
                val passiveHour = passiveMap[checkin.hour]
                checkin.key to PassiveMarkerContext(
                    checkinKey = checkin.key,
                    readings = buildReadingsForCheckin(
                        stepCount = passiveHour?.stepCount,
                        sleepDurationMinutesPreviousNight = passiveHour?.sleepDurationMinutesPreviousNight,
                        screenTimeMinutesInHour = passiveHour?.screenTimeMinutesInHour,
                        includeSteps = stepsEnabled,
                        includeSleep = sleepEnabled,
                        includeScreenTime = screenEnabled
                    ),
                    explanation = EXPLANATION_TEXT
                )
            }
        }
    }

    fun passiveVsActiveDailyComparison(checkins: List<ActivityLogEntity>): Flow<List<PassiveVsActiveDataPoint>> {
        val localDate = checkins.firstOrNull()?.localDate?.toLocalDate() ?: return flowOf(emptyList())

        return combine(
            passiveMarkerRepository.observeDay(localDate),
            settingsRepository.passiveMarkersOptIn,
            settingsRepository.passiveStepsEnabled,
            settingsRepository.passiveSleepEnabled,
            settingsRepository.passiveScreenTimeProximityEnabled
        ) { passiveByHour, optIn, stepsEnabled, sleepEnabled, screenEnabled ->
            if (!optIn || checkins.isEmpty()) return@combine emptyList()

            val point = PassiveVsActiveDataPoint(
                date = localDate,
                valenceAverage = checkins.map { it.valence }.average().toFloat(),
                steps = if (stepsEnabled) passiveByHour.sumOf { it.stepCount ?: 0 } else null,
                sleepDurationHours = if (sleepEnabled) {
                    passiveByHour.mapNotNull { it.sleepDurationMinutesPreviousNight }
                        .distinct()
                        .firstOrNull()
                        ?.let { it / 60f }
                } else {
                    null
                },
                screenTimeMinutes = if (screenEnabled) passiveByHour.sumOf { it.screenTimeMinutesInHour ?: 0 } else null
            )
            listOf(point)
        }
    }

    suspend fun recordHourlyStepCount(date: LocalDate, hour: Int, stepCount: Int) {
        passiveMarkerRepository.upsertHour(date = date, hour = hour, stepCount = stepCount.coerceAtLeast(0))
    }

    suspend fun recordHourlyScreenTime(date: LocalDate, hour: Int, screenMinutes: Int) {
        passiveMarkerRepository.upsertHour(date = date, hour = hour, screenTimeMinutesInHour = screenMinutes.coerceIn(0, 60))
    }

    suspend fun recordSleepForDay(date: LocalDate, sleepMinutesPreviousNight: Int) {
        // Schlafdauer betrifft die vorherige Nacht und wird deshalb für alle Stunden des Tages gespiegelt.
        val normalized = sleepMinutesPreviousNight.coerceIn(0, 24 * 60)
        repeat(24) { hour ->
            passiveMarkerRepository.upsertHour(
                date = date,
                hour = hour,
                sleepDurationMinutesPreviousNight = normalized
            )
        }
    }

    private fun buildReadingsForCheckin(
        stepCount: Int?,
        sleepDurationMinutesPreviousNight: Int?,
        screenTimeMinutesInHour: Int?,
        includeSteps: Boolean,
        includeSleep: Boolean,
        includeScreenTime: Boolean
    ): List<PassiveMarkerReading> {
        val readings = mutableListOf<PassiveMarkerReading>()

        if (includeSteps && stepCount != null) {
            readings += PassiveMarkerReading(
                source = PassiveMarkerSource.STEPS,
                label = "Schrittzahl (pro Stunde, lokal)",
                valueText = "$stepCount Schritte"
            )
        }

        if (includeSleep && sleepDurationMinutesPreviousNight != null) {
            readings += PassiveMarkerReading(
                source = PassiveMarkerSource.SLEEP_DURATION,
                label = "Schlafdauer letzte Nacht (lokal)",
                valueText = "%.1f h".format(sleepDurationMinutesPreviousNight / 60f)
            )
        }

        if (includeScreenTime && screenTimeMinutesInHour != null) {
            readings += PassiveMarkerReading(
                source = PassiveMarkerSource.SCREEN_TIME_PROXIMITY,
                label = "Screen-Time in dieser Stunde (lokal)",
                valueText = "$screenTimeMinutesInHour Min"
            )
        }

        return readings
    }

    private fun Int.toLocalDate(): LocalDate =
        LocalDate.parse(toString(), DateTimeFormatter.BASIC_ISO_DATE)

    private companion object {
        const val EXPLANATION_TEXT =
            "Passive Marker dienen nur als Kontext für deine aktiven Einträge – sie treffen keine Diagnose. " +
                "Du kannst jede Quelle einzeln ein- oder ausschalten. Verarbeitung erfolgt lokal auf dem Gerät. " +
                "Für Schrittzahl ist zusätzlich die Android-Berechtigung zur Aktivitätserkennung nötig; " +
                "bei Entzug wird das Schritt-Tracking automatisch gestoppt."
    }
}

enum class PassiveMarkerSource {
    STEPS,
    SLEEP_DURATION,
    SCREEN_TIME_PROXIMITY
}

data class PassiveMarkerReading(
    val source: PassiveMarkerSource,
    val label: String,
    val valueText: String
)

data class PassiveMarkerContext(
    val checkinKey: String,
    val readings: List<PassiveMarkerReading>,
    val explanation: String
)

data class PassiveVsActiveDataPoint(
    val date: LocalDate,
    val valenceAverage: Float,
    val steps: Int?,
    val sleepDurationHours: Float?,
    val screenTimeMinutes: Int?
)
