package com.empiriact.app.ui.screens.evaluations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.Evaluation
import com.empiriact.app.data.EvaluationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EvaluationViewModel(private val repository: EvaluationRepository) : ViewModel() {

    val evaluations: StateFlow<List<Evaluation>> = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addEvaluation(score: Int, type: String) {
        viewModelScope.launch {
            repository.insert(Evaluation(score = score, type = type))
        }
    }
}
