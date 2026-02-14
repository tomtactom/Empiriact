package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Swipe
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.navigation.Route
import kotlinx.coroutines.launch

@Composable
fun ModuleOneOneInitialConditionsScreen(navController: NavController) {
    val pages = moduleOneOnePages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Modul 1.1 · Klar starten",
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Swipe, contentDescription = null)
            Text(
                text = "Wische nach links oder rechts, um die Seiten zu wechseln.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            val page = pages[pageIndex]
            Card(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text(
                            text = "Schritt ${pageIndex + 1} von ${pages.size}: ${page.title}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    item { Text(page.mainText, style = MaterialTheme.typography.bodyLarge) }
                    items(page.keyPoints) { StoryChip(text = it) }
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Übung", style = MaterialTheme.typography.titleSmall)
                                Text(page.practicePrompt, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage - 1).coerceAtLeast(0))
                    }
                },
                enabled = pagerState.currentPage > 0,
                modifier = Modifier.weight(1f)
            ) { Text("Zurück") }

            OutlinedButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage + 1).coerceAtMost(pages.lastIndex))
                    }
                },
                enabled = pagerState.currentPage < pages.lastIndex,
                modifier = Modifier.weight(1f)
            ) { Text("Weiter") }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Dein roter Faden aus Modul 1.1", style = MaterialTheme.typography.titleMedium)
                JourneyStep(
                    icon = Icons.Outlined.Flag,
                    title = "Richtung wählen",
                    description = "Wähle eine klare Richtung für diese Woche."
                )
                JourneyStep(
                    icon = Icons.Outlined.Navigation,
                    title = "Kleiner Schritt",
                    description = "Plane einen Schritt für 5 bis 15 Minuten."
                )
                JourneyStep(
                    icon = Icons.Outlined.Insights,
                    title = "Eigener Stil",
                    description = "Du wählst Tempo und Schwierigkeit passend für dich."
                )
            }
        }

        Button(
            onClick = { navController.navigate("activity_planner/Lebensbalance") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Jetzt Mini-Schritt im Aktivitätsplaner festlegen")
        }

        OutlinedButton(
            onClick = { navController.navigate(Route.ModuleOneTwoActivityMoodMonitoring.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Weiter zu Modul 1.2")
        }

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zurück zu Inhalte")
        }
    }
}

data class NarrationPage(
    val title: String,
    val mainText: String,
    val keyPoints: List<String>,
    val practicePrompt: String
)

private fun moduleOneOnePages(): List<NarrationPage> = listOf(
    NarrationPage(
        title = "Ankommen und Orientierung",
        mainText = "Du kannst direkt beginnen. Eine klare Richtung hilft dir, aktiv zu werden und dranzubleiben.",
        keyPoints = listOf(
            "Beginne mit einem kleinen Schritt.",
            "Ein erster Schritt bringt Bewegung in den Tag.",
            "Beobachtungen helfen dir beim nächsten Plan."
        ),
        practicePrompt = "In welchem Lebensbereich wünschst du dir in den nächsten 7 Tagen mehr Stabilität?"
    ),
    NarrationPage(
        title = "Richtung wählen",
        mainText = "Ein Fokus pro Woche macht Entscheidungen leicht. Wähle eine Richtung wie Energie, soziale Verbindung, Struktur oder Erholung.",
        keyPoints = listOf(
            "Wo möchtest du öfter aktiv sein?",
            "Deine Priorität darf klar und einfach sein.",
            "Ein Fokus spart Energie im Alltag."
        ),
        practicePrompt = "Vervollständige: Diese Woche richte ich mich öfter auf ... aus."
    ),
    NarrationPage(
        title = "Mikro-Aktivierung planen",
        mainText = "Formuliere eine konkrete Handlung mit Zeitpunkt, Ort und Dauer. Ein klarer Plan ist leicht umsetzbar.",
        keyPoints = listOf(
            "Definiere wann, wo und wie lange.",
            "Wähle eine niedrige Einstiegshürde.",
            "Nutze eine Wenn-dann-Formulierung."
        ),
        practicePrompt = "Notiere einen Mini-Schritt mit Startzeit und Ort."
    ),
    NarrationPage(
        title = "Mit Hindernissen umgehen",
        mainText = "Plane eine kurze Ersatzversion für volle Tage. So bleibt dein Vorhaben im Alltag lebendig.",
        keyPoints = listOf(
            "Lege eine Kurzversion fest.",
            "Nutze eine freundliche Sprache mit dir selbst.",
            "Regelmäßigkeit stärkt deine Routine."
        ),
        practicePrompt = "Lege eine Backup-Variante fest, die in unter 3 Minuten machbar ist."
    ),
    NarrationPage(
        title = "Reflexion und Übergang",
        mainText = "Schau auf die Wirkung: Was hat gut funktioniert und was möchtest du anpassen? In Modul 1.2 beobachtest du Aktivität und Stimmung im Alltag.",
        keyPoints = listOf(
            "Du stärkst deine Selbststeuerung.",
            "Alltagsdaten zeigen dir hilfreiche Muster.",
            "Modul 1.2 baut auf deinem heutigen Mini-Schritt auf."
        ),
        practicePrompt = "Vervollständige: Mein nächster kleinstmöglicher Schritt ist ..."
    )
)

@Composable
private fun JourneyStep(icon: ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun StoryChip(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
