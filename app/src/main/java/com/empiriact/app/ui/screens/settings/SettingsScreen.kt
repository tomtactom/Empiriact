package com.empiriact.app.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.EmpiriactApplication

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as EmpiriactApplication
    val jsonExportViewModel: JsonExportViewModel = viewModel(factory = application.viewModelFactory)
    val exportState by jsonExportViewModel.exportState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Einstellungen", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dunkelmodus")
            Switch(checked = false, onCheckedChange = { /* TODO */ })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Benachrichtigungen")
            Switch(checked = true, onCheckedChange = { /* TODO */ })
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Daten exportieren", style = MaterialTheme.typography.headlineSmall)

        Button(onClick = { jsonExportViewModel.exportToJson() }) {
            Text("Als JSON exportieren")
        }

        when (val state = exportState) {
            is ExportState.Idle -> {
                // Nothing to show
            }
            is ExportState.Loading -> {
                CircularProgressIndicator()
            }
            is ExportState.Success -> {
                Text("Export erfolgreich! Die Daten wurden in die Zwischenablage kopiert.")
            }
            is ExportState.Error -> {
                Text("Fehler beim Export: ${state.message}")
            }
        }
    }
}
