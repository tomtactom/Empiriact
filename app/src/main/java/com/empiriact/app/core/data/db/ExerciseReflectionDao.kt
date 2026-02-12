package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseReflectionDao {
    @Insert
    suspend fun insert(reflection: ExerciseReflectionEntity)

    @Query("SELECT * FROM exercise_reflections WHERE exerciseId = :exerciseId ORDER BY timestamp DESC")
    suspend fun getReflectionsByExerciseId(exerciseId: String): List<ExerciseReflectionEntity>

    @Query("SELECT * FROM exercise_reflections ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getLatestReflections(limit: Int): List<ExerciseReflectionEntity>
}
