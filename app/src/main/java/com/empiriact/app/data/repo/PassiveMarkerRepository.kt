package com.empiriact.app.data.repo

import com.empiriact.app.data.db.PassiveMarkerDao
import com.empiriact.app.data.db.PassiveMarkerHourlyEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class PassiveMarkerRepository(
    private val dao: PassiveMarkerDao
) {
    fun observeDay(date: LocalDate): Flow<List<PassiveMarkerHourlyEntity>> =
        dao.observeDay(date.toYyyyMmDdInt())

    fun observeRange(startDate: LocalDate, endDateExclusive: LocalDate): Flow<List<PassiveMarkerHourlyEntity>> =
        dao.observeRange(startDate.toYyyyMmDdInt(), endDateExclusive.toYyyyMmDdInt())

    suspend fun upsertHour(
        date: LocalDate,
        hour: Int,
        stepCount: Int? = null,
        sleepDurationMinutesPreviousNight: Int? = null,
        screenTimeMinutesInHour: Int? = null
    ) {
        dao.upsert(
            PassiveMarkerHourlyEntity(
                key = "${date.toYyyyMmDdInt()}-$hour",
                localDate = date.toYyyyMmDdInt(),
                hour = hour,
                stepCount = stepCount,
                sleepDurationMinutesPreviousNight = sleepDurationMinutesPreviousNight,
                screenTimeMinutesInHour = screenTimeMinutesInHour,
                updatedAtEpochMs = System.currentTimeMillis()
            )
        )
    }
}

private fun LocalDate.toYyyyMmDdInt(): Int =
    (year * 10000) + (monthValue * 100) + dayOfMonth
