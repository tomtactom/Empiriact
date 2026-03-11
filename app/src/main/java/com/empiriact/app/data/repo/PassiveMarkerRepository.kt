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
        val dateInt = date.toYyyyMmDdInt()
        val existing = dao.getByDateHour(dateInt, hour)
        dao.upsert(
            PassiveMarkerHourlyEntity(
                key = existing?.key ?: "$dateInt-$hour",
                localDate = dateInt,
                hour = hour,
                stepCount = stepCount ?: existing?.stepCount,
                sleepDurationMinutesPreviousNight = sleepDurationMinutesPreviousNight
                    ?: existing?.sleepDurationMinutesPreviousNight,
                screenTimeMinutesInHour = screenTimeMinutesInHour ?: existing?.screenTimeMinutesInHour,
                updatedAtEpochMs = System.currentTimeMillis()
            )
        )
    }
}

private fun LocalDate.toYyyyMmDdInt(): Int =
    (year * 10000) + (monthValue * 100) + dayOfMonth
