package com.empiriact.app.data

import androidx.compose.ui.graphics.vector.ImageVector

enum class QuestionnaireState {
    ON, PAUSED, OFF
}

data class Questionnaire(
    val id: String,
    val title: String,
    val description: String,
    val state: QuestionnaireState,
    val icon: ImageVector? = null
)
