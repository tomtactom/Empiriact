package com.empiriact.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannels {
    const val CHANNEL_HOURLY = "hourly_prompts"

    fun ensure(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val mgr = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            CHANNEL_HOURLY,
            "Stündliche Reflexion",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Impulse zur stündlichen Aktivitäts-Notiz"
        }
        mgr.createNotificationChannel(channel)
    }
}
