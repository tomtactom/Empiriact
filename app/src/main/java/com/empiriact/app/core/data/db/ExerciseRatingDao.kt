package com.empiriact.app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.empiriact.app.data.ExerciseAverageRating
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseRatingDao {

    @Insert
    suspend fun insert(rating: ExerciseRatingEntity)

    @Query("SELECT * FROM exercise_ratings ORDER BY timestamp DESC")
    fun getAllRatings(): Flow<List<ExerciseRatingEntity>>

    @Query("SELECT exerciseId, AVG(rating) as averageRating FROM exercise_ratings GROUP BY exerciseId")
    fun getAllAverageRatings(): Flow<List<ExerciseAverageRating>>
}
