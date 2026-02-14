package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Swipe
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun ModuleOneTwoActivityMoodMonitoringScreen(navController: NavController) {
    val pages = moduleOneTwoPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Modul 1.2 · Aktivität und Stimmung beobachten",
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
                    items(page.keyPoints) { TrackingHint(it) }
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Übung", style = MaterialTheme.typography.titleSmall)
                                Text(page.exercisePrompt, style = MaterialTheme.typography.bodyMedium)
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Tracking in 3 Schritten", style = MaterialTheme.typography.titleMedium)
                TrackingStep(
                    icon = Icons.Outlined.Timeline,
                    title = "1) Aktivität benennen",
                    description = "Kurzer Eintrag: Was mache ich gerade?"
                )
                TrackingStep(
                    icon = Icons.Outlined.BarChart,
                    title = "2) Stimmung markieren",
                    description = "Nutze die Skala von -- bis ++."
                )
                TrackingStep(
                    icon = Icons.Outlined.CheckCircle,
                    title = "3) Muster erkennen",
                    description = "Lies nach einigen Einträgen wiederkehrende Zusammenhänge ab."
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.Psychology, contentDescription = null)
                    Text("Stimmungsskala", style = MaterialTheme.typography.titleMedium)
                }
                MoodBar(label = "++", widthFactor = 1.0f, color = Color(0xFF2E7D32), meaning = "sehr angenehm")
                MoodBar(label = "+", widthFactor = 0.8f, color = Color(0xFF66BB6A), meaning = "angenehm")
                MoodBar(label = "0", widthFactor = 0.6f, color = Color(0xFFFFCA28), meaning = "ausgeglichen")
                MoodBar(label = "-", widthFactor = 0.8f, color = Color(0xFFFFA726), meaning = "eher niedrig")
                MoodBar(label = "--", widthFactor = 1.0f, color = Color(0xFFE53935), meaning = "niedrig")
            }
        }

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zurück zu Inhalte")
        }
    }
}

data class MonitoringNarrationPage(
    val title: String,
    val mainText: String,
    val keyPoints: List<String>,
    val exercisePrompt: String
)

private fun moduleOneTwoPages(): List<MonitoringNarrationPage> = listOf(
    MonitoringNarrationPage(
        title = "Worum es geht",
        mainText = "Dieses Modul unterstützt dich dabei, Aktivität und Stimmung im Alltag leicht zu erfassen.",
        keyPoints = listOf(
            "Kurze Einträge reichen aus.",
            "Regelmäßige Einträge zeigen klare Muster.",
            "Du nutzt die Daten für deine nächsten Entscheidungen."
        ),
        exercisePrompt = "Welche Tageszeit passt dir für einen ersten Tracking-Moment?"
    ),
    MonitoringNarrationPage(
        title = "Vom Moment zur Beobachtung",
        mainText = "Erfasse kurz den Kontext: Ort, Person oder Aufgabe. So wird jeder Eintrag klar und gut vergleichbar.",
        keyPoints = listOf(
            "Ein Kontextsignal pro Eintrag ist ausreichend.",
            "Nutze kurze, neutrale Formulierungen.",
            "Kontext und Stimmung ergeben ein hilfreiches Bild."
        ),
        exercisePrompt = "Schreibe einen kurzen Beispiel-Eintrag aus deinem Alltag."
    ),
    MonitoringNarrationPage(
        title = "Skalieren",
        mainText = "Die Skala von -- bis ++ ist schnell nutzbar und unterstützt eine regelmäßige Anwendung.",
        keyPoints = listOf(
            "Die Skala spart Zeit.",
            "Wiederholbare Einträge helfen dir über mehrere Tage.",
            "Du kannst Detailgrad und Häufigkeit selbst wählen."
        ),
        exercisePrompt = "Möchtest du mit 3 oder 5 Einträgen pro Tag starten?"
    ),
    MonitoringNarrationPage(
        title = "Muster lesen",
        mainText = "Nach einigen Tagen kannst du wiederkehrende Zusammenhänge erkennen und daraus kleine Experimente planen.",
        keyPoints = listOf(
            "Sammle zuerst Einträge.",
            "Leite dann eine einfache Annahme ab.",
            "Plane einen konkreten nächsten Schritt."
        ),
        exercisePrompt = "Welche erste Annahme möchtest du in den nächsten 3 Tagen prüfen?"
    ),
    MonitoringNarrationPage(
        title = "Transfer",
        mainText = "Nutze deine Erkenntnisse direkt für den nächsten Tag. So wächst Schritt für Schritt deine Selbstwirksamkeit.",
        keyPoints = listOf(
            "Daten unterstützen deinen Alltag.",
            "Kleine Anpassungen wirken über die Zeit.",
            "Du triffst deine Entscheidungen in deinem Tempo."
        ),
        exercisePrompt = "Vervollständige: Auf Basis meines Trackings plane ich morgen ..."
    )
)

@Composable
private fun TrackingStep(icon: ImageVector, title: String, description: String) {
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
private fun MoodBar(label: String, widthFactor: Float, color: Color, meaning: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.padding(end = 4.dp), style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier
                .fillMaxWidth(widthFactor)
                .height(12.dp)
                .background(color, RoundedCornerShape(999.dp))
        ) {}
        Text(meaning, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun TrackingHint(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Text(text = "• $text", style = MaterialTheme.typography.bodyMedium)
    }
}
