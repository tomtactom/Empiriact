package com.empiriact.app.ui.screens.rating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.db.ExerciseRatingDao
import com.empiriact.app.data.db.ExerciseRatingEntity
import kotlinx.coroutines.launch

class ExerciseRatingViewModel(private val exerciseRatingDao: ExerciseRatingDao) : ViewModel() {

    fun saveRating(exerciseId: String, rating: Int) {
        viewModelScope.launch {
            val entity = ExerciseRatingEntity(
                exerciseId = exerciseId,
                rating = rating
            )
            exerciseRatingDao.insert(entity)
        }
    }
}
