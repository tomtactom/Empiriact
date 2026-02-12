package com.empiriact.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "gratitude_logs")
data class GratitudeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: LocalDate,
    val entry: String,
    val timestamp: Long = System.currentTimeMillis()
)
