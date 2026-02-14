package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
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
    val pages = moduleOneOneNarrationPages()
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Modul 1.1 · Leo begleitet dich in deinen Start",
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
                text = "Swipe nach links/rechts, um Leos Geschichte Schritt für Schritt weiterzulesen.",
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
                        StoryChip(timeLabel = "Leo", text = point)
                    }
                    if (page.practicePrompt.isNotBlank()) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Mini-Transfer", style = MaterialTheme.typography.titleSmall)
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Dein roter Faden aus Modul 1.1", style = MaterialTheme.typography.titleMedium)
                JourneyStep(
                    icon = Icons.Outlined.Flag,
                    title = "Richtung statt Druck",
                    description = "Verhaltenstherapeutisch gilt: klar benannte Richtung erhöht die Umsetzung, perfekter Plan ist nicht nötig."
                )
                JourneyStep(
                    icon = Icons.Outlined.Navigation,
                    title = "Mikro-Handlung statt Überforderung",
                    description = "Du startest mit 5–15 Minuten. So trainierst du Selbstwirksamkeit und reduzierst Aufschiebeverhalten."
                )
                JourneyStep(
                    icon = Icons.Outlined.Insights,
                    title = "Beobachtung statt Selbstkritik",
                    description = "ASIB: Du bestimmst Tempo und Schwierigkeit. Jeder Versuch liefert Daten, kein Urteil."
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null)
                Text(
                    text = "Wenn du deinen Mini-Schritt geplant hast, wirkt Modul 1.2 noch stärker – dort erkennst du Muster zwischen Aktivität und Stimmung.",
                    style = MaterialTheme.typography.bodyMedium
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
    val narratorText: String,
    val keyPoints: List<String>,
    val practicePrompt: String = ""
)

private fun moduleOneOneNarrationPages(): List<NarrationPage> = listOf(
    NarrationPage(
        title = "Ankommen und Orientierung",
        narratorText = "Ich bin Leo. Stell dir vor, wir gehen heute nicht mit Druck, sondern mit Klarheit los. Dieses Modul steht für sich: Du brauchst nichts vorbereitet zu haben. Wir beginnen mit einer einfachen therapeutischen Grundidee: Verhalten verändert Stimmung häufiger als Grübeln allein. Deshalb starten wir mit einer Richtung, nicht mit einem perfekten Ziel.",
        keyPoints = listOf(
            "Du darfst klein anfangen und trotzdem ernsthaft trainieren.",
            "Das Ziel ist ein erster, machbarer Schritt – nicht ein kompletter Neustart.",
            "Jede Beobachtung ist nützlich, auch wenn es schwer war."
        ),
        practicePrompt = "Frage dich jetzt: In welchem Lebensbereich wünsche ich mir in den nächsten 7 Tagen 5 % mehr Stabilität?"
    ),
    NarrationPage(
        title = "Wertebasierte Richtung wählen",
        narratorText = "Viele Menschen blockieren sich mit dem Gedanken: ‚Ich müsste alles gleichzeitig verbessern.‘ Verhaltenstherapeutisch ist hilfreicher: ein Fokus pro Woche. Wähle eine Richtung wie Energie, soziale Verbindung, Struktur oder Erholung. ASIB bedeutet hier: Du wählst, was sich für dich sinnvoll und stimmig anfühlt.",
        keyPoints = listOf(
            "Richtung heißt: wohin du häufiger handeln willst.",
            "Du musst dich nicht rechtfertigen – deine Priorität ist gültig.",
            "Ein klarer Fokus spart mentale Energie im Alltag."
        ),
        practicePrompt = "Formuliere einen Satz: ‚Diese Woche richte ich mich öfter auf … aus.‘"
    ),
    NarrationPage(
        title = "Mikro-Aktivierung planen",
        narratorText = "Jetzt wird es praktisch: Wir übersetzen die Richtung in eine beobachtbare Handlung. Nicht ‚mehr Sport‘, sondern ‚Dienstag 10 Minuten Spaziergang nach dem Mittagessen‘. Das ist Usability im Alltag: geringe Reibung, klare Auslöser, einfache Durchführung. Kleine Verhaltensschritte sind robuster als motivationale Hochphasen.",
        keyPoints = listOf(
            "Definiere wann, wo und wie lange.",
            "Wähle eine Hürde, die aktuell realistisch niedrig ist.",
            "Lege eine Wenn-dann-Brücke fest: ‚Wenn 13:00 Uhr, dann 10 Minuten raus.‘"
        ),
        practicePrompt = "Notiere einen Mini-Schritt mit Startzeit und Ort."
    ),
    NarrationPage(
        title = "Umgang mit Hindernissen",
        narratorText = "Ich sage es klar: Rückschritte sind keine Ausnahme, sondern Teil des Lernprozesses. In der KVT planen wir deshalb vor: Was tue ich bei Müdigkeit, Zeitdruck oder innerem Widerstand? Eine flexible Ersatzversion schützt deine Kontinuität. Beispiel: statt 15 Minuten nur 3 Minuten – Hauptsache Kontakt zur Gewohnheit bleibt bestehen.",
        keyPoints = listOf(
            "Plane eine Minimalversion für schwierige Tage.",
            "Benutze neutrale Sprache: ‚heute angepasst‘ statt ‚versagt‘.",
            "Kontinuität ist therapeutisch wirksamer als Perfektion."
        ),
        practicePrompt = "Lege jetzt eine Backup-Variante fest, die in unter 3 Minuten machbar ist."
    ),
    NarrationPage(
        title = "Reflexion und Übergang",
        narratorText = "Zum Abschluss von Modul 1.1 richte den Blick auf Wirkung: Was hat dir gutgetan? Was war aufwendig? Genau mit dieser Haltung geht es in Modul 1.2 weiter. Dort erfassen wir Aktivität und Stimmung systematisch, damit du nicht raten musst, sondern Muster erkennst. Du hast also bereits den wichtigsten therapeutischen Schritt geschafft: vom Gedanken ins Verhalten.",
        keyPoints = listOf(
            "Du trainierst Selbststeuerung, nicht Selbstkontrolle unter Druck.",
            "Daten aus dem Alltag ersetzen pauschale Selbsturteile.",
            "Modul 1.2 baut auf deinem heutigen Mini-Schritt auf."
        ),
        practicePrompt = "Beende dieses Modul mit einem Satz: ‚Mein nächster kleinstmöglicher Schritt ist …‘"
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
private fun StoryChip(timeLabel: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(timeLabel, style = MaterialTheme.typography.labelLarge)
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}
