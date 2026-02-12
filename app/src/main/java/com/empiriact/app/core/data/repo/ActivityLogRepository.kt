package com.empiriact.app.data.repo

import com.empiriact.app.data.db.ActivityLogDao
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.ui.screens.overview.ActivityAnalysis
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class ActivityLogRepository(
    private val dao: ActivityLogDao,
    private val zoneId: ZoneId = ZoneId.systemDefault()
) {

    fun getAll(): Flow<List<ActivityLogEntity>> =
        dao.observeRange(0, 99999999)

    fun observeDay(date: LocalDate): Flow<List<ActivityLogEntity>> =
        dao.observeDay(date.toYyyyMmDdInt())

    fun observeDayAvgValence(date: LocalDate): Flow<Double?> =
        dao.observeDayAvgValence(date.toYyyyMmDdInt())

    fun getLogsForDay(startDate: LocalDate, endDate: LocalDate): Flow<List<ActivityLogEntity>> =
        dao.getLogsForDay(startDate.toYyyyMmDdInt(), endDate.toYyyyMmDdInt())

    fun getLogsForWeek(startDate: LocalDate, endDate: LocalDate): Flow<List<ActivityLogEntity>> =
        dao.getLogsForWeek(startDate.toYyyyMmDdInt(), endDate.toYyyyMmDdInt())

    fun getLogsForMonth(yearMonth: YearMonth): Flow<List<ActivityLogEntity>> {
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth().plusDays(1)
        return dao.getLogsForMonth(startDate.toYyyyMmDdInt(), endDate.toYyyyMmDdInt())
    }

    suspend fun upsert(date: LocalDate, hour: Int, text: String, valence: Int) {
        val key = "${date.toYyyyMmDdInt()}-$hour"
        dao.upsert(
            ActivityLogEntity(
                key = key,
                localDate = date.toYyyyMmDdInt(),
                hour = hour,
                activityText = text.trim(),
                valence = valence.coerceIn(-2, 2),
                updatedAtEpochMs = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteDay(date: LocalDate) {
        dao.deleteDay(date.toYyyyMmDdInt())
    }

    fun getUniqueActivities(): Flow<List<String>> =
        dao.getUniqueActivities()

    fun getMoodBoosters(threshold: Int, minRating: Double): Flow<List<ActivityAnalysis>> =
        dao.getMoodBoosters(threshold, minRating)

    fun getMoodDampers(threshold: Int, maxRating: Double): Flow<List<ActivityAnalysis>> =
        dao.getMoodDampers(threshold, maxRating)
}

private fun LocalDate.toYyyyMmDdInt(): Int =
    (year * 10000) + (monthValue * 100) + dayOfMonth
