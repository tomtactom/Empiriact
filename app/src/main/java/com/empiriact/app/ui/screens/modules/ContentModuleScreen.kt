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

enum class ContentRating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}

data class ContentSection(
    val title: String,
    val icon: String,
    val content: String
)

@Composable
fun ContentModuleScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf<ContentRating?>(null) }
    var expandedSection by remember { mutableStateOf(-1) }

    val sections = listOf(
        ContentSection(
            title = "Unterschiedliche Themen",
            icon = "🎯",
            content = """UNTERSCHIEDLICHE INHALTE – ÄHNLICHER PROZESS

Ein wichtiger Perspektivwechsel:

Der DENKPROZESS ist oft ähnlich
– auch wenn die THEMEN unterschiedlich sind!

BEDEUTUNG:
Menschen unterscheiden sich weniger darin,
DASS sie repetitiv negativ denken

Sondern eher WORÜBER sie es tun!

ENTLASTENDE PERSPEKTIVE:

❌ Es geht nicht um persönliches Defizit
✅ Es ist ein VERBREITETES psychologisches Muster

Das bedeutet:
• Du bist nicht allein
• Es ist nicht deine Schuld
• Der Prozess ist trainierbar
• Unabhängig vom Thema

LERNZIEL DIESES MODULS:

Verstehe die verschiedenen Formen von RND
→ Erkenne den gemeinsamen PROZESS
→ Erkenne, dass THEMA weniger wichtig ist als MUSTER
→ Nutze strategien für alle Formen gleich"""
        ),
        ContentSection(
            title = "Selbstbezogenes Grübeln",
            icon = "🪞",
            content = """FOKUS: SELBSTBEWERTUNG & VERGLEICH

TYPISCHE GEDANKEN:

Warum-Fragen:
• "Warum bin ich so?"
• "Was stimmt nicht mit mir?"
• "Warum bekomme ich mein Leben nicht besser hin?"
• "Warum wird es nie besser?"

HÄUFIGE THEMEN:

🔴 Vermeintliche persönliche Schwächen
   └─ "Ich bin zu ängstlich"
   └─ "Ich bin zu kritisch"
   └─ "Ich bin zu sensibel"

🔴 Vergangene Fehler
   └─ "Ich hätte das anders machen sollen"
   └─ "Ich bin schuld an..."
   └─ "Ich werde es mir nie verzeihen"

🔴 Vergleiche mit anderen
   └─ "Sie können das, ich nicht"
   └─ "Alle sind besser als ich"
   └─ "Sie haben es leichter"

🔴 Eigene Stimmung oder Erschöpfung
   └─ "Warum fühle ich mich immer so?"
   └─ "Warum bin ich immer erschöpft?"

DENKSTIL-CHARAKTERISTIKA:

➡️ RÜCKWÄRTSGEWANDT
   Fokus auf: "Warum ist das passiert?"

➡️ STARK ANALYSIEREND
   "Wenn ich nur genug analysiere, verstehe ich mich"

➡️ SELBST-FOKUSSIERT
   Alles wird auf die eigene Person bezogen

➡️ EMOTIONAL AKTIVIEREND
   Schämen, Schuldgefühle, Unbehagen

PARADOX:
Je mehr die Person analysiert
→ Desto mehr Fehler findet sie
→ Desto schlechter fühlt sie sich"""
        ),
        ContentSection(
            title = "Zukunftsbezogenes Sorgen",
            icon = "❓",
            content = """FOKUS: MÖGLICHE GEFAHREN & SZENARIEN

TYPISCHE GEDANKEN:

"Was-wenn"-Fragen dominieren:
• "Was wäre, wenn etwas Schlimmes passiert?"
• "Was ist, wenn ich das nicht schaffe?"
• "Was, wenn ich die Kontrolle verliere?"
• "Und wenn das alles zusammenbricht?"

HÄUFIGE SORGEN-THEMEN:

🔴 Familie und nahestehende Menschen
   └─ "Was, wenn ihnen etwas passiert?"
   └─ "Was, wenn ich sie verliere?"
   └─ "Was, wenn ich ihnen nicht helfen kann?"

🔴 Arbeit und finanzielle Sicherheit
   └─ "Was, wenn ich meinen Job verliere?"
   └─ "Was, wenn ich nicht genug verdiene?"
   └─ "Was, wenn ich finanziell scheitere?"

🔴 Gesundheit
   └─ "Was, wenn ich krank werde?"
   └─ "Was, wenn es ernst ist?"
   └─ "Was, wenn das chronisch wird?"

🔴 Alltägliche Verpflichtungen
   └─ "Was, wenn ich die Deadline nicht schaffe?"
   └─ "Was, wenn ich es vergesse?"
   └─ "Was, wenn es nicht perfekt ist?"

DENKSTIL-CHARAKTERISTIKA:

➡️ ZUKUNFTSORIENTIERT
   Fokus auf: "Was könnte passieren?"

➡️ SZENARIO-SPRINGEN
   Gedanken springen von einer Gefahr zur nächsten

➡️ KATASTROPHALES DENKEN
   Auch unwahrscheinliche Szenarien fühlen sich REAL an

➡️ KONTROLLBEDÜRFNIS
   "Wenn ich nur genug plane, kann ich alles kontrollieren"

BEISPIEL-SPIRALE:
"Mein Partner ist spät"
→ "Was, wenn ihm was passiert?"
→ "Was, wenn er mit mir Schluss machen will?"
→ "Was, wenn ich allein bin?"
→ "Was, wenn ich das nicht schaffe?"

Das Gehirn versucht durch Sorgen zu KONTROLLIEREN
→ Wird aber nur UNSICHERER"""
        ),
        ContentSection(
            title = "Soziale Selbstbeobachtung",
            icon = "👥",
            content = """FOKUS: SOZIALE BEWERTUNG & ABLEHNUNG

TYPISCHE GEDANKEN:

Bewertungs-Fokus:
• "Habe ich komisch gewirkt?"
• "Was denken die anderen über mich?"
• "Was ist, wenn ich mich blamiere?"
• "Bin ich akzeptiert?"

ZWEI HÄUFIGE FORMEN:

🔴 POST-EVENT-GRÜBELN (NACH sozialen Situationen)
   └─ Gedankliches Wiederholen & Analysieren
   └─ "Das war unangenehm"
   └─ "Sie haben mich komisch angeschaut"
   └─ "Ich hätte das anders sagen sollen"
   └─ Kann Stunden bis Tage andauern

🔴 PRE-EVENT-SORGEN (VOR sozialen Situationen)
   └─ Sorgen über mögliches Versagen
   └─ "Was, wenn ich nichts zu sagen habe?"
   └─ "Was, wenn ich nervös werde?"
   └─ Kann zu Vermeidung führen

HÄUFIGE INHALTE:

✗ Körperliche Reaktionen
  "Sie haben gesehen, dass ich nervös war"

✗ Verbale Missgeschicke
  "Ich habe etwas Dummes gesagt"

✗ Soziale Fähigkeiten
  "Ich bin sozial ungeschickt"

✗ Äußeres Erscheinungsbild
  "Wie ich aussah"
  "Was ich anhatte"

DENKSTIL-CHARAKTERISTIKA:

➡️ STARKE SELBSTFOKUSSIERUNG
   Fokus auf: "Wie wirke ich?"

➡️ GEDANKENLESEN
   "Ich weiß, was sie denken"

➡️ EMOTIONALES KREISEN
   Scham, Peinlichkeit, Angst

➡️ VERSTÄRKTER FOKUS NACH STRESS
   Je mehr Stress, desto stärker die Selbstbeobachtung

KREISLAUF:
Soziale Situation → Selbstfokus → Nervosität
→ "Ich wirke nervös" → Mehr Nervosität
→ Post-Event-Grübeln

Der Fokus auf Selbst macht nervöser!"""
        ),
        ContentSection(
            title = "Grübeln über Belastende Ereignisse",
            icon = "⚡",
            content = """FOKUS: SINNSUCHE & TRAUMA-VERARBEITUNG

TYPISCHE GEDANKEN:

Tiefe Fragen:
• "Warum ist mir das passiert?"
• "Hätte ich es verhindern können?"
• "Was bedeutet das für mein Leben?"
• "Was, wenn so etwas wieder passiert?"

HÄUFIGE THEMEN:

🔴 SINNSUCHE
   └─ "Gibt es einen Grund dafür?"
   └─ "Was sollte ich daraus lernen?"
   └─ "Was bedeutet das für mich?"

🔴 SCHULDFRAGEN
   └─ "Bin ich schuld?"
   └─ "Hätte ich es verhindern können?"
   └─ "Habe ich einen Fehler gemacht?"

🔴 KONTROLLVERLUST
   └─ "Warum konnte ich das nicht verhindern?"
   └─ "Bin ich hilflos?"
   └─ "Kann ich mich schützen?"

🔴 SICHERHEITSBEDÜRFNIS
   └─ "Wird mir das wieder passieren?"
   └─ "Ist die Welt sicher?"
   └─ "Kann ich jemandem trauen?"

TYPISCHE EREIGNISSE:

• Trennungen
• Verlust nahestehender Menschen
• Ablehnung oder Zurückweisung
• Beziehungskonflikte
• Unfälle oder Verletzungen
• Kritik oder Fehlschlag

DENKSTIL-CHARAKTERISTIKA:

➡️ VERSTEHENS-VERSUCH
   "Wenn ich nur verstehe, kann ich damit umgehen"

➡️ GEIST-SUCHE
   "Warum"-Schleifen, die keine Antwort finden

➡️ AUTOMATISCHES WIEDERHOLEN
   Immer wieder das Ereignis mental abspielen

➡️ ZUKUNFTS-ANGST
   "Was, wenn das wieder passiert?"

PARADOX DES GRÜBELNS:
Das Denken versucht, Unbegreifliches begreifbar zu machen
→ Bleibt aber häufig in "Warum"-Schleifen stecken
→ Die Antwort hilft nicht, das Gefühl zu lösen"""
        ),
        ContentSection(
            title = "Körper- & Gesundheitsbezogenes Sorgen",
            icon = "💊",
            content = """FOKUS: KÖRPERSIGNALE & GESUNDHEITSANGST

TYPISCHE GEDANKEN:

Interpretations-Fragen:
• "Was bedeutet dieses Symptom?"
• "Was, wenn das etwas Ernstes ist?"
• "Warum hört das nicht auf?"
• "Wird das schlimmer?"

HÄUFIGE FOKUS-PUNKTE:

🔴 SYMPTOM-INTERPRETATION
   └─ Kleine körperliche Veränderungen
   └─ "Das ist normal" → "Das könnte gefährlich sein"
   └─ Katastrophales Denken über Symptome

🔴 INTENSIVE KÖRPERBEOBACHTUNG
   └─ Fokus auf bestimmte Körperteile
   └─ Ständiges Überprüfen
   └─ "Ist das noch da?"
   └─ "Ist es stärker geworden?"

🔴 VERGRÖSSERUNG VON SIGNALEN
   └─ Kleine Veränderungen werden vergrößert
   └─ "Das war früher nicht so"
   └─ "Das wird immer schlimmer"

🔴 ÄRZTE BESUCHE
   └─ "Die haben mich übersehen"
   └─ "Sie nehmen mich nicht ernst"
   └─ Oder: Ständige Arztbesuche zur Beruhigung

DENKSTIL-CHARAKTERISTIKA:

➡️ HYPER-FOKUS
   Fokus auf: Körper & Symptome

➡️ KATASTROPHALISIERUNG
   "Kleine Symptom" → "Schwere Krankheit"

➡️ BESTÄTIGUNGSBIAS
   Sucht nach Infos, die Angst bestätigen

➡️ SICHERHEITS-VERHALTEN
   Arztbesuche, googlen, Überprüfen
   (Verstärkt paradoxerweise die Angst)

KREISLAUF:
Körpersignal → Fokus darauf → Angst
→ "Ich muss überprüfen" → Mehr Fokus
→ "Das wird schlimmer" → Mehr Angst

Der Fokus auf den Körper intensiviert die Wahrnehmung!"""
        ),
        ContentSection(
            title = "Schmerz- & Belastungs-Grübeln",
            icon = "😣",
            content = """FOKUS: DAUER, INTENSITÄT & LANGZEITFOLGEN

TYPISCHE GEDANKEN:

Belastungs-Fragen:
• "Warum ist es heute schlimmer?"
• "Warum geht das nicht weg?"
• "Was bedeutet das für meine Zukunft?"
• "Werden mir diese Schmerzen forever bleiben?"

HÄUFIGE THEMEN:

🔴 DAUER-FOKUS
   └─ "Wie lange hält das schon an?"
   └─ "Wird das je besser?"
   └─ "Wie lange noch?"

🔴 INTENSITÄTS-SCHWANKUNGEN
   └─ "Warum ist es heute schlimmer?"
   └─ "Das Muster verstehe ich nicht"
   └─ "Ich kann nicht vorhersagen, wann es besser ist"

🔴 LANGZEITFOLGEN-ANGST
   └─ "Wird das bleibend?"
   └─ "Kann ich damit arbeiten?"
   └─ "Wird mein Leben begrenzt?"

🔴 HOFFNUNGSLOSIGKEIT
   └─ "Nichts hilft"
   └─ "Ich werde damit leben müssen"
   └─ "Es wird nie besser"

TYPISCHE BELASTUNGEN:

• Chronische Schmerzen
• Müdigkeit / Erschöpfung
• Kopfschmerzen / Migräne
• Schlafprobleme
• Psychische Belastung
• Spannungs-Zustände

DENKSTIL-CHARAKTERISTIKA:

➡️ LEID-FOKUS
   Fokus auf: Intensität & Dauer

➡️ VORHERSAGE-VERSUCHE
   "Kann ich vorhersehen, wann es besser wird?"

➡️ SINNSUCHE
   "Warum passiert das MIR?"

➡️ HOFFNUNGS-VERLUST
   "Das wird nie besser"

VERBINDUNG ZU RND:
Das Grübeln über Schmerzen
→ verstärkt emotional die Belastung
→ erhöht die subjektive Intensität
→ reduziert Bewältigungsfähigkeit"""
        ),
        ContentSection(
            title = "Der Gemeinsame Nenner",
            icon = "🔗",
            content = """SO UNTERSCHIEDLICH DIESE THEMEN SIND...

...der zugrunde liegende PROZESS ist meist DERSELBE!

GEMEINSAME PROZESS-MERKMALE:

1️⃣  WIEDERHOLTES KREISEN
   └─ Um negative Inhalte
   └─ Immer das gleiche Thema
   └─ Schwierig abzuschalten

2️⃣  STARKE SELBST- ODER GEFAHREN-FOKUSSIERUNG
   └─ Bei Selbstgrübeln: Fokus auf "Ich"
   └─ Bei Sorgen: Fokus auf "Gefahr"
   └─ Fokus verstärkt die Emotion

3️⃣  ABSTRAKTE FRAGEN
   └─ "Warum"-Fragen (Selbstgrübeln)
   └─ "Was-wenn"-Fragen (Zukunftssorgen)
   └─ Keine konkreten Antworten erreichbar

4️⃣  ZUNEHMENDE EMOTIONALE AKTIVIERUNG
   └─ Grübeln macht nicht ruhiger
   └─ Sondern emotionaler, angespannter
   └─ Je mehr gedacht, desto intensiver das Gefühl

5️⃣  ABNEHMENDE HANDLUNGSORIENTIERUNG
   └─ Immer mehr Denken
   └─ Immer weniger Tun
   └─ Passivität verstärkt sich

DIE ZENTRALE WAHRHEIT:

Das GEHIRN versucht durch Denken:
→ KONTROLLE herzustellen
→ SICHERHEIT zu finden
→ VERSTÄNDNIS zu gewinnen

PARADOXERWEISE:
Das ständige gedankliche Wiederholen
→ VERSTÄRKT die Unsicherheit
→ REDUZIERT die Kontrolle
→ VERHINDERT Verständnis

WARUM DIESER GEMEINSAME NENNER WICHTIG IST:

❌ Du musst nicht jedes Thema einzeln "lösen"
✅ Du kannst den DENKSTIL selbst verändern

Whether it's Selbstzweifel, Zukunftsängste,
soziale Situationen oder körperliche Symptome:

Die STRATEGIEN zur Unterbrechung sind ÄHNLICH!"""
        ),
        ContentSection(
            title = "Warum diese Unterscheidung Hilft",
            icon = "💡",
            content = """NEUE PERSPEKTIVE ERÖFFNET NEUE MÖGLICHKEITEN

ERKENNTNIS:

Es geht nicht um das KONKRETE THEMA
Es geht um den DENKPROZESS!

BEDEUTUNG FÜR VERÄNDERUNG:

1️⃣  UNABHÄNGIG VOM THEMA LERNBAR

Früher:
"Ich muss alle meine Themen einzeln bearbeiten"
"Ich muss hundert Sorgen einzeln lösen"

Jetzt:
"Ich kann den DENKSTIL insgesamt verändern"
"Wenn ich den Prozess durchbreche, hilft das bei ALLEN Themen"

2️⃣  UNIVERSELLE STRATEGIEN MÖGLICH

GLEICHE STRATEGIEN für:
• Selbstgrübeln → wie für Zukunftssorgen
• Soziale Angst → wie für Gesundheitsangst
• Schmerz-Fokus → wie für Trauma-Grübeln

Du musst nicht hundert verschiedene Techniken lernen!

3️⃣  FLEXIBILITÄT STATT THEMEN-KAMPF

FRÜHER:
"Ich kämpfe gegen meine Angst vor..."
"Ich versuche, meine Gedanken über... zu stoppen"
→ Fokus auf INHALTE

JETZT:
"Ich lerne, meinen Denkstil zu verändern"
"Ich unterbreche den Prozess früher"
→ Fokus auf PROZESS

4️⃣  NORMALISIERUNG

"Es geht nicht um mein Defizit"
"Es ist ein verbreitetes Muster"
"Das haben viele Menschen"
"Es ist trainierbar"

PRAKTISCHE KONSEQUENZ:

Du brauchst NICHT:
✗ Hundert verschiedene Therapien
✗ Jedes Thema einzeln bearbeiten
✗ Mit jedem neuen Thema neu anfangen

Du brauchst:
✅ Den PROZESS verstehen
✅ Eine FLEXIBLe Strategie
✅ REGELMÄSSIGES TRAINIEREN

→ Dann wirkt sich das auf ALLE Themen aus!"""
        ),
        ContentSection(
            title = "Reflexion & Selbstbeobachtung",
            icon = "🔍",
            content = """EINE REFLEXIONSÜBUNG FÜR DICH

Nimm dir Zeit und reflektiere:

1️⃣  WELCHE THEMEN TAUCHEN BEI DIR AUF?

Notiere deine häufigsten Grübel-Themen:
□ Selbstbewertung ("Warum bin ich so?")
□ Zukunftssorgen ("Was, wenn...")
□ Soziale Situationen ("Was denken sie?")
□ Belastende Ereignisse ("Warum mir?")
□ Gesundheit ("Was bedeutet das Symptom?")
□ Körperliche Belastung ("Wird das besser?")
□ Andere:___________________

2️⃣  WELCHER FRAGE-TYP DOMINIERT?

□ "Warum"-Fragen (rückwärtsgewandt)
   → Deutet auf Selbstgrübeln hin

□ "Was-wenn"-Fragen (zukunftsorientiert)
   → Deutet auf Sorgen hin

□ "Wie-wirke-ich"-Fragen (selbstfokussiert)
   → Deutet auf soziale Selbstbeobachtung hin

3️⃣  IN WELCHEN LEBENSBEREICHEN BESONDERS STARK?

□ Familie und Beziehungen
□ Arbeit und Karriere
□ Gesundheit und Körper
□ Soziale Situationen
□ Persönliche Entwicklung
□ Finanzielle Sicherheit
□ Andere:___________________

4️⃣  AUSLÖSER-SITUATIONEN IDENTIFIZIEREN

Wiederkehrende Situationen, die Grübeln auslösen:
□ Allein sein
□ Vor sozialen Ereignissen
□ Bei Stress oder Überlastung
□ Abends im Bett
□ Nach Konflikten
□ Bei körperlichen Symptomen
□ Wenn Kontrolle fehlt
□ Andere:___________________

WICHTIG:

Diese Reflexion hilft dir zu ERKENNEN:
→ Welche Themen sind für DICH relevant
→ Welcher PROZESS dahintersteckt
→ Welche AUSLÖSER es gibt

Mit diesem Verständnis:
→ Kannst du früher eingreifen
→ Kennst du deine Muster
→ Weißt du, was zu trainieren ist"""
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
                    text = "RND: Inhalte & Prozesse",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "[BEISPIELMODUL]",
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
                    ExpandableSectionContent(
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
                        text = "Wie hilfreich war dieses Modul für dich?",
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
                        ContentRating.entries.forEach { rating ->
                            RatingButtonContent(
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
private fun ExpandableSectionContent(
    section: ContentSection,
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
private fun RatingButtonContent(
    rating: ContentRating,
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

