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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_350))
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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_000))
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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_120))
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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_350))
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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_350))
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 1, 11, 5, 0, 0, ZoneId.of("UTC"))
        )

        assertFalse(captured)
        val day = passive.observeDay(ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC")).toLocalDate()).first()
        assertTrue(day.isEmpty())
    }

    @Test
    fun `gap over three hours distributes across missing hours and marks estimate`() = runTest {
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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_401))
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 1, 14, 5, 0, 0, ZoneId.of("UTC"))
        )

        assertTrue(captured)
        val jan1 = passive.observeDay(LocalDate.of(2026, 1, 1)).first().sortedBy { it.hour }
        assertEquals(listOf(11, 12, 13, 14), jan1.map { it.hour })
        assertEquals(listOf(100, 100, 100, 101), jan1.map { it.stepCount })
        assertEquals(listOf(true, true, true, true), jan1.map { it.isEstimated })
    }

    @Test
    fun `gap above twenty four hours only distributes latest twenty four hours`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 10_000,
            hour = ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(11_200))
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 2, 6, 10, 0, 0, ZoneId.of("UTC"))
        )

        assertTrue(captured)

        val jan1 = passive.observeDay(LocalDate.of(2026, 1, 1)).first().sortedBy { it.hour }
        val jan2 = passive.observeDay(LocalDate.of(2026, 1, 2)).first().sortedBy { it.hour }

        assertEquals((7..23).toList(), jan1.map { it.hour })
        assertEquals((0..6).toList(), jan2.map { it.hour })
        assertEquals(24, jan1.size + jan2.size)
        assertEquals(1_200, (jan1 + jan2).sumOf { it.stepCount })
        assertTrue((jan1 + jan2).all { it.isEstimated })
    }

    @Test
    fun `multi-hour backfill up to three hours is distributed and marked estimated`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 2_000,
            hour = ZonedDateTime.of(2026, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(2_305))
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 1, 13, 10, 0, 0, ZoneId.of("UTC"))
        )

        assertTrue(captured)
        val day = passive.observeDay(LocalDate.of(2026, 1, 1)).first().sortedBy { it.hour }
        assertEquals(listOf(11, 12, 13), day.map { it.hour })
        assertEquals(listOf(101, 102, 102), day.map { it.stepCount })
        assertEquals(listOf(true, true, true), day.map { it.isEstimated })
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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_900))
        )

        val captured = service.captureHourlySnapshot(
            ZonedDateTime.of(2026, 1, 2, 1, 15, 0, 0, ZoneId.of("UTC"))
        )

        assertTrue(captured)

        val jan1 = passive.observeDay(LocalDate.of(2026, 1, 1)).first().sortedBy { it.hour }
        val jan2 = passive.observeDay(LocalDate.of(2026, 1, 2)).first().sortedBy { it.hour }

        assertEquals(listOf(23), jan1.map { it.hour })
        assertEquals(listOf(0), jan1.map { it.stepCount })
        assertEquals(listOf(false), jan1.map { it.isEstimated })
        assertEquals(listOf(0, 1), jan2.map { it.hour })
        assertEquals(listOf(0, 0), jan2.map { it.stepCount })
        assertEquals(listOf(false, false), jan2.map { it.isEstimated })
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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(620))
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
        assertEquals(listOf(false, false), day.map { it.isEstimated })
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
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_100))
        )

        val secondHourCapture = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_160))
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
            stepCounterSource = FakeStepCounterSource(readResult = StepReadResult.SensorUnavailable, sensorAvailable = false)
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
            stepCounterSource = FakeStepCounterSource(readResult = StepReadResult.PermissionMissing, hasPermission = false)
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
    fun `repeated timeouts keep tracking enabled and retain latest timeout diagnosis`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 500,
            hour = ZonedDateTime.of(2026, 1, 4, 9, 0, 0, 0, ZoneId.of("UTC"))
        )

        val firstAttempt = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Timeout)
        )

        val secondAttempt = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Timeout)
        )

        assertFalse(
            firstAttempt.captureHourlySnapshot(
                ZonedDateTime.of(2026, 1, 4, 10, 5, 0, 0, ZoneId.of("UTC"))
            )
        )
        assertFalse(
            secondAttempt.captureHourlySnapshot(
                ZonedDateTime.of(2026, 1, 4, 10, 25, 0, 0, ZoneId.of("UTC"))
            )
        )

        assertTrue(settings.passiveStepsCollectionEnabled())
        assertEquals(500, settings.getPassiveStepsLastCounterTotal())

        val diagnostic = settings.passiveStepsLastReadError.first()
        assertEquals(SettingsRepository.PassiveStepsReadErrorReason.TIMEOUT, diagnostic?.reason)
        assertEquals(
            ZonedDateTime.of(2026, 1, 4, 10, 25, 0, 0, ZoneId.of("UTC")),
            diagnostic?.timestamp
        )
    }

    @Test
    fun `timeout diagnosis is cleared after next successful capture`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 1_000,
            hour = ZonedDateTime.of(2026, 1, 4, 10, 0, 0, 0, ZoneId.of("UTC"))
        )

        val timeoutService = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Timeout)
        )

        assertFalse(
            timeoutService.captureHourlySnapshot(
                ZonedDateTime.of(2026, 1, 4, 11, 5, 0, 0, ZoneId.of("UTC"))
            )
        )
        assertEquals(
            SettingsRepository.PassiveStepsReadErrorReason.TIMEOUT,
            settings.passiveStepsLastReadError.first()?.reason
        )

        val recoveryService = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_120))
        )

        assertTrue(
            recoveryService.captureHourlySnapshot(
                ZonedDateTime.of(2026, 1, 4, 12, 5, 0, 0, ZoneId.of("UTC"))
            )
        )

        assertEquals(null, settings.passiveStepsLastReadError.first())
        val day = passive.observeDay(LocalDate.of(2026, 1, 4)).first().sortedBy { it.hour }
        assertEquals(listOf(11, 12), day.map { it.hour })
        assertEquals(listOf(60, 60), day.map { it.stepCount })
    }

    @Test
    fun `uses hourly history as primary source when fully available`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 100,
            hour = ZonedDateTime.of(2026, 1, 5, 10, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Timeout),
            hourlyStepHistorySource = FakeHourlyStepHistorySource(
                HourlyStepHistoryReadResult.Success(
                    listOf(
                        HourBucket(ZonedDateTime.of(2026, 1, 5, 11, 0, 0, 0, ZoneId.of("UTC")), 120),
                        HourBucket(ZonedDateTime.of(2026, 1, 5, 12, 0, 0, 0, ZoneId.of("UTC")), 140)
                    )
                )
            )
        )

        assertTrue(service.captureHourlySnapshot(ZonedDateTime.of(2026, 1, 5, 12, 35, 0, 0, ZoneId.of("UTC"))))

        val day = passive.observeDay(LocalDate.of(2026, 1, 5)).first().sortedBy { it.hour }
        assertEquals(listOf(11, 12), day.map { it.hour })
        assertEquals(listOf(120, 140), day.map { it.stepCount })
        assertEquals(null, settings.passiveStepsLastReadError.first())
    }

    @Test
    fun `history gaps only persist available buckets`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 100,
            hour = ZonedDateTime.of(2026, 1, 6, 10, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Timeout),
            hourlyStepHistorySource = FakeHourlyStepHistorySource(
                HourlyStepHistoryReadResult.Success(
                    listOf(
                        HourBucket(ZonedDateTime.of(2026, 1, 6, 11, 0, 0, 0, ZoneId.of("UTC")), 80),
                        HourBucket(ZonedDateTime.of(2026, 1, 6, 13, 0, 0, 0, ZoneId.of("UTC")), 90)
                    )
                )
            )
        )

        assertTrue(service.captureHourlySnapshot(ZonedDateTime.of(2026, 1, 6, 13, 5, 0, 0, ZoneId.of("UTC"))))

        val day = passive.observeDay(LocalDate.of(2026, 1, 6)).first().sortedBy { it.hour }
        assertEquals(listOf(11, 13), day.map { it.hour })
        assertEquals(listOf(80, 90), day.map { it.stepCount })
    }

    @Test
    fun `falls back to live sensor delta when history source is empty`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 1_000,
            hour = ZonedDateTime.of(2026, 1, 7, 10, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(1_180)),
            hourlyStepHistorySource = FakeHourlyStepHistorySource(HourlyStepHistoryReadResult.SourceEmpty)
        )

        assertTrue(service.captureHourlySnapshot(ZonedDateTime.of(2026, 1, 7, 11, 20, 0, 0, ZoneId.of("UTC"))))

        val day = passive.observeDay(LocalDate.of(2026, 1, 7)).first().sortedBy { it.hour }
        assertEquals(listOf(11), day.map { it.hour })
        assertEquals(listOf(180), day.map { it.stepCount })
    }


    @Test
    fun `worker ausgefallen app geoeffnet fuehrt backfill beim naechsten snapshot aus`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)
        settings.setPassiveStepsLastSnapshot(
            totalSteps = 4_000,
            hour = ZonedDateTime.of(2026, 1, 8, 9, 0, 0, 0, ZoneId.of("UTC"))
        )

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(4_360))
        )

        assertTrue(service.captureHourlySnapshot(ZonedDateTime.of(2026, 1, 8, 12, 3, 0, 0, ZoneId.of("UTC"))))

        val day = passive.observeDay(LocalDate.of(2026, 1, 8)).first().sortedBy { it.hour }
        assertEquals(listOf(10, 11, 12), day.map { it.hour })
        assertEquals(360, day.sumOf { it.stepCount })
    }

    @Test
    fun `throttling verhindert zu haeufige reads innerhalb von fuenfzehn minuten`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val settings = settingsRepo()
        val passive = passiveRepo(context)

        settings.clearAllSettings()
        settings.setPassiveMarkersOptIn(true)
        settings.setPassiveStepsEnabled(true)

        val service = StepTrackingService(
            settingsRepository = settings,
            passiveMarkerRepository = passive,
            stepCounterSource = FakeStepCounterSource(StepReadResult.Success(900))
        )

        val first = service.captureHourlySnapshot(ZonedDateTime.of(2026, 1, 9, 10, 0, 0, 0, ZoneId.of("UTC")))
        val second = service.captureHourlySnapshot(ZonedDateTime.of(2026, 1, 9, 10, 10, 0, 0, ZoneId.of("UTC")))

        assertTrue(first)
        assertFalse(second)
    }

    private class FakeStepCounterSource(
        private val readResult: StepReadResult,
        private val sensorAvailable: Boolean = true,
        private val hasPermission: Boolean = true
    ) : StepCounterSource {
        override suspend fun readCurrentTotalSteps(): StepReadResult = readResult

        override fun isSensorAvailable(): Boolean = sensorAvailable

        override fun hasRequiredPermission(): Boolean = hasPermission
    }

    private class FakeHourlyStepHistorySource(
        private val result: HourlyStepHistoryReadResult
    ) : HourlyStepHistorySource {
        override suspend fun readHourlySteps(start: ZonedDateTime, end: ZonedDateTime): HourlyStepHistoryReadResult = result
    }
}
