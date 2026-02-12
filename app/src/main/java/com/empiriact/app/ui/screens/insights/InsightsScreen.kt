package com.empiriact.app.ui.screens.insights

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun InsightsScreen(factory: ViewModelProvider.Factory) {
    val viewModel: InsightsViewModel = viewModel(factory = factory)
    val selectedDate by viewModel.selectedDate.collectAsState()
    val activities by viewModel.activities.collectAsState()
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
                items(hourlyActivities) { hourlyActivity ->
                    if (hourlyActivity.activity.isNotBlank()) {
                        HourEntry(hourlyActivity) {
                            showEditDialog = it
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

@OptIn(ExperimentalMaterial3Api::class)
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
        -2 -> "😞" // sehr schlecht
        -1 -> "😕" // schlecht
        0 -> "😐"  // neutral
        1 -> "🙂"  // gut
        2 -> "😊"  // sehr gut
        else -> ""
    }
}
