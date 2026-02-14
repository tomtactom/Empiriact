package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ModuleOneTwoActivityMoodMonitoringScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Modul 1.2 · Aktivität & Stimmung sichtbar machen",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Dieses Modul baut auf 1.1 auf: Aus deinem Mini-Schritt wird jetzt ein kleines Tracking-System, das dir klare Hinweise für bessere Alltagsentscheidungen liefert.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Dein Ablauf in 3 Schritten", style = MaterialTheme.typography.titleMedium)
                TrackingStep(
                    icon = Icons.Outlined.Timeline,
                    title = "1) Kurz eintragen",
                    description = "Halte in wenigen Stichworten fest, was du gerade gemacht hast."
                )
                TrackingStep(
                    icon = Icons.Outlined.BarChart,
                    title = "2) Stimmung markieren",
                    description = "Nutze die 5er-Skala von -- bis ++ für eine schnelle Momentaufnahme."
                )
                TrackingStep(
                    icon = Icons.Outlined.CheckCircle,
                    title = "3) Muster erkennen",
                    description = "Nach einigen Einträgen siehst du, welche Kontexte dich eher stärken oder eher ausbremsen."
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
                Text("Visuelle Stimmungsskala", style = MaterialTheme.typography.titleMedium)
                MoodBar(label = "++", widthFactor = 1.0f, color = Color(0xFF2E7D32), meaning = "sehr gut")
                MoodBar(label = "+", widthFactor = 0.8f, color = Color(0xFF66BB6A), meaning = "eher gut")
                MoodBar(label = "0", widthFactor = 0.6f, color = Color(0xFFFFCA28), meaning = "neutral")
                MoodBar(label = "-", widthFactor = 0.8f, color = Color(0xFFFFA726), meaning = "eher niedrig")
                MoodBar(label = "--", widthFactor = 1.0f, color = Color(0xFFE53935), meaning = "niedrig")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Usability-Regeln für nachhaltiges Tracking", style = MaterialTheme.typography.titleMedium)
                Text("• Lieber regelmäßig kurz als selten perfekt.", style = MaterialTheme.typography.bodyMedium)
                Text("• Wähle eine Taktung, die zu deinem Alltag passt (stündlich oder in Blöcken).", style = MaterialTheme.typography.bodyMedium)
                Text("• Nutze neutrale Begriffe: beobachten statt bewerten.", style = MaterialTheme.typography.bodyMedium)
                Text("• Ergänze 1 Kontextsignal pro Eintrag (Ort, Person oder Aufgabe).", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("ASIB-Impuls", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Du steuerst die Intensität: Wenn ein Format zu aufwändig ist, vereinfachst du es. Das Ziel ist ein System, das dir wirklich hilft und langfristig nutzbar bleibt.",
                    style = MaterialTheme.typography.bodyMedium
                )
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
