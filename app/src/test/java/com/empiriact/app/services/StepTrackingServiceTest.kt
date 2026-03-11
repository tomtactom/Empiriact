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
    fun `initial start without previous snapshot sets baseline flag and no hourly delta`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_350)
        )

        val captureTime = ZonedDateTime.of(2026, 1, 1, 11, 5, 0, 0, ZoneId.of("UTC"))
        val captured = service.captureHourlySnapshot(captureTime)

        assertTrue(captured)
        assertEquals(1_350, settings.getPassiveStepsLastCounterTotal())
        assertEquals(captureTime.withMinute(0).withSecond(0).withNano(0), settings.getPassiveStepsLastCounterHour())
        assertTrue(settings.passiveStepsBaselineHourPending.first())

        val day = passive.observeDay(LocalDate.of(2026, 1, 1)).first()
        assertTrue(day.isEmpty())
    }

    @Test
    fun `reactivation after clearing state sets fresh baseline then resolves on next hour`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 900,
            hour = ZonedDateTime.of(2026, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"))
        )
        settings.setPassiveStepsBaselineHourPending(true)
        settings.clearPassiveStepsTrackingState()

        val baselineCapture = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_000)
        )

        assertTrue(
            baselineCapture.captureHourlySnapshot(
                ZonedDateTime.of(2026, 1, 1, 12, 10, 0, 0, ZoneId.of("UTC"))
            )
        )
        assertTrue(settings.passiveStepsBaselineHourPending.first())

        val nextHourCapture = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(1_120)
        )

        assertTrue(
            nextHourCapture.captureHourlySnapshot(
                ZonedDateTime.of(2026, 1, 1, 13, 5, 0, 0, ZoneId.of("UTC"))
            )
        )
        assertFalse(settings.passiveStepsBaselineHourPending.first())

        val day = passive.observeDay(LocalDate.of(2026, 1, 1)).first().sortedBy { it.hour }
        assertEquals(listOf(13), day.map { it.hour })
        assertEquals(listOf(120), day.map { it.stepCount })
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

    @Test
    fun `disables passive steps and clears tracking state when sensor is unavailable`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 800,
            hour = ZonedDateTime.of(2026, 1, 2, 9, 0, 0, 0, ZoneId.of("UTC"))
        )
        settings.setPassiveStepsBaselineHourPending(true)

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(total = 900, sensorAvailable = false)
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 2, 10, 5, 0, 0, ZoneId.of("UTC"))
        )

        assertFalse(captured)
        assertFalse(settings.passiveStepsCollectionEnabled())
        assertEquals(null, settings.getPassiveStepsLastCounterTotal())
        assertEquals(null, settings.getPassiveStepsLastCounterHour())
        assertFalse(settings.passiveStepsBaselineHourPending.first())
    }

    @Test
    fun `disables passive steps and clears tracking state when permission is missing`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 800,
            hour = ZonedDateTime.of(2026, 1, 2, 9, 0, 0, 0, ZoneId.of("UTC"))
        )
        settings.setPassiveStepsBaselineHourPending(true)

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(total = 900, hasPermission = false)
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 2, 10, 5, 0, 0, ZoneId.of("UTC"))
        )

        assertFalse(captured)
        assertFalse(settings.passiveStepsCollectionEnabled())
        assertEquals(null, settings.getPassiveStepsLastCounterTotal())
        assertEquals(null, settings.getPassiveStepsLastCounterHour())
        assertFalse(settings.passiveStepsBaselineHourPending.first())
    }

    private class FakeStepCounterSource(
        private val total: Long?,
        private val sensorAvailable: Boolean = true,
        private val hasPermission: Boolean = true
    ) : StepCounterSource {
        override suspend fun readCurrentTotalSteps(): Long? = total

        override fun isSensorAvailable(): Boolean = sensorAvailable

        override fun hasRequiredPermission(): Boolean = hasPermission
    }
}
