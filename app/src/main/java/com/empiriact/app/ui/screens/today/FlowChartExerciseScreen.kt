package com.empiriact.app.ui.screens.today

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions
import com.empiriact.app.ui.common.ResourceExercise

// Data class to hold activity information
private data class FlowActivity(
    val id: Int,
    val name: String,
    var challenge: Float = 5f,
    var skill: Float = 5f
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowChartExerciseScreen(navController: NavController) {
    // State for the entire exercise
    var currentStep by remember { mutableStateOf(1) }
    val activities = remember { mutableStateListOf<FlowActivity>() }
    var activityIdCounter by remember { mutableStateOf(0) }

    ResourceExercise(onFinish = { navController.popBackStack() }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Flow-Landkarte") },
                    navigationIcon = {
                        IconButton(onClick = {
                            // If they are past the intro, the close button should take them back a step,
                            // otherwise it closes the exercise
                            if (currentStep > 1) {
                                currentStep--
                            } else {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = ContentDescriptions.CLOSE_DIALOG
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (currentStep) {
                    1 -> IntroStep(onStartClick = { currentStep = 2 })
                    2 -> BrainstormStep(
                        activities = activities,
                        onAddActivity = { name ->
                            activities.add(FlowActivity(id = activityIdCounter++, name = name))
                        },
                        onNextClick = { currentStep = 3 }
                    )
                    3 -> RatingStep(
                        activities = activities,
                        onRatingChange = { activity, challenge, skill ->
                             val index = activities.indexOfFirst { it.id == activity.id }
                             if (index != -1) {
                                 activities[index] = activity.copy(challenge = challenge, skill = skill)
                             }
                        },
                        onNextClick = { currentStep = 4 }
                    )
                    4 -> // Placeholder for Chart
                        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text("Schritt 4: Die Flow-Landkarte (wird noch implementiert)", style = MaterialTheme.typography.headlineSmall)
                             Button(onClick = { currentStep = 5 }) {
                                Text("Weiter zur Zusammenfassung")
                            }
                        }
                    5 -> // Placeholder for Summary
                        Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                             Text("Schritt 5: Zusammenfassung (wird noch implementiert)", style = MaterialTheme.typography.headlineSmall)
                             Button(onClick = { navController.popBackStack() }) {
                                Text("Übung beenden")
                            }
                        }
                }
            }
        }
    }
}

@Composable
private fun IntroStep(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Entdecke deinen Flow",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Möchtest du herausfinden, welche Tätigkeiten dich voll und ganz fesseln und dir Energie geben? Diese Übung lädt dich ein, Momente des 'Flows' in deinem Leben zu entdecken.\n\nEs geht darum, eine Balance zwischen Herausforderung und deinen Fähigkeiten zu finden. Nimm dir einen Moment Zeit, um zu erkunden, was dich wirklich begeistert.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onStartClick) {
            Text("Lass uns beginnen")
        }
    }
}

@Composable
private fun BrainstormStep(
    activities: List<FlowActivity>,
    onAddActivity: (String) -> Unit,
    onNextClick: () -> Unit
) {
    var newActivityName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Schritt 1: Deine Aktivitäten",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            "Was machst du in deiner Freizeit oder bei der Arbeit? Sammle hier einige Aktivitäten. Es gibt kein Richtig oder Falsch.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newActivityName,
                onValueChange = { newActivityName = it },
                label = { Text("Neue Aktivität") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newActivityName.isNotBlank()) {
                        onAddActivity(newActivityName)
                        newActivityName = ""
                    }
                },
                enabled = newActivityName.isNotBlank()
            ) {
                Text("Add")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(activities, key = { it.id }) { activity ->
                Text(
                    text = "- ${activity.name}",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNextClick,
            enabled = activities.isNotEmpty(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Weiter")
        }
    }
}

@Composable
private fun RatingStep(
    activities: SnapshotStateList<FlowActivity>,
    onRatingChange: (FlowActivity, Float, Float) -> Unit,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Schritt 2: Einschätzung",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            "Schätze nun für jede Aktivität ein: Wie herausfordernd ist sie für dich und wie hoch sind deine Fähigkeiten dabei? Deine persönliche Wahrnehmung zählt.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(activities, key = { it.id }) { activity ->
                ActivityRatingCard(activity = activity, onRatingChange = { ch, sk -> onRatingChange(activity, ch, sk) })
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNextClick,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Zur Landkarte")
        }
    }
}

@Composable
private fun ActivityRatingCard(activity: FlowActivity, onRatingChange: (Float, Float) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(activity.name, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Challenge Slider
            Text("Herausforderung: ${activity.challenge.toInt()}", style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = activity.challenge,
                onValueChange = { onRatingChange(it, activity.skill) },
                valueRange = 0f..10f,
                steps = 9
            )

            // Skill Slider
            Text("Fähigkeiten: ${activity.skill.toInt()}", style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = activity.skill,
                onValueChange = { onRatingChange(activity.challenge, it) },
                valueRange = 0f..10f,
                steps = 9
            )
        }
    }
}
