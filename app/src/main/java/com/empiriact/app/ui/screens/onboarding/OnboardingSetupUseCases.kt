package com.empiriact.app.ui.screens.onboarding

import com.empiriact.app.data.SettingsRepository
import kotlinx.coroutines.flow.first

class LoadOnboardingSetupStateUseCase(
    private val settingsRepository: SettingsRepository,
    private val permissionStateProvider: PermissionStateProvider
) {
    suspend operator fun invoke(): OnboardingSetupSnapshot {
        return OnboardingSetupSnapshot(
            notificationsEnabled = permissionStateProvider.areNotificationsEnabled(),
            batteryOptimizationDisabled = permissionStateProvider.isIgnoringBatteryOptimizations(),
            activityRecognitionRequired = permissionStateProvider.isActivityRecognitionPermissionRequired(),
            activityRecognitionEnabled = permissionStateProvider.hasActivityRecognitionPermission(),
            dataDonationEnabled = settingsRepository.dataDonationEnabled.first(),
            passiveMarkersOptIn = settingsRepository.passiveMarkersOptIn.first()
        )
    }
}

class SyncPassiveMarkerPermissionUseCase(
    private val settingsRepository: SettingsRepository,
    private val permissionStateProvider: PermissionStateProvider
) {
    suspend operator fun invoke() {
        if (!permissionStateProvider.isActivityRecognitionPermissionRequired()) return

        if (!permissionStateProvider.hasActivityRecognitionPermission() && settingsRepository.passiveStepsCollectionEnabled()) {
            settingsRepository.setPassiveStepsEnabled(false)
        }
    }
}

data class OnboardingSetupSnapshot(
    val notificationsEnabled: Boolean,
    val batteryOptimizationDisabled: Boolean,
    val activityRecognitionRequired: Boolean,
    val activityRecognitionEnabled: Boolean,
    val dataDonationEnabled: Boolean,
    val passiveMarkersOptIn: Boolean
)
