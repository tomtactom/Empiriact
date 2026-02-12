package com.empiriact.app.ui.screens.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.CourseRepository
import com.empiriact.app.data.model.Course
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CourseViewModel(private val courseRepository: CourseRepository) : ViewModel() {

    val courseFlow: StateFlow<Course?> = courseRepository.getFullCourse()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

}
