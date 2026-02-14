package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    val pages = moduleOneTwoNarrationPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Modul 1.2 · Leo zeigt dir Aktivität-Stimmungs-Muster",
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
                text = "Swipe durch das Modul: Jeder Abschnitt baut auf dem vorherigen auf.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            val page = pages[pageIndex]
            Card(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Schritt ${pageIndex + 1} von ${pages.size}: ${page.title}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = page.narratorText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    page.keyPoints.forEach { point ->
                        TrackingHint(point)
                    }
                    if (page.exercisePrompt.isNotBlank()) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Reflexionsimpuls", style = MaterialTheme.typography.titleSmall)
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
            ) {
                Text("Zurück")
            }

            OutlinedButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage((pagerState.currentPage + 1).coerceAtMost(pages.lastIndex))
                    }
                },
                enabled = pagerState.currentPage < pages.lastIndex,
                modifier = Modifier.weight(1f)
            ) {
                Text("Weiter")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Tracking-Architektur in 3 Schritten", style = MaterialTheme.typography.titleMedium)
                TrackingStep(
                    icon = Icons.Outlined.Timeline,
                    title = "1) Aktivität kurz benennen",
                    description = "Eintrag in 5–10 Sekunden: Was mache ich gerade?"
                )
                TrackingStep(
                    icon = Icons.Outlined.BarChart,
                    title = "2) Stimmung als Momentwert markieren",
                    description = "Nutze die 5er-Skala von -- bis ++ ohne lange Analyse."
                )
                TrackingStep(
                    icon = Icons.Outlined.CheckCircle,
                    title = "3) Nach 5–10 Einträgen Muster lesen",
                    description = "Du erkennst auslösende Situationen, statt dich auf Gefühlsschätzungen zu verlassen."
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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.Psychology, contentDescription = null)
                    Text("Leo erklärt die Stimmungsskala", style = MaterialTheme.typography.titleMedium)
                }
                MoodBar(label = "++", widthFactor = 1.0f, color = Color(0xFF2E7D32), meaning = "deutlich stabilisierend")
                MoodBar(label = "+", widthFactor = 0.8f, color = Color(0xFF66BB6A), meaning = "leicht stabilisierend")
                MoodBar(label = "0", widthFactor = 0.6f, color = Color(0xFFFFCA28), meaning = "neutral / unverändert")
                MoodBar(label = "-", widthFactor = 0.8f, color = Color(0xFFFFA726), meaning = "leicht belastend")
                MoodBar(label = "--", widthFactor = 1.0f, color = Color(0xFFE53935), meaning = "deutlich belastend")
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
    val narratorText: String,
    val keyPoints: List<String>,
    val exercisePrompt: String = ""
)

private fun moduleOneTwoNarrationPages(): List<MonitoringNarrationPage> = listOf(
    MonitoringNarrationPage(
        title = "Worum es in diesem Modul geht",
        narratorText = "Ich bin Leo, willkommen in Modul 1.2. Dieses Modul ist eigenständig nutzbar und funktioniert auch dann, wenn dein Alltag gerade chaotisch ist. Unsere therapeutische Idee: Stimmung wird klarer, wenn Verhalten sichtbar wird. Deshalb machen wir aus diffusen Eindrücken ein leichtes, alltagstaugliches Monitoring.",
        keyPoints = listOf(
            "Du brauchst keine perfekte Tagesstruktur.",
            "Kurze, regelmäßige Einträge sind wirksamer als lange Rückblicke.",
            "Ziel ist Orientierung für Entscheidungen, nicht Selbstbewertung."
        ),
        exercisePrompt = "Welche Tageszeit wäre für dich ein realistischer erster Tracking-Moment?"
    ),
    MonitoringNarrationPage(
        title = "Vom Ereignis zur Beobachtung",
        narratorText = "Viele Menschen merken nur: ‚Mir geht es plötzlich schlecht.‘ Wir schärfen den Blick davor: Was hast du gerade getan? Mit wem warst du? Welche Aufgabe lag an? Das ist klassische verhaltenstherapeutische Verhaltensanalyse in kompakter Form. Je konkreter die Situation, desto besser die Ableitung für den nächsten Tag.",
        keyPoints = listOf(
            "Ein Kontextsignal reicht: Ort, Person oder Aufgabe.",
            "Beschreibe neutral: ‚Meeting seit 30 Minuten‘ statt ‚Katastrophe‘.",
            "Kontext + Stimmung ergibt verwertbare Daten."
        ),
        exercisePrompt = "Formuliere einen neutralen Beispiel-Eintrag aus deinem Alltag."
    ),
    MonitoringNarrationPage(
        title = "Skalieren statt Grübeln",
        narratorText = "Wenn du skaliert einträgst (-- bis ++), entlastest du dein Arbeitsgedächtnis. Das ist gute Usability: geringe Eingabelast, klare Entscheidungspunkte, hohe Wiederholbarkeit. Gleichzeitig unterstützt ASIB deine Autonomie: Du entscheidest, wie fein du differenzierst und wann du Einträge vereinfachst.",
        keyPoints = listOf(
            "Skalen sind schneller als freie Texteingabe.",
            "Weniger Aufwand erhöht die Chance auf langfristige Nutzung.",
            "Du passt das System an dich an – nicht umgekehrt."
        ),
        exercisePrompt = "Lege fest: Möchtest du 3 oder 5 Einträge pro Tag als Startziel?"
    ),
    MonitoringNarrationPage(
        title = "Muster erkennen und Hypothesen testen",
        narratorText = "Nach einigen Tagen suchst du nicht nach ‚der Wahrheit‘, sondern nach Arbeitshypothesen: ‚Kurze Bewegung hebt meine Stimmung oft um eine Stufe‘ oder ‚Unklare Aufgaben drücken mich auf -‘. Daraus entstehen kleine Experimente. Dieses iterative Vorgehen ist ein Kern moderner digitaler Gesundheitsinterventionen.",
        keyPoints = listOf(
            "Erst Daten sammeln, dann deuten.",
            "Muster sind Wahrscheinlichkeiten, keine starren Regeln.",
            "Aus jeder Hypothese entsteht ein testbarer nächster Schritt."
        ),
        exercisePrompt = "Welche erste Hypothese möchtest du in den nächsten 3 Tagen prüfen?"
    ),
    MonitoringNarrationPage(
        title = "Transfer in deinen Alltag",
        narratorText = "Zum Abschluss: Dieses Modul endet nicht beim Tracking, sondern bei Handlung. Wenn du erkennst, was stabilisiert, planst du davon bewusst mehr. Wenn du erkennst, was belastet, baust du Schutzfaktoren ein. Genau so entsteht Schritt für Schritt Selbstwirksamkeit – datenbasiert, freundlich und in deinem Tempo.",
        keyPoints = listOf(
            "Du nutzt Daten zur Unterstützung, nicht zur Selbstkritik.",
            "Kleine Anpassungen haben über Wochen große Wirkung.",
            "Du bleibst Autor:in deiner Entscheidungen."
        ),
        exercisePrompt = "Vervollständige: ‚Auf Basis meines Trackings plane ich morgen konkret …‘"
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
