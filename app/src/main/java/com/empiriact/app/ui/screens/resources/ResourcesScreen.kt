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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.net.Uri
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.data.model.Course
import com.empiriact.app.data.model.Module
import com.empiriact.app.ui.navigation.Route
import kotlinx.serialization.json.Json

private data class ResourceExercise(
    val title: String,
    val description: String,
    val route: Route
)

private val allExercises = listOf(
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
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Übungen", "Edukation")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> ExercisesList(navController)
            1 -> EducationList(navController)
        }
    }
}

@Composable
private fun EducationList(navController: NavController) {
    val context = LocalContext.current
    val modules by produceState(initialValue = emptyList<Module>(), context) {
        value = loadEducationModules(context)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Edukation", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Hier findest du psychoedukative Inhalte rund um Grübeln, Aufmerksamkeit und hilfreiche Strategien im Alltag.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (modules.isEmpty()) {
            item {
                Text(
                    "Aktuell sind keine Module verfügbar.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            items(modules) { module ->
                ResourceCard(
                    title = module.title,
                    description = module.description,
                    onClick = {
                        navController.navigate(Route.ModuleDetail.createRoute(Uri.encode(module.id)))
                    }
                )
            }
        }
    }
}

private fun loadEducationModules(context: android.content.Context): List<Module> {
    val json = Json { ignoreUnknownKeys = true }
    val candidateFiles = listOf("courses/gruebeln_foundation.json", "course.json")

    for (file in candidateFiles) {
        val modules = runCatching {
            context.assets.open(file).bufferedReader().use { reader ->
                val course = json.decodeFromString<Course>(reader.readText())
                course.modules
            }
        }.getOrNull()

        if (!modules.isNullOrEmpty()) {
            return modules
        }
    }

    return emptyList()
}

@Composable
private fun ExercisesList(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Inhalte", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Hier findest du eine Sammlung von Übungen, die dir helfen können, mit Grübeln und schwierigen Gefühlen umzugehen.",
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
