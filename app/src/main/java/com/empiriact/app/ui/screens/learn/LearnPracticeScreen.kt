package com.empiriact.app.ui.screens.learn

import androidx.compose.foundation.clickable
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
import com.empiriact.app.ui.navigation.Route

data class PracticeExercise(
    val title: String,
    val category: String,
    val durationHint: String,
    val description: String,
    val route: Route? = null
)

private val exercises = listOf(
    PracticeExercise(
        title = "AB 1 - Denkstil-Check",
        category = "Psychoedukation",
        durationHint = "3-5 Min.",
        description = "Prüfe, ob dein Denken gerade lösungsorientiert oder in einer Rumination-Schleife ist."
    ),
    PracticeExercise(
        title = "AB 2 - 3-Fragen-Daumenregel",
        category = "Früherkennung",
        durationHint = "2-3 Min.",
        description = "Schneller In-App-Check bei akuten Grübelmomenten."
    ),
    PracticeExercise(
        title = "AB 3 - Münztrick",
        category = "Sitzungsintervention",
        durationHint = "1-2 Min.",
        description = "Stoppsignal für offenes Grübeln und Rückkehr zum roten Faden in Gesprächen."
    ),
    PracticeExercise(
        title = "AB 4-6 - Funktionsanalyse",
        category = "Fallkonzeptualisierung",
        durationHint = "10-20 Min.",
        description = "Analysiere eine konkrete Grübelepisode strukturiert (Auslöser, Reaktion, Konsequenzen)."
    ),
    PracticeExercise(
        title = "AB 7 - Behandlungsplan",
        category = "Therapieplanung",
        durationHint = "10 Min.",
        description = "Leite aus der Analyse klare Ziele und Interventionen für den Alltag ab."
    ),
    PracticeExercise(
        title = "AB 8 - Therapietracker",
        category = "Verlauf",
        durationHint = "1-2 Min./Tag",
        description = "Tracke Belastung, Häufigkeit, Dauer, Trigger und wirksame Skills."
    ),
    PracticeExercise(
        title = "Ablenkung als situativer Skill",
        category = "Ressourcenübung",
        durationHint = "2-5 Min.",
        description = "Unterbrich die Schleife über einen bewussten Aufmerksamkeitswechsel.",
        route = Route.DistractionSkillExercise
    ),
    PracticeExercise(
        title = "Refokussierung nach außen",
        category = "Ressourcenübung",
        durationHint = "3-5 Min.",
        description = "Lenke den Fokus aktiv auf äußere Reize zur Deeskalation.",
        route = Route.SituationalAttentionRefocusingExercise
    ),
    PracticeExercise(
        title = "5-4-3-2-1",
        category = "Ressourcenübung",
        durationHint = "2-4 Min.",
        description = "Sinnesbasierte Soforthilfe zur Stabilisierung im Hier-und-Jetzt.",
        route = Route.FiveFourThreeTwoOneExercise
    ),
    PracticeExercise(
        title = "Selektive Aufmerksamkeit",
        category = "Ressourcenübung",
        durationHint = "3-6 Min.",
        description = "Trainiere fokussierte Aufmerksamkeitssteuerung statt Gedankenkreisen.",
        route = Route.SelectiveAttentionExercise
    ),
    PracticeExercise(
        title = "Aufmerksamkeitswechsel",
        category = "Ressourcenübung",
        durationHint = "3-6 Min.",
        description = "Erhöhe kognitive Flexibilität durch systematischen Fokuswechsel.",
        route = Route.AttentionSwitchingExercise
    ),
    PracticeExercise(
        title = "Geteilte Aufmerksamkeit",
        category = "Ressourcenübung",
        durationHint = "3-6 Min.",
        description = "Weite den Aufmerksamkeitsraum und relativiere dominante Grübelreize.",
        route = Route.SharedAttentionExercise
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnPracticeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modul 1: Übungen & Ressourcen") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ContentDescriptions.BACK_BUTTON
                        )
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "App-ready Aufbau: kurze Einheiten, klare Kategorie, Zeitbedarf und direkter Start bei Ressourcenübungen.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(exercises) { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .let { base ->
                            val target = exercise.route
                            if (target != null) {
                                base.clickable {
                                    val route = when (target) {
                                        is Route.FiveFourThreeTwoOneExercise -> target.createRoute("learn")
                                        is Route.SelectiveAttentionExercise -> target.createRoute("learn")
                                        is Route.AttentionSwitchingExercise -> target.createRoute("learn")
                                        is Route.SharedAttentionExercise -> target.createRoute("learn")
                                        is Route.DistractionSkillExercise -> target.createRoute("learn")
                                        is Route.SituationalAttentionRefocusingExercise -> target.createRoute("learn")
                                        else -> target.route
                                    }
                                    navController.navigate(route)
                                }
                            } else {
                                base
                            }
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = exercise.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "${exercise.category} · ${exercise.durationHint}",
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Text(
                            text = exercise.description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        if (exercise.route != null) {
                            Text(
                                text = "Tippe, um die Übung direkt zu starten.",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
