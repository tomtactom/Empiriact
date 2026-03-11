package com.empiriact.app.ui.screens.today

import com.empiriact.app.data.ActivityLogRepository
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.services.PassiveSnapshotRefresher
import com.empiriact.app.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class TodayViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: ActivityLogRepository = mock()
    private val settingsRepository: SettingsRepository = mock()
    private val passiveSnapshotRefresher: PassiveSnapshotRefresher = mock()

    @Test
    fun `cacheHourEntry adds draft and successful save clears draft`() = runTest {
        whenever(repository.getUniqueActivities()).thenReturn(MutableStateFlow(listOf("Spaziergang")))
        whenever(repository.getLogsForDay(any(), any())).thenReturn(MutableStateFlow(emptyList<ActivityLogEntity>()))
        whenever(settingsRepository.todayIntroCompleted).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baInputMode).thenReturn(MutableStateFlow(SettingsRepository.InputMode.STANDARD))
        whenever(settingsRepository.baBaselineStart).thenReturn(MutableStateFlow(null))
        whenever(settingsRepository.baBaselineDays).thenReturn(MutableStateFlow(7))
        whenever(settingsRepository.baActivityCriteriaAcknowledged).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baActivityPreferenceTags).thenReturn(MutableStateFlow(emptySet()))
        whenever(settingsRepository.baMaintenanceStatus).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceStatus.INACTIVE))
        whenever(settingsRepository.baMaintenanceInterval).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceInterval.DAILY))
        whenever(settingsRepository.baActivityCriteriaAcknowledged).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baActivityPreferenceTags).thenReturn(MutableStateFlow(emptySet()))
        whenever(settingsRepository.baMaintenanceStatus).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceStatus.INACTIVE))
        whenever(settingsRepository.baMaintenanceInterval).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceInterval.DAILY))

        val viewModel = TodayViewModel(repository, settingsRepository, passiveSnapshotRefresher)
        val date = LocalDate.of(2026, 1, 10)
        val key = HourEntryKey(date, 8)

        viewModel.cacheHourEntry(date, 8, HourEntryCache(inputText = " Entwurf ", valence = 1))
        assertTrue(viewModel.unsavedChanges.value.containsKey(key))

        viewModel.upsertActivityForHour(date, 8, "  Gehen  ", 2)
        advanceUntilIdle()

        verify(repository).upsert(eq(date), eq(8), eq("Gehen"), eq(2), eq(""), eq(null), eq(null), eq(null))
        assertTrue(viewModel.unsavedChanges.value.containsKey(key).not())
    }

    @Test
    fun `failed save keeps cached entry as error-safe transition`() = runTest {
        whenever(repository.getUniqueActivities()).thenReturn(MutableStateFlow(emptyList()))
        whenever(repository.getLogsForDay(any(), any())).thenReturn(MutableStateFlow(emptyList<ActivityLogEntity>()))
        whenever(settingsRepository.todayIntroCompleted).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baInputMode).thenReturn(MutableStateFlow(SettingsRepository.InputMode.STANDARD))
        whenever(settingsRepository.baBaselineStart).thenReturn(MutableStateFlow(null))
        whenever(settingsRepository.baBaselineDays).thenReturn(MutableStateFlow(7))
        whenever(settingsRepository.baActivityCriteriaAcknowledged).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baActivityPreferenceTags).thenReturn(MutableStateFlow(emptySet()))
        whenever(settingsRepository.baMaintenanceStatus).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceStatus.INACTIVE))
        whenever(settingsRepository.baMaintenanceInterval).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceInterval.DAILY))
        whenever(repository.upsert(any(), any(), any(), any(), any(), any(), any(), any())).thenThrow(IllegalStateException("db down"))

        val viewModel = TodayViewModel(repository, settingsRepository, passiveSnapshotRefresher)
        val date = LocalDate.of(2026, 1, 11)
        val key = HourEntryKey(date, 9)

        viewModel.cacheHourEntry(date, 9, HourEntryCache(inputText = "Unverändert", valence = -1))
        viewModel.upsertActivityForHour(date, 9, "Unverändert", -1)
        advanceUntilIdle()

        assertTrue(viewModel.unsavedChanges.value.containsKey(key))
    }

    @Test
    fun `uniqueActivities normalizes comma separated suggestions by frequency`() = runTest {
        val uniqueActivitiesFlow = MutableSharedFlow<List<String>>(replay = 1)
        whenever(repository.getUniqueActivities()).thenReturn(uniqueActivitiesFlow)
        whenever(repository.getLogsForDay(any(), any())).thenReturn(MutableStateFlow(emptyList<ActivityLogEntity>()))
        whenever(settingsRepository.todayIntroCompleted).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baInputMode).thenReturn(MutableStateFlow(SettingsRepository.InputMode.STANDARD))
        whenever(settingsRepository.baBaselineStart).thenReturn(MutableStateFlow(null))
        whenever(settingsRepository.baBaselineDays).thenReturn(MutableStateFlow(7))
        whenever(settingsRepository.baActivityCriteriaAcknowledged).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baActivityPreferenceTags).thenReturn(MutableStateFlow(emptySet()))
        whenever(settingsRepository.baMaintenanceStatus).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceStatus.INACTIVE))
        whenever(settingsRepository.baMaintenanceInterval).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceInterval.DAILY))
        whenever(settingsRepository.baActivityCriteriaAcknowledged).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baActivityPreferenceTags).thenReturn(MutableStateFlow(emptySet()))
        whenever(settingsRepository.baMaintenanceStatus).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceStatus.INACTIVE))
        whenever(settingsRepository.baMaintenanceInterval).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceInterval.DAILY))

        val viewModel = TodayViewModel(repository, settingsRepository, passiveSnapshotRefresher)

        uniqueActivitiesFlow.emit(listOf("Lesen, Spazieren", "Lesen", " Spazieren ", ""))
        advanceUntilIdle()

        assertEquals(listOf("Lesen", "Spazieren"), viewModel.uniqueActivities.value)
        verify(repository, never()).upsert(any(), any(), any(), any(), any(), any(), any(), any())
    }

    @Test
    fun `baseline mode exposes observation hint`() = runTest {
        whenever(repository.getUniqueActivities()).thenReturn(MutableStateFlow(emptyList()))
        whenever(repository.getLogsForDay(any(), any())).thenReturn(MutableStateFlow(emptyList<ActivityLogEntity>()))
        whenever(settingsRepository.todayIntroCompleted).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baInputMode).thenReturn(MutableStateFlow(SettingsRepository.InputMode.BASELINE))
        whenever(settingsRepository.baBaselineStart).thenReturn(MutableStateFlow(LocalDate.now().minusDays(1)))
        whenever(settingsRepository.baBaselineDays).thenReturn(MutableStateFlow(7))
        whenever(settingsRepository.baActivityCriteriaAcknowledged).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baActivityPreferenceTags).thenReturn(MutableStateFlow(emptySet()))
        whenever(settingsRepository.baMaintenanceStatus).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceStatus.INACTIVE))
        whenever(settingsRepository.baMaintenanceInterval).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceInterval.DAILY))
        whenever(settingsRepository.baActivityCriteriaAcknowledged).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baActivityPreferenceTags).thenReturn(MutableStateFlow(emptySet()))
        whenever(settingsRepository.baMaintenanceStatus).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceStatus.INACTIVE))
        whenever(settingsRepository.baMaintenanceInterval).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceInterval.DAILY))

        val viewModel = TodayViewModel(repository, settingsRepository, passiveSnapshotRefresher)
        advanceUntilIdle()

        assertTrue(viewModel.baselineUiStatus.value.isBaselineMode)
        assertEquals("Tag 2/7 Beobachtung", viewModel.baselineUiStatus.value.observationHint)
    }

    @Test
    fun `check-in save can trigger passive snapshot refresh`() = runTest {
        whenever(repository.getUniqueActivities()).thenReturn(MutableStateFlow(emptyList()))
        whenever(repository.getLogsForDay(any(), any())).thenReturn(MutableStateFlow(emptyList<ActivityLogEntity>()))
        whenever(settingsRepository.todayIntroCompleted).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baInputMode).thenReturn(MutableStateFlow(SettingsRepository.InputMode.STANDARD))
        whenever(settingsRepository.baBaselineStart).thenReturn(MutableStateFlow(null))
        whenever(settingsRepository.baBaselineDays).thenReturn(MutableStateFlow(7))
        whenever(settingsRepository.baActivityCriteriaAcknowledged).thenReturn(MutableStateFlow(false))
        whenever(settingsRepository.baActivityPreferenceTags).thenReturn(MutableStateFlow(emptySet()))
        whenever(settingsRepository.baMaintenanceStatus).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceStatus.INACTIVE))
        whenever(settingsRepository.baMaintenanceInterval).thenReturn(MutableStateFlow(SettingsRepository.BaMaintenanceInterval.DAILY))

        val viewModel = TodayViewModel(repository, settingsRepository, passiveSnapshotRefresher)
        val date = LocalDate.of(2026, 1, 12)

        viewModel.upsertActivityForHour(date, 10, "Spazieren", 1, refreshPassiveSnapshot = true)
        advanceUntilIdle()

        verify(passiveSnapshotRefresher).refreshSnapshot(any())
    }

}
