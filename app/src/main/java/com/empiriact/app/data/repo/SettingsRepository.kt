package com.empiriact.app.data.repo

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "empiriact_settings")

class SettingsRepository(private val context: Context) {

    private val KEY_HOURLY_PROMPTS = booleanPreferencesKey("hourly_prompts_enabled")

    val hourlyPromptsEnabled: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KEY_HOURLY_PROMPTS] ?: true }

    suspend fun setHourlyPromptsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HOURLY_PROMPTS] = enabled
        }
    }
}
