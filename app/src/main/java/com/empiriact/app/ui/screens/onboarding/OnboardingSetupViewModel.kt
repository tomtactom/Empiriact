package com.empiriact.app.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingSetupViewModel(
    private val settingsRepository: SettingsRepository,
    private val loadOnboardingSetupState: LoadOnboardingSetupStateUseCase,
    private val syncPassiveMarkerPermission: SyncPassiveMarkerPermissionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingSetupUiState())
    val uiState: StateFlow<OnboardingSetupUiState> = _uiState.asStateFlow()

    init {
        refreshSetupState()
    }

    fun onResume() {
        refreshSetupState()
    }

    fun defer(item: SetupItemType) {
        _uiState.update { state ->
            state.copy(
                setupItems = state.setupItems.copy(
                    notifications = state.setupItems.notifications.withDeferred(item == SetupItemType.NOTIFICATIONS),
                    batteryOptimization = state.setupItems.batteryOptimization.withDeferred(item == SetupItemType.BATTERY_OPTIMIZATION),
                    activityRecognition = state.setupItems.activityRecognition.withDeferred(item == SetupItemType.ACTIVITY_RECOGNITION)
                ),
                dataCollection = if (item == SetupItemType.DATA_COLLECTION) {
                    state.dataCollection.copy(decision = OnboardingDecision.DEFERRED)
                } else {
                    state.dataCollection
                }
            )
        }
    }

    fun setDataDonationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDataDonationEnabled(enabled)
            _uiState.update { state ->
                state.copy(
                    dataCollection = state.dataCollection.copy(
                        dataDonationEnabled = enabled,
                        decision = OnboardingDecision.MADE
                    )
                )
            }
        }
    }

    fun setPassiveMarkersOptIn(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPassiveMarkersOptIn(enabled)
            _uiState.update { state ->
                state.copy(
                    dataCollection = state.dataCollection.copy(
                        passiveMarkersOptIn = enabled,
                        decision = OnboardingDecision.MADE
                    )
                )
            }
        }
    }

    fun onActivityRecognitionDenied() {
        viewModelScope.launch {
            settingsRepository.setPassiveStepsEnabled(false)
        }
    }

    private fun refreshSetupState() {
        viewModelScope.launch {
            syncPassiveMarkerPermission()
            val snapshot = loadOnboardingSetupState()
            _uiState.update { state ->
                state.copy(
                    setupItems = SetupItemsUiState(
                        notifications = SetupItemUiState(snapshot.notificationsEnabled, state.setupItems.notifications.deferred),
                        batteryOptimization = SetupItemUiState(snapshot.batteryOptimizationDisabled, state.setupItems.batteryOptimization.deferred),
                        activityRecognition = SetupItemUiState(
                            enabled = !snapshot.activityRecognitionRequired || snapshot.activityRecognitionEnabled,
                            deferred = state.setupItems.activityRecognition.deferred,
                            required = snapshot.activityRecognitionRequired
                        )
                    ),
                    dataCollection = state.dataCollection.copy(
                        dataDonationEnabled = snapshot.dataDonationEnabled,
                        passiveMarkersOptIn = snapshot.passiveMarkersOptIn
                    )
                )
            }
        }
    }

    class Factory(
        private val settingsRepository: SettingsRepository,
        private val permissionStateProvider: PermissionStateProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return OnboardingSetupViewModel(
                settingsRepository = settingsRepository,
                loadOnboardingSetupState = LoadOnboardingSetupStateUseCase(settingsRepository, permissionStateProvider),
                syncPassiveMarkerPermission = SyncPassiveMarkerPermissionUseCase(settingsRepository, permissionStateProvider)
            ) as T
        }
    }
}

enum class SetupItemType {
    NOTIFICATIONS,
    BATTERY_OPTIMIZATION,
    ACTIVITY_RECOGNITION,
    DATA_COLLECTION
}

data class OnboardingSetupUiState(
    val setupItems: SetupItemsUiState = SetupItemsUiState(),
    val dataCollection: DataCollectionUiState = DataCollectionUiState()
)

data class SetupItemsUiState(
    val notifications: SetupItemUiState = SetupItemUiState(),
    val batteryOptimization: SetupItemUiState = SetupItemUiState(),
    val activityRecognition: SetupItemUiState = SetupItemUiState(required = false)
)

data class SetupItemUiState(
    val enabled: Boolean = false,
    val deferred: Boolean = false,
    val required: Boolean = true
) {
    fun withDeferred(deferred: Boolean): SetupItemUiState = copy(deferred = deferred)
}

data class DataCollectionUiState(
    val dataDonationEnabled: Boolean = false,
    val passiveMarkersOptIn: Boolean = false,
    val decision: OnboardingDecision = OnboardingDecision.NONE
) {
    val done: Boolean
        get() = dataDonationEnabled || passiveMarkersOptIn || decision != OnboardingDecision.NONE
}

enum class OnboardingDecision {
    NONE,
    MADE,
    DEFERRED
}
