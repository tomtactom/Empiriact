package com.empiriact.app.ui.screens.settings

import android.app.ActivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.EmpiriactApplication
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.util.ActivityRecognitionPermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

    val passiveMarkersOptIn: StateFlow<Boolean> = settingsRepository.passiveMarkersOptIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val passiveStepsEnabled: StateFlow<Boolean> = settingsRepository.passiveStepsEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val passiveSleepEnabled: StateFlow<Boolean> = settingsRepository.passiveSleepEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val passiveScreenTimeProximityEnabled: StateFlow<Boolean> = settingsRepository.passiveScreenTimeProximityEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )


    val baInputMode: StateFlow<SettingsRepository.InputMode> = settingsRepository.baInputMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsRepository.InputMode.STANDARD
        )

    val baBaselineDays: StateFlow<Int> = settingsRepository.baBaselineDays
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 7
        )

    val baActivityCriteriaAcknowledged: StateFlow<Boolean> = settingsRepository.baActivityCriteriaAcknowledged
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val baActivityPreferenceTags: StateFlow<Set<String>> = settingsRepository.baActivityPreferenceTags
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    val baselineEnabled: StateFlow<Boolean> = baInputMode
        .combine(baBaselineDays) { mode, _ -> mode == SettingsRepository.InputMode.BASELINE }
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

    fun setPassiveMarkersOptIn(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPassiveMarkersOptIn(enabled)
        }
    }

    fun setPassiveStepsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPassiveStepsEnabled(enabled)
        }
    }

    fun hasActivityRecognitionPermission(): Boolean =
        ActivityRecognitionPermissionUtils.hasPermission(application)

    fun isActivityRecognitionPermissionRequired(): Boolean =
        ActivityRecognitionPermissionUtils.isPermissionRequired()

    fun onPassiveStepsPermissionDenied() {
        viewModelScope.launch {
            settingsRepository.setPassiveStepsEnabled(false)
            _statusMessage.value =
                "Schrittzahl benötigt die Aktivitätserkennung. Ohne diese Berechtigung bleibt die Funktion aus."
        }
    }

    fun syncPassiveStepsPermissionState() {
        if (!isActivityRecognitionPermissionRequired()) {
            return
        }

        viewModelScope.launch {
            if (settingsRepository.passiveStepsCollectionEnabled() && !hasActivityRecognitionPermission()) {
                settingsRepository.setPassiveStepsEnabled(false)
                _statusMessage.value =
                    "Aktivitätserkennung wurde entzogen. Schritt-Tracking wurde aus Datenschutzgründen gestoppt."
            }
        }
    }

    fun setPassiveSleepEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPassiveSleepEnabled(enabled)
        }
    }

    fun setPassiveScreenTimeProximityEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPassiveScreenTimeProximityEnabled(enabled)
        }
    }

    fun setBaActivityCriteriaAcknowledged(acknowledged: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBaActivityCriteriaAcknowledged(acknowledged)
        }
    }

    fun toggleBaActivityPreferenceTag(tag: String) {
        viewModelScope.launch {
            val updatedTags = baActivityPreferenceTags.value.toMutableSet().apply {
                if (contains(tag)) remove(tag) else add(tag)
            }
            settingsRepository.setBaActivityPreferenceTags(updatedTags)
        }
    }

    fun setBaselineEnabled(enabled: Boolean) {
        viewModelScope.launch {
            if (enabled) {
                settingsRepository.setBaInputMode(SettingsRepository.InputMode.BASELINE)
                if (settingsRepository.baBaselineStart.first() == null) {
                    settingsRepository.setBaBaselineStart(java.time.LocalDate.now())
                }
            } else {
                settingsRepository.setBaInputMode(SettingsRepository.InputMode.STANDARD)
            }
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
            val resetSuccessful = application
                .getSystemService(ActivityManager::class.java)
                ?.clearApplicationUserData() == true

            _statusMessage.value = if (resetSuccessful) {
                "App-Daten wurden auf Werkseinstellungen zurückgesetzt."
            } else {
                "App-Daten konnten nicht zurückgesetzt werden."
            }
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
