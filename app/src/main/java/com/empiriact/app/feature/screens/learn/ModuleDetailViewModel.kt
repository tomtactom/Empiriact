package com.empiriact.app.ui.screens.learn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.CourseRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ModuleDetailViewModel(
    private val courseRepository: CourseRepository,
    private val courseId: String,
    private val moduleId: String
) : ViewModel() {

    val moduleFlow = courseRepository.getModule(courseId, moduleId)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

}
