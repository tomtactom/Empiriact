package com.empiriact.app.ui.screens.modules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border

enum class ThumbRuleRating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}

data class ThumbRuleSection(
    val title: String,
    val icon: String,
    val content: String
)

@Composable
fun ThumbRuleModuleScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf<ThumbRuleRating?>(null) }
    var expandedSection by remember { mutableStateOf(-1) }

    val sections = listOf(
        ThumbRuleSection(
            title = "Die Erkenntnis-Problematik",
            icon = "🤔",
            content = """WARUM IST GRÜBELN SCHWER ZU ERKENNEN?

Das Problem:
❌ Patient:innen merken oft NICHT, dass sie grübeln
❌ Das Denken beginnt KONSTRUKTIV
❌ Es driftet UNMERKLICH in Grübeln ab
❌ Der Übergang ist FLIESSEND

BEISPIEL:

Start (konstruktiv):
"Mir ist ein Fehler passiert. Wie behebe ich das?"
(Sinnvoll, handlungsorientiert)
        ↓
Übergang (unmerklich):
"Wie konnte ich so doof sein?"
        ↓
Grübeln (merkt man oft nicht):
"Warum bin ich immer so unfähig?"
"Warum passiert mir das immer?"
"Werde ich jemals kompetent sein?"
(Zirkulär, destruktiv, emotionalisierend)

ZENTRALE ERKENNTNIS:

Das DENKEN IST NICHT DAS PROBLEM
Das GRÜBELN IST DAS PROBLEM

Deswegen: Lerne, Grübeln zu ERKENNEN!

ZIEL DIESES MODULS:

Dir helfen, schneller zu bemerken:
"Ah, ICH GRÜBLE GERADE"

Mit der 3-Fragen-Daumenregel"""
        ),
        ThumbRuleSection(
            title = "Das Gedankenexperiment",
            icon = "🚗",
            content = """TEIL 1: GRÜBEL-SZENARIO – "WARUM-Fragen"

SITUATION:
Du hast einen Mietwagen für eine Urlaubsreise gebucht.
Nach einer Weile in einer menschenleeren Landschaft
machst du Pause – aber der Motor springt nicht mehr an!

AUFGABE:
Stell dir die Situation vor (ca. 2 Minuten).
Stelle dir möglichst viele WARUM-FRAGEN:

"Warum muss das ausgerechnet JETZT passieren?"
"Warum habe ich das Auto nicht vorher geprüft?"
"Warum passiert immer MIR so etwas?"
"Warum bin ich so unpraktisch?"
"Warum löse ich nie meine Probleme?"

BEOBACHTE:
Wie fühlt sich DEINE STIMMUNG an?
Kommst du einer LÖSUNG näher?

DANACH:

Markiere auf einer Skala (-3 bis +3):
├─ Stimmung: sehr negativ (-3) bis sehr positiv (+3)
└─ Nähe zu Lösung: weit entfernt (-3) bis Plan steht (+3)

DANN:

═══════════════════════════════════════

TEIL 2: KONSTRUKTIV-SZENARIO – "WIE-Fragen"

SITUATION:
Die gleiche Situation!
Motor springt nicht an.
Du bist in einer menschenleeren Landschaft.

AUFGABE:
Stell dir WIEDER vor (ca. 2 Minuten).
Stelle dir möglichst viele WIE-FRAGEN:

"WIE komme ich am schnellsten zurück?"
"WIE gehe ich praktisch vor?"
"WAS IST konkret zu tun?"
"WER kann mir helfen?"
"WAS sind meine Optionen?"

BEOBACHTE:
Wie fühlt sich DEINE STIMMUNG jetzt an?
Kommst du einer LÖSUNG näher?

DANACH:

Markiere WIEDER auf den Skalen (andere Farbe):
├─ Stimmung: sehr negativ (-3) bis sehr positiv (+3)
└─ Nähe zu Lösung: weit entfernt (-3) bis Plan steht (+3)

VERGLEICH:

Schau dir beide Markierungen an.
Unterschiede erkennbar?
Welche Version war konstruktiver?"""
        ),
        ThumbRuleSection(
            title = "Das Erleben & Unterschiede",
            icon = "📊",
            content = """WAS IST TYPISCHERWEISE DAS ERLEBNIS?

GRÜBEL-VERSION (Warum-Fragen):

😔 Stimmung:
   • Meist deutlich negativer (-2 bis -3)
   • Niedergeschlagenheit nimmt zu
   • Hoffnungslosigkeit entsteht

❌ Nähe zu Lösung:
   • Meist deutlich weiter weg (-2 bis -3)
   • Keine klaren Schritte
   • Nur Analyse, keine Aktion

MENSCHEN BERICHTEN:
"Ich fühle mich hilflos"
"Ich sehe keinen Weg"
"Ich bin schuldig"
"Alles ist aussichtslos"

───────────────────────────────────────

KONSTRUKTIV-VERSION (Wie-Fragen):

😊 Stimmung:
   • Meist besser (0 bis +2)
   • Weniger belastet
   • Hoffnung entsteht

✅ Nähe zu Lösung:
   • Meist näher an Lösung (+1 bis +3)
   • Konkrete Schritte erkennbar
   • Handlungsplan entsteht

MENSCHEN BERICHTEN:
"Ich weiß, was zu tun ist"
"Ich sehe eine Lösung"
"Ich kann etwas tun"
"Es gibt einen Plan"

───────────────────────────────────────

DER DEUTLICHE UNTERSCHIED:

Grübel-Version:        Konstruktiv-Version:
Stimmung: -3 → -2     Stimmung: 0 → +2
Lösung: -3 → -2       Lösung: -1 → +3

Das Differenz ist OFFENSICHTLICH!

ZENTRALE ERKENNTNIS:

Grübeln führt zu:
❌ Niedergeschlagenheit
❌ Hoffnungslosigkeit
❌ Entfernung von Lösung
❌ Mehr emotionaler Belastung

Konstruktives Denken führt zu:
✅ Bessere Stimmung
✅ Hoffnung
✅ Nähe zu Lösung
✅ Weniger Belastung

Das ist MESSBAR & SPÜRBAR!"""
        ),
        ThumbRuleSection(
            title = "Grübeln vs. Konstruktiv",
            icon = "⚖️",
            content = """KLARE UNTERSCHEIDUNG

GRÜBELN:
Definition: Grübeln bedeutet, sich immer wieder
mit negativen Fragen zu befassen, ohne dabei
eine Lösung zu finden.

MERKMALE:
❌ Warum-Fokus
   "Warum passiert das mir?"
   "Warum bin ich so?"
   "Warum geht das nicht?"

❌ Zirkulär
   Immer die gleichen Gedanken
   Keine Fortbewegung
   Kreiseln im Kreis

❌ Emotional aktivierend
   Mehr Traurigkeit
   Mehr Angst
   Mehr Hoffnungslosigkeit

❌ Unproduktiv
   Keine Lösung
   Keine Schritte
   Nur Analyse

───────────────────────────────────────

KONSTRUKTIVES DENKEN:
Definition: Konstruktives Denken bedeutet,
ein Problem zu analysieren und zu überlegen,
welche Schritte man konkret tun kann.

MERKMALE:
✅ Wie/Was-Fokus
   "Wie gehe ich vor?"
   "Was kann ich tun?"
   "Was sind meine Optionen?"

✅ Zielgerichtet
   Bewegung nach vorne
   Konkrete Schritte
   Vom Problem zur Lösung

✅ Emotional entlastend
   Weniger Traurigkeit
   Weniger Angst
   Hoffnung & Zuversicht

✅ Produktiv
   Konkreter Plan
   Handlungsschritte
   Lösungsorientiert

───────────────────────────────────────

ABER WICHTIG:

Das Denken beginnt OFT KONSTRUKTIV
und driftet UNMERKLICH ab!

Deswegen:
Du brauchst eine REGEL zum ERKENNEN
Das ist die 3-Fragen-Daumenregel!"""
        ),
        ThumbRuleSection(
            title = "Die 3-Fragen-Daumenregel",
            icon = "👍",
            content = """DIE PRAKTISCHE REGEL FÜR DEN ALLTAG

WENN DU UNSICHER BIST, OB DU GRÜBELST:

Lass das Denken noch 2 MINUTEN weiterlaufen.
Dann stelle dir diese 3 FRAGEN:

───────────────────────────────────────

FRAGE 1: NÄHE ZUR LÖSUNG?

"Bin ich einer Lösung nähergekommen?
 Habe ich eine Idee, wie ich vorgehen werde?"

Fokus: HANDLUNGSPLAN

✅ JA:
   • Du hast konkrete Schritte
   • Du weißt, was zu tun ist
   • Ein Plan entsteht

❌ NEIN:
   • Nur Analyse ohne Lösung
   • Keine konkreten Schritte
   • Du bleibst im Denken stecken

───────────────────────────────────────

FRAGE 2: STIMMUNG BESSER?

"Ist meine Stimmung besser geworden?"

Fokus: EMOTIONALES BEFINDEN

✅ JA:
   • Du fühlst dich hoffnungsvoller
   • Weniger belastet
   • Mehr Zuversicht

❌ NEIN:
   • Deine Stimmung ist gleich oder schlechter
   • Du fühlst dich noch belasteter
   • Hoffnungslosigkeit wächst

───────────────────────────────────────

FRAGE 3: SELBSTKRITIK WENIGER?

"Bin ich weniger selbstkritisch geworden?"

Fokus: SELBSTWERT & SELBSTMITGEFÜHL

✅ JA:
   • Du bist weniger hart mit dir selbst
   • Mehr Selbstmitgefühl
   • Weniger Selbstvorwürfe

❌ NEIN:
   • Mehr Selbstkritik
   • Mehr Schuldgefühle
   • Weniger Selbstmitgefühl

───────────────────────────────────────

DIE ENTSCHEIDUNG:

"Wenn du nicht WENIGSTENS EINE FRAGE
eindeutig BEJAHEN kannst,
handelt es sich SEHR WAHRSCHEINLICH UM GRÜBELN!"

BEISPIELE:

Situation 1:
Frage 1: Bin ich näher an Lösung? JA ✅
Frage 2: Ist Stimmung besser? JA ✅
Frage 3: Weniger Selbstkritik? JA ✅
→ KONSTRUKTIV (mindestens 1 JA)

Situation 2:
Frage 1: Nähe zu Lösung? NEIN ❌
Frage 2: Stimmung besser? NEIN ❌
Frage 3: Weniger Selbstkritik? NEIN ❌
→ GRÜBELN (kein eindeutiges JA)

Situation 3:
Frage 1: Nähe zu Lösung? JA ✅
Frage 2: Stimmung besser? NEIN ❌
Frage 3: Weniger Selbstkritik? NEIN ❌
→ KONSTRUKTIV (mindestens 1 JA)"""
        ),
        ThumbRuleSection(
            title = "Im Alltag anwenden",
            icon = "🌍",
            content = """WIE DU DIE REGEL IM ALLTAG NUTZT

WENN DU MERKST, DASS DU DENKST:

Schritt 1: INNEHALTEN
Nimm dir 2 MINUTEN Zeit
Beobachte dein Denken

Schritt 2: DIE 3 FRAGEN STELLEN
Frage 1: Nähe zu Lösung?
Frage 2: Stimmung besser?
Frage 3: Weniger Selbstkritik?

Schritt 3: INTERPRETIEREN
Mindestens 1 eindeutiges JA?
→ Konstruktiv, weitermachen
→ Kein JA? Grübeln, unterbrechen!

Schritt 4: HANDELN
Falls Grübeln erkannt:
• Aktivität starten
• Konkrete Frage stellen
• Ablenken oder handeln

───────────────────────────────────────

ALLTAGS-BEISPIELE:

Situation 1: Fehler bei der Arbeit

Denken:
"Ich habe einen Fehler gemacht.
 Wie behebe ich ihn?
 Welche Schritte sind nötig?"

2 Min vergangen. 3 Fragen:
Frage 1: Nähe zu Lösung? JA ✅
→ Konkrete Schritte entstehen

Ergebnis: KONSTRUKTIV
→ Weitermachen, den Fehler beheben

───────────────────────────────────────

Situation 2: Sorge um Zukunft

Denken:
"Was ist wenn alles schiefgeht?
 Warum traue ich mir nichts zu?
 Warum bin ich so ängstlich?"

2 Min vergangen. 3 Fragen:
Frage 1: Nähe zu Lösung? NEIN ❌
Frage 2: Stimmung besser? NEIN ❌
Frage 3: Weniger Selbstkritik? NEIN ❌
→ Alle Nein

Ergebnis: GRÜBELN
→ STOP! Aktivität starten, konkrete Fragen

───────────────────────────────────────

DIE FÄHIGKEIT TRAINIEREN:

Mit jeder Anwendung:
✓ Erkennst du schneller
✓ Wendest die Regel automatischer an
✓ Stoppst Grübeln früher
✓ Schaltst schneller um

Je öfter: Desto automatischer!"""
        ),
        ThumbRuleSection(
            title = "Entlastende Problemdefinition",
            icon = "💚",
            content = """WICHTIGE NEUDEFINITION FÜR DICH

DAS KERNPROBLEM

❌ FALSCH zu denken:
"Die Situation ist das Problem"
"Diese Person ist das Problem"
"ICH BIN das Problem"

✅ RICHTIG zu verstehen:
"Das GRÜBELN ist das Problem!"

NICHT die Situation
NICHT die Person
NICHT du als Mensch

SONDERN: Der DENKSTIL!

───────────────────────────────────────

WARUM IST DAS ENTLASTEND?

Wenn du denkst: "Ich bin das Problem"
→ Hoffnungslosigkeit
→ Scham
→ Wertlosigkeit

Wenn du verstehst: "Mein GRÜBELN ist das Problem"
→ Hoffnung (kann ich trainieren!)
→ Selbstmitgefühl (ist nicht meine Schuld)
→ Handlung möglich (kann ich ändern!)

───────────────────────────────────────

DAS UMRAHMEN:

ALT:
"Ich bin unfähig, diese Situation zu lösen.
 Es gibt keinen Weg. Ich bin hoffnungslos."

NEU:
"Die Situation hat eine Lösung.
 Aber mein GRÜBELN verhindert, dass ich sie sehe.
 Wenn ich weniger grüble, sehe ich die Lösung."

───────────────────────────────────────

ENTLASTENDE BOTSCHAFT:

"Auch wenn es sich beim Grübeln so anfühlt,
 als seien eine Situation, Person oder du selbst
 das Hauptproblem:

 Das Hauptproblem ist tatsächlich das GRÜBELN.

 Deswegen arbeiten wir daran,
 dass du lernst, weniger zu grübeln.

 Anschließend werden sich Probleme
 leichter lösen lassen."

───────────────────────────────────────

HOFFNUNG DARAUS:

✅ Das Problem ist nicht dir inhärent
✅ Es ist ein Denkstil (trainierbar!)
✅ Mit Veränderung des Grübelns
✅ Lösen sich Probleme leichter!

Das ist MÄCHTIG!
Das ist HOFFNUNG!
Das ist ECHTE VERÄNDERUNG!"""
        ),
        ThumbRuleSection(
            title = "Zusammenfassung & Training",
            icon = "🎯",
            content = """ZUSAMMENFASSUNG DIESER ÜBUNG

WAS DU GELERNT HAST:

✅ Warum ist Grübeln schwer zu erkennen?
   (Fließender Übergang, unmerklich)

✅ Welcher Unterschied besteht?
   Grübeln: Warum-Fragen, zirkulär, negativ
   Konstruktiv: Wie-Fragen, zielgerichtet, positiv

✅ Wie erkennst du Grübeln?
   Die 3-Fragen-Daumenregel
   (Lösung? Stimmung? Selbstkritik?)

✅ Wer ist schuld?
   Nicht du, nicht die Situation
   Sondern: Das GRÜBELN selbst

───────────────────────────────────────

DIE 3-FRAGEN-DAUMENREGEL NOCHMAL:

"Wenn ich unsicher bin, ob ich grüble:
2 Minuten denken, dann prüfen:

1. Nähe zu Lösung? (Handlungsplan)
2. Stimmung besser? (Emotionales Befinden)
3. Weniger Selbstkritik? (Selbstwert)

Kein eindeutiges JA → Grübeln!
→ STOP & umleiten"

───────────────────────────────────────

TÄGLICHES TRAINING:

Tag 1-3:
Bewusstes Anwenden
Erfordert Aufmerksamkeit
Manchmal vergessen

Tag 4-7:
Etwas leichter
Mehr Achtsamkeit
Schneller erkannt

Woche 2-4:
Deutlich automatischer
Intuitiveres Erkennen
Schnellere Umleitung

Woche 4+:
Sehr automatisch
Du merkst sofort: "Das ist Grübeln"
Du schaltst blitzschnell um

───────────────────────────────────────

ERFOLGS-INDIKATOREN:

✅ Du merkst schneller, wenn du grübelst
✅ Die 3 Fragen werden deine "Intuition"
✅ Du schaltst schneller um
✅ Deine Stimmung bleibt besser
✅ Du findest schneller zu Lösungen

───────────────────────────────────────

DAS WICHTIGSTE:

Es geht nicht um Perfektion.
Es geht um Häufigkeit.

Je öfter du die Regel anwendest:
→ Desto besser wirst du darin
→ Desto automatischer wird es
→ Desto mehr hilft es dir

───────────────────────────────────────

ABSCHLUSSBOTSCHAFT:

Du schuldest NICHT daran,
dass du grübelst.

ABER:

Du schuldest DICH daran,
es zu trainieren und zu ändern.

Mit dieser Regel hast du ein WERKZEUG.
Mit Übung wird es zur GEWOHNHEIT.
Mit Zeit wird es DEIN STANDARD.

Viel Erfolg beim Training! 🌟"""
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "3-Fragen-Daumenregel",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "[INTERAKTIVE ÜBUNG]",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            IconButton(
                onClick = { isBookmarked = !isBookmarked }
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,
                    contentDescription = "Lesezeichen",
                    tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((currentStep + 1) / sections.size.toFloat())
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }

        // Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                sections.forEachIndexed { index, section ->
                    ExpandableSectionThumbRule(
                        section = section,
                        isExpanded = expandedSection == index,
                        onToggle = {
                            expandedSection = if (expandedSection == index) -1 else index
                        },
                        isCurrent = index == currentStep
                    )
                }
            }
        }

        // Footer Navigation & Rating
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { if (currentStep > 0) currentStep-- },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = currentStep > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text("Zurück")
                }

                if (currentStep < sections.size - 1) {
                    Button(
                        onClick = { currentStep++ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Weiter")
                    }
                } else {
                    Button(
                        onClick = {
                            // Module completion logic
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Fertig!")
                    }
                }
            }

            // Rating Section (Only at the end)
            AnimatedVisibility(
                visible = currentStep == sections.size - 1,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Wie hilfreich war diese Übung für dich?",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ThumbRuleRating.entries.forEach { rating ->
                            RatingButtonThumbRule(
                                rating = rating,
                                isSelected = selectedRating == rating,
                                onClick = { selectedRating = rating },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    if (selectedRating != null) {
                        Text(
                            text = "Danke für deine Bewertung!",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableSectionThumbRule(
    section: ThumbRuleSection,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    isCurrent: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .shadow(
                elevation = if (isExpanded) 8.dp else 2.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .then(
                if (isCurrent) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = section.icon,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.size(32.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = section.content,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RatingButtonThumbRule(
    rating: ThumbRuleRating,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = rating.emoji,
            fontSize = 24.sp
        )
        Text(
            text = rating.label,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

