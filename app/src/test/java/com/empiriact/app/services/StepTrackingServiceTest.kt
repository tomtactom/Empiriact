package com.empiriact.app.services

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.EmpiriactDatabase
import com.empiriact.app.data.repo.PassiveMarkerRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class StepTrackingServiceTest {

    private fun settingsRepo(): SettingsRepository {
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
    fun `stores hourly delta when consent and steps are enabled`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 1_000,
            hour = ZonedDateTime.of(2026, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_350)
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 1, 11, 5, 0, 0, ZoneId.of("UTC"))
        )

        assertTrue(captured)
        val day = passive.observeDay(ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")).toLocalDate()).first()
        assertEquals(1, day.size)
        assertEquals(11, day.first().hour)
        assertEquals(350, day.first().stepCount)
    }

    @Test
    fun `does not store snapshot when opt-in disabled`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(false)
        settings.setPassiveStepsEnabled(true)

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_350)
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 1, 11, 5, 0, 0, ZoneId.of("UTC"))
        )

        assertFalse(captured)
        val day = passive.observeDay(ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")).toLocalDate()).first()
        assertTrue(day.isEmpty())
    }

    @Test
    fun `backfills evenly across multi-hour gap`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 1_000,
            hour = ZonedDateTime.of(2026, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_401)
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 1, 14, 5, 0, 0, ZoneId.of("UTC"))
        )

        assertTrue(captured)
        val jan1 = passive.observeDay(LocalDate.of(2026, 1, 1)).first().sortedBy { it.hour }
        assertEquals(4, jan1.size)
        assertEquals(listOf(11, 12, 13, 14), jan1.map { it.hour })
        assertEquals(listOf(100, 100, 100, 101), jan1.map { it.stepCount })
    }

    @Test
    fun `backfills zeros when sensor total decreases`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 2_000,
            hour = ZonedDateTime.of(2026, 1, 1, 22, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_900)
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 2, 1, 15, 0, 0, ZoneId.of("UTC"))
        )

        assertTrue(captured)

        val jan1 = passive.observeDay(LocalDate.of(2026, 1, 1)).first().sortedBy { it.hour }
        val jan2 = passive.observeDay(LocalDate.of(2026, 1, 2)).first().sortedBy { it.hour }

        assertEquals(listOf(23), jan1.map { it.hour })
        assertEquals(listOf(0), jan1.map { it.stepCount })
        assertEquals(listOf(0, 1), jan2.map { it.hour })
        assertEquals(listOf(0, 0), jan2.map { it.stepCount })
    }

    @Test
    fun `one-hour gap stores only current hour and keeps existing previous hour`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 500,
            hour = ZonedDateTime.of(2026, 1, 3, 7, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(620)
        )

        passive.upsertHour(
            date = LocalDate.of(2026, 1, 3),
            hour = 7,
            stepCount = 55
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 3, 8, 1, 0, 0, ZoneId.of("UTC"))
        )

        assertTrue(captured)
        val day = passive.observeDay(LocalDate.of(2026, 1, 3)).first().sortedBy { it.hour }
        assertEquals(2, day.size)
        assertEquals(listOf(7, 8), day.map { it.hour })
        assertEquals(listOf(55, 120), day.map { it.stepCount })
    }

    @Test
    fun `same-hour retry updates snapshot to avoid inflated next hour delta`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 1_000,
            hour = ZonedDateTime.of(2026, 1, 3, 10, 0, 0, 0, ZoneId.of("UTC"))
        )

        val firstRetry = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_100)
        )

        val secondHourCapture = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_160)
        )

        assertTrue(
            firstRetry.captureHourlySnapshot(
                ZonedDateTime.of(2026, 1, 3, 10, 45, 0, 0, ZoneId.of("UTC"))
            )
        )

        assertTrue(
            secondHourCapture.captureHourlySnapshot(
                ZonedDateTime.of(2026, 1, 3, 11, 1, 0, 0, ZoneId.of("UTC"))
            )
        )

        val day = passive.observeDay(LocalDate.of(2026, 1, 3)).first().sortedBy { it.hour }
        assertEquals(listOf(11), day.map { it.hour })
        assertEquals(listOf(60), day.map { it.stepCount })
    }

    private class FakeStepCounterSource(
        private val total: Long?
    ) : StepCounterSource {
        override suspend fun readCurrentTotalSteps(): Long? = total
    }
}
