package com.empiriact.app.ui.screens.resources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.navigation.Route

private data class ResourceExercise(
    val title: String,
    val description: String,
    val route: Route
)

private val allExercises = listOf(
    ResourceExercise(
        title = "Modul 1.1 · Gute Ausgangsbedingungen schaffen",
        description = "Psychoedukatives Einstiegsmodul zur Klärung von Rollen, Erwartungen und dem gemeinsamen Lernprozess in der werteorientierten Verhaltensaktivierung – mit autonomiefördernder Ausrichtung (ASIB).",
        route = Route.ModuleOneOneInitialConditions
    ),
    ResourceExercise(
        title = "Modul 1.2 · Aktivitäten und Stimmung beobachten",
        description = "Edukatives Kernmodul zur strukturierten Alltagsbeobachtung: Aktivitäten und Stimmung zeitnah dokumentieren, Muster erkennen und eine praktikable, autonomiefördernde Erfassungsroutine aufbauen.",
        route = Route.ModuleOneTwoActivityMoodMonitoring
    ),
    ResourceExercise(
        title = "Ablenkung als situativer Skill",
        description = "Kurzfristige, bewusste Hinwendung zu einer externen Tätigkeit mit dem Ziel, Grübeln zu unterbrechen. Die Ablenkung wird als zeitlich begrenzter Aufmerksamkeitswechsel verstanden, nicht als dauerhafte Vermeidung.",
        route = Route.DistractionSkillExercise
    ),
    ResourceExercise(
        title = "Refokussierung nach außen (Situational Attention Refocusing)",
        description = "Die Aufmerksamkeit wird bewusst auf äußere neutrale oder sichere Reize gelenkt (z. B. Umgebung, Gesprächspartner, Tätigkeit), insbesondere in Situationen mit erhöhter innerer Aktivierung. Abschwächung von Bedrohungsmonitoring; Stabilisierung im Hier-und-Jetzt; Unterstützung emotionaler Regulation durch Kontextorientierung.",
        route = Route.SituationalAttentionRefocusingExercise
    ),
    ResourceExercise(
        title = "5-4-3-2-1 Übung",
        description = "Eine Achtsamkeitsübung, um dich im Hier und Jetzt zu verankern.",
        route = Route.FiveFourThreeTwoOneExercise
    ),
    ResourceExercise(
        title = "Selektive Aufmerksamkeit",
        description = "Trainiere deine Fähigkeit, deine Aufmerksamkeit gezielt auf einen Reiz zu lenken und Grübelschleifen zu unterbrechen.",
        route = Route.SelectiveAttentionExercise
    ),
    ResourceExercise(
        title = "Aufmerksamkeitswechsel",
        description = "Trainiere die Flexibilität deiner Aufmerksamkeit durch bewusstes Wechseln zwischen äußeren und inneren Reizen. Breche starre Aufmerksamkeitsmuster auf.",
        route = Route.AttentionSwitchingExercise
    ),
    ResourceExercise(
        title = "Geteilte Aufmerksamkeit",
        description = "Lerne, deine Aufmerksamkeit zu weiten und mehrere Reize gleichzeitig wahrzunehmen. Entwickle einen breiten Aufmerksamkeitsraum und relativiere dominante Reize.",
        route = Route.SharedAttentionExercise
    ),
    ResourceExercise(
        title = "Flow-Landkarte",
        description = "Entdecke, welche Aktivitäten dich in den Flow bringen.",
        route = Route.FlowChartExercise
    )
)

@Composable
fun ResourcesScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Inhalte", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Hier findest du eine stabile Grundlage mit praktischen Übungen, die dir helfen können, mit Grübeln und schwierigen Gefühlen umzugehen.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        items(allExercises) { exercise ->
            ResourceCard(
                title = exercise.title,
                description = exercise.description,
                onClick = {
                    when (exercise.route) {
                        is Route.FiveFourThreeTwoOneExercise -> {
                            navController.navigate(Route.FiveFourThreeTwoOneExercise.createRoute(from = "resources"))
                        }
                        is Route.SelectiveAttentionExercise -> {
                            navController.navigate(Route.SelectiveAttentionExercise.createRoute(from = "resources"))
                        }
                        is Route.AttentionSwitchingExercise -> {
                            navController.navigate(Route.AttentionSwitchingExercise.createRoute(from = "resources"))
                        }
                        is Route.SharedAttentionExercise -> {
                            navController.navigate(Route.SharedAttentionExercise.createRoute(from = "resources"))
                        }
                        is Route.DistractionSkillExercise -> {
                            navController.navigate(Route.DistractionSkillExercise.createRoute(from = "resources"))
                        }
                        is Route.SituationalAttentionRefocusingExercise -> {
                            navController.navigate(Route.SituationalAttentionRefocusingExercise.createRoute(from = "resources"))
                        }
                        else -> {
                            navController.navigate(exercise.route.route)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ResourceCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
