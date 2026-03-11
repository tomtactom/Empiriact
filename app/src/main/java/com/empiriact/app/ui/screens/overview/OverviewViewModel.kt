package com.empiriact.app.ui.screens.overview

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.ExerciseRepository
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.data.repo.ActivityLogRepository
import com.empiriact.app.data.repo.PassiveMarkerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

data class ActivityAnalysis(val activity: String, val averageRating: Double)
data class ActivityFrequency(val activity: String, val count: Int)
data class ValenceTrend(val date: LocalDate, val averageValence: Double)
data class ProtocolLogUiModel(
    val activityLog: ActivityLogEntity,
    val stepDisplayState: StepDisplayState
)

sealed interface StepDisplayState {
    data class Recorded(val count: Int) : StepDisplayState
    data class NotRecorded(val reason: Reason = Reason.UNKNOWN) : StepDisplayState {
        enum class Reason {
            UNKNOWN,
            PERMISSION_MISSING,
            BASELINE_PENDING
        }
    }

    data object Disabled : StepDisplayState
}

class OverviewViewModel(
    private val activityLogRepository: ActivityLogRepository,
    private val exerciseRepository: ExerciseRepository,
    private val passiveMarkerRepository: PassiveMarkerRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val SIGNIFICANCE_THRESHOLD = 3
    private val protocolStartDate = LocalDate.now().minusDays(7)
    private val protocolEndDateExclusive = LocalDate.now().plusDays(1)

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
    val dayLogs: StateFlow<List<ProtocolLogUiModel>> = combine(
        activityLogRepository.getLogsForDay(
            startDate = protocolStartDate,
            endDate = protocolEndDateExclusive
        ),
        passiveMarkerRepository.observeRange(
            startDate = protocolStartDate,
            endDateExclusive = protocolEndDateExclusive
        ),
        settingsRepository.passiveStepsEnabled,
        settingsRepository.passiveStepsLastReadError,
        settingsRepository.passiveStepsBaselineHourPending
    ) { logs, passiveMarkers, passiveStepsEnabled, lastReadError, baselineHourPending ->
        val markerByDateAndHour = passiveMarkers.associateBy { it.localDate to it.hour }
        logs.map { log ->
            ProtocolLogUiModel(
                activityLog = log,
                stepDisplayState = resolveStepDisplayState(
                    stepCount = markerByDateAndHour[log.localDate to log.hour]?.stepCount,
                    passiveStepsEnabled = passiveStepsEnabled,
                    lastReadErrorReason = lastReadError?.reason,
                    baselineHourPending = baselineHourPending
                )
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

internal fun resolveStepDisplayState(
    stepCount: Int?,
    passiveStepsEnabled: Boolean,
    lastReadErrorReason: SettingsRepository.PassiveStepsReadErrorReason?,
    baselineHourPending: Boolean
): StepDisplayState {
    if (stepCount != null) {
        return StepDisplayState.Recorded(stepCount)
    }
    if (!passiveStepsEnabled) {
        return StepDisplayState.Disabled
    }
    if (baselineHourPending) {
        return StepDisplayState.NotRecorded(StepDisplayState.NotRecorded.Reason.BASELINE_PENDING)
    }
    if (lastReadErrorReason == SettingsRepository.PassiveStepsReadErrorReason.PERMISSION_MISSING) {
        return StepDisplayState.NotRecorded(StepDisplayState.NotRecorded.Reason.PERMISSION_MISSING)
    }
    return StepDisplayState.NotRecorded()
}
