package com.empiriact.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "evaluations")
data class Evaluation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val score: Int,
    val type: String,
    val timestamp: Long = System.currentTimeMillis()
)
