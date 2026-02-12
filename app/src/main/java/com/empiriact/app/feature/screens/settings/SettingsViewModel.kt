package com.empiriact.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val hourlyPromptsEnabled: StateFlow<Boolean> = settingsRepository.hourlyPromptsEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun setHourlyPromptsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHourlyPromptsEnabled(enabled)
        }
    }
}
