package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
fun ModuleOneOneInitialConditionsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Modul 1.1 · Gute Ausgangsbedingungen schaffen",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Ziel dieser Einheit: Einen gemeinsamen, sicheren Rahmen für die nächsten Wochen schaffen – mit Klarheit über Rollen, Erwartungen und den Lernprozess.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Was jetzt wichtig ist", style = MaterialTheme.typography.titleMedium)
                Text(
                    "• Du übernimmst eine aktive Rolle in der Therapie.\n" +
                        "• Die Therapeutin begleitet dich als Coach.\n" +
                        "• Zwischen den Sitzungen probierst du geplante Aktivitäten und kleine Übungen aus.\n" +
                        "• Es geht um Lernen durch Erfahrung – nicht um Perfektion.",
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
                Text("ASIB-Leitgedanke: Autonomie unterstützen", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Du entscheidest mit, welche Schritte du ausprobierst. Die Übungen sind Einladungen, keine Tests. " +
                        "Auch wenn etwas nicht klappt, ist das eine wertvolle Information darüber, was dir in deinem Alltag hilft – oder noch angepasst werden sollte.",
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
                Text("Ausblick auf die nächsten Wochen", style = MaterialTheme.typography.titleMedium)
                Text(
                    "In den kommenden Wochen lernst du, mit depressiver Stimmung, Antriebsverlust und weiteren Alltagsschwierigkeiten hilfreicher umzugehen. " +
                        "Dafür sammeln wir in den Sitzungen und zuhause Erfahrungen: Was stabilisiert dich langfristig? Was passt zu deinen persönlichen Werten?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "Die hilfreichen Schritte sind individuell. Gemeinsam finden wir heraus, was dir wichtig ist und wie du diese Werte in kleinen, machbaren Handlungen im Alltag leben kannst.",
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
                Text("Therapie-Statement (für die gemeinsame Orientierung)", style = MaterialTheme.typography.titleMedium)
                Text(
                    "„In den kommenden Wochen soll es darum gehen, dass Sie lernen, wie Sie besser mit Ihrer depressiven Stimmung, " +
                        "dem Antriebsverlust und anderen Schwierigkeiten in Ihrem Alltag umgehen können. Das Ziel der Therapie ist es, " +
                        "dass wir hier gemeinsam in den Sitzungen und auch Sie alleine zuhause Dinge ausprobieren und Ihre Erfahrungen " +
                        "damit sammeln, was Ihnen hilft, sich dauerhaft stabiler zu fühlen. Dabei ist es erfahrungsgemäß so, dass diese " +
                        "hilfreichen Dinge nicht für jede Person die gleichen sind, sondern dass es darum geht, für Sie persönlich " +
                        "herauszufinden, was für Sie wirklich wichtig ist und wie Sie das in Ihrem Alltag wieder mehr leben können. " +
                        "Das heißt, ich möchte mit Ihnen gemeinsam herausfinden, was Ihre individuellen Werte sind und wie Sie diese " +
                        "durch verschiedene Aktivitäten wieder mehr in Ihren Alltag integrieren können. Dazu möchte ich gerne am Ende " +
                        "jeder Sitzung kleine Übungen mit Ihnen vereinbaren, die Sie dann bis zur nächsten Woche zu Hause ausprobieren " +
                        "können. Dabei geht es nicht darum, dass diese Übungen unbedingt klappen oder Spaß machen müssen, sondern einfach " +
                        "darum, dass Sie Erfahrungen damit sammeln, was Ihnen hilft und was nicht. Diese Erfahrungen werden wir dann zu " +
                        "Beginn jeder Sitzung besprechen und gemeinsam überlegen, ob Sie die Übung so beibehalten oder verändern wollen.“",
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Mikro-Commitment bis zur nächsten Woche", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Wähle eine kleine Aktivität (5–15 Minuten), die zu einem persönlichen Wert passt. " +
                        "Notiere nach der Durchführung kurz: Was hat geholfen? Was war schwierig?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = { navController.navigate("activity_planner/Lebensbalance") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Übung im Aktivitätsplaner vorbereiten")
        }

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zurück zu Inhalte")
        }
    }
}
