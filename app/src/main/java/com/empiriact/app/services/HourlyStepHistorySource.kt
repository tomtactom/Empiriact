package com.empiriact.app.services

import java.time.ZonedDateTime

data class HourBucket(
    val start: ZonedDateTime,
    val stepCount: Int,
    val isEstimated: Boolean = false
)

sealed class HourlyStepHistoryReadResult {
    data class Success(val buckets: List<HourBucket>) : HourlyStepHistoryReadResult()
    data object SourceEmpty : HourlyStepHistoryReadResult()
    data object PermissionMissing : HourlyStepHistoryReadResult()
    data object SyncError : HourlyStepHistoryReadResult()
}

interface HourlyStepHistorySource {
    suspend fun readHourlySteps(start: ZonedDateTime, end: ZonedDateTime): HourlyStepHistoryReadResult
}

class NoOpHourlyStepHistorySource : HourlyStepHistorySource {
    override suspend fun readHourlySteps(start: ZonedDateTime, end: ZonedDateTime): HourlyStepHistoryReadResult =
        HourlyStepHistoryReadResult.SourceEmpty
}
