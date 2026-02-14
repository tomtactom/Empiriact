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
    val source: String,
    val description: String,
    val route: Route? = null
)

private val exercises = listOf(
    PracticeExercise(
        title = "AB 1 - Schlechter Denkstil oder konstruktives Denken?",
        source = "Modul 1",
        description = "Vergleiche eine aktuelle Gedankenschleife mit konstruktivem Problemlösen (konkret, lösungsorientiert, zeitlich begrenzt)."
    ),
    PracticeExercise(
        title = "AB 2 - 3-Fragen-Daumenregel",
        source = "Modul 1",
        description = "Wende die 3 Fragen direkt auf eine aktuelle Grübelsituation an und entscheide, ob du im Rumination-Modus bist."
    ),
    PracticeExercise(
        title = "AB 3 - Münztrick (Impact-Technik)",
        source = "Modul 1",
        description = "Markiere den Wechsel von Erzählen zu Grübeln mit einem klaren Signal und kehre zum roten Faden zurück."
    ),
    PracticeExercise(
        title = "AB 4-6 - Funktionsanalyse",
        source = "Modul 1",
        description = "Erarbeite eine vollständige Mikro-/Makroanalyse mit Situation, Reaktion, Konsequenzen, Vulnerabilitäten und aufrechterhaltenden Faktoren."
    ),
    PracticeExercise(
        title = "AB 7 - Individueller Behandlungsplan",
        source = "Modul 1",
        description = "Leite aus der Funktionsanalyse konkrete Therapieziele und Interventionen ab (Auslöser, Aufrechterhaltung, Prädisposition, Hindernisse)."
    ),
    PracticeExercise(
        title = "AB 8 - Therapietracker",
        source = "Modul 1",
        description = "Tracke Leidensdruck, Häufigkeit, Dauer, Auslöser, eingesetzte Skills und Schwierigkeiten über mehrere Zeitpunkte."
    ),
    PracticeExercise(
        title = "Ablenkung als situativer Skill",
        source = "Integrierte Ressourcenübung",
        description = "Nutze bewussten Aufmerksamkeitswechsel, um Grübeln kurzfristig zu unterbrechen.",
        route = Route.DistractionSkillExercise
    ),
    PracticeExercise(
        title = "Refokussierung nach außen",
        source = "Integrierte Ressourcenübung",
        description = "Verankere dich im Außenfokus und reduziere Bedrohungsmonitoring sowie Selbstfokussierung.",
        route = Route.SituationalAttentionRefocusingExercise
    ),
    PracticeExercise(
        title = "5-4-3-2-1",
        source = "Integrierte Ressourcenübung",
        description = "Stabilisiere dich im Hier-und-Jetzt über Sinnesanker.",
        route = Route.FiveFourThreeTwoOneExercise
    ),
    PracticeExercise(
        title = "Selektive Aufmerksamkeit",
        source = "Integrierte Ressourcenübung",
        description = "Trainiere fokussierte Aufmerksamkeit statt automatischer Grübelschleifen.",
        route = Route.SelectiveAttentionExercise
    ),
    PracticeExercise(
        title = "Aufmerksamkeitswechsel",
        source = "Integrierte Ressourcenübung",
        description = "Steigere kognitive Flexibilität durch geplanten Fokuswechsel.",
        route = Route.AttentionSwitchingExercise
    ),
    PracticeExercise(
        title = "Geteilte Aufmerksamkeit",
        source = "Integrierte Ressourcenübung",
        description = "Erweitere den Aufmerksamkeitsraum und entlaste dominante negative Reize.",
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
                    text = "Alle Arbeitsblätter (AB) und integrierten Ressourcen-Übungen für Modul 1 an einem Ort.",
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
                            text = exercise.source,
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
                                text = "Tippe, um die Übung zu starten.",
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
