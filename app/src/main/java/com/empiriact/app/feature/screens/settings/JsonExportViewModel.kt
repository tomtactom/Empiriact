package com.empiriact.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.repo.ActivityLogRepository
import com.empiriact.app.services.JsonExportService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JsonExportViewModel(
    private val activityLogRepository: ActivityLogRepository,
    private val jsonExportService: JsonExportService
) : ViewModel() {

    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState = _exportState.asStateFlow()

    fun exportToJson() {
        viewModelScope.launch {
            _exportState.value = ExportState.Loading
            activityLogRepository.getAll().collect { logs ->
                val json = jsonExportService.export(logs)
                _exportState.value = ExportState.Success(json)
            }
        }
    }
}

sealed class ExportState {
    object Idle : ExportState()
    object Loading : ExportState()
    data class Success(val json: String) : ExportState()
    data class Error(val message: String) : ExportState()
}
