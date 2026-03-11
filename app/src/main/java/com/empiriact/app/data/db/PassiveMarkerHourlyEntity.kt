package com.empiriact.app.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "passive_marker_hourly",
    indices = [Index(value = ["localDate", "hour"], unique = true)]
)
data class PassiveMarkerHourlyEntity(
    @PrimaryKey
    val key: String, // "$localDate-$hour"
    val localDate: Int,
    val hour: Int,
    val stepCount: Int? = null,
    val isEstimated: Boolean = false,
    val sleepDurationMinutesPreviousNight: Int? = null,
    val screenTimeMinutesInHour: Int? = null,
    val updatedAtEpochMs: Long
)
