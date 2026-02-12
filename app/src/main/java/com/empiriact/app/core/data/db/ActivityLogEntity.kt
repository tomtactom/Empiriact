package com.empiriact.app.data.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activity_logs",
    indices = [Index(value = ["localDate", "hour"], unique = true)]
)
data class ActivityLogEntity(
    @PrimaryKey
    val key: String,          // "$localDate-$hour"
    val localDate: Int,       // yyyyMMdd
    val hour: Int,            // 0..23
    val activityText: String,
    val valence: Int,         // -2..2
    val updatedAtEpochMs: Long
)
