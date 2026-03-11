package com.empiriact.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.BuildConfig
import com.empiriact.app.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val shouldShowOnboarding: StateFlow<Boolean?> = settingsRepository.shouldShowOnboarding(BuildConfig.VERSION_CODE)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null // null represents the loading state
        )

    fun markOnboardingCompletedForCurrentVersion() {
        viewModelScope.launch {
            settingsRepository.markOnboardingCompletedForVersion(BuildConfig.VERSION_CODE)
        }
    }
}
