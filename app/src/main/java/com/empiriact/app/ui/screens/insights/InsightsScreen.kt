package com.empiriact.app.ui.screens.insights

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.EmpiriactApplication
import com.empiriact.app.R
import com.empiriact.app.services.PassiveMarkerService
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun InsightsScreen(@Suppress("UNUSED_PARAMETER") factory: ViewModelProvider.Factory) {
    val context = LocalContext.current
    val application = context.applicationContext as EmpiriactApplication

    val viewModel: InsightsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return InsightsViewModel(
                    application.activityLogRepository,
                    PassiveMarkerService(application.settingsRepository, application.passiveMarkerRepository)
                ) as T
            }
        }
    )

    val selectedDate by viewModel.selectedDate.collectAsState()
    val activities by viewModel.activities.collectAsState()
    val passiveContextByCheckin by viewModel.passiveContextByCheckin.collectAsState()
    val passiveVsActive by viewModel.passiveVsActive.collectAsState()
    var showEditDialog by remember { mutableStateOf<HourlyActivity?>(null) }

    Scaffold(
        topBar = {
            DateNavigator(
                selectedDate = selectedDate,
                onDateChange = { viewModel.onDateChange(it) }
            )
        }
    ) { padding ->
        val hourlyActivities = activities.getOrElse(selectedDate) { emptyList() }
        val hasActivities = hourlyActivities.any { it.activity.isNotBlank() }

        if (hasActivities) {
            LazyColumn(modifier = Modifier.padding(padding).padding(horizontal = 16.dp)) {
                item {
                    InsightsTransparencyCard()
                    if (passiveVsActive.isNotEmpty()) {
                        PassiveVsActiveSection(passiveVsActive)
                    }
                }
                items(hourlyActivities) { hourlyActivity ->
                    if (hourlyActivity.activity.isNotBlank()) {
                        HourEntry(hourlyActivity) {
                            showEditDialog = it
                        }

                        val contextItem = passiveContextByCheckin[hourlyActivity.checkinKey]
                        if (contextItem != null && contextItem.readings.isNotEmpty()) {
                            PassiveContextCard(
                                explanation = contextItem.explanation,
                                lines = contextItem.readings.map { "• ${it.label}: ${it.valueText}" }
                            )
                        }
                    }
                }
            }
        } else {
            EmptyState(modifier = Modifier.padding(padding))
        }

        if (showEditDialog != null) {
            EditHourEntryDialog(
                entry = showEditDialog!!,
                onDismiss = { showEditDialog = null },
                onConfirm = { activity, valence ->
                    viewModel.updateActivity(selectedDate, showEditDialog!!.hour, activity, valence)
                    showEditDialog = null
                }
            )
        }
    }
}

@Composable
private fun InsightsTransparencyCard() {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Transparenz", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Passive Marker werden nur als Kontext neben aktiven Check-ins angezeigt. " +
                    "Keine verdeckte Diagnostik, keine automatische Bewertung deiner Person.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun PassiveVsActiveSection(points: List<com.empiriact.app.services.PassiveVsActiveDataPoint>) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Vergleich: aktive Valenz vs. passive Marker", style = MaterialTheme.typography.titleMedium)
            points.takeLast(7).forEach { point ->
                Text(
                    text = "${point.date.format(DateTimeFormatter.ofPattern("dd.MM"))}: " +
                        "Valenz Ø ${"%.1f".format(point.valenceAverage)} | " +
                        "Schritte ${point.steps?.toString() ?: "–"} | " +
                        "Schlaf ${point.sleepDurationHours?.let { "%.1f".format(it) + "h" } ?: "–"} | " +
                        "Screen-Time ${point.screenTimeMinutes?.let { "$it Min" } ?: "–"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun PassiveContextCard(explanation: String, lines: List<String>) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Passiver Kontext", style = MaterialTheme.typography.titleSmall)
            lines.forEach { line ->
                Text(line, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(4.dp))
            Text(explanation, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.illustration_insights_empty),
            contentDescription = "No data illustration"
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Für diesen Tag gibt es keine Einträge. Zeit, die Vergangenheit ruhen zu lassen?",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DateNavigator(selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { onDateChange(selectedDate.minusDays(1)) }) { Text("<") }
        Text(selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), style = MaterialTheme.typography.titleMedium)
        Button(onClick = { onDateChange(selectedDate.plusDays(1)) }) { Text(">") }
    }
}

@Composable
fun HourEntry(entry: HourlyActivity, onClick: (HourlyActivity) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick(entry) }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(String.format("%02d:00", entry.hour), modifier = Modifier.width(80.dp))
        Spacer(Modifier.width(16.dp))
        Text(entry.activity, modifier = Modifier.weight(1f))
        Text(valenceToEmoji(entry.valence), style = MaterialTheme.typography.bodyLarge)
    }
    HorizontalDivider()
}

@Composable
fun EditHourEntryDialog(
    entry: HourlyActivity,
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var activity by remember { mutableStateOf(entry.activity) }
    var valence by remember { mutableStateOf(entry.valence) }

    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.large) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Eintrag für ${entry.hour}:00 Uhr", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(value = activity, onValueChange = { activity = it }, label = { Text("Wichtigste Aktivität") })
                Spacer(Modifier.height(16.dp))

                Text("Stimmung: ${valenceToEmoji(valence)}")
                Slider(value = valence.toFloat(), onValueChange = { valence = it.toInt() }, valueRange = -2f..2f, steps = 3)

                Spacer(Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Abbrechen") }
                    Button(onClick = { onConfirm(activity, valence) }) { Text("Speichern") }
                }
            }
        }
    }
}

private fun valenceToEmoji(valence: Int): String {
    return when (valence) {
        -2 -> "😞"
        -1 -> "😕"
        0 -> "😐"
        1 -> "🙂"
        2 -> "😊"
        else -> ""
    }
}
