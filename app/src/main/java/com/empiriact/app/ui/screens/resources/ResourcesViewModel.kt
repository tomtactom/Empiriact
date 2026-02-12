package com.empiriact.app.ui.screens.resources

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.Resource
import com.empiriact.app.data.ResourceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ResourcesViewModel(private val repository: ResourceRepository) : ViewModel() {

    val resources: StateFlow<List<Resource>> = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addResource(name: String, description: String) {
        viewModelScope.launch {
            repository.insert(Resource(name = name, description = description))
        }
    }
}
