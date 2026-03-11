package com.empiriact.app.notifications

import com.empiriact.app.data.SettingsRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit

internal object HourlyPromptPolicy {

    fun shouldSendReminder(
        inputMode: SettingsRepository.InputMode,
        maintenanceStatus: SettingsRepository.BaMaintenanceStatus,
        maintenanceInterval: SettingsRepository.BaMaintenanceInterval,
        lastReminderDate: LocalDate?,
        today: LocalDate = LocalDate.now()
    ): Boolean {
        if (inputMode == SettingsRepository.InputMode.BASELINE) {
            return true
        }

        if (maintenanceStatus != SettingsRepository.BaMaintenanceStatus.ACTIVE) {
            return true
        }

        val reminderDate = lastReminderDate ?: return true
        val daysSinceReminder = ChronoUnit.DAYS.between(reminderDate, today)
        return daysSinceReminder >= maintenanceInterval.requiredDays
    }
}
