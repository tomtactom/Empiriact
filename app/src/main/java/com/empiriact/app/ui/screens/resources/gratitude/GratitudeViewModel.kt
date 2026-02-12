package com.empiriact.app.ui.screens.resources.gratitude

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.db.GratitudeEntity
import com.empiriact.app.data.repo.GratitudeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class GratitudeViewModel(private val repository: GratitudeRepository) : ViewModel() {

    fun getGratitudeLogForDate(date: LocalDate): StateFlow<GratitudeEntity?> {
        // This is not ideal, but for the sake of simplicity we will use it this way.
        // A better approach would be to have a single source of truth for the selected date.
        return repository.getGratitudeLogForDate(date)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    fun saveGratitudeEntry(entry: String, date: LocalDate) {
        viewModelScope.launch {
            repository.upsertGratitudeLog(entry, date)
        }
    }
}
