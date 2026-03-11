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
import androidx.compose.foundation.shape.CircleShape
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

enum class DenkstilRating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}

data class DenkstilSection(
    val title: String,
    val icon: String,
    val content: String
)

@Composable
fun DenkstileModuleScreen(onBack: () -> Unit) {
    var currentStep by remember { mutableStateOf(0) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf<DenkstilRating?>(null) }
    var expandedSection by remember { mutableStateOf(-1) }

    val sections = listOf(
        DenkstilSection(
            title = "Gewohnheiten & RND",
            icon = "🧠",
            content = """Grübeln gehört zu den negativen Denkschleifen, in der Fachsprache repetitives negatives Denken (RND) genannt.

Eine Gewohnheit ist ein automatisches Verhaltensmuster, das unabsichtlich und unbewusst ausgelöst wird.

Wenn Personen wiederholt das gleiche Verhalten (z.B. Grübeln) ausüben, automatisiert sich dieses Verhalten. Man trainiert es sich sozusagen an.

Besondere Auslöser:
• Bestimmte Situationen (z.B. im Bett vor dem Einschlafen)
• Können das Grübeln »triggern« wenn regelmäßig dort gegrübelt wurde
• Das Gehirn lernt: "Diese Situation = Zeit zum Grübeln"

Die gute Nachricht:
»Leider verfallen wir immer leichter ins Grübeln, je öfter wir damit anfangen. Machen wir uns in negativer Stimmung zu viele Gedanken, trainieren wir die Netzwerke negativer Gedanken und Erinnerungen und stärken die Verbindungen zwischen ihnen.« (Susan Nolen-Hoeksema, 2006)

⚠️ Aber: Grübeln ist KEIN Teil der Persönlichkeit!
Gewohnheiten können wieder abtrainiert werden."""
        ),
        DenkstilSection(
            title = "Ungünstige Denkmuster",
            icon = "⛔",
            content = """AUTOMATISIERT & UNKONTROLLIERBAR
Das Denken startet unabsichtlich und unbewusst
Schwierig zu beeinflussen oder zu beenden
Lädt auch in Situationen los, wo man es nicht will
(z.B. auf der Arbeit, vor dem Einschlafen)

SICH WIEDERHOLEND
Kreist um den gleichen Inhalt
Springt sprunghaft zwischen Problemen
Verliert schnell den roten Faden
→ Kein Problem wird befriedigend gelöst

NEGATIV & IRRATIONAL
Sieht nur negative Aspekte
Blendet Positive/Neutrale aus
Tendiert zu negativer Überbewertung
Zu selbstkritisch, selbstabwertend oder katastrophisierend

UNPRODUKTIV
Keine Lösung, kein Handlungsplan, keine Entscheidung
Fokus auf Hindernisse & Schwierigkeiten
Kreist um Nicht-Lösungen (To-do-not-Liste)
→ Hinterher: hilflos und verwirrt

ABSTRAKT & VERALLGEMEINERND
Bleibt oberflächlich, keine Details
Verallgemeinert unzulässig (»immer«, »nie«)
Vernachlässigt die konkrete Situation

STARR
Bleibt auf negativen Inhalten gerichtet
Lässt sich nicht auf andere Aspekte lenken
Neue Umstände führen zu keiner Anpassung

EMOTION-VERMEIDEND
Versuch, unangenehme Emotionen zu verdrängen

PASSIV & DEMOTIVIEREND
Verleitet zur Passivität
Zu wenig Handlung, zu viel Denken
Entmutigt durch Fokus auf Schwierigkeiten"""
        ),
        DenkstilSection(
            title = "Günstige Denkmuster",
            icon = "✅",
            content = """KONTROLLIERBAR & LÖSUNGSORIENTIERT
✓ Denken lässt sich steuern
✓ Verfolgt Fragen & Antworten in Richtung Lösung
✓ Aktive Gestaltung möglich

FOKUSSIERT
✓ Bleibt bei einem Thema/Problem
✓ Konzentriert auf Wichtiges
✓ Blendet Unwichtiges aus

AUSGEWOGEN & RATIONAL
✓ Positive UND negative Aspekte betrachtet
✓ Wohlwollende Haltung sich selbst gegenüber
✓ Stärken UND Schwächen akzeptiert
✓ Berücksichtigt Wahrscheinlichkeiten & Fakten
✓ Prüft, ob Schlüsse folgerichtig sind

PRODUKTIV
✓ Zielt auf Problemlösung ab
✓ Erzeugt konkrete Lösungen & Handlungspläne
✓ Ermöglicht Entscheidungen
✓ Hinterher: Weiß man, wie man vorgehen kann (To-do-Liste)

KONKRET & SITUATIONSBEZOGEN
✓ Geht in die Tiefe
✓ Befasst sich mit wichtigen Details
✓ Betrachtet konkrete Situationen

FLEXIBEL
✓ Lässt sich von negativen auf positive Aspekte lenken
✓ Ziele & Erwartungen passen sich neuen Umständen an
✓ Offen für neue Informationen

EMOTION-AKZEPTIEREND
✓ Emotionen werden akzeptiert
✓ Werden mitberücksichtigt, ohne überbewertung

AKTIV & MOTIVIEREND
✓ Entsteht konkreter Handlungsplan
✓ Klar, welche Schritte zu gehen sind
✓ Gute Balance zwischen Denken & Handeln
✓ Ermutigt zum Ausprobieren (»Probieren geht über Studieren!«)"""
        ),
        DenkstilSection(
            title = "Vergleich: Ungünstig vs. Günstig",
            icon = "⚖️",
            content = """UNGÜNSTIGER DENKSTIL → GÜNSTIGER DENKSTIL

Automatisch ............ Kontrollierbar
Unkontrollierbar ....... Lösungsorientiert
Sich wiederholend ...... Fokussiert
Negativ & irrational ... Ausgewogen & rational
Unproduktiv ............ Produktiv
Abstrakt ............... Konkret
Starr .................. Flexibel
Emotion-vermeidend .... Emotion-akzeptierend
Passiv ................. Aktiv
Demotivierend .......... Motivierend

🎯 ZIEL:
Vom ungünstigen zum günstigen Denkstil wechseln

💡 GUTE NACHRICHT:
Dies ist ein Prozess, den man trainieren kann!
Mit Bewusstsein und Übung können alte Gewohnheiten
durch neue, günstigere Denkmuster ersetzt werden."""
        ),
        DenkstilSection(
            title = "Praktische Implikationen",
            icon = "🚀",
            content = """ERKENNE DEINEN AKTUELLEN DENKSTIL:
1. In welchen Situationen grübelst du automatisch?
2. Welche Trigger lösen Grübeln bei dir aus?
3. Wie fühlt sich dein Denken an? (Ungünstig? Günstig?)

BEWUSSTSEINSSCHRITTE:
1. Beobachte: Wenn das Grübeln startet
2. Unterbreche: Das automatische Muster
3. Umlenke: Zu einem günstigen Denkmuster
4. Wiederhole: Trainiere neue Gewohnheiten

TRAINING NEUER GEWOHNHEITEN:
✓ Regelmäßige Praxis erforderlich
✓ Mit der Zeit automatisiert sich das Neue
✓ Alte Auslöser bekommen neue Bedeutung
✓ Gehirn lernt: "Diese Situation = konstruktives Denken"

LANGFRISTIG:
• Wenn du regelmäßig konstruktiv denkst, trainierst du 
  Netzwerke positiver Gedanken
• Du stärkst die Verbindungen zwischen günstigen Denkmustern
• Mit der Zeit wird das neue Denkmuster die Gewohnheit

💪 DU HAST DIE KRAFT!
Grübeln ist ein automatisches Muster, das du dir selbst 
antrainiert hast. Du kannst dir auch andere, bessere 
Muster antrainieren!"""
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
                    text = "Denkstile: Ungünstig vs. Günstig",
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
                    ExpandableSectionDenkstil(
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
                        DenkstilRating.entries.forEach { rating ->
                            RatingButtonDenkstil(
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
private fun ExpandableSectionDenkstil(
    section: DenkstilSection,
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
private fun RatingButtonDenkstil(
    rating: DenkstilRating,
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

