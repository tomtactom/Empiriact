package com.empiriact.app.ui.screens.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.ActivityLogRepository
import com.empiriact.app.data.db.ActivityLogEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate

class InsightsViewModel(
    private val activityLogRepository: ActivityLogRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _activities = MutableStateFlow<Map<LocalDate, List<HourlyActivity>>>(emptyMap())
    val activities: StateFlow<Map<LocalDate, List<HourlyActivity>>> = _activities

    init {
        // Observe changes for the selected date
        _selectedDate.onEach { date ->
            activityLogRepository.getLogsForDay(
                startDate = date,
                endDate = date.plusDays(1)
            ).onEach { entities ->
                val hourlyActivities = List(24) { hour ->
                    val entity = entities.find { it.hour == hour }
                    HourlyActivity(hour, entity?.activityText ?: "", entity?.valence ?: 0)
                }
                _activities.value = _activities.value + (date to hourlyActivities)
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
}

data class HourlyActivity(val hour: Int, val activity: String, val valence: Int)

