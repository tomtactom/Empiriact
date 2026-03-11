package com.empiriact.app.ui.screens.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.ActivityLogRepository
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.data.db.ActivityLogEntity
import com.empiriact.app.services.PassiveMarkerContext
import com.empiriact.app.services.PassiveMarkerService
import com.empiriact.app.services.PassiveVsActiveDataPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate

class InsightsViewModel(
    private val activityLogRepository: ActivityLogRepository,
    private val passiveMarkerService: PassiveMarkerService,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _activities = MutableStateFlow<Map<LocalDate, List<HourlyActivity>>>(emptyMap())
    val activities: StateFlow<Map<LocalDate, List<HourlyActivity>>> = _activities

    private val _passiveContextByCheckin = MutableStateFlow<Map<String, PassiveMarkerContext>>(emptyMap())
    val passiveContextByCheckin: StateFlow<Map<String, PassiveMarkerContext>> = _passiveContextByCheckin

    private val _passiveVsActive = MutableStateFlow<List<PassiveVsActiveDataPoint>>(emptyList())
    val passiveVsActive: StateFlow<List<PassiveVsActiveDataPoint>> = _passiveVsActive

    private val _showStepsBaselineInfo = MutableStateFlow(false)
    val showStepsBaselineInfo: StateFlow<Boolean> = _showStepsBaselineInfo

    private var passiveContextJob: Job? = null
    private var passiveComparisonJob: Job? = null

    init {
        settingsRepository.passiveStepsBaselineHourPending
            .onEach { isPending -> _showStepsBaselineInfo.value = isPending }
            .launchIn(viewModelScope)

        _selectedDate.onEach { date ->
            activityLogRepository.getLogsForDay(
                startDate = date,
                endDate = date.plusDays(1)
            ).onEach { entities ->
                updateDailyActivities(date, entities)
                observePassiveContext(entities)
                observePassiveComparison(entities)
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }

    fun onDateChange(newDate: LocalDate) {
        _selectedDate.value = newDate
    }

    fun updateActivity(date: LocalDate, hour: Int, activity: String, valence: Int) {
        viewModelScope.launch {
            activityLogRepository.upsert(
                date = date,
                hour = hour,
                text = activity,
                valence = valence
            )
        }
    }

    private fun updateDailyActivities(date: LocalDate, entities: List<ActivityLogEntity>) {
        val hourlyActivities = List(24) { hour ->
            val entity = entities.find { it.hour == hour }
            HourlyActivity(hour, entity?.key.orEmpty(), entity?.activityText ?: "", entity?.valence ?: 0)
        }
        _activities.value = _activities.value + (date to hourlyActivities)
    }

    private fun observePassiveContext(entities: List<ActivityLogEntity>) {
        passiveContextJob?.cancel()
        passiveContextJob = passiveMarkerService.contextForCheckins(entities)
            .onEach { context -> _passiveContextByCheckin.value = context }
            .launchIn(viewModelScope)
    }

    private fun observePassiveComparison(entities: List<ActivityLogEntity>) {
        passiveComparisonJob?.cancel()
        passiveComparisonJob = passiveMarkerService.passiveVsActiveDailyComparison(entities)
            .onEach { points -> _passiveVsActive.value = points }
            .launchIn(viewModelScope)
    }
}

data class HourlyActivity(
    val hour: Int,
    val checkinKey: String,
    val activity: String,
    val valence: Int
)
