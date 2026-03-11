package com.empiriact.app.ui.screens.settings

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.EmpiriactApplication
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.ui.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    val application = context.applicationContext as EmpiriactApplication
    val lifecycleOwner = LocalLifecycleOwner.current

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
    val passiveMarkersOptIn by settingsViewModel.passiveMarkersOptIn.collectAsState()
    val passiveStepsEnabled by settingsViewModel.passiveStepsEnabled.collectAsState()
    val stepCounterSensorAvailable = settingsViewModel.stepCounterSensorAvailable
    val passiveSleepEnabled by settingsViewModel.passiveSleepEnabled.collectAsState()
    val passiveStepsDiagnosticMessage by settingsViewModel.passiveStepsDiagnosticMessage.collectAsState()
    val passiveScreenTimeProximityEnabled by settingsViewModel.passiveScreenTimeProximityEnabled.collectAsState()
    val baselineEnabled by settingsViewModel.baselineEnabled.collectAsState()
    val baselineDays by settingsViewModel.baBaselineDays.collectAsState()
    val baCriteriaAcknowledged by settingsViewModel.baActivityCriteriaAcknowledged.collectAsState()
    val baPreferenceTags by settingsViewModel.baActivityPreferenceTags.collectAsState()

    val optionalBaTags = remember {
        listOf("Kleiner Schritt", "Eigeninitiative", "Routine", "Aktive Handlung")
    }

    var isThemeMenuExpanded by remember { mutableStateOf(false) }
    val activityRecognitionPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            settingsViewModel.setPassiveStepsEnabled(true)
        } else {
            settingsViewModel.onPassiveStepsPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        settingsViewModel.syncPassiveStepsPermissionState()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                settingsViewModel.syncPassiveStepsPermissionState()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Passive Marker (nur mit Einwilligung)", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Passive Marker (z. B. Schritte, Schlafdauer, Screen-Time-Nähe) werden ausschließlich lokal verarbeitet. " +
                    "Sie dienen nur als Kontext für deine aktiven Check-ins und ersetzen keine diagnostische Einschätzung. " +
                    "Wenn aktiviert, werden Werte stündlich lokal gespeichert (pro Stunde) und mit aktiven Einträgen verglichen.",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Für Schrittzahl braucht Android zusätzlich die Berechtigung \"Aktivität erkennen\". " +
                    "Ohne Zustimmung bleibt Schritt-Tracking deaktiviert; du kannst weiterhin alle anderen Funktionen nutzen.",
                style = MaterialTheme.typography.bodySmall
            )
            if (!stepCounterSensorAvailable) {
                Text(
                    text = "Hardware-Hinweis: Dieses Gerät unterstützt keinen Schrittzähler-Sensor. Schritt-Tracking ist daher nicht verfügbar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Globale Einwilligung")
                Switch(
                    checked = passiveMarkersOptIn,
                    onCheckedChange = settingsViewModel::setPassiveMarkersOptIn
                )
            }
            Text(
                text = "Quellensteuerung (fein granular):",
                style = MaterialTheme.typography.labelMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Schrittzahl")
                Switch(
                    checked = passiveMarkersOptIn && passiveStepsEnabled,
                    onCheckedChange = { enabled ->
                        if (!enabled) {
                            settingsViewModel.setPassiveStepsEnabled(false)
                        } else if (settingsViewModel.hasActivityRecognitionPermission()) {
                            settingsViewModel.setPassiveStepsEnabled(true)
                        } else {
                            activityRecognitionPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                        }
                    },
                    enabled = passiveMarkersOptIn && stepCounterSensorAvailable
                )
            }
            if (passiveMarkersOptIn && passiveStepsEnabled && passiveStepsDiagnosticMessage != null) {
                Text(
                    text = passiveStepsDiagnosticMessage.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Schlafdauer (Health Connect)")
                Switch(
                    checked = passiveMarkersOptIn && passiveSleepEnabled,
                    onCheckedChange = { settingsViewModel.setPassiveSleepEnabled(it) },
                    enabled = passiveMarkersOptIn
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Screen-Time-Nähe")
                Switch(
                    checked = passiveMarkersOptIn && passiveScreenTimeProximityEnabled,
                    onCheckedChange = { settingsViewModel.setPassiveScreenTimeProximityEnabled(it) },
                    enabled = passiveMarkersOptIn
                )
            }
        }

        Text("Verhaltensaktivierung", style = MaterialTheme.typography.headlineSmall)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("BA-Klärung: Was zählt hier als Aktivität?")
            Text(
                text = "Als Aktivität zählt alles, was du bewusst tust: auch kleine Schritte, kurze Routinen und Anfänge. Wichtig ist Eigeninitiative statt Perfektion. Bitte ohne Selbstabwertung dokumentieren.",
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Leitlinien verstanden")
                Switch(
                    checked = baCriteriaAcknowledged,
                    onCheckedChange = settingsViewModel::setBaActivityCriteriaAcknowledged
                )
            }
            Text(
                text = "Optionale Präferenz-Tags (freiwillig)",
                style = MaterialTheme.typography.labelMedium
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                modifier = Modifier.fillMaxWidth()
            ) {
                optionalBaTags.forEach { tag ->
                    FilterChip(
                        selected = baPreferenceTags.contains(tag),
                        onClick = { settingsViewModel.toggleBaActivityPreferenceTag(tag) },
                        label = { Text(tag) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Baseline nutzen (optional)")
                Text(
                    text = if (baselineEnabled) {
                        "Aktiv: $baselineDays Tage beobachtend dokumentieren (ohne Bewertung)."
                    } else {
                        "Deaktiviert: Standard-Eingabemodus. Bereits erfasste Daten bleiben erhalten."
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Switch(
                checked = baselineEnabled,
                onCheckedChange = settingsViewModel::setBaselineEnabled
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
