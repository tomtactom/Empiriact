package com.empiriact.app.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_ratings")
data class ExerciseRatingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exerciseId: String,
    val rating: Int,
    val timestamp: Long = System.currentTimeMillis()
)
