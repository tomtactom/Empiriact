package com.empiriact.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CourseState(
    val completedModuleIds: Set<String> = emptySet()
)
