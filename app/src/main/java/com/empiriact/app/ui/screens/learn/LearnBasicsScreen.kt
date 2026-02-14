package com.empiriact.app.ui.screens.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.empiriact.app.ui.common.ExpandableCard

data class ModuleIntro(
    val title: String,
    val subtitle: String,
    val audience: String
)

data class ModuleBlock(
    val title: String,
    val summary: String,
    val bullets: List<String>
)

private val intro = ModuleIntro(
    title = "Modul 1: Wissenswertes über repetitives negatives Denken",
    subtitle = "Psychoedukation, Fallkonzeptualisierung und erste Interventionskompetenz bei Rumination.",
    audience = "Für Patient:innen und Therapeut:innen"
)

private val moduleBlocks = listOf(
    ModuleBlock(
        title = "Lernziele (app-kompakt)",
        summary = "Nach diesem Modul kannst du Grübeln sicher erkennen, einordnen und erste Gegenstrategien auswählen.",
        bullets = listOf(
            "RND/Rumination von konstruktivem Denken unterscheiden",
            "offenes und verdecktes Grübeln in Gesprächen erkennen",
            "mit Mikro-/Makroanalyse ein individuelles Störungsmodell aufbauen",
            "aus Analysezielen passende Interventionen ableiten",
            "Verlauf über einen Therapietracker bewerten"
        )
    ),
    ModuleBlock(
        title = "INFO 1-5 + AB 1: Was ist RND und warum ist es problematisch?",
        summary = "RND ist wiederholtes negatives Kreisen ohne ausreichenden Erkenntnis- oder Handlungsschritt.",
        bullets = listOf(
            "Rumination: eher vergangenheitsorientiert; Sorgen: eher zukunftsorientiert",
            "Übermaß von RND verstärkt emotionale Belastung und hält Störungen aufrecht",
            "Normales Nachdenken ist zeitlich begrenzt, konkret und lösungsorientiert",
            "AB 1 unterstützt den Vergleich: Gedankenkaugummi vs. konstruktives Problemlösen"
        )
    ),
    ModuleBlock(
        title = "INFO 6-9 + AB 2-3: Erkennen & Unterbrechen",
        summary = "Zentrale VT-Fähigkeit: früh merken, wann Denken von hilfreich zu dysfunktional kippt.",
        bullets = listOf(
            "3-Fragen-Daumenregel (AB 2): Klarheit, Handlungsfähigkeit, Endlosschleifen prüfen",
            "Offenes Grübeln: lange kreisende Monologe ohne neuen Schritt",
            "Verdecktes Grübeln: inneres Abdriften, stockende Antworten, Aufmerksamkeitsverlust",
            "Münztrick (AB 3): Markersignal, um den roten Faden zurückzugewinnen"
        )
    ),
    ModuleBlock(
        title = "INFO 10 + AB 4-7: Funktionsanalyse und Behandlungsplanung",
        summary = "SORKC-nahe Denkweise: Auslöser, Organismus, Reaktion, Konsequenzen und Kontingenzen betrachten.",
        bullets = listOf(
            "Mikroanalyse: konkrete Episode (Situation -> Reaktion -> Konsequenz)",
            "Makroanalyse: Vulnerabilitäten, biografische Auslöser, aktuelle Aufrechterhaltung",
            "AB 5 integriert Prädisposition, Metakognitionen, Strategien und Hindernisse",
            "AB 6 liefert strukturierten Fragenkatalog für die Exploration",
            "AB 7 übersetzt Analyse direkt in Ziele und Interventionen"
        )
    ),
    ModuleBlock(
        title = "INFO 11 + AB 8: Gemeinsame Problemdefinition & Verlaufsmonitoring",
        summary = "Therapieerfolg steigt, wenn beide Seiten dasselbe Problem bearbeiten: nicht nur das Was, sondern das Wie des Denkens.",
        bullets = listOf(
            "Kernbotschaft: Grübeln als Prozess ist das Ziel der Veränderung",
            "Erweiterte Perspektive: Nicht jeder negative Gedanke ist problematisch, sondern die Haltung dazu",
            "Sokratische Fragen fördern kognitive Flexibilität statt reiner Inhaltsdiskussion",
            "AB 8 trackt Leidensdruck, Häufigkeit, Dauer, Trigger, Skills und Umsetzungsbarrieren"
        )
    ),
    ModuleBlock(
        title = "Transfer in den Alltag",
        summary = "Plane pro Tag einen kurzen Check-in statt Perfektion: klein, regelmäßig, messbar.",
        bullets = listOf(
            "1x täglich 2-Minuten-Selbstcheck: Bin ich im Problemlösen oder im Grübeln?",
            "bei Grübelstart: eine Ressourcenübung sofort anwenden",
            "am Abend 1 Zeile im Therapietracker dokumentieren",
            "wöchentlich auswerten: Was hilft zuverlässig?"
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnBasicsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modul 1: RND verstehen") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = intro.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = intro.subtitle, style = MaterialTheme.typography.bodyMedium)
                    Text(text = intro.audience, style = MaterialTheme.typography.labelMedium)
                }
            }

            items(moduleBlocks) { block ->
                ExpandableCard(title = block.title) {
                    Text(text = block.summary, style = MaterialTheme.typography.bodyMedium)
                    Column(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        block.bullets.forEach { bullet ->
                            Text(
                                text = "• $bullet",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
