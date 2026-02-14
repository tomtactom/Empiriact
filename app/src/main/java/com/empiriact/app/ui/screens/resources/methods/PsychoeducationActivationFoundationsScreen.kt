package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions

private val experimentSuggestions = listOf(
    "10 Minuten spazieren",
    "5 Minuten frische Luft am Fenster/Balkon",
    "Kurze Nachricht an eine vertraute Person",
    "15 Minuten ruhiges Aufräumen",
    "2 Minuten Atemfokus + Körperwahrnehmung"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsychoeducationActivationFoundationsScreen(navController: NavController) {
    var weekExperiment by remember { mutableStateOf("") }
    var reflectionStep by remember { mutableStateOf("") }
    var reflectionDirection by remember { mutableStateOf("") }
    var reflectionSupport by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gute Ausgangsbedingungen schaffen") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = ContentDescriptions.CLOSE_DIALOG)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ModuleCard(title = "1) Worum es hier geht") {
                    Text(
                        "Damit Veränderung im Alltag leichter starten kann, hilft ein klarer Rahmen: Du übernimmst eine aktive Rolle – die App unterstützt dich dabei wie ein Coach.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    SpacerLine()
                    Text(
                        "Coach (Erklärung): Begleitung, die hilft zu strukturieren, zu reflektieren und dranzubleiben – ohne zu bewerten.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            item {
                ModuleCard(title = "2) Kernidee: Aktiv werden als Lernprozess") {
                    Bullet("Du planst kleine Aktivitäten und probierst sie zwischen deinen Check-ins aus.")
                    Bullet("Werteorientiert heißt: Du richtest dich an dem aus, was dir wirklich wichtig ist (z. B. Nähe, Gesundheit, Freiheit, Lernen).")
                    Bullet("Es geht eher um Ausprobieren als um „perfekt machen“.")
                    Bullet("Wenn etwas nicht klappt, ist das keine Niederlage, sondern Datenmaterial für den nächsten Versuch.")
                    SpacerLine()
                    Text(
                        "Wenn etwas nicht klappt, frage dich:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Bullet("Was hat gebremst?")
                    Bullet("Was hat geholfen – auch ein bisschen?")
                    Bullet("Was passt (noch) nicht zu dir?")
                    SpacerLine()
                    Text(
                        "Fachbegriff: antidepressiv wirksam = etwas, das Stimmung, Antrieb und Stabilität langfristig unterstützen kann (bei jeder Person etwas anders).",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            item {
                ModuleCard(title = "3) Was dich in den nächsten Wochen typischerweise erwartet") {
                    Text("Du lernst Schritt für Schritt:", style = MaterialTheme.typography.bodyMedium)
                    Bullet("depressive Stimmung und Antriebsverlust besser einzuordnen")
                    Bullet("zu erkennen, was dich stärkt (und was dich eher runterzieht)")
                    Bullet("deine Werte zu entdecken")
                    Bullet("Aktivitäten zu wählen, die diese Werte im Alltag wieder sichtbarer machen")
                    SpacerLine()
                    Text(
                        "Mini-Text für einen App-Screen (kompakt)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "Du sammelst in den nächsten Wochen Erfahrungen damit, was dir hilft, dich stabiler zu fühlen. Dafür testest du kleine Aktivitäten im Alltag. Nicht alles fühlt sich sofort gut an – und auch das kann dir zeigen, was du brauchst und was besser zu dir passt.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            item {
                ModuleCard(title = "4) Mini-Übung: Experiment statt Prüfung (2 Minuten)") {
                    Text(
                        "Wähle für die nächste Woche eine kleine Sache, die du testen möchtest.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    SpacerLine()
                    Text(
                        "Vorschläge (optional):",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    experimentSuggestions.forEach { suggestion ->
                        AssistChip(
                            onClick = { weekExperiment = suggestion },
                            label = { Text(suggestion) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                    }
                    OutlinedTextField(
                        value = weekExperiment,
                        onValueChange = { weekExperiment = it },
                        label = { Text("Mein Wochen-Experiment") },
                        supportingText = {
                            Text("Halte es klein und realistisch (ca. 2–15 Minuten).")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                ModuleCard(title = "5) Reflexionsfragen") {
                    Text(
                        "Nutze die Fragen als Kompass statt als Test.",
                        style = MaterialTheme.typography.bodySmall
                    )
                    OutlinedTextField(
                        value = reflectionStep,
                        onValueChange = { reflectionStep = it },
                        label = { Text("Was wäre ein kleiner Schritt, der zu mir passen könnte?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = reflectionDirection,
                        onValueChange = { reflectionDirection = it },
                        label = { Text("Woran würde ich 5% Fortschritt merken?") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = reflectionSupport,
                        onValueChange = { reflectionSupport = it },
                        label = { Text("Welche Unterstützung könnte den Schritt erleichtern?") },
                        supportingText = { Text("z. B. Zeit, Ort, Erinnerung, Person") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun ModuleCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}

@Composable
private fun Bullet(text: String) {
    Text("• $text", style = MaterialTheme.typography.bodyMedium)
}

@Composable
private fun SpacerLine() {
    Divider(modifier = Modifier.padding(vertical = 2.dp))
}
