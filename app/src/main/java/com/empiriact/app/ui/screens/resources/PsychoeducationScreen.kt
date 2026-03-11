package com.empiriact.app.ui.screens.resources

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============== Datenmodelle ==============

enum class ModuleRating(val label: String, val value: Int) {
    VERY_NEGATIVE("--", -2),
    NEGATIVE("-", -1),
    NEUTRAL("0", 0),
    POSITIVE("+", 1),
    VERY_POSITIVE("++", 2)
}

private data class PsychoeducationModule(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val chapters: List<Chapter>,
    val estimatedReadTime: Int, // in Minuten
    val difficulty: String, // "Anfänger", "Fortgeschrittene", "Alle"
    var isBookmarked: Boolean = false,
    var rating: ModuleRating? = null,
    val isExample: Boolean = true // Kennzeichne als Beispiel-Modul
)

private data class Chapter(
    val id: String,
    val title: String,
    val content: String,
    val sections: List<Section>,
    val keyTakeaways: List<String>
)

private data class Section(
    val heading: String,
    val text: String,
    val examples: List<String> = emptyList(),
    val isExpandable: Boolean = false
)

// ============== Psychoedukative Inhalte ==============

private fun getPsychoeducationModules(): List<PsychoeducationModule> {
    return listOf(
        PsychoeducationModule(
            id = "emotional_regulation",
            title = "Emotionsregulation",
            subtitle = "Verstehe deine Gefühle und gestalte sie bewusst",
            icon = Icons.Default.School,
            color = Color(0xFF6366F1),
            estimatedReadTime = 8,
            difficulty = "Anfänger",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Was sind Emotionen wirklich?",
                    content = "Emotionen sind natürliche, biologische Reaktionen deines Körpers und Geistes. Sie sind nicht 'gut' oder 'schlecht' – sie sind Informationen, die du nutzen kannst.",
                    sections = listOf(
                        Section(
                            heading = "Die vier Komponenten von Emotionen",
                            text = "Jede Emotion besteht aus vier zusammenwirkenden Komponenten. Diese verstehen hilft dir, Emotionen besser zu managen.",
                            examples = listOf(
                                "🧠 Körper: Erhöhter Puls, Schwitzen, Muskelverspannung",
                                "💭 Gedanke: Automatische Überzeugungen ('Das ist gefährlich')",
                                "🎯 Verhalten: Flucht-, Angriffs- oder Erstarungsimpulse",
                                "😊 Ausdruck: Gesichtszüge, Stimme, Körperhaltung"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Warum existieren Emotionen?",
                            text = "Emotionen sind evolutionäre Überlebensstrategien. Sie helfen dir, auf Herausforderungen zu reagieren und deine Werte zu leben.",
                            examples = listOf(
                                "😰 Angst warnt dich vor Gefahren und bereitet dich vor",
                                "😢 Trauer hilft dir, Verluste zu verarbeiten und Unterstützung zu suchen",
                                "😠 Wut gibt dir Kraft, Grenzen zu setzen und gegen Ungerechtigkeit zu handeln"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Emotionen sind körperliche und mentale Prozesse – nicht etwas, das du 'sein' kannst",
                        "Jede Emotion hat einen Zweck und kommuniziert dir etwas Wichtiges",
                        "Emotionen zu akzeptieren ist der erste Schritt zur Regulation"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Praktische Strategien zur Emotionsregulation",
                    content = "Es gibt bewährte, wissenschaftlich fundierte Techniken, die du sofort nutzen kannst.",
                    sections = listOf(
                        Section(
                            heading = "RAIN: Recognize-Allow-Investigate-Non-identify",
                            text = "Eine vier-schrittrige Mindfulness-Methode, um mit intensiven Emotionen umzugehen. Besonders effektiv bei Angst, Trauer und Wut.",
                            examples = listOf(
                                "1️⃣ Recognize: 'Ich bemerke Angst in meiner Brust und meinem Bauch'",
                                "2️⃣ Allow: 'Es ist okay, dass diese Emotion da ist. Ich widerstehe ihr nicht'",
                                "3️⃣ Investigate: 'Was sagt mir diese Angst? Was brauche ich gerade?'",
                                "4️⃣ Non-identify: 'Ich bin nicht meine Angst – sie ist eine vorübergehende Erfahrung'"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Das ABC-Modell der Kognitiven Verhaltenstherapie",
                            text = "A=Situation, B=dein Gedanke über die Situation, C=deine Emotion. Wenn du B veränderst, veränderst du C.",
                            examples = listOf(
                                "❌ Problematischer Weg: Kritik vom Chef → 'Ich bin total unzulänglich' → Verzweiflung",
                                "✅ Alternativer Weg: Kritik vom Chef → 'Das ist Feedback zur Verbesserung' → Motivation"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "RAIN hilft dir, intensive Gefühle ohne Widerstand zu durchleben",
                        "Deine Gedanken beeinflussen direkt deine Emotionen – Veränderung ist möglich",
                        "Kleine Gedanken-Shifts können große emotionale Verschiebungen auslösen"
                    )
                )
            )
        ),
        PsychoeducationModule(
            id = "anxiety_understanding",
            title = "Angststörungen verstehen",
            subtitle = "Von Besorgnis zu Bewältigung",
            icon = Icons.Default.Lightbulb,
            color = Color(0xFFF59E0B),
            estimatedReadTime = 10,
            difficulty = "Fortgeschrittene",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Die Neurobiologie der Angst",
                    content = "Angst ist nicht ein Fehler deines Gehirns – es ist ein überlebenswichtiges System. Das Problem: Es reagiert manchmal zu empfindlich.",
                    sections = listOf(
                        Section(
                            heading = "Das Kampf-Flucht-Erstarrungs-System",
                            text = "Wenn dein Gehirn eine Bedrohung (real oder imaginär) erkennt, aktiviert es automatisch Überlebensmechanismen. Diese waren früher notwendig – heute oft überreagiert.",
                            examples = listOf(
                                "⚔️ Kampf: 'Ich werde aggressiv oder konfrontativ' (Adrenalin)",
                                "🏃 Flucht: 'Ich vermeide oder fliehe' (Adrenalin + Cortisol)",
                                "🧊 Erstarrung: 'Ich bin gelähmt oder dissoziiert' (Freeze-Response)"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Angstverstärkende Zyklen",
                            text = "Manche Verhaltensweisen fühlen sich kurzfristig gut an, verstärken aber langfristig die Angst.",
                            examples = listOf(
                                "🔴 Vermeidung: 'Ich meide Aufzüge' → kurzfristig Erleichterung → aber Angst wächst",
                                "🔴 Sicherheitsverhalten: 'Ich trinke Alkohol zur Beruhigung' → Abhängigkeit",
                                "🔴 Rumination: 'Was wenn...?' Gedankenschleifen → Angst verstärkt sich"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Angst ist ein natürliches System, das manchmal überreagiert",
                        "Vermeidung verstärkt Angst langfristig – auch wenn sie kurzfristig hilft",
                        "Dein Gehirn kann umlernen: Angst ist nicht permanent"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Evidenzbasierte Behandlung",
                    content = "Es gibt gut erforschte, wirksame Methoden zur Angstbewältigung.",
                    sections = listOf(
                        Section(
                            heading = "Kognitive Verhaltenstherapie (KVT)",
                            text = "KVT hilft dir, angstverstärkende Gedanken zu identifizieren und zu überprüfen. Studien zeigen 60-80% Erfolgsquote.",
                            examples = listOf(
                                "🧠 Gedankenfalle: 'Es wird sicher etwas Schlimmes passieren'",
                                "❓ Realitätsprüfung: 'Was spricht dafür? Was dagegen? Wie wahrscheinlich?'",
                                "✅ Neuer Gedanke: 'Ich habe schwierige Dinge schon überstanden'"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Expositionstherapie (Konfrontation)",
                            text = "Graduell die Angst-Situation aufsuchen (nicht vermeiden) zeigt dir: Du bist sicher. Das ist die effektivste Methode.",
                            examples = listOf(
                                "📍 Leicht: Gedanken an die Angst-Situation erlauben",
                                "📍 Mittel: Die Situation aufsuchen und beobachten",
                                "📍 Schwer: Aktiv in der Situation handeln"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "KVT und Expositionstherapie sind evidenzbasiert wirksam",
                        "Deine Angst-Reaktion ist normal – nicht abnormal oder schambehaftet",
                        "Mit Geduld und Unterstützung wird es leichter – Heilung ist möglich"
                    )
                )
            )
        ),
        PsychoeducationModule(
            id = "cognitive_defusion",
            title = "Kognitive Defusion",
            subtitle = "Befreie dich von belastenden Gedanken",
            icon = Icons.Default.School,
            color = Color(0xFF10B981),
            estimatedReadTime = 7,
            difficulty = "Anfänger",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Gedanken sind nicht Fakten",
                    content = "Dein Gehirn ist eine Gedankenmaschine – aber nicht alle Gedanken sind wahr oder hilfreich.",
                    sections = listOf(
                        Section(
                            heading = "Kognitive Fusion vs. Defusion",
                            text = "Fusion: Du glaubst dem Gedanken = \"Ich bin ein Versager\". Defusion: Du bemerkst den Gedanken = \"Ich habe den Gedanken, dass ich ein Versager bin\".",
                            examples = listOf(
                                "Fusion: Angst, Vermeidung, Handlungsunfähigkeit",
                                "Defusion: Neugier, Akzeptanz, Handlungsfähigkeit"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Woher kommen die problematischen Gedanken?",
                            text = "Oft aus Gewohnheit, früheren Erfahrungen oder Stress – nicht weil sie wahr sind.",
                            examples = listOf(
                                "Gedanke: \"Ich werde scheitern\" (kommt vielleicht von einer frühen Kritik)",
                                "Gedanke: \"Alle starren mich an\" (kommt vielleicht von Angststörung)"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Du bist nicht deine Gedanken",
                        "Gedanken sind mentale Ereignisse, keine Fakten",
                        "Du kannst Gedanken haben und trotzdem handeln"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Praktische Defusions-Techniken",
                    content = "Einfache Übungen, um Gedanken zu entkoppeln und neu zu bewerten.",
                    sections = listOf(
                        Section(
                            heading = "\"Danke für den Gedanken\"-Technique",
                            text = "Wenn ein belastender Gedanke auftaucht, sage innerlich: \"Danke, mein Gehirn, dass du mich schützen möchtest\".",
                            examples = listOf(
                                "Gedanke: \"Ich werde die Präsentation vermasseln\"",
                                "Antwort: \"Danke für die Sorge – ich habe vorbereitet und werde es schaffen\""
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Gedanken visualisieren und vorbeiziehen lassen",
                            text = "Stell dir vor, deine Gedanken sind Wolken. Beobachte sie, wie sie vorbeizieht – ohne dich daran zu klammern.",
                            examples = listOf(
                                "Der Gedanke \"Ich bin nicht gut genug\" kommt → Beobachte ihn → Lass ihn gehen"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Beobachtung von Gedanken reduziert ihre Kraft",
                        "\"Danke für den Gedanken\" ist eine liebevolle Reaktion",
                        "Mit Übung wird das leichter"
                    )
                )
            )
        ),
        PsychoeducationModule(
            id = "values_alignment",
            title = "Werteorientiertes Leben",
            subtitle = "Lebe nach dem, was dir wirklich wichtig ist",
            icon = Icons.Default.FavoriteBorder,
            color = Color(0xFFEC4899),
            estimatedReadTime = 9,
            difficulty = "Alle",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Was sind Werte und warum sind sie wichtig?",
                    content = "Werte sind die tiefsten Dinge, die dir im Leben wichtig sind – nicht die Ziele selbst, sondern die Richtung.",
                    sections = listOf(
                        Section(
                            heading = "Werte vs. Ziele",
                            text = "Ein Ziel hat ein Ende (\"Mein Haus abbezahlen\"). Ein Wert ist eine kontinuierliche Richtung (\"Finanzielle Verantwortung\").",
                            examples = listOf(
                                "Ziel: \"Fit sein\" – Wert: \"Gesundheit und Selbstfürsorge\"",
                                "Ziel: \"Beziehung eingehen\" – Wert: \"Liebe und Intimität\""
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Warum Werteklärung transformativ ist",
                            text = "Wenn du weißt, was dir wichtig ist, hast du einen inneren Kompass. Jeder Tag kann werteorientiert gelebt werden.",
                            examples = listOf(
                                "Mit Werten: \"Ich mache diese Aufgabe, weil sie meinen Werten entspricht\"",
                                "Ohne Werte: \"Ich mache das, weil ich muss\" (Müdigkeit, Widerstand)"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Werte geben dir Richtung und Sinn",
                        "Ziele sind kurzfristig – Werte sind lebenslang",
                        "Werteorientiertes Handeln fühlt sich erfüllend an"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Deine Werte klären und aktivieren",
                    content = "Praktische Wege, um herauszufinden, was dir wirklich wichtig ist und es in die Tat umzusetzen.",
                    sections = listOf(
                        Section(
                            heading = "Die \"Aktueller Zustand vs. Idealer Zustand\"-Übung",
                            text = "Schreibe auf, wie du aktuell lebst vs. wie du gerne leben würdest. Die Unterschiede sind oft Hinweise auf deine Werte.",
                            examples = listOf(
                                "Aktuell: \"Ich bin zu beschäftigt zum Lachen\"",
                                "Ideal: \"Ich lache täglich und bin leicht\"",
                                "Wert: \"Freude und Leichtigkeit\""
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Werte in tägliche Handlungen übersetzen",
                            text = "Werte sind abstakt. Mache sie konkret durch kleine, tägliche Handlungen.",
                            examples = listOf(
                                "Wert: \"Familie\" → Aktion: \"Täglich 10 Minuten mit einem Familienmitglied verbringen\"",
                                "Wert: \"Kreativität\" → Aktion: \"30 Minuten Kreativität 3x pro Woche\""
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Werteklärung ist ein Prozess, kein einmaliges Event",
                        "Kleine werteorientierte Handlungen addieren sich",
                        "Wenn du in Krisen bist, halte dich an deine Werte – sie sind dein Anker"
                    )
                )
            )
        ),
        // ====== 6 GRUEBELN-MODULE ======
        PsychoeducationModule(
            id = "gruebeln_basics",
            title = "Grübeln: Gedankenkaugummi",
            subtitle = "[BEISPIEL] Verstehe Rumination und ihre Auswirkungen",
            icon = Icons.Default.School,
            color = Color(0xFFEC4899),
            estimatedReadTime = 15,
            difficulty = "Anfänger",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Was ist Grübeln?",
                    content = "Grübeln bedeutet, bildlich gesprochen, eine Sache immer wieder »durchzukauen«. Ein zentrales Verhaltensmuster, das viele Menschen betrifft.",
                    sections = listOf(
                        Section(
                            heading = "Die Definition von Rumination",
                            text = "In der Fachsprache nennt man Grübeln Rumination, ein Begriff, der aus dem Tierreich von den Wiederkäuern (z.B. Kühen) stammt. Grübeln kann man sich vorstellen, als würden wir auf unseren Gedanken Kaugummi kauen.",
                            examples = listOf(
                                "🧠 Wiederkäuen von Gedanken und Problemen",
                                "⏰ Anhaltende Fokussierung auf negative Inhalte",
                                "🔄 Schwierig, sich von diesen Gedanken zu befreien"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln ist ein automatisches, wiederholendes Denkmuster",
                        "Es fühlt sich an wie 'Gedankenkaugummi kauen'",
                        "Viele Menschen grübeln – es ist ein normales Phänomen"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Fokus: Vergangenheit & Zukunft",
                    content = "Grübeln beschäftigt sich mit zwei zeitlichen Dimensionen: belastender Vergangenheit und bedrohlicher Zukunft.",
                    sections = listOf(
                        Section(
                            heading = "Häufige Grübel-Themen",
                            text = "Inhaltlich befasst sich Grübeln mit vergangenen Ereignissen, die belastend waren, und zukünftigen Ereignissen, von denen angenommen wird, sie könnten bedrohlich sein.",
                            examples = listOf(
                                "✗ Negative Bewertungen der eigenen Person (Selbstkritik)",
                                "✗ Negative Vorstellungen über die Zukunft (Zukunftsängste)",
                                "✗ Negatives Denken über andere Menschen",
                                "✗ Angst vor negativen Bewertungen durch andere",
                                "✗ Selbstvorwürfe bezüglich vergangener Ereignisse",
                                "✗ Schamgefühle, Schuldgefühle, Ärger über Situationen"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln fixiert sich auf Vergangenheit oder Zukunft",
                        "Typische Themen sind Selbstkritik und Sorgen",
                        "Das Gegenwärtige wird oft übersehen"
                    )
                ),
                Chapter(
                    id = "ch3",
                    title = "Unproduktives Denken",
                    content = "Das paradoxe Problem von Grübeln: Es fühlt sich wie Problemlösen an, führt aber zu keinen Lösungen.",
                    sections = listOf(
                        Section(
                            heading = "Warum Grübeln ineffektiv ist",
                            text = "Grübeln beschäftigt sich wiederholt und andauernd mit negativen Inhalten. Es kann schwerfallen, sich von den Gedanken zu lösen. Das Denken ist unproduktiv, weil es weder zu einer Problemlösung noch zu einer Verbesserung der Situation beiträgt.",
                            examples = listOf(
                                "🔄 Gedanken drehen sich im Kreis",
                                "❌ Kein konkreter Handlungsplan entsteht",
                                "😔 Negative Stimmung verstärkt sich stattdessen"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Die Konsequenzen",
                            text = "Es fühlt sich vielleicht so an, als würde man etwas tun, um seine Probleme zu lösen. Doch leider verringert Grübeln die Fähigkeit, das Problem zu lösen.",
                            examples = listOf(
                                "→ Negative Stimmung, Hilflosigkeit, Hoffnungslosigkeit",
                                "→ Mehr negative Gedanken und Passivität folgen",
                                "→ Eine passiv-negative Haltung entsteht"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln fühlt sich produktiv an, ist es aber nicht",
                        "Es reduziert tatsächlich die Problemlösungsfähigkeit",
                        "Passive Haltung entsteht statt aktiv Probleme zu lösen"
                    )
                ),
                Chapter(
                    id = "ch4",
                    title = "Negative Folgen des Grübelns",
                    content = "Übermäßiges Grübeln hat weitreichende negative Konsequenzen für Körper, Geist und soziale Beziehungen.",
                    sections = listOf(
                        Section(
                            heading = "Emotionale & körperliche Auswirkungen",
                            text = "Übermäßiges Grübeln zieht verschiedene negative Folgen nach sich.",
                            examples = listOf(
                                "😔 Unangenehme Emotionen: Traurigkeit, Angst, Ärger bis Hoffnungslosigkeit, Panik",
                                "💪 Körperliche Zustände: Anspannung, Herzrasen, Schwitzen",
                                "😴 Erschöpfung, Antriebslosigkeit, Schlafstörungen",
                                "🧠 Konzentrationsschwierigkeiten"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Psychische & soziale Folgen",
                            text = "Langfristige Auswirkungen erstrecken sich auf mentale Gesundheit und Beziehungen.",
                            examples = listOf(
                                "🏥 Entstehung psychischer Erkrankungen (Depressionen, Angststörungen)",
                                "👥 Probleme im sozialen Umfeld (Unverständnis, Konflikte)"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln wirkt sich auf alle Bereiche aus: Körper, Geist, Beziehungen",
                        "Psychische Erkrankungen können dadurch aufrechterhalten werden",
                        "Soziale Spannungen entstehen durch wiederholtes Grübeln"
                    )
                ),
                Chapter(
                    id = "ch5",
                    title = "Grübeln ist normal – in Maßen",
                    content = "Grübeln ist ein verbreitetes, menschliches Phänomen. Der Unterschied liegt in Häufigkeit und Intensität.",
                    sections = listOf(
                        Section(
                            heading = "Gelegentliches vs. problematisches Grübeln",
                            text = "Grübeln ist ein normales, weitverbreitetes Phänomen. Wir alle grübeln gelegentlich. Auch gesunde Menschen grübeln in einem gewissen Umfang und Rahmen.",
                            examples = listOf(
                                "✓ Normal: Gelegentliches Grübeln, wenn Partnerschaft endet",
                                "✓ Normal: Sich Sorgen machen vor einem Vortrag",
                                "🟢 Gesund: Man kann die Gedanken wieder loslassen",
                                "🔴 Problematisch: Ständiges, unkontrollierbares Grübeln",
                                "🔴 Belastend: Beeinträchtigt die Lebensqualität"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Gelegentliches Grübeln ist vollkommen normal",
                        "Der Unterschied liegt in Häufigkeit und Kontrolle",
                        "Problematisches Grübeln beeinträchtigt die Lebensqualität"
                    )
                )
            )
        ),
        PsychoeducationModule(
            id = "denkstile_comparison",
            title = "Denkstile: Ungünstig vs. Günstig",
            subtitle = "[BEISPIEL] Lerne den Unterschied zwischen Denkmustern",
            icon = Icons.Default.School,
            color = Color(0xFF06B6D4),
            estimatedReadTime = 18,
            difficulty = "Fortgeschrittene",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Grübeln als ungünstige Gewohnheit",
                    content = "Grübeln gehört zu den negativen Denkschleifen und ist ein erlerntes Muster, das wieder verlernt werden kann.",
                    sections = listOf(
                        Section(
                            heading = "Automatisiert und unbewusst",
                            text = "Das Denken startet unabsichtlich und unbewusst. Es ist schwierig zu beeinflussen oder zu beenden. Oft laden Gedanken auch in Situationen los, in denen man es gar nicht will.",
                            examples = listOf(
                                "🧠 Unabsichtlich auftauchend",
                                "📍 In bestimmten Situationen automatisch ausgelöst (z.B. im Bett vor dem Einschlafen)",
                                "❌ Kaum kontrollierbar"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Sich wiederholend und springhaft",
                            text = "Das Denken kreist immer wieder um den gleichen Inhalt. Es verliert schnell den roten Faden und springt von einem Problem zum nächsten.",
                            examples = listOf(
                                "🔄 Kreist um dieselben Inhalte",
                                "💫 Springt sprunghaft zwischen Problemen",
                                "❌ Kein Problem wird befriedigend gelöst"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Negative, extreme, irrationale Inhalte",
                            text = "Die Inhalte des Denkens sehen nur negative Aspekte. Positive oder neutrale Aspekte werden ausgeblendet.",
                            examples = listOf(
                                "😔 Nur negative Aspekte sichtbar",
                                "🚫 Positive/Neutrale ausgeblendet",
                                "🎭 Tendenz zu Überbewertung und Katastrophisierung"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Unproduktiv und abstrakt",
                            text = "Es entsteht keine Lösung, kein Handlungsplan. Das Denken bleibt oberflächlich und verallgemeinernd, ohne konkrete Details zu betrachten.",
                            examples = listOf(
                                "❌ Keine Lösung oder Handlungsplan",
                                "🌫️ Bleibt abstrakt, ohne Details",
                                "📋 Verallgemeinert unzulässig ('immer', 'nie')"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln ist automatisiert und schwer kontrollierbar",
                        "Es ist eine erlernte Gewohnheit, keine Charaktereigenschaft",
                        "Gewohnheiten können wieder verlernt werden"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Konstruktives, günstiges Denken",
                    content = "Konstruktives Denken ist kontrollierbar, fokussiert und führt zu konkreten Lösungen.",
                    sections = listOf(
                        Section(
                            heading = "Kernmerkmale produktiven Denkens",
                            text = "Konstruktives Nachdenken lässt sich steuern und verfolgt Fragen in Richtung einer Lösung.",
                            examples = listOf(
                                "✅ Lässt sich steuern und bewusst gestalten",
                                "🎯 Verfolgt konkrete Lösungsschritte",
                                "💡 Führt zu Klarheit und Handlung"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Ausgewogen, rational und faktenbasiert",
                            text = "Die positiven und negativen Aspekte einer Situation werden gleichermäßen betrachtet. Gegenüber der eigenen Person besteht eine wohlwollende Haltung.",
                            examples = listOf(
                                "⚖️ Sowohl positive als auch negative Aspekte betrachtet",
                                "💭 Selbstmitgefühl statt Selbstkritik",
                                "📊 Faktenbasiert und rational"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Produktiv und konkret",
                            text = "Das Denken zielt auf eine Problemlösung ab. Es entstehen konkrete, schrittweise Lösungen und Handlungspläne.",
                            examples = listOf(
                                "✅ Konkrete Schritte identifizieren",
                                "📋 To-do-Listen statt To-not-do-Listen",
                                "🚀 Handlungsorientierung"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Flexibel und aktiv",
                            text = "Die Gedanken lassen sich bewusst lenken. Neue Informationen führen zu Anpassung von Zielen. Es besteht eine Balance zwischen Denken und Handeln.",
                            examples = listOf(
                                "🔄 Flexibel zwischen Perspektiven wechseln",
                                "🎯 Ziele basierend auf neuen Informationen anpassen",
                                "⚖️ Gute Balance zwischen Denken und Handeln"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Konstruktives Denken ist kontrollierbar und zielgerichtet",
                        "Es berücksichtigt mehrere Perspektiven",
                        "Es führt zu konkreten Handlungsschritten"
                    )
                ),
                Chapter(
                    id = "ch3",
                    title = "Der Unterschied in der Praxis",
                    content = "Praktische Beispiele zeigen, wie sich ungünstige und günstige Denkmuster unterscheiden.",
                    sections = listOf(
                        Section(
                            heading = "Beispiel: Ein Fehler bei der Arbeit",
                            text = "Vergleich zwischen Grübeln und konstruktivem Denken in einer realistischen Situation.",
                            examples = listOf(
                                "🔴 Grübeln: 'Warum mache ich immer alles falsch?' → Gedankenschleife → Verzweiflung",
                                "🟢 Konstruktiv: 'Was ist passiert? Wie kann ich es verbessern?' → Handlungsplan → Verbesserung"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Der Denkstil bestimmt das Ergebnis",
                        "Bewusstsein über das Denkmuster ist der erste Schritt",
                        "Veränderung ist durch wiederholte Übung möglich"
                    )
                )
            )
        ),
        PsychoeducationModule(
            id = "rnd_understanding",
            title = "RND: Verstehen & Regulieren",
            subtitle = "[BEISPIEL] Repetitives negatives Denken erkennen und regulieren",
            icon = Icons.Default.School,
            color = Color(0xFF8B5CF6),
            estimatedReadTime = 22,
            difficulty = "Fortgeschrittene",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Was ist RND?",
                    content = "Repetitives negatives Denken (RND) ist ein universeller psychologischer Prozess, der bei vielen psychischen Belastungen eine Rolle spielt.",
                    sections = listOf(
                        Section(
                            heading = "Definition & Merkmale",
                            text = "Repetitives negatives Denken bedeutet, dass sich Gedanken wiederholt mit negativen Inhalten beschäftigen und dabei als schwer steuerbar erlebt werden.",
                            examples = listOf(
                                "🔄 Wiederkehrend – Gedanken kreisen immer wieder um dasselbe Thema",
                                "😔 Negativ gefärbt – oft geht es um Fehler, Defizite, Gefahren",
                                "🚨 Aufdringlich – tauchen scheinbar automatisch auf",
                                "🌫️ Abstrakt – etwa in Form von 'Warum bin ich so?' oder 'Was, wenn alles schiefgeht?'",
                                "❌ Unkontrollierbar – fühlen sich wie innerer Zwang an"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Zwei Formen von RND",
                            text = "RND hat viele Ausprägungen, zwei sind besonders häufig und gut erforscht.",
                            examples = listOf(
                                "📖 Grübeln: Vergangenheitsbezogen (über Fehler, Kränkungen)",
                                "🔮 Sich-Sorgen: Zukunftsbezogen (über mögliche Gefahren)"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "RND ist ein universeller psychologischer Prozess",
                        "Es findet sich bei vielen verschiedenen psychischen Problemen",
                        "Zwei Hauptformen: Grübeln (Vergangenheit) und Sorgen (Zukunft)"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Die Funktionen und das Problem",
                    content = "RND erfüllt ursprünglich sinnvolle Funktionen – wird aber problematisch, wenn es exzessiv wird.",
                    sections = listOf(
                        Section(
                            heading = "Ursprüngliche Funktionen von RND",
                            text = "Grübeln und Sorgen haben ursprünglich sinnvolle Funktionen für unser Überleben.",
                            examples = listOf(
                                "⚠️ Warnfunktion: aufmerksam auf mögliche Gefahren machen",
                                "🎯 Relevanzfunktion: zeigen, dass etwas wichtig für uns ist",
                                "👁️ Aufmerksamkeitsfunktion: Aufmerksamkeit auf ein Problem lenken",
                                "🔧 Problemlösefunktion: Problemlösung anstoßen"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Wann wird es problematisch?",
                            text = "RND wird belastend und dysfunktional, wenn bestimmte Merkmale erfüllt sind.",
                            examples = listOf(
                                "⏱️ Es nimmt sehr viel Zeit ein",
                                "🔁 Es tritt gewohnheitsmäßig auf (z.B. abends im Bett)",
                                "🌫️ Es bleibt abstrakt und verallgemeinernd",
                                "❌ Es bringt keine konkreten Lösungen hervor",
                                "📈 Es führt zu emotionaler Verschlechterung"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Der Kreislauf des Grübelns",
                            text = "Ein sich selbst verstärkender Kreislauf entsteht, der RND perpetuiert.",
                            examples = listOf(
                                "1. Belastendes Ereignis oder Gefühl tritt auf",
                                "2. Nachdenken beginnt",
                                "3. Denken wird abstrakter ('Warum passiert mir das immer?')",
                                "4. Negative Gefühle nehmen zu",
                                "5. Bedürfnis zu analysieren wächst",
                                "6. Gedanken verstärken sich → Denken wird selbst zum Stressor"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "RND hat ursprünglich sinnvolle Funktionen",
                        "Problematisch wird es, wenn es exzessiv und unkontrollierbar ist",
                        "Ein sich selbst verstärkender Kreislauf entsteht"
                    )
                ),
                Chapter(
                    id = "ch3",
                    title = "Das realistische Ziel",
                    content = "Das Ziel ist nicht Gedankenvermeidung, sondern Denkregulation und mehr Flexibilität.",
                    sections = listOf(
                        Section(
                            heading = "Was ist kein realistisches Ziel?",
                            text = "Ein häufiges Missverständnis ist die Annahme, das Ziel müsse sein, 'nie wieder negativ zu denken'. Das ist weder möglich noch sinnvoll.",
                            examples = listOf(
                                "🚫 'Nie wieder negativ denken' - unmöglich",
                                "🚫 Gedanken komplett unterdrücken - kontraproduktiv",
                                "✓ Negative Gedanken gehören zum Menschsein"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Hilfreiche Ziele",
                            text = "Ein hilfreiches Ziel fokussiert auf Bewusstsein, Unterscheidung und Flexibilität.",
                            examples = listOf(
                                "👁️ Früher merken, wenn ich ins RND gerate",
                                "🔍 Zwischen hilfreichem und unhilfreichem Denken unterscheiden",
                                "🔄 Flexibler zwischen Denken und Handeln wechseln",
                                "🎯 Aufmerksamkeit bewusster steuern",
                                "💡 Es geht um Denkregulation, nicht Gedankenvermeidung"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Das Ziel ist nicht Gedankenvermeidung",
                        "Es geht um Denkregulation und Bewusstsein",
                        "Flexibilität ist der Schlüssel"
                    )
                )
            )
        ),
        PsychoeducationModule(
            id = "rumination_consequences",
            title = "Grübeln & Negative Folgen",
            subtitle = "[BEISPIEL] Verstehe die Auswirkungen von Rumination",
            icon = Icons.Default.School,
            color = Color(0xFFF43F5E),
            estimatedReadTime = 20,
            difficulty = "Anfänger",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Grübeln und Depression",
                    content = "Grübeln ist eng mit depressiven Verstimmungen verbunden und kann depressionäre Zustände aufrechterhalten.",
                    sections = listOf(
                        Section(
                            heading = "Die Beziehung zwischen Grübeln und Depression",
                            text = "Die Forschung zeigt, dass Grübeln besonders eng mit depressiven Verstimmungen verbunden ist.",
                            examples = listOf(
                                "😢 Menschen mit depressiven Symptomen grübeln stark",
                                "🔄 Sie denken über ihre Niedergeschlagenheit, deren Ursachen nach",
                                "📚 Dies trägt aber nicht zur Verbesserung bei – Stimmung bleibt negativ oder verschlechtert sich"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Wichtig zu verstehen",
                            text = "Grübeln verursacht keine Depression, aber es kann ein wichtiger Mechanismus sein, der depressive Zustände aufrechthält.",
                            examples = listOf(
                                "⚡ Es ist nicht einfach 'negatives Denken' ",
                                "🔄 Grübeln verhindert Heilung durch Perpetuierung",
                                "✓ Wenn wir Grübeln reduzieren, kann sich Stimmung verbessern"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln ist eng mit Depression verbunden",
                        "Es hält depressive Zustände aufrecht",
                        "Reduktion von Grübeln kann Verbesserung ermöglichen"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Auswirkungen auf Emotionen und Körper",
                    content = "Grübeln hat weitreichende Konsequenzen, die Emotionen, Körpergefühle und Funktionsfähigkeit beeinträchtigen.",
                    sections = listOf(
                        Section(
                            heading = "Das Verstärkungs-Netzwerk",
                            text = "Unser Gedächtnis verbindet Gedanken, Gefühle, Körperreaktionen und Erinnerungen miteinander. Grübeln aktiviert diese Netzwerke.",
                            examples = listOf(
                                "🧠 Trauriger Gedanke aktiviert wird",
                                "🔗 Weitere traurige Erinnerungen werden leicht aktiviert",
                                "📚 Weitere negative Bewertungen folgen",
                                "⚖️ Stimmung kann sich deutlich verschlechtern"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Körperliche Folgen",
                            text = "Grübeln hat auch körperliche Auswirkungen, die das Wohlbefinden beeinträchtigen.",
                            examples = listOf(
                                "💪 Anspannung, Muskelverspannungen",
                                "❤️ Herzrasen, Blutdruckerhöhung",
                                "😓 Schwitzen",
                                "😴 Schlafprobleme und Konzentrationsschwierigkeiten",
                                "😑 Antriebslosigkeit und Erschöpfung"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln verstärkt negative Gefühle durch Netzwerkeffekte",
                        "Es hat auch körperliche Auswirkungen",
                        "Ein Teufelskreis aus Körper und Geist entsteht"
                    )
                ),
                Chapter(
                    id = "ch3",
                    title = "Auswirkungen auf Handeln und Beziehungen",
                    content = "Grübeln führt zu Handlungsblockaden und Beziehungsproblemen, die die Situation verschärfen.",
                    sections = listOf(
                        Section(
                            heading = "Handlungsblockade",
                            text = "Ein großes Problem ist die Balance zwischen Denken und Handeln. Grübeln kann dazu führen, dass sehr viel Energie ins Analysieren fließt.",
                            examples = listOf(
                                "🧠 Viel Energie ins Denken",
                                "📉 Wenig in konkrete Schritte",
                                "📚 Je länger gedanklich bei Problem verweilt, desto überwältigender es wird",
                                "❌ Motivation und Handlungsbereitschaft sinken"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Auswirkungen auf Beziehungen",
                            text = "Grübeln betrifft nicht nur die innere Welt, sondern auch das soziale Umfeld.",
                            examples = listOf(
                                "🔄 Immer wieder dieselben Themen besprechen",
                                "❓ Häufige Rückversicherungen einfordern",
                                "😔 Lösungsvorschläge nicht umsetzen",
                                "😞 Angehörige fühlen sich belastet",
                                "💔 Betroffene fühlen sich unverstanden"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln führt zu Handlungsblockade",
                        "Es hat auch Auswirkungen auf Beziehungen",
                        "Ein Multiplikator-Effekt kann entstehen"
                    )
                )
            )
        ),
        PsychoeducationModule(
            id = "gruebeln_as_habit",
            title = "Grübeln als Gewohnheit",
            subtitle = "[BEISPIEL] Lerne, Grübeln als trainierbare Gewohnheit zu verstehen",
            icon = Icons.Default.School,
            color = Color(0xFF14B8A6),
            estimatedReadTime = 18,
            difficulty = "Fortgeschrittene",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Grübeln ist erlernt",
                    content = "Grübeln ist nicht angeboren – es ist eine erlernte, oft automatisierte Strategie, die verändert werden kann.",
                    sections = listOf(
                        Section(
                            heading = "Was ist eine Gewohnheit?",
                            text = "Eine Gewohnheit ist ein automatisches Verhaltensmuster, das unabsichtlich und unbewusst ausgelöst wird.",
                            examples = listOf(
                                "🚗 Auto fahren – unbewusst",
                                "🧘 Meditieren – wurde eingeübt",
                                "🚭 Nägelkauen – automatische Reaktion auf Stress"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Wie entsteht eine Denkgewohnheit?",
                            text = "Grübeln entwickelt sich als Gewohnheit durch wiederholte Verknüpfung zwischen Situation und Reaktion.",
                            examples = listOf(
                                "😔 Stimmung wird traurig oder unsicher",
                                "🧠 Automatisch beginnt man zu grübeln",
                                "🔁 Dies wiederholt sich immer wieder",
                                "⚡ Mit Zeit reicht nur die Stimmung, um Grübeln auszulösen",
                                "🤖 Die Verbindung wird automatisch und schnell"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln ist eine erlernte Gewohnheit, nicht angeboren",
                        "Sie entsteht durch wiederholte Verknüpfung",
                        "Gewohnheiten können verändert werden"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Gewohnheiten können verändert werden",
                    content = "Das Wichtigste: Wenn Grübeln gelernt wurde, kann man auch etwas Neues lernen.",
                    sections = listOf(
                        Section(
                            heading = "Das entlastende Verständnis",
                            text = "Diese Perspektive kann transformativ sein. Grübeln ist nicht Ihre Identität – es ist ein Muster.",
                            examples = listOf(
                                "🚫 NICHT: 'Ich bin ein Grübler' (Identität)",
                                "✅ SONDERN: 'Ich habe mir Grübeln angewöhnt' (Gewohnheit)",
                                "🎯 Und Gewohnheiten können verändert werden!"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Das realistischeZiel: Flexibilität",
                            text = "Das Ziel ist nicht, Grübeln vollständig zu stoppen, sondern flexibler zu werden.",
                            examples = listOf(
                                "👁️ Grübeln früher erkennen",
                                "⚡ Den automatischen Ablauf unterbrechen",
                                "🔄 Schneller in hilfreicheres Denken wechseln",
                                "🎯 Alternative Reaktionen einüben, bis sie selbst zur Gewohnheit werden"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Gegenkonditionierung: Der Schlüssel",
                            text = "Wenn Grübeln eine Gewohnheit ist, hilft es, auf Auslöser neue Reaktionen zu trainieren.",
                            examples = listOf(
                                "😔 Wenn Niedergeschlagenheit auftritt → bewusst kleine Aktivität beginnen",
                                "🔄 Wenn Gedanken kreisen → in konkretes Problemlösen wechseln",
                                "😰 Wenn innere Anspannung entsteht → Entspannungsübung durchführen"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Grübeln ist nicht Ihre Identität",
                        "Neue Gewohnheiten entstehen durch wiederholtes Üben",
                        "Gegenkonditionierung ist der Schlüssel zur Veränderung"
                    )
                )
            )
        ),
        PsychoeducationModule(
            id = "rnd_contents",
            title = "RND: Inhalte & Prozesse",
            subtitle = "[BEISPIEL] Der Prozess ist ähnlich, die Inhalte unterschiedlich",
            icon = Icons.Default.School,
            color = Color(0xFFA78BFA),
            estimatedReadTime = 20,
            difficulty = "Fortgeschrittene",
            chapters = listOf(
                Chapter(
                    id = "ch1",
                    title = "Der gemeinsame Prozess",
                    content = "So unterschiedlich die Themen von RND sind – der zugrunde liegende Prozess ist meist ähnlich.",
                    sections = listOf(
                        Section(
                            heading = "Ein verbreitetes Muster",
                            text = "Menschen unterscheiden sich weniger darin, dass sie RND haben, sondern eher darin, worüber sie es tun.",
                            examples = listOf(
                                "🔄 Wiederholtes Kreisen um negative Inhalte",
                                "🎯 Starke Selbst- oder Gefahrenfokussierung",
                                "❓ Abstrakte 'Warum'- oder 'Was-wäre-wenn'-Fragen",
                                "📈 Zunehmende emotionale Aktivierung",
                                "📉 Abnehmende Handlungsorientierung"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Warum ist diese Unterscheidung hilfreich?",
                            text = "Wenn wir verstehen, dass es um den Prozess geht, nicht um die Inhalte, entsteht neue Hoffnung.",
                            examples = listOf(
                                "✓ Sie müssen nicht jedes Thema einzeln 'lösen'",
                                "✓ Sie können den Denkstil selbst verändern",
                                "✓ Strategien zur Unterbrechung sind ähnlich, unabhängig vom Thema"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "Alle RND teilen einen ähnlichen Prozess",
                        "Die Inhalte unterscheiden sich, der Mechanismus ist gleich",
                        "Veränderung des Prozesses hilft bei allen Themen"
                    )
                ),
                Chapter(
                    id = "ch2",
                    title = "Unterschiedliche Inhalte von RND",
                    content = "RND kann sich auf sehr verschiedene Lebensbereiche beziehen. Hier sind typische Schwerpunkte.",
                    sections = listOf(
                        Section(
                            heading = "Selbstbezogenes Grübeln",
                            text = "Hier stehen häufig Selbstbewertungen im Mittelpunkt.",
                            examples = listOf(
                                "❓ 'Warum bin ich so?'",
                                "❓ 'Was stimmt nicht mit mir?'",
                                "❓ 'Warum wird es nie besser?'",
                                "📍 Fokus: Schwächen, Fehler, Vergleiche mit anderen"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Zukunftsbezogenes Sorgen",
                            text = "Die Frage 'Was, wenn...?' steht im Vordergrund.",
                            examples = listOf(
                                "❓ 'Was wäre, wenn etwas Schlimmes passiert?'",
                                "❓ 'Was ist, wenn ich das nicht schaffe?'",
                                "🎯 Häufige Themen: Familie, Arbeit, Gesundheit, Sicherheit",
                                "💭 Denken springt von einer möglichen Gefahr zur nächsten"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Soziale Selbstbeobachtung",
                            text = "Gedanken kreisen um soziale Situationen und Bewertung durch andere.",
                            examples = listOf(
                                "❓ 'Habe ich komisch gewirkt?'",
                                "❓ 'Was denken die anderen über mich?'",
                                "📍 Post-Event-Grübeln nach sozialen Situationen",
                                "😰 Angst vor Ablehnung und Blamage"
                            ),
                            isExpandable = true
                        ),
                        Section(
                            heading = "Körper- und gesundheitsbezogenes Sorgen",
                            text = "Aufmerksamkeit richtet sich intensiv auf körperliche Empfindungen.",
                            examples = listOf(
                                "❓ 'Was bedeutet dieses Symptom?'",
                                "❓ 'Was, wenn das etwas Ernstes ist?'",
                                "📍 Körperliche Signale werden intensiv beobachtet",
                                "🔍 Kleine Veränderungen können stark vergrößert werden"
                            ),
                            isExpandable = true
                        )
                    ),
                    keyTakeaways = listOf(
                        "RND hat viele verschiedene thematische Formen",
                        "Der Prozess bleibt aber ähnlich",
                        "Alle lassen sich durch Prozessveränderung beeinflussen"
                    )
                )
            )
        )
    )
}

// ============== UI-Komponenten ==============

@Composable
fun PsychoeducationScreen(onBack: () -> Unit) {
    var selectedModuleId by rememberSaveable { mutableStateOf<String?>(null) }
    val modules = remember { getPsychoeducationModules() }

    Crossfade(targetState = selectedModuleId, label = "ModuleTransition") { moduleId ->
        if (moduleId == null) {
            PsychoeducationListScreen(
                modules = modules,
                onSelectModule = { selectedModuleId = it },
                onBack = onBack
            )
        } else {
            val module = modules.find { it.id == moduleId }
            if (module != null) {
                PsychoeducationDetailScreen(
                    module = module,
                    onBack = { selectedModuleId = null }
                )
            }
        }
    }
}

@Composable
private fun PsychoeducationListScreen(
    modules: List<PsychoeducationModule>,
    onSelectModule: (String) -> Unit,
    onBack: () -> Unit
) {
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
            Text(
                text = "Psychoedukation",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Box(modifier = Modifier.size(40.dp)) // Platzhalter für Balance
        }

        // Intro Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Verstehe die Psychologie hinter deinen Erfahrungen",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Jedes Modul ist mit wissenschaftlichen Erkenntnissen fundiert und praktischen Beispielen gestaltet.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Module List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)
        ) {
            items(modules) { module ->
                ModuleCard(
                    module = module,
                    onClick = { onSelectModule(module.id) }
                )
            }
        }
    }
}

@Composable
private fun ModuleCard(module: PsychoeducationModule, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Beispiel-Badge wenn isExample = true
            if (module.isExample) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFEF08A), // Gelb
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        )
                        .padding(vertical = 6.dp, horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "⚡ BEISPIEL-MODUL",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF713F00) // Dunkelbraun für Kontrast
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Farbiger Icon-Kreis
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(module.color, module.color.copy(alpha = 0.7f))),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Inhalte
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = module.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DifficultyBadge(module.difficulty)
                        Text(
                            text = "${module.estimatedReadTime} min",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: String) {
    val color = when (difficulty) {
        "Anfänger" -> Color(0xFF10B981)
        "Fortgeschrittene" -> Color(0xFFF59E0B)
        else -> Color(0xFF6B7280)
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.1f), shape = RoundedCornerShape(6.dp))
            .border(1.dp, color, RoundedCornerShape(6.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            text = difficulty,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun PsychoeducationDetailScreen(
    module: PsychoeducationModule,
    onBack: () -> Unit
) {
    var selectedChapterIndex by rememberSaveable { mutableIntStateOf(0) }
    var isBookmarked by rememberSaveable { mutableStateOf(module.isBookmarked) }
    var showRatingScreen by rememberSaveable { mutableStateOf(false) }

    val selectedChapter = module.chapters[selectedChapterIndex]

    if (showRatingScreen) {
        // RATING SCREEN (separate Seite nach "Fertig" Klick)
        RatingScreen(
            module = module,
            onRatingSubmitted = onBack,
            onBack = { showRatingScreen = false }
        )
    } else {
        // NORMALE LESESEITE
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Header mit Progress
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                module.color.copy(alpha = 0.1f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = {
                        isBookmarked = !isBookmarked
                        module.isBookmarked = isBookmarked
                    }) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Speichern",
                            tint = if (isBookmarked) module.color else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { (selectedChapterIndex + 1) / module.chapters.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = module.color
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Kapitel ${selectedChapterIndex + 1} von ${module.chapters.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Kapitel-Titel
                Text(
                    text = selectedChapter.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Kapitel-Einführung
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = module.color.copy(alpha = 0.08f)
                    )
                ) {
                    Text(
                        text = selectedChapter.content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Sections
                selectedChapter.sections.forEach { section ->
                    ExpandableSection(section = section, moduleColor = module.color)
                }

                // Key Takeaways
                KeyTakeawaysCard(
                    takeaways = selectedChapter.keyTakeaways,
                    moduleColor = module.color
                )
            }

            // Navigation Buttons
            val isLastChapter = selectedChapterIndex == module.chapters.size - 1

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (selectedChapterIndex > 0) selectedChapterIndex--
                    },
                    modifier = Modifier.weight(1f),
                    enabled = selectedChapterIndex > 0
                ) {
                    Text("← Zurück")
                }

                if (isLastChapter) {
                    // Letztes Kapitel: Zeige "Fertig" Button
                    Button(
                        onClick = { showRatingScreen = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("✓ Fertig")
                    }
                } else {
                    // Normale Kapitel: Zeige "Weiter" Button
                    Button(
                        onClick = {
                            if (selectedChapterIndex < module.chapters.size - 1) {
                                selectedChapterIndex++
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedChapterIndex < module.chapters.size - 1
                    ) {
                        Text("Weiter →")
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableSection(section: Section, moduleColor: Color) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    Column {
        if (section.isExpandable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = section.heading,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = moduleColor
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = moduleColor
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300, easing = LinearOutSlowInEasing)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = moduleColor.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = section.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface // Besserer Kontrast
                    )

                    if (section.examples.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            section.examples.forEach { example ->
                                ExampleBox(example = example, moduleColor = moduleColor)
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = section.heading,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = moduleColor
                )
                Text(
                    text = section.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface // Besserer Kontrast
                )

                if (section.examples.isNotEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        section.examples.forEach { example ->
                            ExampleBox(example = example, moduleColor = moduleColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExampleBox(example: String, moduleColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(moduleColor, shape = CircleShape)
                    .align(Alignment.Top)
                    .padding(top = 4.dp)
            )
            Text(
                text = example,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface, // Besserer Kontrast
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KeyTakeawaysCard(takeaways: List<String>, moduleColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = moduleColor.copy(alpha = 0.08f)
        ),
        border = androidx.compose.material3.CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = moduleColor.copy(alpha = 0.3f)
            ).brush
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = moduleColor,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Wichtige Erkenntnisse",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = moduleColor
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                takeaways.forEach { takeaway ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(moduleColor, shape = CircleShape)
                        )
                        Text(
                            text = takeaway,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface // Besserer Kontrast
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RatingButton(
    rating: ModuleRating,
    isSelected: Boolean,
    moduleColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = if (isSelected) moduleColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) moduleColor else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = rating.label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) moduleColor else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RatingScreen(
    module: PsychoeducationModule,
    onRatingSubmitted: () -> Unit,
    onBack: () -> Unit
) {
    var selectedRating by rememberSaveable { mutableStateOf<ModuleRating?>(null) }
    var showFeedback by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ============ CELEBRATION SECTION ============
        Spacer(modifier = Modifier.height(40.dp))

        // Animated Celebration Emoji
        Text(
            text = "🎉",
            style = MaterialTheme.typography.displayMedium.copy(fontSize = 80.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Main Headline - Psychologisch positiv formuliert
        Text(
            text = "Großartig gemacht!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = module.color,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Supportive Subheading - Growth Mindset
        Text(
            text = "Du hast gerade etwas Wichtiges für deine\npersönliche Entwicklung getan.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // ============ ACHIEVEMENT CARD ============
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = module.color.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Achievement Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = module.color.copy(alpha = 0.2f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = module.color,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Modul abgeschlossen",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = module.color
                        )
                        Text(
                            text = module.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Divider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(module.color.copy(alpha = 0.2f))
                )

                // Module Stats - Drei Spalten Layout
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatItem(
                        icon = "⏱️",
                        label = "Zeit",
                        value = "${module.estimatedReadTime} Min",
                        modifier = Modifier.weight(1f)
                    )
                    StatItem(
                        icon = "📚",
                        label = "Kapitel",
                        value = "${module.chapters.size}",
                        modifier = Modifier.weight(1f)
                    )
                    StatItem(
                        icon = "📊",
                        label = "Schwierigkeit",
                        value = module.difficulty,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ============ REFLECTION SECTION ============
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Psychologically Positive Question
            Text(
                text = "Was hat dich an diesem Modul geholfen?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Explanation - Validating user experience
            Text(
                text = "Dein Feedback hilft uns, die Inhalte noch besser auf deine Bedürfnisse abzustimmen und dir in Zukunft noch mehr zu helfen.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ============ INNOVATIVE RATING SYSTEM ============
            // Rating Scale mit Labels - Reframed für positive Psychologie
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Scale Labels
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nicht\nwirklich",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        fontSize = 10.sp,
                        modifier = Modifier.width(50.dp)
                    )
                    Text(
                        text = "Sehr\nhilfreich",
                        style = MaterialTheme.typography.labelSmall,
                        color = module.color,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 10.sp,
                        modifier = Modifier.width(50.dp)
                    )
                }

                // Rating Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ModuleRating.values().forEach { rating ->
                        RatingButtonImproved(
                            modifier = Modifier.weight(1f),
                            rating = rating,
                            isSelected = selectedRating == rating,
                            moduleColor = module.color,
                            onClick = {
                                selectedRating = rating
                                module.rating = rating
                                showFeedback = true
                            }
                        )
                    }
                }
            }

            // ============ ANIMATED FEEDBACK MESSAGE ============
            AnimatedVisibility(
                visible = showFeedback && selectedRating != null,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Positive reinforcement message
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFD1FAE5) // Light green
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "✓ Danke für dein ehrliches Feedback!",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                            Text(
                                text = "Das hilft uns, dir noch bessere Unterstützung zu bieten.",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF047857)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // ============ BOTTOM ACTION BUTTONS ============
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Primary Button - enabled nur mit Rating
            Button(
                onClick = onRatingSubmitted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = selectedRating != null,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = module.color,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "✓ Fertig & Weiter",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            // Secondary Button - optional back
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("← Zurück")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun StatItem(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 24.sp
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun RatingButtonImproved(
    modifier: Modifier = Modifier,
    rating: ModuleRating,
    isSelected: Boolean,
    moduleColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (isSelected) moduleColor.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            )
            .border(
                width = if (isSelected) 2.5.dp else 1.5.dp,
                color = if (isSelected) moduleColor else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = rating.label,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) moduleColor else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp
        )
        Text(
            text = getRatingLabel(rating),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) moduleColor else MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontSize = 8.sp
        )
    }
}

private fun getRatingLabel(rating: ModuleRating): String {
    return when (rating) {
        ModuleRating.VERY_NEGATIVE -> "Nicht\nwirklich"
        ModuleRating.NEGATIVE -> "Wenig\nhilfreich"
        ModuleRating.NEUTRAL -> "Okay"
        ModuleRating.POSITIVE -> "Hilf-\nreich"
        ModuleRating.VERY_POSITIVE -> "Sehr\nhilf-\nreich"
    }
}
