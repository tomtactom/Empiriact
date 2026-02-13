package com.empiriact.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.ui.common.ViewModelFactory
import com.empiriact.app.ui.navigation.EmpiriactNavGraph
import com.empiriact.app.ui.theme.EmpiriactTheme

@Composable
fun EmpiriactApp(factory: ViewModelFactory, settingsRepository: SettingsRepository) {
    val themeMode by settingsRepository.themeMode.collectAsState(initial = SettingsRepository.ThemeMode.SYSTEM)
    val darkTheme = when (themeMode) {
        SettingsRepository.ThemeMode.SYSTEM -> isSystemInDarkTheme()
        SettingsRepository.ThemeMode.LIGHT -> false
        SettingsRepository.ThemeMode.DARK -> true
    }

    EmpiriactTheme(darkTheme = darkTheme) {
        EmpiriactNavGraph(factory, settingsRepository)
    }
}
