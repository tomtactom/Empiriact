package com.empiriact.app.data

import com.empiriact.app.data.db.ExerciseAverageRating
import com.empiriact.app.data.db.ExerciseRatingDao
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(private val exerciseRatingDao: ExerciseRatingDao) {

    fun getAllExercisesWithRatings(): Flow<List<ExerciseAverageRating>> {
        return exerciseRatingDao.getAllAverageRatings()
    }
}
