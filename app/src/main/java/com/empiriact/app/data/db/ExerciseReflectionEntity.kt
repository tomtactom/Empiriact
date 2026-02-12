package com.empiriact.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_reflections")
data class ExerciseReflectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: String,
    val reflection: String,
    val timestamp: Long = System.currentTimeMillis()
)
