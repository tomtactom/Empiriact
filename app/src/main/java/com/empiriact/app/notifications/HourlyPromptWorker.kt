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
import com.empiriact.app.data.db.EmpiriactDatabase
import com.empiriact.app.data.repo.PassiveMarkerRepository
import com.empiriact.app.services.SensorStepCounterSource
import com.empiriact.app.services.StepTrackingService
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class HourlyPromptWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val settingsRepository = SettingsRepository(applicationContext)
        val passiveMarkerRepository = PassiveMarkerRepository(
            EmpiriactDatabase.getDatabase(applicationContext).passiveMarkerDao()
        )
        val stepTrackingService = StepTrackingService(
            settingsRepository = settingsRepository,
            passiveMarkerRepository = passiveMarkerRepository,
            stepCounterSource = SensorStepCounterSource(applicationContext)
        )
        stepTrackingService.captureHourlySnapshot()

        val enabled = settingsRepository.hourlyPromptsEnabled.first()
        if (enabled && shouldSendReminder(settingsRepository)) {
            NotificationChannels.ensure(applicationContext)
            showNotification()
            settingsRepository.setBaMaintenanceLastReminderDate(LocalDate.now())
        }

        HourlyPromptScheduler.ensureScheduled(applicationContext)
        return Result.success()
    }

    private suspend fun shouldSendReminder(settingsRepository: SettingsRepository): Boolean {
        return HourlyPromptPolicy.shouldSendReminder(
            inputMode = settingsRepository.baInputMode.first(),
            maintenanceStatus = settingsRepository.baMaintenanceStatus.first(),
            maintenanceInterval = settingsRepository.baMaintenanceInterval.first(),
            lastReminderDate = settingsRepository.baMaintenanceLastReminderDate.first(),
            today = LocalDate.now()
        )
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
