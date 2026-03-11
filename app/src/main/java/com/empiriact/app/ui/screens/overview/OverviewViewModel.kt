package com.empiriact.app.ui.screens.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.data.repo.ActivityLogRepository
import com.empiriact.app.data.ExerciseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class ActivityAnalysis(val activity: String, val averageRating: Double)
data class ActivityFrequency(val activity: String, val count: Int)
data class ValenceTrend(val date: LocalDate, val averageValence: Double)

class OverviewViewModel(
    private val activityLogRepository: ActivityLogRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val SIGNIFICANCE_THRESHOLD = 3

    val exerciseRatings: StateFlow<List<ExerciseWithRating>> = exerciseRepository.getAllExercisesWithRatings()
        .map { list -> list.map { ExerciseWithRating(it.exerciseId, it.averageRating) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Analysis for Boosters and Dampers
    val moodBoosters: StateFlow<List<ActivityAnalysis>> = activityLogRepository.getMoodBoosters(
        threshold = SIGNIFICANCE_THRESHOLD,
        minRating = 0.5
    ).map { list -> Log.d("OverviewVM", "MoodBoosters loaded: ${list.size} items"); list }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val moodDampers: StateFlow<List<ActivityAnalysis>> = activityLogRepository.getMoodDampers(
        threshold = SIGNIFICANCE_THRESHOLD,
        maxRating = -0.5
    ).map { list -> Log.d("OverviewVM", "MoodDampers loaded: ${list.size} items"); list }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for the protocol overviews
    val dayLogs: StateFlow<List<ActivityLogEntity>> = activityLogRepository.getLogsForDay(
        startDate = LocalDate.now().minusDays(7),
        endDate = LocalDate.now().plusDays(1)
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for activity frequency
    val activityFrequency: StateFlow<List<ActivityFrequency>> = activityLogRepository.getAll()
        .map { logs ->
            logs.flatMap { it.activityText.split(",").map { it.trim() } }
                .filter { it.isNotBlank() }
                .groupingBy { it }
                .eachCount()
                .toList()
                .sortedByDescending { it.second }
                .take(10)
                .map { ActivityFrequency(it.first, it.second) }
        }.map { list -> Log.d("OverviewVM", "ActivityFrequency loaded: ${list.size} items"); list }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for valence trends (last 7 days)
    val valenceTrends: StateFlow<List<ValenceTrend>> = activityLogRepository.getLogsForDay(
        startDate = LocalDate.now().minusDays(6),
        endDate = LocalDate.now().plusDays(1)
    ).map { logs ->
        logs.groupBy { LocalDate.of(it.localDate / 10000, (it.localDate / 100) % 100, it.localDate % 100) }
            .map { (date, dayLogs) ->
                val avgValence = dayLogs.map { it.valence.toDouble() }.average()
                if (avgValence.isNaN()) Log.w("OverviewVM", "NaN valence for date $date")
                ValenceTrend(date, avgValence)
            }
            .sortedBy { it.date }
    }.map { list -> Log.d("OverviewVM", "ValenceTrends loaded: ${list.size} items"); list }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
