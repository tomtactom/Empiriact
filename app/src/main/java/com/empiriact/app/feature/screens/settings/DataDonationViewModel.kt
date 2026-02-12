package com.empiriact.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.ActivityLogRepository
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.services.DataDonationService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataDonationViewModel(
    private val activityLogRepository: ActivityLogRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val dataDonationService = DataDonationService()

    val dataDonationEnabled: StateFlow<Boolean> = settingsRepository.dataDonationEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun setDataDonationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDataDonationEnabled(enabled)
            if (enabled) {
                val logs = activityLogRepository.getAll().first()
                dataDonationService.donate(logs)
            }
        }
    }
}
