package com.empiriact.app.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId

object HourlyPromptScheduler {

    private const val UNIQUE_WORK_NAME = "empiriact_hourly_prompt"

    fun ensureScheduled(context: Context) {
        val delay = computeDelayUntilNextPrompt()
        val req = OneTimeWorkRequestBuilder<HourlyPromptWorker>()
            .setInitialDelay(delay)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(UNIQUE_WORK_NAME, ExistingWorkPolicy.REPLACE, req)
    }

    fun computeDelayUntilNextPrompt(
        now: LocalDateTime = LocalDateTime.now(),
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Duration {
        val targetMinute = 55 // Reminder at the end of the hour
        val next = when {
            now.minute < targetMinute -> now.withMinute(targetMinute).withSecond(0).withNano(0)
            else -> now.plusHours(1).withMinute(targetMinute).withSecond(0).withNano(0)
        }
        return Duration.between(now, next).coerceAtLeast(Duration.ofSeconds(5))
    }
}
