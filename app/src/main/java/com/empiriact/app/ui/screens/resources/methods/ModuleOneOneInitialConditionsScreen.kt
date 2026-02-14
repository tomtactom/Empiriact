package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.navigation.Route

@Composable
fun ModuleOneOneInitialConditionsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Modul 1.1 · Dein klarer Start in der App",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Heute legst du den Grundstein: Du schärfst deine Richtung, wählst passende Mini-Schritte und nutzt die App als Lernraum für deinen Alltag.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("So arbeitet Modul 1.1", style = MaterialTheme.typography.titleMedium)
                JourneyStep(
                    icon = Icons.Outlined.Flag,
                    title = "1) Richtung wählen",
                    description = "Du definierst, was dir in den nächsten Tagen spürbar wichtiger werden soll (z. B. Energie, Struktur, Verbindung, Fokus)."
                )
                JourneyStep(
                    icon = Icons.Outlined.Navigation,
                    title = "2) Mini-Schritt planen",
                    description = "Du entscheidest dich für eine kurze, realistische Handlung (5–15 Minuten), die zu deiner Richtung passt."
                )
                JourneyStep(
                    icon = Icons.Outlined.Insights,
                    title = "3) Wirkung beobachten",
                    description = "Du schaust danach kurz auf Wirkung und Aufwand – nicht um zu bewerten, sondern um besser zu verstehen, was für dich funktioniert."
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("ASIB in der Praxis: Du behältst die Steuerung", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Die App gibt dir Vorschläge – du entscheidest über Tempo, Umfang und Schwierigkeit. Jeder Eintrag ist hilfreiches Feedback für die nächste Anpassung.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Leitsatz: klein starten, ehrlich beobachten, gezielt nachjustieren.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Mini-Storyboard für diese Woche", style = MaterialTheme.typography.titleMedium)
                StoryChip("🌅 Morgen", "Eine kleine Startaktivität festlegen, die den Tag strukturierter macht.")
                StoryChip("🕒 Tagsüber", "Kurz notieren, wann dir Fokus oder Energie leichter/schwerer fällt.")
                StoryChip("🌙 Abend", "1 Satz Rückblick: Was hat heute am meisten geholfen?")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Outlined.AutoAwesome, contentDescription = null)
                    Text("Dein nächster sinnvoller Schritt", style = MaterialTheme.typography.titleMedium)
                }
                Text(
                    "Nutze jetzt den Aktivitätsplaner für deinen ersten Mini-Schritt. Danach baust du in Modul 1.2 auf diesen Start auf und erkennst Muster zwischen Aktivität und Stimmung.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = { navController.navigate("activity_planner/Lebensbalance") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mini-Schritt im Aktivitätsplaner anlegen")
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
