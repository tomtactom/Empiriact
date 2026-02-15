package com.empiriact.app.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.empiriact.app.MainActivity
import com.empiriact.app.R
import com.empiriact.app.data.SettingsRepository
import kotlinx.coroutines.flow.first

class HourlyPromptWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val enabled = SettingsRepository(applicationContext).hourlyPromptsEnabled.first()
        if (enabled) {
            NotificationChannels.ensure(applicationContext)
            showNotification()
        }

        HourlyPromptScheduler.ensureScheduled(applicationContext)
        return Result.success()
    }

    private fun showNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, NotificationChannels.CHANNEL_HOURLY)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText("Kurzer Check-in: Hauptaktivität der Stunde notieren 🙂")
            .setContentIntent(pi)
            .setAutoCancel(true)

        NotificationManagerCompat.from(applicationContext).notify(2001, builder.build())
    }
}
