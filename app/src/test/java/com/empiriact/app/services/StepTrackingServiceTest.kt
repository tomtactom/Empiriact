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
        assertEquals(10, day.first().hour)
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

    private class FakeStepCounterSource(
        private val total: Long?
    ) : StepCounterSource {
        override suspend fun readCurrentTotalSteps(): Long? = total
    }
}
