package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
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
            text = "Modul 1.2 · Aktivitäten und Stimmung beobachten",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Ziel dieser Einheit: Gemeinsam eine praktikable, alltagstaugliche Beobachtung von Aktivitäten und Stimmung etablieren, um später hilfreiche Zusammenhänge zu erkennen.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Warum diese Beobachtung wichtig ist", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Zu Beginn der Therapie erstellen wir ein möglichst realistisches Bild deiner aktuellen Lebenssituation und deines Aktivitätsniveaus. " +
                        "Dafür trägst du täglich ein, was du tust und wie deine Stimmung dabei ist. Diese Daten helfen euch im nächsten Schritt, " +
                        "Muster zwischen konkreten Tätigkeiten, Kontexten und Stimmungsschwankungen zu erkennen.",
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
                Text("So kann die Erfassung aussehen", style = MaterialTheme.typography.titleMedium)
                Text(
                    "• Variante AB 1: Stündliche Erfassung (empfohlen für den Einstieg, wenn möglich)\n" +
                        "• Variante AB 1a: Erfassung in größeren Zeitblöcken (wenn stündlich im Alltag nicht praktikabel ist)",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Stündliche Einträge liefern oft die aussagekräftigeren Informationen und machen subtile Veränderungen besser sichtbar. " +
                        "Wenn das gerade zu aufwändig ist, ist eine vereinfachte Version trotzdem sinnvoll – wichtig ist, dass sie nachhaltig umsetzbar bleibt.",
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
                Text("Wie detailliert solltest du Aktivitäten notieren?", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Du musst nicht jede Einzelhandlung dokumentieren. Nutze passende Oberbegriffe (z. B. „Arbeit: Kundenkontakt“), " +
                        "oder notiere die Aktivität, die in dieser Stunde am längsten gedauert hat oder subjektiv am wichtigsten war.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Beispiel: Das 10‑minütige Telefonat mit einer Freundin kann relevanter sein als 30 Minuten Zeitunglesen – dann darf genau dieses Gespräch im Protokoll stehen.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic
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
                Text("Stimmungsskala (einfach und schnell)", style = MaterialTheme.typography.titleMedium)
                Text(
                    "• –– = schlecht\n" +
                        "• – = eher schlecht\n" +
                        "• 0 = neutral\n" +
                        "• + = gut\n" +
                        "• ++ = sehr gut",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Wichtig ist keine perfekte Genauigkeit, sondern eine wiederholte, ehrliche Momentaufnahme.",
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
                Text("ASIB-Leitlinie: Autonomie und Machbarkeit", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Du entscheidest gemeinsam mit deiner Therapeutin, welche Form der Dokumentation für dich aktuell realistisch ist. " +
                        "Die Protokolle sind eine Starthilfe. Im Verlauf könnt ihr auf alltagstaugliche Alternativen umsteigen, z. B. Taschenkalender oder Smartphone-Notizen.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Fokus ist nicht „Stimmung sofort verbessern“, sondern zunächst ein klares Verständnis dafür zu gewinnen, " +
                        "wie dein Alltag mit deinen persönlichen Werten zusammenhängt.",
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
                Text("Mögliche Schwierigkeiten – und hilfreiche Lösungen", style = MaterialTheme.typography.titleMedium)
                Text(
                    "1) Zu grobe Einträge (z. B. „9–17 Uhr Arbeit“, Stimmung „–“):\n" +
                        "   Besser: Teilabschnitte und relevante Momente notieren (Besprechung, Mittagspause, Kundentelefonat).\n\n" +
                        "2) Retrospektives Ausfüllen am Tagesende oder erst nach Tagen:\n" +
                        "   Besser: so nah am Zeitpunkt wie möglich dokumentieren, um feine Stimmungsschwankungen nicht zu verlieren.\n\n" +
                        "3) Protokolle bleiben leer oder unvollständig:\n" +
                        "   Wichtig: Auch Routinetätigkeiten oder subjektiv „wenig sinnvolle“ Zeiten (z. B. langes Scrollen/TV) sind zentrale Informationen – ohne Bewertung.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Mini-Start für diese Woche", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Wähle heute eine realistische Beobachtungsform (stündlich oder in Blöcken) und beginne mit einem Tag. " +
                        "Reflektiere kurz: Was hat das Eintragen erleichtert? Wo brauchst du eine Anpassung?",
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
