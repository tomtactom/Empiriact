package com.empiriact.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(context: Context) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val HOURLY_PROMPTS_ENABLED = booleanPreferencesKey("hourly_prompts_enabled")
        val DATA_DONATION_ENABLED = booleanPreferencesKey("data_donation_enabled")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val TODAY_INTRO_COMPLETED = booleanPreferencesKey("today_intro_completed")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    enum class ThemeMode {
        SYSTEM,
        LIGHT,
        DARK
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

    val todayIntroCompleted: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.TODAY_INTRO_COMPLETED] ?: false
        }

    suspend fun setTodayIntroCompleted(completed: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.TODAY_INTRO_COMPLETED] = completed
        }
    }

    fun completeOnboarding() {
        scope.launch {
            setOnboardingCompleted(true)
        }
    }

    val themeMode: Flow<ThemeMode> = dataStore.data
        .map { preferences ->
            ThemeMode.entries.firstOrNull { it.name == preferences[PreferencesKeys.THEME_MODE] }
                ?: ThemeMode.SYSTEM
        }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun clearAllSettings() {
        dataStore.edit { settings ->
            settings.clear()
        }
    }
}
