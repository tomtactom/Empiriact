package com.empiriact.app.ui.screens.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions

data class PracticeExercise(val title: String, val difficulty: String, val description: String)

private val exercises = listOf(
    PracticeExercise(
        "Reflexionstagebuch",
        "Anfänger",
        "Schreibe täglich auf, was du gelernt hast und wie du es anwenden kannst."
    ),
    PracticeExercise(
        "Zielanalyse",
        "Mittelstufe",
        "Analysiere deine Ziele und erstelle einen umsetzbaren Plan."
    ),
    PracticeExercise(
        "Gewohnheitsprotokoll",
        "Mittelstufe",
        "Dokumentiere deine täglichen Gewohnheiten und identifiziere Verbesserungspotenziale."
    ),
    PracticeExercise(
        "Fallstudienanalyse",
        "Fortgeschritten",
        "Untersuche reale Fallstudien und entwickle deine Problemlösungsfähigkeiten."
    ),
    PracticeExercise(
        "Integrationsprojekt",
        "Fortgeschritten",
        "Wende mehrere gelernte Techniken in einem umfassenden Projekt an."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnPracticeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Praktische Übungen") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = ContentDescriptions.BACK_BUTTON)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(exercises) { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = exercise.title,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Schwierigkeit: ${exercise.difficulty}",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = exercise.description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
