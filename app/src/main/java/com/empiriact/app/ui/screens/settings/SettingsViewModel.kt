package com.empiriact.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.EmpiriactApplication
import com.empiriact.app.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val application: EmpiriactApplication,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val hourlyPromptsEnabled: StateFlow<Boolean> = settingsRepository.hourlyPromptsEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    val themeMode: StateFlow<SettingsRepository.ThemeMode> = settingsRepository.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsRepository.ThemeMode.SYSTEM
        )

    val dataDonationEnabled: StateFlow<Boolean> = settingsRepository.dataDonationEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    fun setHourlyPromptsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setHourlyPromptsEnabled(enabled)
        }
    }

    fun setThemeMode(mode: SettingsRepository.ThemeMode) {
        viewModelScope.launch {
            settingsRepository.setThemeMode(mode)
        }
    }

    fun setDataDonationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDataDonationEnabled(enabled)
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            application.cacheDir.deleteRecursively()
            _statusMessage.value = "Cache wurde gelöscht."
        }
    }

    fun resetAppToFactoryDefaults() {
        viewModelScope.launch {
            application.clearApplicationUserData()
            _statusMessage.value = "App-Daten wurden auf Werkseinstellungen zurückgesetzt."
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
