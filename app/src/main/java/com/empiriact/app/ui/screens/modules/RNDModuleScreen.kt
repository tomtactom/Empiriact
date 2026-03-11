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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border

enum class RNDRating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}

data class RNDSection(
    val title: String,
    val icon: String,
    val content: String
)

@Composable
fun RNDModuleScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf<RNDRating?>(null) }
    var expandedSection by remember { mutableStateOf(-1) }

    val sections = listOf(
        RNDSection(
            title = "Was ist RND?",
            icon = "🧠",
            content = """Repetitives Negatives Denken (RND) - Ein verbreiteter Prozess

Viele Menschen kennen es: Ein Gedanke taucht auf – und bleibt. 
Er dreht sich im Kreis, kommt immer wieder, lässt sich schwer stoppen.

Typische Auslöser:
• Ein Fehler, den du gemacht hast
• Eine mögliche Gefahr in der Zukunft
• Etwas, das sich ungerecht oder kränkend angefühlt hat

WICHTIG: RND ist kein Zeichen von Schwäche!
Es ist ein verbreiteter psychischer Prozess bei sehr vielen Menschen.

Die Forschung zeigt:
RND ist ein zentraler Mechanismus, der beteiligt ist an:
✗ Entstehung psychischer Belastungen
✗ Aufrechterhaltung von Problemen
✗ Wiederauftreten von Symptomen

Deshalb lohnt es sich, diesen Denkprozess genauer zu verstehen."""
        ),
        RNDSection(
            title = "Merkmale von RND",
            icon = "🔄",
            content = """5 Charakteristische Merkmale:

1️⃣  WIEDERKEHREND
   • Die Gedanken kreisen immer wieder um dasselbe Thema
   • Tauchen ohne Aufforderung auf
   • Schwer abzuschalten

2️⃣  NEGATIV GEFÄRBT
   • Es geht um Fehler, Defizite, Gefahren oder Verluste
   • Fokus auf das Problematische
   • Wenig Raum für Positives

3️⃣  AUFDRINGLICH
   • Tauchen scheinbar automatisch auf
   • Greifen unvermittelt in Bewusstsein ein
   • Schwer zu ignorieren

4️⃣  ABSTRAKT
   • Warum bin ich so? (Verallgemeinerung)
   • Was, wenn alles schiefgeht? (Katastrophisieren)
   • Keine Bindung an konkrete Details

5️⃣  UNKONTROLLIERBAR
   • Obwohl du an etwas anderes denken möchtest
   • Gedanken weigern sich zu gehen
   • Anstrengung führt oft zu mehr Grübeln

ZWEI HÄUFIGE FORMEN:
• Grübeln: Vergangenheitsbezogen (Fehler, Kränkungen)
• Sorgen: Zukunftsbezogen (mögliche Gefahren, Szenarien)"""
        ),
        RNDSection(
            title = "RND ist transdiagnostisch",
            icon = "🔗",
            content = """RND findet sich nicht nur bei EINER psychischen Erkrankung!

Es ist bei mehreren Problemen präsent:

🔴 Depression
   └─ Grübeln über Versagen, Wertlosigkeit, Hoffnungslosigkeit

🔴 Angststörungen
   └─ Sorgen über Gefahren, Kontrollverlust, Katastrophen

🔴 Traumafolgestörungen
   └─ Sich-Beschäftigung mit Trauma, Sicherheit, Vertrauen

🔴 Schlafstörungen
   └─ Grübeln vor dem Einschlafen, über den Schlaf selbst

🔴 Essstörungen
   └─ Gedanken über Körper, Kontrolle, Gewicht

🔴 Viele weitere psychische Probleme

DESHALB: RND ist ein „TRANSDIAGNOSTISCHER PROZESS"
└─ Es übergreift verschiedene Diagnosen
└─ Es ist ein gemeinsamer Mechanismus
└─ Wenn du RND regulierst, hilft das bei vielen Problemen"""
        ),
        RNDSection(
            title = "Warum denkt unser Gehirn so?",
            icon = "🛡️",
            content = """Grübeln und Sorgen haben URSPRÜNGLICH SINNVOLLE FUNKTIONEN:

✓ WARNFUNKTION
   Auf mögliche Gefahren aufmerksam machen
   └─ "Vorsicht, hier könnte es Probleme geben!"

✓ RELEVANZFUNKTION
   Zeigt, dass etwas wichtig für uns ist
   └─ "Das ist mir wichtig, deshalb kreise ich drum herum"

✓ AUFMERKSAMKEITSFUNKTION
   Lenkt Aufmerksamkeit auf ein Problem
   └─ "Hey, kümmere dich um dieses Problem!"

✓ PROBLEMLÖSEFUNKTION
   Kann Problemlösung anstoßen
   └─ "Lass mich analysieren, wie ich das behebe"

BEISPIELE HILFREICHER GRÜBEL:
• Du sorgst dich vor einem wichtigen Vortrag
  → Das führt dazu, dass du dich besser vorbereitest ✓

• Du denkst über eine schwierige Beziehung nach
  → Das hilft, Veränderungen anzustoßen ✓

DAS KERNPROBLEM:
Das Problem entsteht NICHT dadurch, dass du negative Gedanken hast!
Problematisch wird es, wenn das DENKEN SELBST unproduktiv wird."""
        ),
        RNDSection(
            title = "Wann wird RND problematisch?",
            icon = "⚠️",
            content = """Repetitives Negatives Denken wird belastend, wenn:

❌ ES SEHR VIEL ZEIT EINNIMMT
   Mehrere Stunden täglich statt Minuten

❌ ES GEWOHNHEITSMÄSSIG AUFTRITT
   Automatisch zu bestimmten Zeiten
   (z.B. abends im Bett, unter der Dusche)

❌ ES ABSTRAKT BLEIBT
   "Immer passiert mir das"
   "Ich bin einfach unfähig"
   Keine konkrete Analyse

❌ KEINE LÖSUNGEN ENTSTEHEN
   Endlose Gedankenschleifen statt Klärung
   Analyse ohne Ergebnis

❌ ES HANDELN ERSETZT
   Statt zu handeln, wird nur analysiert
   Passivität statt Aktion

❌ EMOTIONALE VERSCHLECHTERUNG
   Mehr Niedergeschlagenheit
   Mehr Angst
   Mehr Anspannung
   Statt Besserung

HILFREICHER UNTERSCHIED:
→ Konstruktives Nachdenken führt zu KLARHEIT & HANDLUNG
→ Repetitives Negatives Denken führt zu KREISEN & ERSCHÖPFUNG

ACHTUNG: Der Übergang ist oft unmerklich!
Es beginnt harmlos mit einer berechtigten Frage oder 
einem echten Problem – und übergeht dann unmerklich 
in endlose Gedankenschleifen.
DIESER ÜBERGANG ist therapeutisch besonders wichtig!"""
        ),
        RNDSection(
            title = "Der Kreislauf des Grübelns",
            icon = "🔁",
            content = """Ein sich selbst verstärkender Kreislauf:

START
  ↓
1️⃣  Belastendes Ereignis oder Gefühl tritt auf
    (Fehler, Ablehnung, Kritik, Angst)
  ↓
2️⃣  Du beginnst nachzudenken
    "Was ist passiert? Warum?"
  ↓
3️⃣  Das Denken wird abstrakter
    "Warum passiert mir das immer?"
    "Was ist falsch mit mir?"
  ↓
4️⃣  Negative Gefühle nehmen zu
    Trauer, Angst, Scham, Hoffnungslosigkeit
  ↓
5️⃣  Das Bedürfnis zu analysieren wächst
    "Ich muss das verstehen!"
  ↓
6️⃣  Die Gedanken verstärken sich
    Mehr Grübeln, tiefere Abwärtsspirale
  ↓
ZURÜCK ZU 2️⃣ - Der Kreislauf beginnt von vorne!

WICHTIG:
Das Denken WIRD SELBST ZUM STRESSOR!

Es fühlt sich nicht mehr wie Problemlösen an,
sondern wie ein INNERER ZWANG, weiterdenken zu müssen.

Das ist der kritische Punkt: 
Der Gedanke soll das Problem LÖSEN,
macht es aber SCHLIMMER."""
        ),
        RNDSection(
            title = "Realistisches Ziel",
            icon = "🎯",
            content = """Ein wichtiges Missverständnis:

❌ UNREALISTISCHES ZIEL:
"Ich will nie wieder negativ denken"

Warum das nicht funktioniert:
• Das ist unmöglich
• Das ist nicht sinnvoll
• Negative Gedanken gehören zum Menschsein!
• Der Versuch führt oft zu Frustration

✅ HILFREICHE ZIELE STATTDESSEN:

1️⃣  "Ich möchte früher merken, wenn ich ins Grübeln gerate"
    Bewusstsein schaffen, nicht bekämpfen

2️⃣  "Ich möchte zwischen hilfreichem und 
    unhilfreichem Denken unterscheiden lernen"
    Unterscheidungsfähigkeit entwickeln

3️⃣  "Ich möchte flexibler zwischen Denken 
    und Handeln wechseln können"
    Balance finden

4️⃣  "Ich möchte meine Aufmerksamkeit 
    bewusster steuern"
    Kontrolle zurückgewinnen

ZENTRALES PRINZIP:
Es geht nicht um GEDANKENVERMEIDUNG,
sondern um DENKREGULATION!

Nicht: "Ich darf nicht grübeln"
Sondern: "Ich lerne, mit Grübeln anders umzugehen"

Das ist eine transformative Unterscheidung! 🔄"""
        ),
        RNDSection(
            title = "Der erste Schritt: Wahrnehmen",
            icon = "👀",
            content = """In der Verhaltenstherapie gilt:

"VERÄNDERUNG BEGINNT MIT BEOBACHTUNG"

Bevor du etwas regulieren kannst, musst du es ERKENNEN.

FRAGEN ZUM SELBSTBEOBACHTEN:

1️⃣  Woran merke ich, dass ich gerade grüble oder mich sorge?
    • Körpergefühle? (Anspannung, Schweißausbruch?)
    • Gedankenmuster? (Wiederholung, Schnelligkeit?)
    • Emotionen? (Angst, Traurigkeit wächst?)
    • Verhalten? (Bewegungslos sitzen, unruhig werden?)

2️⃣  Welche typischen Einstiegssätze habe ich?
    • "Warum…?"
    • "Was, wenn…?"
    • "Wenn das passiert, dann…?"
    • Deine persönlichen Trigger-Sätze?

3️⃣  Fühle ich mich nach 10 Minuten klarer – oder erschöpfter?
    • Klarheit = Konstruktives Denken ✓
    • Erschöpfung = RND 🔴

4️⃣  Entsteht ein konkreter Handlungsplan – oder bleibe 
    ich im Analysieren stecken?
    • Plan = Konstruktiv ✓
    • Kreisen = RND 🔴

TYPISCHE GRÜBELZEITEN/-ORTE IDENTIFIZIEREN:
Viele berichten von verstärktem RND:
• Abends im Bett
• Unter der Dusche
• Nach sozialen Situationen
• Bei Langeweile
• In stressigen Phasen

→ Diese zu kennen hilft dir zu VORAUSSCHAUEN!"""
        ),
        RNDSection(
            title = "Achtsamkeit & Selbstmitgefühl",
            icon = "💚",
            content = """Wenn du RND bei dir bemerken, ist eines BESONDERS WICHTIG:

❌ NICHT ZU TUN:
Dich zusätzlich zu verurteilen!

"Warum kann ich nicht aufhören?"
"Ich bin so dumm, dass ich wieder grüble"
"Andere können das, nur ich nicht"

Diese Selbstverurteilung macht alles SCHLIMMER!
Sie VERSTÄRKT den Stress und das RND.

✅ STATTDESSEN: HILFREICHE INNERE HALTUNG

💚 "Ah, mein Gehirn versucht gerade, 
   ein Problem durch Denken zu lösen"

Das ist die Wahrheit!
Dein Gehirn ist nicht kaputt,
es folgt nur einer alten, automatischen Strategie.

WARUM HILFT DIESES BENENNEN?

1️⃣  Es schafft ABSTAND
    Du wirst zum Beobachter statt Leidtragenden
    Psychologische Distanz entsteht

2️⃣  ABSTAND schafft WAHLMÖGLICHKEITEN
    Mit Distanz kannst du reagieren, nicht nur reagieren
    Du hast wieder Kontrolle

ACHTSAMKEIT ≠ BEKÄMPFUNG

Achtsamkeit bedeutet:
• Bemerken ohne zu urteilen
• Akzeptieren, dass Gedanken da sind
• Mit ihnen mitgefühlvoll umgehen
• Sie beobachten, nicht mit ihnen kämpfen

Paradox: Mit Akzeptanz entsteht oft mehr Kontrolle
als mit Bekämpfung!"""
        ),
        RNDSection(
            title = "Handlungsbalance",
            icon = "⚡",
            content = """Ein zentrales verhaltenstherapeutisches Prinzip:

BALANCE ZWISCHEN DENKEN UND HANDELN

Das Problem:
❌ Wenn viel Zeit ins ANALYSIEREN fließt,
   aber wenig ins AUSPROBIEREN
   → entsteht PASSIVITÄT

Warum das problematisch ist:
  Passivität
    ↓
  Negative Gefühle verstärken sich
    ↓
  Weiteres Grübeln wird ausgelöst
    ↓
  Mehr Passivität
    ↓
  🔁 Der Kreislauf verstärkt sich!

STATTDESSEN: DER HANDLUNGS-AUSGLEICH

Wenn du viel grübelst, brauchst du HANDLUNG:
✓ Ein kleiner, konkreter Schritt kann hilfreicher sein
  als eine weitere Stunde Nachdenken

✓ Bewegung: Spaziergang, Sport, Tanz
✓ Tun: An einem Projekt arbeiten, Aufgaben anpacken
✓ Engagement: Andere unterstützen, mit Menschen sein
✓ Neues: Etwas ausprobieren, experimentieren

PRAKTISCHES BEISPIEL:
Problem: Du grübelst über einen Fehler bei der Arbeit
❌ Falsch: 2 Stunden über "Was hätte ich anders machen sollen?"
✓ Richtig: 10 Min überlegen → dann einen konkreten
         Schritt zur Behebung machen

FORMEL:
Konstruktives Denken + Handlung = Bessere Lösungen
Repetitives Denken + Passivität = Mehr Probleme"""
        ),
        RNDSection(
            title = "Zusammenfassung & Hoffnung",
            icon = "🌟",
            content = """KERNERKENNTNISSE:

✓ RND ist ein weit verbreiteter, menschlicher Prozess
✓ Es erfüllt grundsätzlich sinnvolle Funktionen
✓ Problematisch wird es, wenn es:
  • Exzessiv wird (zu oft, zu lange)
  • Abstrakt bleibt (keine Lösungen)
  • Rigide ist (nicht flexibel)
  • Unproduktiv wird (keine Ergebnisse)
  • Zu emotionaler Belastung führt
  • Handlungsblockaden erzeugt

DER ERSTE & WICHTIGSTE SCHRITT:

"NICHT DAS DENKEN BEKÄMPFEN,
 SONDERN ES ERKENNEN"

Mit wachsender Bewusstheit entsteht die Möglichkeit,
ANDERS ZU REAGIEREN:
• Flexibler
• Konkreter
• Handlungsorientierter

WICHTIGE BOTSCHAFT:

Wenn du dich in diesen Beschreibungen wiederfindest,
bedeutet das NICHT, dass mit dir »etwas nicht stimmt«!

Es bedeutet lediglich, dass dein Gehirn eine Strategie
ÜBERMÄSSIG NUTZT, die ursprünglich sinnvoll war.

UND: STRATEGIEN LASSEN SICH VERÄNDERN! 💪

Mit Verständnis, Geduld und Übung kannst du
lernen, anders mit Gedanken umzugehen.

Du bist nicht allein. Das ist trainierbar. 
Es lohnt sich. 🌈"""
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
                    text = "RND: Verstehen & Regulieren",
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
                    ExpandableSectionRND(
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
                        RNDRating.entries.forEach { rating ->
                            RatingButtonRND(
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
private fun ExpandableSectionRND(
    section: RNDSection,
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
private fun RatingButtonRND(
    rating: RNDRating,
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

