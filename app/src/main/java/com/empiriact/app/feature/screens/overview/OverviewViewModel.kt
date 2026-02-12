package com.empiriact.app.ui.screens.overview

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
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val moodDampers: StateFlow<List<ActivityAnalysis>> = activityLogRepository.getMoodDampers(
        threshold = SIGNIFICANCE_THRESHOLD,
        maxRating = -0.5
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Flow for the protocol overviews
    val dayLogs: StateFlow<List<ActivityLogEntity>> = activityLogRepository.getLogsForDay(
        startDate = LocalDate.now().minusDays(7),
        endDate = LocalDate.now().plusDays(1)
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
