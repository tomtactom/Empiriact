package com.empiriact.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "distraction_skill")

class DistractionSkillRepository(private val context: Context) {

    private val selectedActivitiesKey = stringSetPreferencesKey("selected_activities")

    val selectedActivities: Flow<Set<String>> = context.dataStore.data
        .map {
            it[selectedActivitiesKey] ?: emptySet()
        }

    suspend fun saveSelectedActivities(activities: Set<String>) {
        context.dataStore.edit {
            it[selectedActivitiesKey] = activities
        }
    }
}