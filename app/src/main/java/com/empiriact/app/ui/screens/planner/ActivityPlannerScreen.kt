package com.empiriact.app.ui.screens.planner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.empiriact.app.data.PlannedActivity
import com.empiriact.app.ui.common.InputValidationFeedback
import com.empiriact.app.ui.common.ViewModelFactory
import com.empiriact.app.ui.theme.Dimensions

@Composable
fun ActivityPlannerScreen(
    valueName: String, // The value to plan for
    factory: ViewModelFactory
) {
    val viewModel: ActivityPlannerViewModel = viewModel(factory = factory)
    var newActivityText by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf("") }
    val activities by viewModel.activities.collectAsState()

    // Load activities when the screen is first displayed
    LaunchedEffect(valueName) {
        viewModel.loadActivitiesForValue(valueName)
    }

    val maxActivityLength = 150
    val isInputValid = newActivityText.isNotBlank() && newActivityText.length <= maxActivityLength

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(Dimensions.paddingMedium)
        ) {
            Text("Aktivitäten planen für:", style = MaterialTheme.typography.titleMedium)
            Text(valueName, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(Dimensions.spacingLarge))

            // Input for new activities
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newActivityText,
                        onValueChange = { newText ->
                            newActivityText = newText
                            validationError = when {
                                newText.isEmpty() -> ""
                                newText.length > maxActivityLength -> "Maximal $maxActivityLength Zeichen"
                                else -> ""
                            }
                        },
                        label = { Text("Neue werteorientierte Aktivität") },
                        modifier = Modifier.weight(1f),
                        isError = validationError.isNotEmpty(),
                        supportingText = {
                            Text("${newActivityText.length}/$maxActivityLength")
                        }
                    )
                    Spacer(Modifier.width(Dimensions.spacingSmall))
                    Button(
                        onClick = {
                            if (isInputValid) {
                                viewModel.addActivity(newActivityText)
                                newActivityText = ""
                                validationError = ""
                            } else {
                                validationError = "Bitte geben Sie eine Aktivität ein"
                            }
                        },
                        enabled = isInputValid,
                        modifier = Modifier.height(Dimensions.buttonHeight)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aktivität hinzufügen")
                    }
                }
                InputValidationFeedback(
                    isValid = validationError.isEmpty(),
                    errorMessage = validationError,
                    modifier = Modifier.padding(top = Dimensions.spacingSmall)
                )
            }

            Spacer(Modifier.height(Dimensions.spacingLarge))

            // List of planned activities
            Text("Geplante Aktivitäten", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(Dimensions.spacingSmall))

            if (activities.isEmpty()) {
                Text(
                    "Noch keine Aktivitäten für diesen Wert geplant.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall)) {
                    items(activities) { activity ->
                        ActivityItem(activity.description)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Text(
            description,
            modifier = Modifier.padding(Dimensions.paddingMedium),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
