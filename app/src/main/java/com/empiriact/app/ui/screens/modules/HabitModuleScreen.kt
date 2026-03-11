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

enum class HabitRating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}

data class HabitSection(
    val title: String,
    val icon: String,
    val content: String
)

@Composable
fun HabitModuleScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf<HabitRating?>(null) }
    var expandedSection by remember { mutableStateOf(-1) }

    val sections = listOf(
        HabitSection(
            title = "Grübeln als Gewohnheit",
            icon = "🔄",
            content = """THERAPEUTISCHE PERSPEKTIVE:

Grübeln ist NICHT:
❌ Charaktereigenschaft
❌ Schwäche
❌ Bewusste Entscheidung

SONDERN:
✅ Ungünstige GEWOHNHEIT
✅ Ein erLERNTER Denkstil
✅ Automatisches Verhaltensmuster

WARUM DIESE PERSPEKTIVE ENTLASTEND IST:

Charaktereigenschaften sind (scheinbar) unveränderbar.
ABER: Gewohnheiten sind VERÄNDERBAR!

MERKMALE EINER DENKGEWOHNHEIT:

1️⃣  Es läuft AUTOMATISCH ab
   └─ Ohne bewusste Steuerung

2️⃣  Es beginnt oft UNBEWUSST
   └─ Du merkst nicht, wie es startet

3️⃣  Es wird nicht aktiv gewählt
   └─ "Das passiert einfach"

4️⃣  Es fühlt sich schwer kontrollierbar an
   └─ "Ich kann es nicht stoppen"

5️⃣  Es wiederholt sich in ähnlichen Situationen
   └─ Immer wieder der gleiche Ablauf

WICHTIG:
Grübeln entwickelt sich nicht von heute auf morgen.
Es wird über längere Zeit EINGEÜBT – meist UNABSICHTLICH."""
        ),
        HabitSection(
            title = "Ursprung der Gewohnheit",
            icon = "🌱",
            content = """WIE WIRD GRÜBELN ZUR GEWOHNHEIT?

HÄUFIGE LERNGESCHICHTEN:

1️⃣  DURCH FAMILIENKULTUR
   └─ "In unserer Familie wurde viel analysiert"
   └─ "Es wurde viel hinterfragt"
   └─ "Es gab viel Kritik"
   └─ Du lernst: "So geht man mit Problemen um"

2️⃣  DURCH KONTROLLVERSUCHE
   └─ "Ich konnte Situationen nur durch Nachdenken kontrollieren"
   └─ "Wenn ich genug analysiere, kann ich vorbereitet sein"
   └─ "Mit Gedanken kann ich Unsicherheit reduzieren"

3️⃣  DURCH VORAHNUNG
   └─ "Durch Grübeln kann ich Probleme vorhersehen"
   └─ "Wenn ich über alles nachdenke, passiert nichts Schlimmes"
   └─ "Grübeln schützt mich"

4️⃣  ALS BEWÄLTIGUNGSSTRATEGIE
   └─ "Wenn ich traurig bin, lenke ich mich durch Analyse ab"
   └─ "Nachdenken fühlt sich produktiver an"
   └─ "Es fühlt sich so an, als würde ich etwas tun"

PARADOX DER KURZFRISTIGEN HILFREICHE:

Grübeln KANN kurzfristig helfen:
✓ Fühlt sich aktiv an
✓ Fühlt sich produktiv an
✓ Vermittelt Kontrolle
✓ Lenkt von Angst ab

ABER LANGFRISTIG:
❌ Verliert es seinen Nutzen
❌ Wird selbst zum Problem
❌ Verstärkt negative Gefühle
❌ Blockiert echte Lösungen"""
        ),
        HabitSection(
            title = "Wie Gewohnheiten entstehen",
            icon = "🔗",
            content = """GEWOHNHEITEN ENTSTEHEN DURCH WIEDERHOLUNG

Das funktioniert wie KLASSISCHES KONDITIONIEREN:

PHASE 1: ERSTE WIEDERHOLUNG
├─ Belastendes Gefühl tritt auf (z.B. Traurigkeit)
├─ Du reagierst mit Grübeln
├─ Eine Verknüpfung entsteht

PHASE 2: WIEDERHOLTE WIEDERHOLUNGEN
├─ Jedes Mal wenn Traurigkeit auftritt
├─ Folgst du automatisch mit Grübeln
├─ Die Verknüpfung wird stärker

PHASE 3: AUTOMATISIERUNG
├─ Die Verbindung wird schneller
├─ Die Verbindung wird automatischer
├─ Das Grübeln scheint "von selbst" zu starten
├─ Irgendwann:
│   Gefühl tritt auf → Grübeln startet
│   Ohne dass du bewusst entscheidest

BEISPIEL DER AUTOMATISIERUNG:

Tag 1: Traurigkeit → Bewusstes Grübeln
Tag 10: Traurigkeit → Etwas schneller grübeln
Tag 30: Traurigkeit → Grübeln ist automatisch
Tag 100: Traurigkeit → Grübeln beginnt sofort
1 Jahr: Traurigkeit → Du merkst gar nicht mehr, wie es startet

ABER WICHTIG:
Auch wenn es automatisch läuft
→ Es ist NICHT unveränderlich!
→ Automatische Muster können UMGELERNT werden!"""
        ),
        HabitSection(
            title = "Der wichtige Perspektivwechsel",
            icon = "💡",
            content = """WENN GRÜBELN EINE GEWOHNHEIT IST...

...folgt daraus etwas ENTSCHEIDENDES:

✅ Gewohnheiten sind ERLERNT
✅ Deshalb sind sie auch VERLERNBAR

ANALOGIE MIT ANDEREN GEWOHNHEITEN:

Nägelkauen:
❌ Sagen Sie nicht: "Ich bin Nägelkauer als Persönlichkeit"
✅ Sondern: "Ich habe mir das angewöhnt"

Aufschieben:
❌ Nicht: "Ich bin prokrastinator-typ"
✅ Sondern: "Das ist meine Gewohnheit"

Zu spät ins Bett gehen:
❌ Nicht: "So bin ich halt"
✅ Sondern: "Das ist ein Verhaltensmuster"

DASSELBE GILT FÜR GRÜBELN:

❌ NICHT: "Ich bin ein Grübler als Persönlichkeit"
✅ SONDERN: "Ich habe mir Grübeln angewöhnt"

DER KERNGEDANKE:

Grübeln ist nicht DEINE IDENTITÄT
Grübeln ist ein AUTOMATISIERTES MUSTER

UND: MUSTER KÖNNEN VERÄNDERT WERDEN!

THERAPEUTISCHE HOFFNUNG:

Wenn du es dir selbst beigebracht hast
→ Dann kannst du dir auch ETWAS ANDERES beibringen!

Die Fähigkeit zum Lernen ist dieselbe
Die Neuroplastizität ist da
Das Gehirn kann neue Wege gehen"""
        ),
        HabitSection(
            title = "Realistisches Ziel",
            icon = "🎯",
            content = """WAS IST DAS REALISTISCHE ZIEL?

❌ NICHT: "Grübeln vollständig stoppen"

WARUM NICHT?
• Gedanken lassen sich nicht abschalten
• Der Versuch, Gedanken zu unterdrücken
  → führt oft dazu, dass sie STÄRKER zurückkommen
• Das nennt sich "Ironic Rebound Effect"

✅ DAS REALISTISCHE ZIEL:

1️⃣  FRÜHER ERKENNEN
   "Ah, ich grüble gerade"
   statt: plötzlich 2 Stunden später merken

2️⃣  AUTOMATISCHEN ABLAUF UNTERBRECHEN
   "Ich bemerke die Gedankenspirale"
   "Ich unterbreche das Muster"
   statt: zulassen, dass es weitergehen kann

3️⃣  SCHNELLER WECHSELN
   "Ich wechsle zu hilfreichem Denken"
   oder: "Ich wechsle zu Handlung"
   statt: bei Grübeln bleiben

4️⃣  ALTERNATIVE REAKTIONEN EINÜBEN
   "Ich trainiere neue Reaktionsmuster"
   "Diese werden selbst zur Gewohnheit"
   statt: alte Muster behalten

NEUE AUTOMATISMEN SCHAFFEN:

Alt:    Stress auftritt → Grübeln startet (automatisch)
                  ↓
Neu:    Stress auftritt → Tiefes Atmen + Aktion (neue Gewohnheit)

KERNIDEE:
Es geht nicht darum, das Denken zu VERBIETEN
Es geht darum, FLEXIBLER zu werden!

Von: "Ich kann nur grübeln"
Zu: "Ich habe mehrere Optionen wie ich reagiere"

DAS IST FREIHEIT! 🔓"""
        ),
        HabitSection(
            title = "Gegenkonditionierung",
            icon = "⚡",
            content = """WENN GRÜBELN EINE GEWOHNHEIT IST...

...reichen reine Denk-INHALTE manchmal nicht aus!

WARUM?

Es reicht nicht zu sagen:
"Der Gedanke ist irrational"
"Das ist logisch nicht wahr"
"Das ergibt keinen Sinn"

WARUM NICHT?
Die Gewohnheit betrifft nicht den INHALT
Die Gewohnheit betrifft den PROZESS!

Beispiel:
Du sagst: "Das ist irrational"
Aber: Das Gehirn hat gelernt, automatisch zu grübeln
→ Logik hilft nicht gegen Automatismus

DESHALB: GEGENKONDITIONIERUNG

Das ist eine VERHALTENSTHERAPEUTISCHE STRATEGIE:

STATT: "Ich stoppe meine Gedanken"
MACHEN: "Ich antworte mit einer neuen, inkompatiblen Reaktion"

PRAKTISCHE BEISPIELE:

1️⃣  WENN NIEDERGESCHLAGENHEIT AUFTRITT
   ❌ Alt: Sich zurückziehen + grübeln
   ✅ Neu: Bewusst eine kleine Aktivität beginnen
   └─ Spaziergang, Handwerk, mit jemandem reden
   └─ Grübeln ist mit Aktivität nicht kompatibel

2️⃣  WENN GEDANKEN IM KREIS DREHEN
   ❌ Alt: Weiter analysieren
   ✅ Neu: In KONKRETES Problemlösen wechseln
   └─ Stift und Papier, konkrete Schritte
   └─ Konkretheit ist unkompatibel mit abstraktem Grübeln

3️⃣  WENN INNERE ANSPANNUNG ENTSTEHT
   ❌ Alt: Anspannung durch Denken klären
   ✅ Neu: ENTSPANNUNGSÜBUNG durchführen
   └─ Atem, Progressive Muskelentspannung, Yoga
   └─ Entspannung ist unkompatibel mit Anspannungsmuster

4️⃣  WENN "WARUM"-FRAGEN AUFTAUCHEN
   ❌ Alt: Tiefer in Analyse gehen
   ✅ Neu: Bewusst ins HIER-UND-JETZT wechseln
   └─ Sinneswahrnehmung, Achtsamkeit
   └─ Gegenwart ist unkompatibel mit Vergangenheit-Analyse

DIE WISSENSCHAFT:

GEGENKONDITIONIERUNG erzeugt neue Verknüpfungen:
Alt: Auslöser → Grübeln
Neu: Auslöser → Hilfreiche Reaktion

JE HÄUFIGER du das neue Muster durchführst
→ DESTO STÄRKER wird die neue Verknüpfung
→ DESTO AUTOMATISCHER wird sie"""
        ),
        HabitSection(
            title = "Neue Gewohnheiten trainieren",
            icon = "🏋️",
            content = """NEUE GEWOHNHEITEN BRAUCHEN WIEDERHOLUNG

ANALOGIE: Sport & Sprache lernen

Beim Sprache-Lernen:
📚 Am Anfang: Alles ist bewusst + anstrengend
   "Ich muss jedes Wort überlegen"
   "Ich muss Grammatik bewusst anwenden"
   
📚 Mit der Zeit: Es wird automatischer
   "Ich spreche flüssig"
   "Ich überlege nicht mehr"
   
📚 Irgendwann: Es ist Automatismus
   "Ich spreche ohne zu denken"

GENAUSO VERHÄLT ES SICH MIT DENKGEWOHNHEITEN!

PHASE 1: BEWUSSTES TRAINIEREN
├─ ⏱️ Anfangs: Braucht viel Aufmerksamkeit
├─ 💪 Kostet viel Energie
├─ 🧠 Erfordert bewusste Anstrengung
└─ 📋 Braucht Wiederholung

BEISPIEL:
"Wenn ich merke, dass ich grüble
 → Ich atme bewusst ein
 → Ich sage mir: 'Das ist Grübeln'
 → Ich mache einen Schritt nach vorne"

Am Anfang: ANSTRENGEND und BEWUSST!

PHASE 2: WIEDERHOLTES ÜBEN
├─ Mit jeder Wiederholung wird es leichter
├─ Der Ablauf wird schneller
├─ Es kostet weniger Energie
└─ Es wird routinierter

Nach 10x: "Es fällt mir leichter"
Nach 30x: "Ich muss weniger denken"
Nach 100x: "Das ist fast automatisch"

PHASE 3: NEUE AUTOMATISMEN
├─ Das neue Verhalten wird selbst zur Gewohnheit
├─ Es läuft automatisch ab
├─ Es braucht keine bewusste Steuerung mehr
└─ Ein NEUER AUTOMATISMUS ist entstanden!

DIE GEDULDS-KOMPONENTE:

✅ Mit Rückschlägen rechnen
   "Manchmal werde ich wieder ins alte Muster zurückfallen"
   "Das ist normal beim Lernen"

✅ Erneutes Üben einplanen
   "Ich trainiere weiter"
   "Jede Wiederholung zählt"

✅ Langzeitperspektive haben
   "Das ist ein Lernprozess, kein einmaliger Entschluss"
   "Veränderung braucht Zeit"

DAS ZIEL:

Belastende Stimmung tritt auf
↓
AUTOMATISCHE NEW Reaktion
(statt Grübeln)

Das nennt sich: NEUE GEWOHNHEIT

Und das ist ERLERNBAR! 💪"""
        ),
        HabitSection(
            title = "Grübeln während der Lektüre",
            icon = "📖",
            content = """EINE WICHTIGE BEOBACHTUNG:

Vielleicht bemerken Sie SOGAR JETZT beim Lesen:
• Dass Gedanken ABSCHWEIFEN
• Dass sie anfangen zu ANALYSIEREN
• "Warum bin ich so?"
• "Ob das bei mir klappen wird?"

DAS IST NICHT UNGEWÖHNLICH!

WARUM PASSIERT DAS?

Grübeln kann auch NEUE INFORMATIONEN überlagern!
• Es bindet GEISTIGE KAPAZITÄT
• Es beeinträchtigt Konzentration
• Es beeinträchtigt Gedächtnis
• Die Ressource "Aufmerksamkeit" wird verbraucht

WENN DAS PASSIERT:
Das ist BEREITS EINE TRAININGSMÖGLICHKEIT!

SCHNELLE ÜBUNG:

Falls Sie bemerken, dass Ihre Gedanken abschweifen:

1️⃣  KURZ INNEHALTEN
   └─ Pausieren Sie einen Moment

2️⃣  EINEN BEWUSSTEN ATEMZUG NEHMEN
   └─ Tief ein, langsam aus

3️⃣  DEN SATZ ERNEUT LESEN
   └─ Zurück zum Text

4️⃣  SICH INNERLICH SAGEN:
   └─ "Das war Grübeln – ich kehre zurück"

THERAPEUTISCHE BOTSCHAFT:

Auch DIES ist bereits Übung im Gewohnheitswechsel!
✓ Du merkst das Muster
✓ Du unterbrechen es
✓ Du wendest dich dem Hilfreichem zu

DAS IST GENAU DER PROZESS, DEN DU TRAINIERST!

Kleine Praktiken, hundertfach wiederholt
→ werden zur neuen Gewohnheit"""
        ),
        HabitSection(
            title = "Zusammenfassung & Hoffnung",
            icon = "🌈",
            content = """ZUSAMMENFASSUNG DES THERAPIERATIONALS:

✅ Grübeln ist eine automatische DENKGEWOHNHEIT
✅ Gewohnheiten entstehen durch WIEDERHOLUNG
✅ Grübeln ist NICHT Teil Ihrer PERSÖNLICHKEIT
✅ Gewohnheiten können VERlernt werden
✅ Neue können an ihre Stelle treten
✅ Ziel ist nicht GEDANKENLOSIGKEIT
✅ Sondern: FLEXIBILITÄT
✅ Veränderung entsteht durch:
   • Bewusstes Wahrnehmen
   • Wiederholtes Einüben
   • Alternative Reaktionen trainieren
✅ GEDULD UND ÜBUNG sind entscheidend

VIELLEICHT FÜHLT SICH DAS ZUNÄCHST AN...
...wie viel Arbeit

GLEICHZEITIG LIEGT HIER ETWAS SEHR HOFFNUNGSVOLLES:

"Wenn Grübeln erlernt wurde,
 dann können Sie auch etwas NEUES lernen!"

DAS IST DIE ZENTRALE BOTSCHAFT:

Du bist nicht gefangen.
Du bist nicht schuldig.
Du bist nicht schwach.

SONDERN:

Du hast ein Muster gelernt.
Und du kannst ein NEUES Muster lernen.

Die Fähigkeit dazu ist DA.
Das Gehirn ist PLASTISCH.
Die Neuronen sind LERNFÄHIG.

KONKRETE HOFFNUNG:

Wie lange hat es gedauert, bis das alte Muster automatisch wurde?
Vielleicht Jahre?

Aber: Das NEUE Muster braucht nicht so lange!
Mit BEWUSSTEM Trainieren geht es schneller.

DAS IST DIE CHANCE:

Mit neuer Erkenntnis + systematischem Üben
kannst du ein ANDERES automatisches Muster aufbauen.

In Wochen oder Monaten
statt Jahren.

KRAFTVOLLE ABSCHLUSSBOTSCHAFT:

Die Tatsache, dass Grübeln erlernt wurde,
bedeutet nicht, dass du schwach bist.

Es bedeutet, dass du lernen KANNST.

Und darum geht es: Neues lernen. 💪✨"""
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
                    text = "Grübeln als Gewohnheit",
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
                    ExpandableSectionHabit(
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
                        HabitRating.entries.forEach { rating ->
                            RatingButtonHabit(
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
private fun ExpandableSectionHabit(
    section: HabitSection,
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
private fun RatingButtonHabit(
    rating: HabitRating,
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

