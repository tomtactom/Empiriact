package com.empiriact.app.ui.screens.growth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.CourseRepository
import com.empiriact.app.data.datastore.SettingsDataStore
import com.empiriact.app.data.model.CourseState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GrowthViewModel(
    private val courseRepository: CourseRepository,
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val courseStateFlow = courseRepository.getFullCourse()
        .combine(settingsDataStore.courseState) { course, courseState ->
            Pair(course, courseState)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, Pair(null, null))

    fun saveCourseState(courseState: CourseState) {
        viewModelScope.launch {
            settingsDataStore.saveCourseState(courseState)
        }
    }
}
