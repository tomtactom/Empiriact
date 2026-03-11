package com.empiriact.app.ui.screens.onboarding

import android.content.Context
import android.os.PowerManager
import androidx.core.app.NotificationManagerCompat
import com.empiriact.app.util.ActivityRecognitionOnboardingHelper

interface PermissionStateProvider {
    fun areNotificationsEnabled(): Boolean
    fun isIgnoringBatteryOptimizations(): Boolean
    fun isActivityRecognitionPermissionRequired(): Boolean
    fun hasActivityRecognitionPermission(): Boolean
}

class AndroidPermissionStateProvider(
    private val context: Context
) : PermissionStateProvider {
    override fun areNotificationsEnabled(): Boolean =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    override fun isIgnoringBatteryOptimizations(): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }

    override fun isActivityRecognitionPermissionRequired(): Boolean =
        ActivityRecognitionOnboardingHelper.isPermissionRequired()

    override fun hasActivityRecognitionPermission(): Boolean =
        ActivityRecognitionOnboardingHelper.hasPermission(context)
}
