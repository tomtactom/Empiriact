package com.empiriact.app.ui.screens.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.CourseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class LearnViewModel(private val courseRepository: CourseRepository) : ViewModel() {

    val courseFlow = courseRepository.getFullCourse()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

}
