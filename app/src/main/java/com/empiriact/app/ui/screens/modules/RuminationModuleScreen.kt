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

enum class RuminationRating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}

data class RuminationSection(
    val title: String,
    val icon: String,
    val content: String
)

@Composable
fun RuminationModuleScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf<RuminationRating?>(null) }
    var expandedSection by remember { mutableStateOf(-1) }

    val sections = listOf(
        RuminationSection(
            title = "Was ist Grübeln?",
            icon = "🧠",
            content = """Grübeln = Rumination = Gedankliches Wiederkäuen

Der Begriff „Rumination" bedeutet ursprünglich „Wiederkäuen".
In der Psychologie: gedankliches Wiederkäuen belastender Inhalte.

Ein Gedanke wird nicht nur einmal durchdacht,
sondern IMMER WIEDER hervorgeholt, überprüft, neu bewertet
– oft mit zunehmender emotionaler Belastung.

PSYCHOLOGISCHE DEFINITION:
Wiederkehendes, anhaltendes NEGATIVES NACHDENKEN über:
• sich selbst
• die eigenen Gefühle
• Probleme
• belastende Erlebnisse

Häufig mit einer Diskrepanz im Mittelpunkt:
"So, wie es gerade ist, soll es eigentlich nicht sein"

TYPISCHE GRÜBELFRAGEN:
• "Warum bin ich so?"
• "Wie konnte mir das passieren?"
• "Was stimmt nicht mit mir?"
• "Warum komme ich da nicht raus?"

Es geht oft um:
✗ Ursachen
✗ Schuld
✗ Konsequenzen
✗ Selbstbewertungen

DAS PARADOXE:
Grübeln fühlt sich wie ein Problemlöseversuch an
– aber einer, der sich IM KREIS DREHT."""
        ),
        RuminationSection(
            title = "Grübeln & Depression",
            icon = "😔",
            content = """ENGE VERBINDUNG ZUR DEPRESSION:

Die Forschung hat gezeigt:
Grübeln ist besonders eng mit DEPRESSIVEN VERSTIMMUNGEN verbunden.

WAS PASSIERT:
Menschen mit depressiven Symptomen neigen dazu:
→ Stark über ihre Niedergeschlagenheit nachzudenken
→ Über deren URSACHEN nachzudenken
→ Über mögliche FOLGEN nachzudenken

DAS PROBLEM:
Dieses Grübeln trägt HÄUFIG DAZU BEI, dass sich die Stimmung:
❌ NICHT verbessert
❌ Stabil NEGATIV bleibt
❌ Oder sich WEITER VERSCHLECHTERT

WICHTIG ZU VERSTEHEN:
❌ Grübeln verursacht nicht die Depression

✅ ABER: Es kann ein WICHTIGER MECHANISMUS sein,
   der DEPRESSIVE ZUSTÄNDE AUFRECHTERHÄLT

Das ist therapeutisch entscheidend:
Wenn Grübeln die Depression aufrechterhält,
dann kann die Reduktion von Grübeln die Depression bessern!"""
        ),
        RuminationSection(
            title = "Grübeln & Geschlecht",
            icon = "👥",
            content = """FORSCHUNGSERGEBNISSE:

Studien zeigen:
FRAUEN neigen im Durchschnitt HÄUFIGER 
zu einem ruminativen Denkstil als MÄNNER.

WANN BEGINNT DAS?
Bereits im JUGENDALTER lassen sich Unterschiede beobachten.

MÖGLICHE ERKLÄRUNGEN:
Soziale Lernerfahrungen:
• Mädchen: Häufiger ermutigt, sich mit Gefühlen auseinanderzusetzen
• Jungen: Eher handlungsorientiert reagieren sollen
• Unterschiedliche Reaktionen auf Emotionen
• Verschiedene Bewältigungsstile

ABER WICHTIG ZU WISSEN:

❌ Grübeln ist KEIN „weibliches Problem"!
✅ Es ist ein MENSCHLICHER Denkstil
✅ Er kommt bei ALLEN GESCHLECHTERN vor

UNTERSCHEIDUNG:
Die Forschung zeigt statistische TENDENZEN
nicht individuelle Fähigkeiten oder Schwächen!

→ Es geht nicht um Geschlecht oder Schwäche
→ Es geht um Denkstile, die jeder ändern kann"""
        ),
        RuminationSection(
            title = "Negative Folgen: Stimmung",
            icon = "📉",
            content = """GRÜBELN BLEIBT NICHT OHNE WIRKUNG!

HÄUFIGSTE FOLGE:
Verstärkung NEGATIVER STIMMUNG

Betroffene berichten:
"Je mehr ich darüber nachdenke, desto schlechter fühle ich mich"

DAS NETZWERKMODELL erklärt das:

In unserem Gedächtnis sind verknüpft:
✓ Gedanken
✓ Gefühle
✓ Körperreaktionen
✓ Erinnerungen

WENN EIN TRAURIGER GEDANKE AKTIVIERT WIRD:
↓
Werden LEICHTER weitere TRAURIGE ERINNERUNGEN aktiviert
↓
WEITERE NEGATIVE BEWERTUNGEN werden aktiviert
↓
MEHR TRAURIGKEIT entsteht

GRÜBELN WIRKT WIE EIN VERSTÄRKER:
Negative Inhalte → ziehen WEITERE negative Inhalte nach sich

METAPHER: Hefeteig
Je länger man ihn stehen lässt, desto mehr geht er auf!

GRÜBELN VERSTÄRKT:
😔 Traurigkeit
😠 Ärger
😰 Angst
😳 Scham

RESULTATE:
→ Gefühle von Hoffnungslosigkeit
→ Minderwertigkeit
→ Gereiztheit (intensivieren sich)"""
        ),
        RuminationSection(
            title = "Weitere negative Folgen",
            icon = "⚠️",
            content = """ÜBER DIE STIMMUNG HINAUS:

KÖRPERLICHE & KOGNITIVE FOLGEN:

🔴 Verminderter Antrieb
   └─ Weniger Energie
   └─ Apathie
   └─ Passivität

🔴 Konzentrationsschwierigkeiten
   └─ Mind wandering
   └─ Schwierig, bei Aufgaben zu bleiben
   └─ Geistige Müdigkeit

🔴 Schlafprobleme
   └─ Grübeln vor dem Einschlafen
   └─ Durchschlafstörungen
   └─ Weniger erholsamer Schlaf

🔴 Eingeschränkte Problemlösefähigkeit
   └─ Paradox: Grübeln soll Probleme LÖSEN
   └─ Macht aber oft das Lösen SCHWÄCHER
   └─ Tunnel-Blick statt kreatives Denken

DAS ZENTRALE PARADOXON:

Grübeln erschwert oft genau das, 
was es eigentlich erreichen möchte:
eine LÖSUNG

Selbst wenn eine Lösung erkannt wird:
→ sinkt durch anhaltendes Grübeln
→ die Wahrscheinlichkeit, sie UMZUSETZEN

WARUM?
Weil Grübeln zu Passivität und Hoffnungslosigkeit führt!"""
        ),
        RuminationSection(
            title = "Grübeln & Handlungsblockade",
            icon = "🚫",
            content = """DAS VERHALTENSTHERAPEUTISCHE PROBLEM:

Balance zwischen DENKEN und HANDELN ist zentral.

WAS PASSIERT BEIM GRÜBELN:
Sehr viel Energie → INS ANALYSIEREN
Wenig Energie → IN KONKRETE SCHRITTE

DER TEUFELSKREIS:

1️⃣  Je länger man gedanklich bei einem Problem VERWEILT
    ↓
2️⃣  Desto ÜBERWÄLTIGENDER kann es erscheinen
    ↓
3️⃣  Das reduziert MOTIVATION & HANDLUNGSBEREITSCHAFT
    ↓
4️⃣  ENTSTEHT KREISLAUF:
    
    Denken → negative Stimmung → Passivität
         ↑_________________↓
              
5️⃣  Passivität verstärkt das Problem
    ↓
    Mehr Grübeln über die Passivität
    ↓
    Noch weniger Handlung

BEISPIEL:
Problem: Fehler bei der Arbeit gemacht

❌ Grübeln: "2 Stunden über Fehler nachdenken"
   → Fühlt sich schlechter an
   → Mut sinkt
   → Nicht korrigiert

✅ Besser: "10 Min nachdenken + dann konkret handeln"
   → Problem angehen
   → Aktiv sein
   → Besser fühlen

KERNPRINZIP:
Kleine Schritte > endloses Denken"""
        ),
        RuminationSection(
            title = "Auswirkungen auf Beziehungen",
            icon = "💔",
            content = """GRÜBELN BETRIFFT NICHT NUR DIE INNERE WELT!

Es wirkt sich oft auch auf das SOZIALE UMFELD aus.

WAS ANGEHÖRIGE ERLEBEN:

Sie erleben es mitunter als BELASTEND, wenn:

🔴 Immer wieder dieselben Themen besprochen werden
   └─ Das gleiche Problem wird ständig thematisiert
   └─ Ohne dass es vorangeht

🔴 Lösungsvorschläge nicht umgesetzt werden
   └─ "Warum fragst du mich um Rat, machst aber nix?"
   └─ Frustration für Angehörige

🔴 Häufig Rückversicherungen eingefordert werden
   └─ "Bin ich nicht egoistisch?"
   └─ "Vergibst du mir?"
   └─ Ständiges Brauchen von Bestätigung

🔴 Stimmungsschwankungen anhalten
   └─ Betroffener ist emotional belastet
   └─ Ausstrahlung belastet auch andere

DAS UNVERSTANDENSEIN:

Betroffene fühlen sich häufig UNVERSTANDEN, wenn ihnen geraten wird:
"Mach dir doch nicht so viele Gedanken"

WARUM DAS NICHT HILFT:
✗ Grübeln wird nicht als freiwillige Entscheidung erlebt
✗ Sondern als INNERER DRANG oder KONTROLLVERLUST
✗ Man kann es "nicht einfach abstellen"

KREISLAUF AUCH IN BEZIEHUNGEN:

Wenn soziale Spannungen entstehen
↓
Kann das NEUES GRÜBELN auslösen
↓
Über: Konflikte, Zurückweisungen, Missverständnisse
↓
Die Beziehung wird belasteter
↓
Mehr Grübeln"""
        ),
        RuminationSection(
            title = "Grübeln als Verstärker",
            icon = "🔊",
            content = """GRÜBELN ALS ZENTRALER FAKTOR IN DER PSYCHIATRIE:

Aufgrund dieser vielfältigen Prozesse gilt Grübeln heute als:

⭐ WICHTIGER FAKTOR bei:
  • Entstehung psychischer Störungen
  • Aufrechterhaltung psychischer Störungen
  • Wiederauftreten von Symptomen

🔴 BESONDERS BEI:
  └─ DEPRESSION
  └─ ANGSTSTÖRUNGEN

WICHTIGE KLARSTELLUNG:

❌ Grübeln ist nicht „schuld"!
❌ Du bist nicht verantwortlich für deine Belastung!

✅ SONDERN:
Bestimmte Denkgewohnheiten haben
STARKE AUSWIRKUNGEN auf:
• Stimmung
• Verhalten
• Beziehungen

DENK-MODELL:
Wenn ein Denkstil Belastung VERSTÄRKEN kann
→ dann kann eine Veränderung Entlastung ERMÖGLICHEN!

DIE GUTE NACHRICHT:

Es ist nicht dein Fehler, ABER es ist deine Chance!

WEIL:
Grübeln ist KEIN Charakterzug
Grübeln ist eine ERLERNTE, oft automatisierte STRATEGIE

UND:
STRATEGIEN LASSEN SICH VERÄNDERN! 💪

Schritt für Schritt, mit Verständnis und Geduld
kannst du lernen, anders mit Gedanken umzugehen."""
        ),
        RuminationSection(
            title = "Der Perspektivwechsel",
            icon = "🌈",
            content = """VIELLEICHT ZUNÄCHST ENTMUTIGEND...

Zu hören, welche NEGATIVEN FOLGEN Grübeln haben kann,
kann sich schwer anfühlen.

ABER GENAU HIER LIEGT EINE WICHTIGE CHANCE!

🔑 KERNGEDANKE:

Wenn ein Denkstil Belastung VERSTÄRKEN kann
→ dann kann eine VERÄNDERUNG auch ENTLASTUNG ermöglichen!

DAS IST THERAPEUTISCH ENTSCHEIDEND!

TRANSFORMATION MÖGLICH:

Von: "Mein Grübeln macht mich kaputt"
Zu: "Mein Grübeln kann ich regulieren und verändern"

WAS GRÜBELN NICHT IST:

❌ Keine unveränderbare Eigenschaft
❌ Kein Charakterzug, mit dem du leben musst
❌ Keine Strafe für etwas, das du getan hast

WAS GRÜBELN IST:

✅ Eine ERLERNTE Strategie
✅ Oft AUTOMATISIERT durch Wiederholung
✅ TRAINIERT durch dein Gehirn
✅ VERÄNDERBAR durch neues Training

GEHIRN-PLASTIZITÄT:
Dein Gehirn ist TRAINIERBAR!
Wie ein Muskel, der durch Training stärker wird.

DER WEG NACH VORNE:

1️⃣  ERKENNEN: Das Grübeln bewusst bemerken
    (wie im RND-Modul beschrieben)

2️⃣  VERSTEHEN: Unterschied zwischen konstruktiv & Grübeln
    (wird in diesem Modul vermittelt)

3️⃣  LERNEN: Aus Gedankenschleifen aussteigen
    (ohne gegen Gedanken kämpfen zu müssen)

HOFFUNGSBOTSCHAFT:

Du bist nicht hilflos.
Du bist nicht schuldig.
Du bist nicht allein.

Aber du hast die Macht zu verändern! 💪✨"""
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
                    text = "Grübeln & Negative Folgen",
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
                    ExpandableSectionRumination(
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
                        RuminationRating.entries.forEach { rating ->
                            RatingButtonRumination(
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
private fun ExpandableSectionRumination(
    section: RuminationSection,
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
private fun RatingButtonRumination(
    rating: RuminationRating,
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

