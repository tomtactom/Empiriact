package com.empiriact.app.services

import java.time.ZonedDateTime

interface PassiveSnapshotRefresher {
    suspend fun refreshSnapshot(now: ZonedDateTime = ZonedDateTime.now()): Boolean
}

class StepTrackingSnapshotRefresher(
    private val stepTrackingService: StepTrackingService
) : PassiveSnapshotRefresher {
    override suspend fun refreshSnapshot(now: ZonedDateTime): Boolean {
        return stepTrackingService.captureHourlySnapshot(now)
    }
}
