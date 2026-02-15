package com.empiriact.app.data.repo

import com.empiriact.app.data.ExerciseAverageRating
import com.empiriact.app.data.db.ActivityLogDao
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.data.db.ExerciseRatingDao
import com.empiriact.app.data.db.ExerciseRatingEntity
import com.empiriact.app.data.db.GratitudeDao
import com.empiriact.app.data.db.GratitudeEntity
import com.empiriact.app.ui.screens.overview.ActivityAnalysis
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryWriteReadCycleTest {

    @Test
    fun `activity log repository writes trimmed and clamped values and reads day cycle`() = runTest {
        val dao = FakeActivityLogDao()
        val repository = ActivityLogRepository(dao)
        val date = LocalDate.of(2026, 2, 3)

        repository.upsert(date, hour = 14, text = "  Spaziergang  ", valence = 9)

        val stored = repository.observeDay(date).first()
        assertEquals(1, stored.size)
        assertEquals("Spaziergang", stored.first().activityText)
        assertEquals(2, stored.first().valence)
    }

    @Test
    fun `activity log repository reads month interval correctly`() = runTest {
        val dao = FakeActivityLogDao()
        val repository = ActivityLogRepository(dao)

        repository.upsert(LocalDate.of(2026, 2, 1), 9, "A", 1)
        repository.upsert(LocalDate.of(2026, 2, 20), 9, "B", -1)
        repository.upsert(LocalDate.of(2026, 3, 1), 9, "C", 1)

        val februaryLogs = repository.getLogsForMonth(YearMonth.of(2026, 2)).first()
        assertEquals(2, februaryLogs.size)
        assertEquals(setOf("A", "B"), februaryLogs.map { it.activityText }.toSet())
    }

    @Test
    fun `gratitude repository upsert and load for same date roundtrip`() = runTest {
        val dao = FakeGratitudeDao()
        val repository = GratitudeRepository(dao)
        val date = LocalDate.of(2026, 2, 4)

        repository.upsertGratitudeLog("Heute war ich geduldig mit mir.", date)

        val loaded = repository.getGratitudeLogForDate(date).first()
        assertNotNull(loaded)
        assertEquals("Heute war ich geduldig mit mir.", loaded?.entry)
    }

    @Test
    fun `exercise rating dao write-read cycle keeps latest first and computes average`() = runTest {
        val dao = FakeExerciseRatingDao()

        dao.insert(ExerciseRatingEntity(exerciseId = "grounding", rating = 2, timestamp = 100))
        dao.insert(ExerciseRatingEntity(exerciseId = "grounding", rating = 4, timestamp = 200))
        dao.insert(ExerciseRatingEntity(exerciseId = "breathing", rating = 5, timestamp = 150))

        val all = dao.getAllRatings().first()
        assertEquals(listOf(200L, 150L, 100L), all.map { it.timestamp })

        val averages = dao.getAllAverageRatings().first().associateBy { it.exerciseId }
        assertEquals(3.0, averages.getValue("grounding").averageRating, 0.0001)
        assertEquals(5.0, averages.getValue("breathing").averageRating, 0.0001)
    }
}

private class FakeActivityLogDao : ActivityLogDao {
    private val entries = MutableStateFlow<List<ActivityLogEntity>>(emptyList())

    override fun observeDay(dateInt: Int): Flow<List<ActivityLogEntity>> =
        MutableStateFlow(entries.value.filter { it.localDate == dateInt }.sortedBy { it.hour })

    override fun observeRange(startDateInt: Int, endDateInt: Int): Flow<List<ActivityLogEntity>> =
        MutableStateFlow(entries.value.filter { it.localDate in startDateInt until endDateInt }.sortedByDescending { it.localDate })

    override suspend fun upsert(entity: ActivityLogEntity) {
        entries.value = entries.value.filterNot { it.key == entity.key } + entity
    }

    override suspend fun deleteDay(dateInt: Int) {
        entries.value = entries.value.filterNot { it.localDate == dateInt }
    }

    override fun observeDayAvgValence(dateInt: Int): Flow<Double?> {
        val dayEntries = entries.value.filter { it.localDate == dateInt }
        val avg = if (dayEntries.isEmpty()) null else dayEntries.map { it.valence }.average()
        return MutableStateFlow(avg)
    }

    override fun getAll(): Flow<List<ActivityLogEntity>> = entries

    override fun getLogsForDay(startOfDay: Int, endOfDay: Int): Flow<List<ActivityLogEntity>> =
        MutableStateFlow(entries.value.filter { it.localDate in startOfDay until endOfDay }.sortedByDescending { it.hour })

    override fun getLogsForWeek(startOfWeek: Int, endOfWeek: Int): Flow<List<ActivityLogEntity>> =
        getLogsForDay(startOfWeek, endOfWeek)

    override fun getLogsForMonth(startOfMonth: Int, endOfMonth: Int): Flow<List<ActivityLogEntity>> =
        MutableStateFlow(entries.value.filter { it.localDate in startOfMonth until endOfMonth })

    override fun getUniqueActivities(): Flow<List<String>> =
        MutableStateFlow(entries.value.map { it.activityText }.distinct().sorted())

    override fun getMoodBoosters(threshold: Int, minRating: Double): Flow<List<ActivityAnalysis>> =
        MutableStateFlow(emptyList())

    override fun getMoodDampers(threshold: Int, maxRating: Double): Flow<List<ActivityAnalysis>> =
        MutableStateFlow(emptyList())
}

private class FakeGratitudeDao : GratitudeDao {
    private val entries = mutableMapOf<LocalDate, GratitudeEntity>()

    override fun getGratitudeLogForDate(date: LocalDate): Flow<GratitudeEntity?> =
        MutableStateFlow(entries[date])

    override suspend fun upsert(log: GratitudeEntity) {
        entries[log.date] = log
    }
}

private class FakeExerciseRatingDao : ExerciseRatingDao {
    private val entries = mutableListOf<ExerciseRatingEntity>()

    override suspend fun insert(rating: ExerciseRatingEntity) {
        entries += rating.copy(id = (entries.size + 1).toLong())
    }

    override fun getAllRatings(): Flow<List<ExerciseRatingEntity>> =
        MutableStateFlow(entries.sortedByDescending { it.timestamp })

    override fun getAllAverageRatings(): Flow<List<ExerciseAverageRating>> =
        MutableStateFlow(
            entries.groupBy { it.exerciseId }
                .map { (exerciseId, values) ->
                    ExerciseAverageRating(exerciseId, values.map { it.rating }.average())
                }
        )
}
