package com.empiriact.app.ui.screens.exercises

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

data class ActivityItem(
    val id: Int,
    val name: String,
    val challenge: Float, // 0.0 (bottom) to 1.0 (top)
    val skill: Float      // 0.0 (left) to 1.0 (right)
)

@Composable
fun FlowChartExercise() {
    var activities by remember { mutableStateOf(listOf<ActivityItem>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var nextId by remember { mutableIntStateOf(0) }
    var newActivityPosition by remember { mutableStateOf(Offset.Zero) }

    fun addActivity(name: String, challenge: Float, skill: Float) {
        activities = activities + ActivityItem(nextId++, name, challenge, skill)
    }

    Scaffold {
 padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Flow-Landkarte", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Tippe auf eine Stelle im Diagramm, um eine Aktivität hinzuzufügen.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            FlowChart(
                activities = activities,
                onAddActivityAt = { skill, challenge ->
                    newActivityPosition = Offset(skill, challenge)
                    showAddDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Spacer(Modifier.height(16.dp))
            Text("Deine Aktivitäten", style = MaterialTheme.typography.titleMedium)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(activities) { activity ->
                    Text(text = "- ${activity.name} (Fähigkeit: ${String.format("%.1f", activity.skill)}, Herausforderung: ${String.format("%.1f", activity.challenge)}) ")
                }
            }
        }
    }

    if (showAddDialog) {
        AddActivityDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { names ->
                addActivity(names.joinToString(", "), newActivityPosition.y, newActivityPosition.x)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun FlowChart(
    activities: List<ActivityItem>,
    onAddActivityAt: (Float, Float) -> Unit, // Returns skill and challenge
    modifier: Modifier = Modifier
) {
    val axisColor = MaterialTheme.colorScheme.onSurface
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val primaryColor = MaterialTheme.colorScheme.primary // Correct: Hoist color out of non-composable scope

    BoxWithConstraints(modifier = modifier
        .background(backgroundColor)
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                onAddActivityAt(
                    offset.x / size.width,
                    (size.height - offset.y) / size.height
                )
            }
        }
    ) {
        val boxWidth = this.maxWidth
        val boxHeight = this.maxHeight

        Canvas(modifier = Modifier.fillMaxSize()) {
            val midX = size.width / 2
            val midY = size.height / 2

            drawLine(axisColor, start = Offset(midX, 0f), end = Offset(midX, size.height))
            drawLine(axisColor, start = Offset(0f, midY), end = Offset(size.width, midY))

            activities.forEach { activity ->
                drawCircle(
                    color = primaryColor, // Correct: Use the hoisted color
                    radius = 8.dp.toPx(),
                    center = Offset(activity.skill * size.width, (1 - activity.challenge) * size.height)
                )
            }
        }

        Text("Langeweile", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.BottomStart).padding(4.dp))
        Text("Routine", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp))
        Text("Überforderung", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.TopStart).padding(4.dp))
        Text("Flow", style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.TopEnd).padding(4.dp))

        Text("Hohe Herausforderung", style = MaterialTheme.typography.labelSmall, modifier = Modifier.align(Alignment.CenterStart).padding(start = 4.dp).offset(y = -boxHeight / 4))
        Text("Niedrige Herausforderung", style = MaterialTheme.typography.labelSmall, modifier = Modifier.align(Alignment.CenterStart).padding(start = 4.dp).offset(y = boxHeight / 4))
        Text("Niedrige Fähigkeit", style = MaterialTheme.typography.labelSmall, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp).offset(x = -boxWidth / 4))
        Text("Hohe Fähigkeit", style = MaterialTheme.typography.labelSmall, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp).offset(x = boxWidth / 4))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddActivityDialog(onDismiss: () -> Unit, onConfirm: (List<String>) -> Unit) {
    var text by remember { mutableStateOf("") }
    var selectedActivities by remember { mutableStateOf<List<String>>(emptyList()) }
    val allActivities = listOf("Arbeiten", "Lesen", "Sport", "Kochen", "Putzen", "Freunde treffen", "Programmieren")
    var expanded by remember { mutableStateOf(false) }

    val filteredActivities = allActivities.filter { it.contains(text, ignoreCase = true) && it !in selectedActivities }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Aktivität hinzufügen", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                            expanded = true
                         },
                        label = { Text("Name der Aktivität") },
                        modifier = Modifier.menuAnchor(),
                        readOnly = selectedActivities.size >= 3,
                        leadingIcon = {
                            Row(modifier = Modifier.padding(start = 8.dp)) {
                                selectedActivities.forEach { activity ->
                                    InputChip(
                                        selected = false,
                                        onClick = {
                                            selectedActivities = selectedActivities - activity
                                        },
                                        label = { Text(activity) },
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                }
                            }
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded && filteredActivities.isNotEmpty() && selectedActivities.size < 3,
                        onDismissRequest = { expanded = false }
                    ) {
                        filteredActivities.forEach { activity ->
                            DropdownMenuItem(
                                text = { Text(activity) },
                                onClick = {
                                    if (selectedActivities.size < 3) {
                                        selectedActivities = selectedActivities + activity
                                        text = ""
                                        expanded = false
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Abbrechen") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val activitiesToConfirm = selectedActivities.toMutableList()
                            if (text.isNotBlank() && selectedActivities.size < 3) {
                                activitiesToConfirm.add(text)
                            }
                            onConfirm(activitiesToConfirm)
                         },
                        enabled = selectedActivities.isNotEmpty() || text.isNotBlank()
                    ) {
                        Text("Speichern")
                    }
                }
            }
        }
    }
}
