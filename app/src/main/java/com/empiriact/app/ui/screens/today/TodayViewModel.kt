package com.empiriact.app.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.data.repo.ActivityLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// Data class to hold the unsaved state for an hour entry.
data class HourEntryCache(val activities: List<String> = emptyList(), val valence: Int = 0, val inputText: String = "")

class TodayViewModel(private val repository: ActivityLogRepository) : ViewModel() {

    // Cache for unsaved changes. Key is the hour of the day.
    private val _unsavedChanges = MutableStateFlow<Map<Int, HourEntryCache>>(emptyMap())
    val unsavedChanges: StateFlow<Map<Int, HourEntryCache>> = _unsavedChanges.asStateFlow()

    // Flow for logs of the last 2 days to cover "yesterday" entries shown after midnight.
    val todayLogs: StateFlow<List<ActivityLogEntity>> = repository.getLogsForDay(
        startDate = LocalDate.now().minusDays(1),
        endDate = LocalDate.now().plusDays(1)
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Flow for unique activity names for suggestions.
    val uniqueActivities: StateFlow<List<String>> = repository.getUniqueActivities()
        .map { activities ->
            activities
                .flatMap { it.split(",").map { it.trim() } }
                .filter { it.isNotBlank() }
                .groupingBy { it }
                .eachCount()
                .toList()
                .sortedByDescending { it.second }
                .map { it.first }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Caches the current state of an hour entry.
     */
    fun cacheHourEntry(hour: Int, cache: HourEntryCache) {
        val currentCache = _unsavedChanges.value.toMutableMap()
        currentCache[hour] = cache
        _unsavedChanges.value = currentCache
    }

    /**
     * Inserts or updates an activity log for a specific hour on a specific date,
     * then clears the cache for that hour.
     */
    fun upsertActivityForHour(date: LocalDate, hour: Int, activityText: String, valence: Int) {
        viewModelScope.launch {
            repository.upsert(
                date = date,
                hour = hour,
                text = activityText.trim(),
                valence = valence
            )
            // Clear the cache for the successfully saved hour.
            val currentCache = _unsavedChanges.value.toMutableMap()
            currentCache.remove(hour)
            _unsavedChanges.value = currentCache
        }
    }
}
