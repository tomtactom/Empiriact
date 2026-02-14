package com.empiriact.app.ui.screens.learn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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

data class ModuleSection(val title: String, val body: String)

private val moduleOneSections = listOf(
    ModuleSection(
        "Modul 1: Wissenswertes über repetitives negatives Denken",
        "Dieses Modul erklärt RND und Grübeln, zeigt Folgen auf und unterstützt dich dabei, Grübeln sicher von konstruktivem Denken zu unterscheiden. Außerdem lernst du, wie RND in der Therapiestunde erkannt, analysiert und gezielt bearbeitet wird."
    ),
    ModuleSection(
        "INFO 1-4: Definition, Folgen und Störungsbezug",
        "RND umfasst wiederkehrende, schwer steuerbare negative Denkprozesse. Rumination (Grübeln) ist häufig vergangenheitsorientiert, Sorgen eher zukunftsbezogen. Beide können psychische Belastung erhöhen und stehen mit verschiedenen Störungen in Zusammenhang. Für die Therapie gilt: normales Nachdenken von dysfunktionalem Kreisen unterscheiden."
    ),
    ModuleSection(
        "INFO 5 + AB 1: Gedankenkaugummi vs. konstruktives Denken",
        "Grübeln ist oft abstrakt, wiederholend und führt selten zu neuen Lösungen. Konstruktives Denken ist konkret, handlungsorientiert und zeitlich begrenzt. Leitfrage: Führt mein Denken zu einem nächsten hilfreichen Schritt – oder drehe ich mich im Kreis?"
    ),
    ModuleSection(
        "INFO 6 + AB 2: Rumination erkennen (3-Fragen-Daumenregel)",
        "Frage 1: Bin ich im Warum-/Was-wäre-wenn-Modus statt im Was-kann-ich-jetzt-tun-Modus?\nFrage 2: Wird mein Denken mit der Zeit klarer oder verworrener?\nFrage 3: Fühle ich mich danach handlungsfähiger – oder erschöpfter?\nWenn 2-3 Antworten in Richtung Unklarheit, Erschöpfung, Endlosschleife gehen, ist es wahrscheinlich Rumination."
    ),
    ModuleSection(
        "INFO 7-9 + AB 3: Grübeln in der Sitzung erkennen und stoppen",
        "Offenes Grübeln zeigt sich z. B. durch lange Gedankenschleifen beim Sprechen. Verdecktes Grübeln kann sich als Abwesenheit, stockende Antworten oder innere Fixierung zeigen. Der Münztrick (AB 3): Eine Münze markiert den Moment, in dem die Person vom Erzählen ins Grübeln kippt; dadurch wird der rote Faden sichtbar und eine Rückkehr ins konkrete Arbeiten erleichtert."
    ),
    ModuleSection(
        "INFO 10: In drei Schritten zur passenden Intervention",
        "1) Rumination präzise identifizieren (Auslöser, Form, Funktion).\n2) Mit Funktionsanalyse auslösende und aufrechterhaltende Bedingungen verstehen.\n3) Daraus individualisierte Ziele und Interventionen ableiten (statt Standardlösung für alle)."
    ),
    ModuleSection(
        "AB 4-6: Funktionsanalyse (Mikro + Makro) und Leitfragen",
        "Mikroanalyse: Konkrete Episode (Situation -> Reaktion -> Konsequenz).\nMakroanalyse: Lebensgeschichtliche Vulnerabilitäten, Auslöser und aktuelle Aufrechterhaltung.\nLeitfragen (AB 6) erfassen Kontext, Gedanken, Gefühle, Körperreaktionen, Verhalten, Konsequenzen, Metakognitionen und Veränderungsbarrieren."
    ),
    ModuleSection(
        "AB 5 (1/2): Makroanalyse - Vulnerabilitäten",
        "- Genetische Prädisposition für bestimmte psychische Störungen\n- Ungünstige Temperamentsfaktoren (z. B. Impulsivität, Ängstlichkeit)\n- Neurobiologie (z. B. ADHS)\n- Somatische Erkrankungen (z. B. chronische Erkrankungen mit wiederholtem Sorgeanlass)\n- Metakognitionen\n- Dysfunktionale (Grund-)Annahmen (z. B. Perfektionismus, Gefährlichkeitsannahmen, rigide Emotionsnormen)\n- Rigide Persönlichkeitsstile / ungünstige Persönlichkeitsmerkmale (z. B. Unsicherheitsintoleranz)\n- Dysfunktionaler elterlicher Einfluss (modellierte Rumination, übermäßige Kritik/Kontrolle)\n- Defizite emotionaler Kompetenz (z. B. Erlebnisvermeidung, dysfunktionale Emotionseinstellungen)"
    ),
    ModuleSection(
        "AB 5 (2/2): Makroanalyse - Auslösung und Aufrechterhaltung",
        "Auslösung in der Lebensgeschichte:\n- Kritische Lebensereignisse (z. B. Scheidung, Jobverlust, Erkrankung)\n- Chronischer Stress\n- Entwicklungsaufgaben\n- Anpassungsanfordernde neue Situationen\n- Verhaltensdefizite bei Bewältigung\n\nAktuelle Aufrechterhaltung:\n- Internale Funktionalität (z. B. Emotions-/Konfliktvermeidung)\n- Externale Funktionalität (z. B. soziale Zuwendung)\n- Dysfunktionale mentale Strategien (z. B. Gedankenunterdrückung)\n- Dysfunktionale behaviorale Strategien (z. B. Rückversichern, Vermeidung, Substanzkonsum)\n- Dysfunktionale Aufmerksamkeitslenkung (Bedrohungsmonitoring, Selbstfokus)\n- Hindernisse für Verbesserung (z. B. chronische Belastungsfaktoren)"
    ),
    ModuleSection(
        "AB 6: Situative Verhaltensanalyse - Fragenkatalog",
        "Situation: letzte/typische Grübelsituation filmisch rekonstruieren (Zeit, Ort, Personen, vorherige Gedanken/Bilder/Erinnerungen, Emotionen, Körperzustand).\nReaktion: Grübelinhalt, Dauer, begleitende Gefühle/Körperreaktionen, Verhalten inkl. dysfunktionaler Strategien.\nKonsequenzen: unmittelbare und nachgelagerte Folgen, neue Gedanken, Verhalten danach, Reaktionen anderer, Entwicklung der Anfangsgefühle.\nOrganismus: familiäre Belastung, anfällig machende Einstellungen, körperliche Faktoren, positive/negative Metakognitionen.\nWeitere Exploration: Häufigkeit, Verstärker/Unterbrecher, typische Zeiten/Orte."
    ),
    ModuleSection(
        "AB 7: Behandlungsplan auf Basis des Störungsmodells",
        "Aus den Ergebnissen der Funktionsanalyse wird ein individuelles Entstehungs- und Aufrechterhaltungsmodell abgeleitet. Für jede Analyseebene werden Ziele und Interventionen abgeleitet:\n- Auslösende Bedingungen + Verhaltensdefizite\n- Aufrechterhaltende Bedingungen (Funktionalität, Strategien, Aufmerksamkeitsprozesse)\n- Prädisponierende Bedingungen (Vulnerabilitäten)\n- Hindernisse der Veränderung"
    ),
    ModuleSection(
        "INFO 11: Gemeinsame Problemdefinition",
        "Kernbotschaft: Nicht primär das \"Was\" (Inhalt), sondern das \"Wie\" (Denkstil) ist das Problem. Therapieerfolg braucht ein gemeinsames Verständnis: Grübeln im aktuellen Ausmaß ist dysfunktional und hält Beschwerden aufrecht. Später wird die Definition erweitert: Nicht negative Gedanken an sich sind das Problem, sondern die Haltung dazu (automatisch für wahr/relevant halten oder nicht akzeptierend bekämpfen)."
    ),
    ModuleSection(
        "AB 8: Therapietracker zur Verlaufsevaluation",
        "Regelmäßige Selbsteinschätzung mit Datumsspalten:\n- Leidensdruck durch Grübeln (0-10)\n- Häufigkeit (0-5)\n- Dauer der Grübelepisoden (1-4)\n- Auslöser\n- Erprobte Alternativen/Skills und erlebte Wirkung (+, +/-, -)\n- Schwierigkeiten bei der Anwendung der Alternativen/Skills"
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
            items(moduleOneSections) { section ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = section.body,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
