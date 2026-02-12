package com.empiriact.app.ui.screens.resources.methods

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.DistractionSkillRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// TODO: This is now obsolete and should be removed in a future refactoring
data class DistractionActivity(val name: String, val isSelected: Boolean = false)

// TODO: This is now obsolete and should be removed in a future refactoring
internal val allActivities = listOf(
    DistractionActivity("Bügeln"),
    DistractionActivity("Wohnung dekorieren"),
    DistractionActivity("Etwas lesen"),
    DistractionActivity("Hörbuch oder Podcast hören"),
    DistractionActivity("Musik hören"),
    DistractionActivity("Tanzen"),
    DistractionActivity("Sport machen"),
    DistractionActivity("Entspannungsübungen machen"),
    DistractionActivity("Rätsel lösen"),
    DistractionActivity("Textnachrichten schreiben"),
    DistractionActivity("Kochen"),
    DistractionActivity("Malen"),
    DistractionActivity("Basteln"),
    DistractionActivity("Handarbeiten"),
    DistractionActivity("Telefonieren"),
    DistractionActivity("Schreiben (z. B. Geschichten, Tagebuch)"),
    DistractionActivity("Boden wischen"),
    DistractionActivity("Staubsaugen"),
    DistractionActivity("Staub wischen"),
    DistractionActivity("Ein Bad nehmen oder duschen"),
    DistractionActivity("Fenster putzen"),
    DistractionActivity("Aufräumen"),
    DistractionActivity("Filme ansehen"),
    DistractionActivity("Fotografieren"),
    DistractionActivity("Singen"),
    DistractionActivity("Tiere pflegen")
)

class DistractionSkillViewModel(private val repository: DistractionSkillRepository) : ViewModel() {

    val activities: StateFlow<List<DistractionActivity>> = repository.selectedActivities
        .map { selectedActivityNames ->
            allActivities.map { activity ->
                activity.copy(isSelected = selectedActivityNames.contains(activity.name))
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = allActivities
        )

    fun toggleActivity(activityName: String) {
        viewModelScope.launch {
            val currentSelection = activities.value.filter { it.isSelected }.map { it.name }.toMutableSet()
            if (currentSelection.contains(activityName)) {
                currentSelection.remove(activityName)
            } else {
                currentSelection.add(activityName)
            }
            repository.saveSelectedActivities(currentSelection)
        }
    }
}