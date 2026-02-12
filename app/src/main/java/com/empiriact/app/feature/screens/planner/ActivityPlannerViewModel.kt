package com.empiriact.app.ui.screens.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.PlannedActivity
import com.empiriact.app.data.Routine
import com.empiriact.app.data.RoutineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ActivityPlannerViewModel(
    private val routineRepository: RoutineRepository
) : ViewModel() {
    private val _activities = MutableStateFlow<List<PlannedActivity>>(emptyList())
    val activities: StateFlow<List<PlannedActivity>> = _activities.asStateFlow()

    private var valueName: String? = null

    fun loadActivitiesForValue(valueName: String) {
        this.valueName = valueName
        viewModelScope.launch {
            routineRepository.getAll().map { routines ->
                routines
                    .filter { it.name == valueName }
                    .map { PlannedActivity(it.id.toInt(), it.description) }
            }.collect {
                _activities.value = it
            }
        }
    }

    fun addActivity(description: String) {
        val value = valueName ?: return
        viewModelScope.launch {
            routineRepository.insert(Routine(name = value, description = description))
        }
    }
}
