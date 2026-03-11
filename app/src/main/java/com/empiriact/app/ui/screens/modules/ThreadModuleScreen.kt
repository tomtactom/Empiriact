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

enum class ThreadRating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}

data class ThreadSection(
    val title: String,
    val icon: String,
    val content: String
)

@Composable
fun ThreadModuleScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf<ThreadRating?>(null) }
    var expandedSection by remember { mutableStateOf(-1) }

    val sections = listOf(
        ThreadSection(
            title = "Warum eine Erlebensübung?",
            icon = "🎯",
            content = """ERLEBNIS STATT NUR EINSICHT

Das Problem mit reiner Einsicht:
❌ Verstandenes Wissen = nicht automatisch Veränderung
❌ Du weißt, dass Grübeln nicht hilft → machst es aber trotzdem
❌ Logik allein stoppt Gewohnheiten nicht

WARUM ERLEBNISORIENTIERT?

Grübeln ist eine GEWOHNHEIT
Gewohnheiten verändern sich durch ERFAHRUNG
Nicht durch reine Einsicht!

KÖRPERLICHE ERFAHRUNG:

Wenn du Grübeln körperlich und räumlich
nachvollziehst → entsteht ein klares INNERES BILD

Dieses Bild kannst du später abrufen:
"Ah, das fühlt sich wie ein Fadennetz an"

NEUROBIOLOGIE:

Der Körper & Raum aktivieren andere Gehirn-Systeme
als nur "darüber nachdenken"

→ Tiefere, intuitivere Verankerung
→ Im Alltag schneller abrufbar
→ Automatischer wirksam

DIESE ÜBUNG IST:

✅ Praktisch & konkret
✅ Körperorientiert
✅ Räumlich-visuell
✅ Erlebbar
✅ Später abrufbar

ZIEL:

Nicht perfektes Verständnis
Sondern: Ein Bild, das hilft im Alltag"""
        ),
        ThreadSection(
            title = "Teil 1: Das Grübelnetz",
            icon = "🕸️",
            content = """DIE ÜBUNG MIT WOLLE ODER FADEN

VORBEREITUNG:

Du brauchst:
✓ Ein Wollknäuel oder langen Faden
✓ Einen Raum mit verschiedenen Ankerpunkten
✓ 10-15 Minuten Zeit
✓ Eine Grübelsituation (nicht zu belastend!)

SCHRITT 1: DER STARTGEDANKE

Denke an eine Situation, in der du viel gegrübelt hast.
Nicht überwältigend – sondern zum Üben!

BEISPIELE:
• "Warum mache ich immer alles falsch?"
• "Was denken die jetzt von mir?"
• "Was, wenn das schlimme Folgen hat?"

Befestige das Fadenende an einem festen Punkt:
→ Stuhlbein, Regal, Türgriff
→ Das ist dein START-PUNKT
→ Symbolisiert den ursprünglichen Gedanken

SCHRITT 2: DIE GEDANKENSPRÜNGE

Gehe jetzt gedanklich weiter durch die Grübelei:
"Welcher Gedanke kam als nächstes?"

Dabei spannst du den Faden zu neuen Punkten im Raum!

WICHTIG: Körperliche Symbolik!

Wenn Gedanken stark springen:
→ Mach auch räumlich einen GROSSEN Schritt

Wenn du dich im Kreis drehst:
→ Wickle den Faden mehrfach um DENSELBEN Punkt

Wenn du "hineinsteigert":
→ Führe den Faden ENGER oder TIEFER

BEISPIEL-KREISLAUF:

Start: "Ich habe einen Fehler gemacht"
       ↓ (Faden zu Stuhl)
"Das war dumm von mir"
       ↓ (Faden zu Tisch)
"Ich bin unfähig"
       ↓ (Faden zu Regal – wieder zurück!)
"Ich werde das nie hinbekommen"
       ↓ (Faden um Regal ZWEIMAL)
"Warum bin ich so?"
       ↓ (Wiederholung, Faden schlingt sich)

ERGEBNIS NACH EINIGEN MINUTEN:

Ein sichtbares, verworrenes FADENNETZ
- Viele Verbindungen
- Viele Schleifen
- Wirrwarr
- Verwirrung sichtbar gemacht!

INNEHALTEN & REFLEKTIEREN:

Bleib stehen und betrachte das Netz.

Fragen:
• Wie fühlt sich dieses Bild an?
• Erlebe ich Enge?
• Erlebe ich Chaos?
• Fühle ich mich gefangen?
• Fühle ich mich überwältigt?

ERKENNTNISSE:

Viele Menschen berichten:
"Genau so fühlt sich mein Grübeln an!"
"Verstrickt, orientierungslos, blockiert"

Das Fadennetz SYMBOLISIERT den Grübelprozess:
→ Viele Verbindungen
→ Viele Schleifen
→ Wenig Klarheit
→ Keine klare Lösung"""
        ),
        ThreadSection(
            title = "Teil 2: Der rote Faden",
            icon = "🧵",
            content = """DER KONTRAST: KONSTRUKTIVES DENKEN

Jetzt zeigen wir das GEGENTEIL.

VORBEREITUNG:

Nimm ein NEUES Stück Faden
Länge: 2-3 Meter
Lege ihn GERADE durch den Raum
Von START-PUNKT zu ZIEL-PUNKT

START-PUNKT:
Das konkrete PROBLEM
Beispiel: "Mir ist bei der Arbeit ein Fehler passiert"

ZIEL-PUNKT:
Dein Ziel / deine Handlung
Beispiel: Türe (symbolisiert "Schritt nach vorne")

SCHRITTWEISES VORANGEHEN:

Gehe LANGSAM entlang des Fadens.
Bei jedem Schritt stellst du eine konstruktive Frage:

1️⃣  "Wie schwerwiegend ist das wirklich?"
    └─ Objektivität statt Katastrophalisierung

2️⃣  "Was ist konkret passiert?"
    └─ Fakten statt Bewertung
    └─ "Ein Fehler wurde gemacht" (nicht "Ich bin dumm")

3️⃣  "Welche Handlungsmöglichkeiten habe ich?"
    └─ Konkrete Optionen
    └─ "Ich könnte es dem Chef sagen"
    └─ "Ich könnte es korrigieren"

4️⃣  "Was wäre ein erster kleiner Schritt?"
    └─ Konkrete Aktion
    └─ "Ich mache jetzt X"

WICHTIG: WENN GEDANKEN ABSCHWEIFEN

Du merkst: "Moment, jetzt denke ich wieder..."
"Ich bin unfähig"
"Das war so dumm"

WAS DU DANN MACHST:

1. Bewusst eine KLEINE SCHLAUFE im Faden machen
   (Symbolisiert das Abschweifen)

2. Dann BEWUSST zurück auf die gerade Linie

3. Weitermachen mit konstruktiver Frage

ZENTRALE ERKENNTNIS:

Das Ziel ist NICHT perfektes Denken
Das Ziel ist: IMMER WIEDER ZURÜCKKEHREN

Du wirst abschweifen – das ist normal!
Aber du merkst es und kommst zurück.

VERGLEICH BEIDER FÄDEN:

Das Grübelnetz:
❌ Viele Richtungen
❌ Sprünge
❌ Wiederholungen
❌ Enge
❌ Kein klarer Ausgang

Der rote Faden:
✅ Ein klarer Start
✅ Kleine, konkrete Schritte
✅ Bewegung in Richtung Lösung
✅ Orientierung
✅ Ein Ziel

ABSTRAKT vs. KONKRET:

Grübeln: "Warum bin ich so?"
         (abstrakt, kein Ausweg)

Konstruktiv: "Was kann ich jetzt tun?"
             (konkret, handlungsorientiert)"""
        ),
        ThreadSection(
            title = "Die zentrale Fähigkeit",
            icon = "🔄",
            content = """BEMERKEN UND ZURÜCKKEHREN

DAS GEHEIMNIS:

Es geht nicht darum, KEINE störenden Gedanken zu haben!

Es geht darum:
✅ Sie zu BEMERKEN
✅ BEWUSST zurückzukehren

WARUM IST DAS WICHTIG?

Du kannst Gedanken nicht unterdrücken
Versuch: "Denke nicht an Grübeln"
Resultat: Noch mehr Grübeln!

ABER:

Du kannst deine AUFMERKSAMKEIT lenken
Du kannst lernen zu BEMERKEN, wenn du abschweifst
Du kannst dich BEWUSST zurückführen

TRAINIERBAR:

Diese Fähigkeit ist nicht angeboren.
Diese Fähigkeit ist TRAINIERBAR!

Je öfter du es machst:
→ Desto automatischer wird es
→ Desto schneller merkst du ab
→ Desto leichter kehrst du zurück

UNTERSCHIED ZU FRÜHER:

Früher: "Ich kann das nicht kontrollieren"
Jetzt: "Ich merke es und steuere um"

Das ist FREIHEIT!

KÖRPERLICHE MANIFESTATION:

Bei der Woll-Übung SICHTBAR:
• Fadennetz = "Ich bin verloren"
• Rückkehr auf roten Faden = "Ich habe Kontrolle"

Diese Bilder verankern sich KÖRPERLICH

KLEINE ÜBUNG IM ALLTAG:

Wenn du merkst, dass Gedanken kreisen:
→ Kurz innehalten
→ Einen bewussten Atemzug
→ Innerlich sagen: "Rückkaudio zum roten Faden"
→ Eine konkrete Frage stellen: "Was jetzt konkret?"

Mit der Zeit: AUTOMATISCH"""
        ),
        ThreadSection(
            title = "Anwendung im Alltag",
            icon = "🌍",
            content = """DAS BILD IM ALLTAG NUTZEN

DER INNERE KOMPASS:

Du trägst die Bilder in dir:
✓ Das verworrene Fadennetz = Grübeln
✓ Der rote Faden = Konstruktives Denken

WENN IM ALLTAG GRÜBELN STARTET:

Situation: Du merkst, dass Gedanken kreisen

Option 1: DAS BILD NUTZEN
"Ah, ich verstricke mich gerade im Fadennetz"

Allein dieses BENENNEN schafft ABSTAND
Abstand = erste Stufe zur Veränderung

Option 2: FRAGEN STELLEN
"Wo ist mein roter Faden?"
"Was ist der nächste konkrete Schritt?"

Option 3: KÖRPERLICH REAGIEREN
"Ich mache einen Schritt"
"Ich atme bewusst"
"Ich tue etwas Konkretes"

PRAKTISCHE BEISPIELE:

Situation 1: Du grübelst über Kritik
Grübeln: "Warum hat sie das gesagt? Bin ich so schlecht?"
         (Fadennetz)
Rückkehr: "Was genau ist das Problem? Was kann ich tun?"
          (Roter Faden)

Situation 2: Du sorgst dich um die Zukunft
Sorgen: "Was, wenn alles schiefgeht? Ich schaffe das nie"
        (Fadennetz, Schleifen)
Rückkehr: "Was ist der nächste konkrete Schritt heute?"
          (Roter Faden)

Situation 3: Du analyisiert über Beziehung
Analyse: "Warum liebe ich nicht mehr? Was stimmt mit mir nicht?"
         (Fadennetz, Kreise)
Rückkehr: "Was kann ich konkret tun? Mit wem rede ich?"
          (Roter Faden)

DER AUTOMATISMUS:

Am Anfang:
❌ Bewusster Prozess
❌ Erfordert Aufmerksamkeit
❌ Manchmal vergessen

Mit Übung:
✅ Schneller & leichter
✅ Mehr automatisch
✅ Intuitiver

Je häufiger du übst:
→ Desto automatischer wird der Wechsel

ZENTRALE BOTSCHAFT:

Das Bild des Fadennets hilft dir,
SCHNELLER zu bemerken: "Ich grüble"

Und es hilft dir zu wissen:
"Es gibt einen anderen Weg - den roten Faden"

Du bist NICHT gefangen
Du hast WAHLMÖGLICHKEITEN"""
        ),
        ThreadSection(
            title = "Kleine Trainingsideen",
            icon = "💪",
            content = """GEWOHNHEIT AUFBAUEN: KONKRETE PRAKTIKEN

IDEE 1: KNOTEN-TRACKING

Einen kurzen Wollfaden bei dir tragen

Immer wenn:
✓ Du Grübeln BEMERKST
✓ Du zum roten Faden ZURÜCKKEHRST
✓ Du einen Wechsel SCHAFFST

→ Mache einen KNOTEN in den Faden

WARUM?

✓ Visualisierung von Erfolgen
✓ Motivationssystem
✓ Körperliche Erinnerung
✓ Am Abend: zählen & proud sein!

Ziel: Mit der Zeit immer mehr Knoten

IDEE 2: MINDFUL PAUSE

Immer wenn du Grübeln MERKST:

1. KURZ INNEHALTEN
2. EINEN BEWUSSTEN ATEMZUG
3. SAGEN: "Das ist Grübeln"
4. ZURÜCKKEHREN zum Hier-und-Jetzt

Diese 10-Sekunden-Routine:
→ Unterbricht den Kreislauf
→ Schafft Abstand
→ Ermöglicht Wahl

IDEE 3: FRAGENWECHSEL

Trainiere, deine Standard-Fragen zu verändern:

VON:
❌ "Warum?"
❌ "Was, wenn...?"
❌ "Warum bin ich...?"

ZU:
✅ "Wie?"
✅ "Was konkret?"
✅ "Was kann ich TUN?"

Kleine Sätze:
"Nicht Warum - sondern Was?"
"Nicht Analyse - sondern Aktion"

Sag dir das morgens:
"Heute: Konkret statt Abstrakt"

IDEE 4: HANDLUNG STARTET

Wenn du merkst, dass Denken ins KREISEN übergeht:

NICHT mehr denken!
SONDERN: Eine kleine AKTIVITÄT beginnen

Beispiele:
• Aufstehen & Wasser trinken
• Spaziergang 5 Min
• Musik hören
• Etwas Kreatives machen
• Mit jemand reden
• Putzen

WARUM?

Grübeln + Bewegung = nicht kompatibel
Körper & Denken sind verbunden
Aktion stoppt mentale Schleifen

IDEE 5: ABEND-REFLEXION

Abends für 2 Min:

"Wie oft habe ich heute den roten Faden gefunden?"
"Welche Momente waren gut?"
"Woran merke ich den nächsten Kreis?"

Schreib auf oder denk einfach nach
→ Verstärkt die Neuroplastizität

IDEE 6: PARTNER-SYSTEM

Wenn möglich:
Ein Freund/Partnerin einweihen

Ihr könnt euch gegenseitig helfen:
"Hey, ich merke, ich verwirre dich gerade"
"Lass uns zum roten Faden zurück"

Externe Unterstützung:
→ Schneller Feedback
→ Nicht allein
→ Motivierender

DIE MAGIC NUMMER:

Wie oft brauchst du es?

Neurowissenschaft sagt:
Mindestens 66 Tage für neue Gewohnheit
Aber: Mit bewusstem Training schneller!

TIMELINE:

Woche 1-2: Bewusst, anstrengend
Woche 3-4: Etwas leichter
Woche 5-8: Immer automatischer
Nach 8+ Wochen: Neue Gewohnheit!

DEIN ZIEL:

Nicht Perfektion
Sondern: Häufigkeit + Wiederholung

Je öfter du übst:
→ Desto automatischer wird es
→ Desto schneller merkst du ab
→ Desto leichter kehrst du zurück

ERFOLGS-INDIKATOREN:

✅ Du merkst schneller, dass du grübelst
✅ Du kehrst öfter zum roten Faden zurück
✅ Du brauchst weniger Anstrengung
✅ Es fühlt sich mehr normal an
✅ Deine Stimmung verbessert sich

KLEINSTE ÄNDERUNG = ERFOLG

Du musst nicht perfekt sein
Du musst nur HÄUFIGER trainieren"""
        ),
        ThreadSection(
            title = "Zusammenfassung & Abschluss",
            icon = "🌈",
            content = """ZUSAMMENFASSUNG DER ÜBUNG

WAS DU HEUTE GELERNT HAST:

✅ Grübeln ist ein SICHTBARES Muster (Fadennetz)
✅ Konstruktives Denken hat einen KLAREN WEG (roter Faden)
✅ Die zentrale Fähigkeit: BEMERKEN & ZURÜCKKEHREN
✅ Es ist TRAINIERBAR
✅ Mit Übung wird es automatischer

DAS WICHTIGSTE:

Es geht nicht um Perfektion
Es geht um HÄUFIGKEIT

Du wirst noch grübeln
Das ist OK
Aber du wirst es schneller BEMERKEN
Und schneller ZURÜCKKEHREN

DEIN KÖRPER HILFT DIR:

Das Bild des Fadennets:
→ Verankert sich körperlich
→ Ist abrufbar im Alltag
→ Hilft schneller zu reagieren

Der Körper & Raum sind deine Verbündeten
(nicht nur Gedanken)

KLEINE STEPS, GROSSE WIRKUNG:

Ein Knoten pro Tag → 30 Knoten im Monat
5 Sekunden Pause → neue Gewohnheit
Eine konkrete Frage → Wechsel vom Fadennetz zum roten Faden

DIE ZENTRALE HOFFNUNGSBOTSCHAFT:

Du bist nicht gefangen
Es gibt immer einen roten Faden
Du kannst LERNEN, ihn zu finden

Mit Übung wird es:
→ Schneller
→ Leichter
→ Automatischer
→ Natürlicher

VON HIER AUS:

Diese Übung ist der ANFANG
Mit den anderen Modulen lernst du:
✅ Techniken zum Unterbrechen
✅ Achtsamkeit & Präsenz
✅ Alternative Reaktionen

Aber DIESE ÜBUNG:
→ Gibt dir das innere Bild
→ Das du im Alltag brauchst
→ Um Veränderung zu erleben

DEIN TRAININGSPLAN:

Diese Woche:
1. Führe die Woll-Übung durch (einmal)
2. Nutze das Bild im Alltag
3. Beginne mit einer Trainingsidee

Nächste Woche:
1. Wiederhole bei Bedarf
2. Verstärke dein Training
3. Kombiniere mit anderen Modulen

Mit der Zeit:
1. Es wird automatischer
2. Du brauchst weniger bewusste Anstrengung
3. Der rote Faden wird dein Standard

ABSCHLUSSBOTSCHAFT:

Grübeln ist wie ein Fadennetz:
Komplex, verwirrend, eng, orientierungslos

Aber:

Du kannst lernen, das Netz zu SEHEN
Du kannst dich BEWUSST BEFREIEN
Du kannst den roten Faden FINDEN

Und das wichtigste:
Mit Übung wird das AUTOMATISCH

Du hast die Fähigkeit dazu
Du brauchst nur zu TRAINIEREN

Viel Erfolg! 🌟"""
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
                    text = "Roten Faden finden",
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
                    ExpandableSectionThread(
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
                        ThreadRating.entries.forEach { rating ->
                            RatingButtonThread(
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
private fun ExpandableSectionThread(
    section: ThreadSection,
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
private fun RatingButtonThread(
    rating: ThreadRating,
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

