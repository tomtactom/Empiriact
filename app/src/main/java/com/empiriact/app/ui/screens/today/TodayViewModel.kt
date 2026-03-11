package com.empiriact.app.ui.screens.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.data.repo.ActivityLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// Data class to hold the unsaved state for an hour entry.
data class HourEntryCache(
    val activities: List<String> = emptyList(),
    val valence: Int = 0,
    val inputText: String = "",
    val peopleText: String = "",
    val peopleChips: List<String> = emptyList(),
    val peopleInputText: String = "",
    val durationMinutes: Int? = null,
    val difficultyRating: Int? = null,
    val activationLatencyMinutes: Int? = null
)
data class HourEntryKey(val date: LocalDate, val hour: Int)


data class BaselineUiStatus(
    val isBaselineMode: Boolean = false,
    val observationHint: String? = null,
    val baselineDays: Int = 7
)

class TodayViewModel(
    private val repository: ActivityLogRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // Cache for unsaved changes. Key is the hour of the day.
    private val _unsavedChanges = MutableStateFlow<Map<HourEntryKey, HourEntryCache>>(emptyMap())
    val unsavedChanges: StateFlow<Map<HourEntryKey, HourEntryCache>> = _unsavedChanges.asStateFlow()


    val todayIntroCompleted: StateFlow<Boolean> = settingsRepository.todayIntroCompleted
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val baselineUiStatus: StateFlow<BaselineUiStatus> = combine(
        settingsRepository.baInputMode,
        settingsRepository.baBaselineStart,
        settingsRepository.baBaselineDays
    ) { inputMode, baselineStart, baselineDays ->
        if (inputMode != SettingsRepository.InputMode.BASELINE) {
            BaselineUiStatus(isBaselineMode = false, baselineDays = baselineDays)
        } else {
            val start = baselineStart ?: LocalDate.now()
            val dayOfBaseline = java.time.temporal.ChronoUnit.DAYS.between(start, LocalDate.now()).toInt() + 1
            val safeDays = baselineDays.coerceAtLeast(1)
            val shownDay = dayOfBaseline.coerceAtLeast(1)
            BaselineUiStatus(
                isBaselineMode = true,
                observationHint = "Tag ${shownDay}/${safeDays} Beobachtung",
                baselineDays = safeDays
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BaselineUiStatus()
    )


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

    // Flow for unique people names for suggestions.
    val uniquePeople: StateFlow<List<String>> = repository.getAll()
        .map { logs ->
            logs.flatMap { log ->
                log.peopleText.split(",").map { it.trim() }.filter { it.isNotBlank() }
            }.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }.map { it.first }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Caches the current state of an hour entry.
     */
    fun cacheHourEntry(date: LocalDate, hour: Int, cache: HourEntryCache) {
        val currentCache = _unsavedChanges.value.toMutableMap()
        currentCache[HourEntryKey(date = date, hour = hour)] = cache
        _unsavedChanges.value = currentCache
    }

    /**
     * Inserts or updates an activity log for a specific hour on a specific date,
     * then clears the cache for that hour.
     */
    fun upsertActivityForHour(
        date: LocalDate,
        hour: Int,
        activityText: String,
        valence: Int,
        peopleText: String = "",
        durationMinutes: Int? = null,
        difficultyRating: Int? = null,
        activationLatencyMinutes: Int? = null
    ) {
        viewModelScope.launch {
            repository.upsert(
                date = date,
                hour = hour,
                text = activityText.trim(),
                valence = valence,
                peopleText = peopleText.trim(),
                durationMinutes = durationMinutes,
                difficultyRating = difficultyRating,
                activationLatencyMinutes = activationLatencyMinutes
            )
            // Clear the cache for the successfully saved hour.
            val currentCache = _unsavedChanges.value.toMutableMap()
            currentCache.remove(HourEntryKey(date = date, hour = hour))
            _unsavedChanges.value = currentCache
        }
    }

    fun completeTodayIntro() {
        viewModelScope.launch {
            settingsRepository.setTodayIntroCompleted(true)
        }
    }

    suspend fun exportDataAsCsv(): String {
        val logs = repository.getAll().first()
        val csvBuilder = StringBuilder()
        csvBuilder.append("Date,Hour,ActivityText,Valence,PeopleText\n")
        logs.forEach { log ->
            val year = log.localDate / 10000
            val month = (log.localDate / 100) % 100
            val day = log.localDate % 100
            val date = LocalDate.of(year, month, day)
            csvBuilder.append("${date},${log.hour},\"${log.activityText.replace("\"", "\"\"")}\",${log.valence},\"${log.peopleText.replace("\"", "\"\"")}\"\n")
        }
        return csvBuilder.toString()
    }
}
