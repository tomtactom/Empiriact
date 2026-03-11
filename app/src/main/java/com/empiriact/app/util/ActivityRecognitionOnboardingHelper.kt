package com.empiriact.app.util

import android.content.Context
import com.empiriact.app.data.SettingsRepository

object ActivityRecognitionOnboardingHelper {

    fun hasPermission(context: Context): Boolean =
        ActivityRecognitionPermissionUtils.hasPermission(context)

    fun isPermissionRequired(): Boolean =
        ActivityRecognitionPermissionUtils.isPermissionRequired()

    suspend fun onPermissionDenied(settingsRepository: SettingsRepository) {
        settingsRepository.setPassiveStepsEnabled(false)
    }
}
