package com.empiriact.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

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
        val BA_INPUT_MODE = stringPreferencesKey("ba_input_mode")
        val BA_BASELINE_START = stringPreferencesKey("ba_baseline_start")
        val BA_BASELINE_DAYS = intPreferencesKey("ba_baseline_days")
        val BA_ACTIVITY_CRITERIA_ACKNOWLEDGED = booleanPreferencesKey("ba_activity_criteria_acknowledged")
        val BA_ACTIVITY_PREFERENCE_TAGS = stringSetPreferencesKey("ba_activity_preference_tags")
        val BA_MAINTENANCE_STATUS = stringPreferencesKey("ba_maintenance_status")
        val BA_MAINTENANCE_INTERVAL = stringPreferencesKey("ba_maintenance_interval")
        val BA_MAINTENANCE_LAST_REMINDER_DATE = stringPreferencesKey("ba_maintenance_last_reminder_date")
        val PASSIVE_MARKERS_OPT_IN = booleanPreferencesKey("passive_markers_opt_in")
        val PASSIVE_STEPS_ENABLED = booleanPreferencesKey("passive_steps_enabled")
        val PASSIVE_SLEEP_ENABLED = booleanPreferencesKey("passive_sleep_enabled")
        val PASSIVE_SCREEN_TIME_PROXIMITY_ENABLED = booleanPreferencesKey("passive_screen_time_proximity_enabled")
    }

    enum class ThemeMode {
        SYSTEM,
        LIGHT,
        DARK
    }

    enum class InputMode(val persistedValue: String) {
        STANDARD("standard"),
        BASELINE("baseline");

        companion object {
            fun fromPersisted(value: String?): InputMode {
                return entries.firstOrNull { it.persistedValue == value } ?: STANDARD
            }
        }
    }

    enum class BaMaintenanceStatus(val persistedValue: String) {
        OFF("off"),
        ACTIVE("active");

        companion object {
            fun fromPersisted(value: String?): BaMaintenanceStatus {
                return entries.firstOrNull { it.persistedValue == value } ?: OFF
            }
        }
    }

    enum class BaMaintenanceInterval(val persistedValue: String, val requiredDays: Long) {
        DAILY("daily", 1),
        BIWEEKLY("biweekly", 14),
        MONTHLY("monthly", 30);

        companion object {
            fun fromPersisted(value: String?): BaMaintenanceInterval {
                return entries.firstOrNull { it.persistedValue == value } ?: DAILY
            }
        }
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

    val baInputMode: Flow<InputMode> = dataStore.data
        .map { preferences ->
            InputMode.fromPersisted(preferences[PreferencesKeys.BA_INPUT_MODE])
        }

    suspend fun setBaInputMode(mode: InputMode) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.BA_INPUT_MODE] = mode.persistedValue
        }
    }

    val baBaselineStart: Flow<LocalDate?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.BA_BASELINE_START]?.let { LocalDate.parse(it) }
        }

    suspend fun setBaBaselineStart(date: LocalDate?) {
        dataStore.edit { settings ->
            if (date == null) {
                settings.remove(PreferencesKeys.BA_BASELINE_START)
            } else {
                settings[PreferencesKeys.BA_BASELINE_START] = date.toString()
            }
        }
    }

    val baBaselineDays: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.BA_BASELINE_DAYS] ?: 7
        }

    suspend fun setBaBaselineDays(days: Int) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.BA_BASELINE_DAYS] = days
        }
    }

    val baActivityCriteriaAcknowledged: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.BA_ACTIVITY_CRITERIA_ACKNOWLEDGED] ?: false
        }

    suspend fun setBaActivityCriteriaAcknowledged(acknowledged: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.BA_ACTIVITY_CRITERIA_ACKNOWLEDGED] = acknowledged
        }
    }

    val baActivityPreferenceTags: Flow<Set<String>> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.BA_ACTIVITY_PREFERENCE_TAGS] ?: emptySet()
        }

    suspend fun setBaActivityPreferenceTags(tags: Set<String>) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.BA_ACTIVITY_PREFERENCE_TAGS] = tags
        }
    }

    val baMaintenanceStatus: Flow<BaMaintenanceStatus> = dataStore.data
        .map { preferences ->
            BaMaintenanceStatus.fromPersisted(preferences[PreferencesKeys.BA_MAINTENANCE_STATUS])
        }

    suspend fun setBaMaintenanceStatus(status: BaMaintenanceStatus) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.BA_MAINTENANCE_STATUS] = status.persistedValue
        }
    }

    val baMaintenanceInterval: Flow<BaMaintenanceInterval> = dataStore.data
        .map { preferences ->
            BaMaintenanceInterval.fromPersisted(preferences[PreferencesKeys.BA_MAINTENANCE_INTERVAL])
        }

    suspend fun setBaMaintenanceInterval(interval: BaMaintenanceInterval) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.BA_MAINTENANCE_INTERVAL] = interval.persistedValue
        }
    }

    val baMaintenanceLastReminderDate: Flow<LocalDate?> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.BA_MAINTENANCE_LAST_REMINDER_DATE]?.let(LocalDate::parse)
        }

    suspend fun setBaMaintenanceLastReminderDate(date: LocalDate?) {
        dataStore.edit { settings ->
            if (date == null) {
                settings.remove(PreferencesKeys.BA_MAINTENANCE_LAST_REMINDER_DATE)
            } else {
                settings[PreferencesKeys.BA_MAINTENANCE_LAST_REMINDER_DATE] = date.toString()
            }
        }
    }

    val passiveMarkersOptIn: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.PASSIVE_MARKERS_OPT_IN] ?: false
        }

    suspend fun setPassiveMarkersOptIn(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.PASSIVE_MARKERS_OPT_IN] = enabled
            if (!enabled) {
                settings[PreferencesKeys.PASSIVE_STEPS_ENABLED] = false
                settings[PreferencesKeys.PASSIVE_SLEEP_ENABLED] = false
                settings[PreferencesKeys.PASSIVE_SCREEN_TIME_PROXIMITY_ENABLED] = false
            }
        }
    }

    val passiveStepsEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.PASSIVE_STEPS_ENABLED] ?: false
        }

    suspend fun setPassiveStepsEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.PASSIVE_STEPS_ENABLED] = enabled
        }
    }

    val passiveSleepEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.PASSIVE_SLEEP_ENABLED] ?: false
        }

    suspend fun setPassiveSleepEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.PASSIVE_SLEEP_ENABLED] = enabled
        }
    }

    val passiveScreenTimeProximityEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.PASSIVE_SCREEN_TIME_PROXIMITY_ENABLED] ?: false
        }

    suspend fun setPassiveScreenTimeProximityEnabled(enabled: Boolean) {
        dataStore.edit { settings ->
            settings[PreferencesKeys.PASSIVE_SCREEN_TIME_PROXIMITY_ENABLED] = enabled
        }
    }

    suspend fun clearAllSettings() {
        dataStore.edit { settings ->
            settings.clear()
        }
    }
}
