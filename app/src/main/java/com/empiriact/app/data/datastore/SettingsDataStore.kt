package com.empiriact.app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.empiriact.app.data.model.CourseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    private val courseStateKey = stringPreferencesKey("course_state")

    val courseState: Flow<CourseState?> = context.dataStore.data
        .map { preferences ->
            preferences[courseStateKey]?.let {
                Json.decodeFromString<CourseState>(it)
            }
        }

    suspend fun saveCourseState(courseState: CourseState) {
        context.dataStore.edit {
            it[courseStateKey] = Json.encodeToString(courseState)
        }
    }
}
