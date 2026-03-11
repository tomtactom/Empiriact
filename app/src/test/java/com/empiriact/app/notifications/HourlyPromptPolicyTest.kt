package com.empiriact.app.notifications

import com.empiriact.app.data.SettingsRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class HourlyPromptPolicyTest {

    @Test
    fun `baseline mode always sends reminder regardless of maintenance interval`() {
        val shouldSend = HourlyPromptPolicy.shouldSendReminder(
            inputMode = SettingsRepository.InputMode.BASELINE,
            maintenanceStatus = SettingsRepository.BaMaintenanceStatus.ACTIVE,
            maintenanceInterval = SettingsRepository.BaMaintenanceInterval.MONTHLY,
            lastReminderDate = LocalDate.of(2026, 1, 10),
            today = LocalDate.of(2026, 1, 11)
        )

        assertTrue(shouldSend)
    }

    @Test
    fun `active maintenance blocks reminder before interval is reached`() {
        val shouldSend = HourlyPromptPolicy.shouldSendReminder(
            inputMode = SettingsRepository.InputMode.STANDARD,
            maintenanceStatus = SettingsRepository.BaMaintenanceStatus.ACTIVE,
            maintenanceInterval = SettingsRepository.BaMaintenanceInterval.BIWEEKLY,
            lastReminderDate = LocalDate.of(2026, 1, 10),
            today = LocalDate.of(2026, 1, 20)
        )

        assertFalse(shouldSend)
    }
}
