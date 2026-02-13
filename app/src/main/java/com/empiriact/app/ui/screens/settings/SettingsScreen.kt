package com.empiriact.app.ui.screens.settings

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.EmpiriactApplication
import com.empiriact.app.data.SettingsRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    val application = context.applicationContext as EmpiriactApplication

    val settingsViewModel: SettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SettingsViewModel(application, application.settingsRepository) as T
            }
        }
    )
    val jsonExportViewModel: JsonExportViewModel = viewModel(factory = application.viewModelFactory)

    val exportState by jsonExportViewModel.exportState.collectAsState()
    val themeMode by settingsViewModel.themeMode.collectAsState()
    val hourlyPromptsEnabled by settingsViewModel.hourlyPromptsEnabled.collectAsState()
    val dataDonationEnabled by settingsViewModel.dataDonationEnabled.collectAsState()
    val statusMessage by settingsViewModel.statusMessage.collectAsState()

    var isThemeMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Einstellungen", style = MaterialTheme.typography.headlineMedium)

        Text("Darstellung", style = MaterialTheme.typography.headlineSmall)
        ExposedDropdownMenuBox(
            expanded = isThemeMenuExpanded,
            onExpandedChange = { isThemeMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = when (themeMode) {
                    SettingsRepository.ThemeMode.SYSTEM -> "Systemeinstellung"
                    SettingsRepository.ThemeMode.LIGHT -> "Hell"
                    SettingsRepository.ThemeMode.DARK -> "Dunkel"
                },
                onValueChange = {},
                readOnly = true,
                label = { Text("Design") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isThemeMenuExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isThemeMenuExpanded,
                onDismissRequest = { isThemeMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Systemeinstellung") },
                    onClick = {
                        settingsViewModel.setThemeMode(SettingsRepository.ThemeMode.SYSTEM)
                        isThemeMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Hell") },
                    onClick = {
                        settingsViewModel.setThemeMode(SettingsRepository.ThemeMode.LIGHT)
                        isThemeMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Dunkel") },
                    onClick = {
                        settingsViewModel.setThemeMode(SettingsRepository.ThemeMode.DARK)
                        isThemeMenuExpanded = false
                    }
                )
            }
        }

        Text("Benachrichtigungen & Datenschutz", style = MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Stündliche Erinnerungen")
            Switch(
                checked = hourlyPromptsEnabled,
                onCheckedChange = settingsViewModel::setHourlyPromptsEnabled
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Anonyme Datenspende")
            Switch(
                checked = dataDonationEnabled,
                onCheckedChange = settingsViewModel::setDataDonationEnabled
            )
        }

        Text("Daten", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { jsonExportViewModel.exportToJson() }) {
            Text("Als JSON exportieren")
        }

        OutlinedButton(onClick = settingsViewModel::clearCache) {
            Text("Cache löschen")
        }

        OutlinedButton(onClick = settingsViewModel::resetAppToFactoryDefaults) {
            Text("Alle App-Daten löschen (Werkseinstellungen)")
        }

        OutlinedButton(onClick = { activity?.finishAffinity() }) {
            Text("App schließen")
        }

        when (val state = exportState) {
            is ExportState.Idle -> Unit
            is ExportState.Loading -> CircularProgressIndicator()
            is ExportState.Success -> Text("Export erfolgreich! Die Daten wurden in die Zwischenablage kopiert.")
            is ExportState.Error -> Text("Fehler beim Export: ${state.message}")
        }

        if (statusMessage != null) {
            Text(statusMessage.orEmpty())
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedButton(onClick = settingsViewModel::clearStatusMessage) {
                Text("Hinweis ausblenden")
            }
        }
    }
}
