package com.empiriact.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(context: Context) {
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val HOURLY_PROMPTS_ENABLED = booleanPreferencesKey("hourly_prompts_enabled")
        val DATA_DONATION_ENABLED = booleanPreferencesKey("data_donation_enabled")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    val hourlyPromptsEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.HOURLY_PROMPTS_ENABLED] ?: true
        }

    suspend fun setHourlyPromptsEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.HOURLY_PROMPTS_ENABLED] = enabled
        }
    }

    val dataDonationEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DATA_DONATION_ENABLED] ?: false
        }

    suspend fun setDataDonationEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.DATA_DONATION_ENABLED] = enabled
        }
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }
}
