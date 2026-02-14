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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.navigation.Route

private data class ResourceExercise(
    val title: String,
    val learningGoal: String,
    val whyItMatters: String,
    val route: Route
)

private data class ResourceSection(
    val title: String,
    val description: String,
    val exercises: List<ResourceExercise>
)

private val moduleSections: List<ResourceSection> = emptyList()

private val exerciseSections = listOf(
    ResourceSection(
        title = "Stabilisieren im Moment",
        description = "Kurze verhaltenstherapeutische Übungen für hohe innere Anspannung.",
        exercises = listOf(
            ResourceExercise(
                title = "Refokussierung nach außen",
                learningGoal = "Aufmerksamkeit auf sichere, neutrale Reize in der Umgebung lenken.",
                whyItMatters = "Reduziert Grübeldruck und unterstützt Emotionsregulation im Hier und Jetzt.",
                route = Route.SituationalAttentionRefocusingExercise
            ),
            ResourceExercise(
                title = "5-4-3-2-1 Übung",
                learningGoal = "Mit den Sinnen schnell Bodenkontakt herstellen.",
                whyItMatters = "Ein strukturierter Ablauf hilft, in angespannten Situationen handlungsfähig zu bleiben.",
                route = Route.FiveFourThreeTwoOneExercise
            ),
            ResourceExercise(
                title = "Ablenkung als situativer Skill",
                learningGoal = "Grübeln kurz unterbrechen und mentale Distanz gewinnen.",
                whyItMatters = "Gezielte Kurzzeit-Ablenkung ist ein Skill zur Entlastung, nicht zur Vermeidung.",
                route = Route.DistractionSkillExercise
            )
        )
    ),
    ResourceSection(
        title = "Aufmerksamkeit flexibel trainieren",
        description = "Aufmerksamkeitskontrolle schrittweise erweitern: fokussieren, wechseln und weiten.",
        exercises = listOf(
            ResourceExercise(
                title = "Selektive Aufmerksamkeit",
                learningGoal = "Einen Reiz bewusst auswählen und halten.",
                whyItMatters = "Trainiert kognitive Kontrolle und unterbricht automatische Grübelschleifen.",
                route = Route.SelectiveAttentionExercise
            ),
            ResourceExercise(
                title = "Aufmerksamkeitswechsel",
                learningGoal = "Zwischen Reizen flexibel hin- und herschalten.",
                whyItMatters = "Löst starre Aufmerksamkeitsmuster und fördert psychische Flexibilität.",
                route = Route.AttentionSwitchingExercise
            ),
            ResourceExercise(
                title = "Geteilte Aufmerksamkeit",
                learningGoal = "Mehrere Reize gleichzeitig wahrnehmen und gewichten.",
                whyItMatters = "Verbreitert den Aufmerksamkeitsraum und relativiert dominante Belastungsreize.",
                route = Route.SharedAttentionExercise
            )
        )
    ),
    ResourceSection(
        title = "Transfer in den Alltag",
        description = "Kompetenzen festigen und in persönliche, motivierende Aktivitäten übertragen.",
        exercises = listOf(
            ResourceExercise(
                title = "Flow-Landkarte",
                learningGoal = "Aktivitäten identifizieren, die Energie und Fokus fördern.",
                whyItMatters = "Erleichtert die Planung von Verhalten, das zu deinen Werten und Zielen passt.",
                route = Route.FlowChartExercise
            )
        )
    )
)

@Composable
fun ResourcesScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Inhalte", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Hier findest du alle psychoedukativen Inhalte strukturiert in Module und Übungen.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text("Module", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            if (moduleSections.isEmpty()) {
                Text(
                    "Aktuell sind noch keine Module verfügbar. Neue Module erscheinen hier, sobald sie erstellt wurden.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        moduleSections.forEach { section ->
            item { SectionHeader(title = section.title, description = section.description) }
            items(section.exercises) { exercise ->
                ResourceCard(
                    title = exercise.title,
                    learningGoal = exercise.learningGoal,
                    whyItMatters = exercise.whyItMatters,
                    onClick = { navController.navigate(exercise.route.route) }
                )
            }
        }

        item {
            Text("Übungen", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(
                "Alle aktuell verfügbaren Übungen und Ressourcen mit verhaltenstherapeutischem Fokus.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        exerciseSections.forEach { section ->
            item { SectionHeader(title = section.title, description = section.description) }

            items(section.exercises) { exercise ->
                ResourceCard(
                    title = exercise.title,
                    learningGoal = exercise.learningGoal,
                    whyItMatters = exercise.whyItMatters,
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
}

@Composable
private fun SectionHeader(title: String, description: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(description, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ResourceCard(
    title: String,
    learningGoal: String,
    whyItMatters: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text("Ziel: $learningGoal", style = MaterialTheme.typography.bodyMedium)
            Text("Warum: $whyItMatters", style = MaterialTheme.typography.bodySmall)
        }
    }
}
