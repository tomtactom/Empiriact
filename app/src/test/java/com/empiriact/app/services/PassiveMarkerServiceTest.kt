package com.empiriact.app.services

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.data.db.EmpiriactDatabase
import com.empiriact.app.data.repo.PassiveMarkerRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class PassiveMarkerServiceTest {

    private fun repo(): SettingsRepository {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return SettingsRepository(context)
    }

    private fun passiveRepo(context: Context): PassiveMarkerRepository {
        val db = Room.inMemoryDatabaseBuilder(context, EmpiriactDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        return PassiveMarkerRepository(db.passiveMarkerDao())
    }

    @Test
    fun `context empty when global opt-in disabled`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = repo()
        repository.setPassiveMarkersOptIn(false)

        val service = PassiveMarkerService(repository, passiveRepo(context))
        val markerContext = service.contextForCheckins(sampleCheckins()).first()

        assertTrue(markerContext.isEmpty())
    }

    @Test
    fun `context contains enabled sources only`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = repo()
        val passive = passiveRepo(context)

        repository.setPassiveMarkersOptIn(true)
        repository.setPassiveStepsEnabled(true)
        repository.setPassiveSleepEnabled(false)
        repository.setPassiveScreenTimeProximityEnabled(true)

        passive.upsertHour(LocalDate.of(2026, 1, 1), 10, stepCount = 1200, sleepDurationMinutesPreviousNight = 450, screenTimeMinutesInHour = 28)

        val service = PassiveMarkerService(repository, passive)
        val markerContext = service.contextForCheckins(sampleCheckins()).first()

        val first = markerContext.values.first()
        assertTrue(first.readings.any { it.source == PassiveMarkerSource.STEPS })
        assertTrue(first.readings.any { it.source == PassiveMarkerSource.SCREEN_TIME_PROXIMITY })
        assertFalse(first.readings.any { it.source == PassiveMarkerSource.SLEEP_DURATION })
    }

    @Test
    fun `hourly values are used for daily comparison`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = repo()
        val passive = passiveRepo(context)

        repository.setPassiveMarkersOptIn(true)
        repository.setPassiveStepsEnabled(true)
        repository.setPassiveSleepEnabled(true)
        repository.setPassiveScreenTimeProximityEnabled(true)

        passive.upsertHour(LocalDate.of(2026, 1, 1), 10, stepCount = 1000, sleepDurationMinutesPreviousNight = 420, screenTimeMinutesInHour = 20)
        passive.upsertHour(LocalDate.of(2026, 1, 1), 11, stepCount = 800, sleepDurationMinutesPreviousNight = 420, screenTimeMinutesInHour = 15)

        val service = PassiveMarkerService(repository, passive)
        val points = service.passiveVsActiveDailyComparison(sampleCheckins()).first()

        assertEquals(1, points.size)
        assertEquals(1800, points.first().steps)
        assertEquals(35, points.first().screenTimeMinutes)
        assertEquals(7.0f, points.first().sleepDurationHours ?: 0f, 0.01f)
    }

    private fun sampleCheckins(): List<ActivityLogEntity> {
        return listOf(
            ActivityLogEntity(
                key = "20260101-10",
                localDate = 20260101,
                hour = 10,
                activityText = "Spaziergang",
                valence = 1,
                peopleText = "",
                updatedAtEpochMs = System.currentTimeMillis()
            )
        )
    }
}
