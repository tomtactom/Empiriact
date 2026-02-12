package com.empiriact.app.ui.screens.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.CheckinRepository
import com.empiriact.app.data.Questionnaire
import com.empiriact.app.data.QuestionnaireState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CheckinViewModel(private val repository: CheckinRepository) : ViewModel() {

    val questionnaires: StateFlow<List<Questionnaire>> = repository.getQuestionnaires()
        .map { list -> list.filter { it.state != QuestionnaireState.OFF } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
