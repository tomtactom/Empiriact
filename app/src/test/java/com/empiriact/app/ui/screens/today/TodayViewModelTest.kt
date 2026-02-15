package com.empiriact.app.ui.screens.today

import com.empiriact.app.data.ActivityLogRepository
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.ActivityLogEntity
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

    @Test
    fun `cacheHourEntry adds draft and successful save clears draft`() = runTest {
        whenever(repository.getUniqueActivities()).thenReturn(MutableStateFlow(listOf("Spaziergang")))
        whenever(repository.getLogsForDay(any(), any())).thenReturn(MutableStateFlow(emptyList<ActivityLogEntity>()))
        whenever(settingsRepository.todayIntroCompleted).thenReturn(MutableStateFlow(false))

        val viewModel = TodayViewModel(repository, settingsRepository)
        val date = LocalDate.of(2026, 1, 10)
        val key = HourEntryKey(date, 8)

        viewModel.cacheHourEntry(date, 8, HourEntryCache(inputText = " Entwurf ", valence = 1))
        assertTrue(viewModel.unsavedChanges.value.containsKey(key))

        viewModel.upsertActivityForHour(date, 8, "  Gehen  ", 2)
        advanceUntilIdle()

        verify(repository).upsert(eq(date), eq(8), eq("Gehen"), eq(2))
        assertTrue(viewModel.unsavedChanges.value.containsKey(key).not())
    }

    @Test
    fun `failed save keeps cached entry as error-safe transition`() = runTest {
        whenever(repository.getUniqueActivities()).thenReturn(MutableStateFlow(emptyList()))
        whenever(repository.getLogsForDay(any(), any())).thenReturn(MutableStateFlow(emptyList<ActivityLogEntity>()))
        whenever(settingsRepository.todayIntroCompleted).thenReturn(MutableStateFlow(false))
        whenever(repository.upsert(any(), any(), any(), any())).thenThrow(IllegalStateException("db down"))

        val viewModel = TodayViewModel(repository, settingsRepository)
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

        val viewModel = TodayViewModel(repository, settingsRepository)

        uniqueActivitiesFlow.emit(listOf("Lesen, Spazieren", "Lesen", " Spazieren ", ""))
        advanceUntilIdle()

        assertEquals(listOf("Lesen", "Spazieren"), viewModel.uniqueActivities.value)
        verify(repository, never()).upsert(any(), any(), any(), any())
    }
}
