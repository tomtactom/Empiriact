package com.empiriact.app.ui.screens.reflect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.Routine
import com.empiriact.app.data.RoutineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReflectViewModel(private val repository: RoutineRepository) : ViewModel() {

    val routines: StateFlow<List<Routine>> = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addRoutine(name: String, description: String) {
        viewModelScope.launch {
            repository.insert(Routine(name = name, description = description))
        }
    }
}
