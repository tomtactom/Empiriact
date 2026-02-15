package com.empiriact.app.ui.screens.resources.builder

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 🏗️ PSYCHOEDUCATION MODULE BUILDER SYSTEM
 *
 * Ein flexibles Baukastensystem zur Erstellung neuer psychoedukativer Module.
 * Folge diesem Schema, um neue Module schnell und konsistent zu erstellen.
 *
 * VERWENDUNG:
 * 1. Kopiere dieses Template
 * 2. Ersetze die Placeholder-Werte
 * 3. Verwende PsychoeducationModuleBuilder.createModule()
 * 4. Das Modul wird automatisch registriert
 */

// ============================================================
// SCHRITT 1: DEFINIERE DEIN MODUL
// ============================================================

data class PsychoeducationModuleDefinition(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val estimatedReadTime: Int, // in Minuten
    val difficulty: String, // "Anfänger" oder "Fortgeschrittene"
    val category: String, // z.B. "Angststörungen", "Entspannung"
    val chapters: List<ChapterDefinition>
)

data class ChapterDefinition(
    val id: String,
    val title: String,
    val content: String, // Intro-Text für das Kapitel
    val sections: List<SectionDefinition>,
    val keyTakeaways: List<String> // 3-5 wichtige Erkenntnisse
)

data class SectionDefinition(
    val heading: String,
    val text: String,
    val examples: List<String> = emptyList(),
    val isExpandable: Boolean = false // Wenn true: Content ist zusammenklappbar
)

// ============================================================
// SCHRITT 2: MODULE BUILDER (Hilfsfunktionen)
// ============================================================

/**
 * Helper-Funktionen zur Erstellung von Modulen
 * Nutze diese statt direkter Konstruktor-Aufrufe für Konsistenz
 */
object PsychoeducationModuleBuilder {

    /**
     * Erstelle ein neues Modul mit allen erforderlichen Eigenschaften
     * @return Ein vollständig konfiguriertes Modul-Objekt
     */
    fun createModule(
        id: String,
        title: String,
        subtitle: String,
        icon: ImageVector,
        color: Color,
        estimatedReadTime: Int,
        difficulty: String,
        category: String,
        chaptersBuilder: () -> List<ChapterDefinition>
    ): PsychoeducationModuleDefinition {
        return PsychoeducationModuleDefinition(
            id = id,
            title = title,
            subtitle = subtitle,
            icon = icon,
            color = color,
            estimatedReadTime = estimatedReadTime,
            difficulty = difficulty,
            category = category,
            chapters = chaptersBuilder()
        )
    }

    /**
     * Erstelle ein Kapitel mit strukturiertem Format
     */
    fun createChapter(
        id: String,
        title: String,
        content: String,
        sectionsBuilder: () -> List<SectionDefinition>,
        takeawaysBuilder: () -> List<String>
    ): ChapterDefinition {
        return ChapterDefinition(
            id = id,
            title = title,
            content = content,
            sections = sectionsBuilder(),
            keyTakeaways = takeawaysBuilder()
        )
    }

    /**
     * Erstelle eine expandierbare Sektion (Zusammenklappbar)
     */
    fun createExpandableSection(
        heading: String,
        text: String,
        examples: List<String> = emptyList()
    ): SectionDefinition {
        return SectionDefinition(
            heading = heading,
            text = text,
            examples = examples,
            isExpandable = true
        )
    }

    /**
     * Erstelle eine statische Sektion (Immer sichtbar)
     */
    fun createStaticSection(
        heading: String,
        text: String,
        examples: List<String> = emptyList()
    ): SectionDefinition {
        return SectionDefinition(
            heading = heading,
            text = text,
            examples = examples,
            isExpandable = false
        )
    }
}

// ============================================================
// SCHRITT 3: TEMPLATE - KOPIERE FÜR NEUE MODULE
// ============================================================

/**
 * TEMPLATE für neue Module
 * Kopiere diese Funktion und passe die Werte an!
 */
fun createMyNewModule(): PsychoeducationModuleDefinition {
    return PsychoeducationModuleBuilder.createModule(
        id = "my_module_id", // Eindeutige ID, z.B. "stress_management"
        title = "Mein Modul Titel", // Kurz und aussagekräftig
        subtitle = "Eine kurze Beschreibung des Moduls",
        icon = Icons.Default.School, // Wähle ein aussagekräftiges Icon
        color = Color(0xFF6366F1), // Modul-Farbe (Hex-Code)
        estimatedReadTime = 8, // Geschätzte Lesedauer in Minuten
        difficulty = "Anfänger", // "Anfänger" oder "Fortgeschrittene"
        category = "Kategorie", // z.B. "Angststörungen", "Entspannung"

        chaptersBuilder = {
            listOf(
                // KAPITEL 1
                PsychoeducationModuleBuilder.createChapter(
                    id = "ch1",
                    title = "Kapitel 1: Grundlagen",
                    content = "Einführungstext, der das Thema beschreibt...",

                    sectionsBuilder = {
                        listOf(
                            // Expandierbare Sektion
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Was ist das Thema?",
                                text = "Erklärung des Konzepts...",
                                examples = listOf(
                                    "Beispiel 1: ...",
                                    "Beispiel 2: ..."
                                )
                            ),
                            // Statische Sektion
                            PsychoeducationModuleBuilder.createStaticSection(
                                heading = "Warum ist das wichtig?",
                                text = "Bedeutung des Themas...",
                                examples = listOf(
                                    "Aspekt 1: ...",
                                    "Aspekt 2: ..."
                                )
                            )
                        )
                    },

                    takeawaysBuilder = {
                        listOf(
                            "Wichtige Erkenntnis 1",
                            "Wichtige Erkenntnis 2",
                            "Wichtige Erkenntnis 3"
                        )
                    }
                ),

                // KAPITEL 2
                PsychoeducationModuleBuilder.createChapter(
                    id = "ch2",
                    title = "Kapitel 2: Praktische Anwendung",
                    content = "Wie man das Gelernte anwendet...",

                    sectionsBuilder = {
                        listOf(
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Erste Schritte",
                                text = "Was zu tun ist...",
                                examples = listOf("Schritt 1", "Schritt 2")
                            )
                        )
                    },

                    takeawaysBuilder = {
                        listOf(
                            "Takeaway 1",
                            "Takeaway 2",
                            "Takeaway 3"
                        )
                    }
                )
            )
        }
    )
}

// ============================================================
// SCHRITT 4: BEST PRACTICES & RICHTLINIEN
// ============================================================

/**
 * BEST PRACTICES FÜR MODULE:
 *
 * 1. STRUKTUR
 *    - 2-3 Kapitel pro Modul
 *    - Jedes Kapitel: 2-4 Sektionen
 *    - Jede Sektion: Max. 200 Wörter
 *    - 3-5 Key Takeaways pro Kapitel
 *
 * 2. SCHWIERIGKEITSGRAD
 *    "Anfänger" - Grundlagen, leicht verständlich
 *    "Fortgeschrittene" - Tiefere Konzepte, vorwissen nötig
 *
 * 3. FARBEN
 *    Verwende eine konsistente Farbe pro Modul.
 *    Farben sollten unterscheidbar sein:
 *    - 0xFF6366F1 = Indigo (Emotionsregulation)
 *    - 0xFFF59E0B = Bernstein (Angststörungen)
 *    - 0xFF10B981 = Grün (Defusion)
 *    - 0xFFEC4899 = Pink (Werte)
 *    - 0xFF3B82F6 = Blau (Neue Module)
 *
 * 4. BEISPIELE
 *    - Nutze 2-3 konkrete Beispiele pro Sektion
 *    - Beispiele sollten relatable sein
 *    - Format: "Situation: ... → Gedanke: ... → Emotion: ..."
 *
 * 5. SPRACHE
 *    - Nutze "du"-Form (persönlich)
 *    - Kurze, prägnante Sätze
 *    - Aktive Stimme bevorzugen
 *    - Vermeide Jargon oder erkläre es
 *
 * 6. PSYCHOLOGISCHER GEHALT
 *    - Evidenzbasiert (Forschung/Therapie)
 *    - Praktisch anwendbar
 *    - Nicht moralisierend
 *    - Selbstmitgefühl-fokussiert
 *
 * 7. EXPANDIERBARE SEKTIONEN
 *    - Nutze für: Tiefergehende Konzepte, Optionale Info
 *    - Haupttext sollte auch ohne Expansion verständlich sein
 *    - Expandable = isExpandable: true
 *
 * 8. KEY TAKEAWAYS
 *    - 3-5 pro Kapitel
 *    - Kurz & prägnant (max. 1 Satz)
 *    - Zusammenfassung der Hauptpunkte
 *    - Sollte zum Merken sein
 */

// ============================================================
// SCHRITT 5: INTEGRATION IN DEIN PROJEKT
// ============================================================

/**
 * ANLEITUNG ZUM HINZUFÜGEN EINES NEUEN MODULS:
 *
 * 1. DEFINE MODUL:
 *    - Erstelle eine neue Funktion createMyNewModule() (wie das Template)
 *    - Oder kopiere die Beispiele aus createMyNewModule()
 *
 * 2. ADD ZU LISTE:
 *    - Gehe zu PsychoeducationScreen.kt
 *    - Finde die Funktion getPsychoeducationModules()
 *    - Füge dein Modul zur Liste hinzu:
 *      ```
 *      return listOf(
 *          createMyNewModule(),  // ← HINZUFÜGEN
 *          // ...bestehende Module...
 *      )
 *      ```
 *
 * 3. TEST:
 *    - Kompiliere: ./gradlew compileDebugKotlin
 *    - Öffne App → Module Tab
 *    - Dein neues Modul sollte erscheinen!
 *
 * 4. OPTIONAL - ROUTES:
 *    - Wenn du ein neues Screen/Feature hinzufügst,
 *      registriere es in Route.kt und EmpiriactNavGraph.kt
 */

// ============================================================
// SCHRITT 6: BEISPIEL - NEUES MODUL "SCHLAFHYGIENE"
// ============================================================

fun createSleepHygieneModule(): PsychoeducationModuleDefinition {
    return PsychoeducationModuleBuilder.createModule(
        id = "sleep_hygiene",
        title = "Schlafhygiene",
        subtitle = "Besserer Schlaf durch bewusste Gewohnheiten",
        icon = Icons.Default.School,
        color = Color(0xFF3B82F6), // Neuer Blau-Ton
        estimatedReadTime = 6,
        difficulty = "Anfänger",
        category = "Schlaf",

        chaptersBuilder = {
            listOf(
                PsychoeducationModuleBuilder.createChapter(
                    id = "ch1",
                    title = "Was ist Schlafhygiene?",
                    content = "Schlafhygiene bezieht sich auf Gewohnheiten und Umgebungsfaktoren, die förderlich für guten Schlaf sind.",

                    sectionsBuilder = {
                        listOf(
                            PsychoeducationModuleBuilder.createStaticSection(
                                heading = "Definition",
                                text = "Schlafhygiene ist die Praxis, Verhaltensweisen und Umgebungen zu etablieren, die konsistenten, hochqualitativen Schlaf fördern."
                            ),
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Warum ist Schlafhygiene wichtig?",
                                text = "Schlechter Schlaf beeinflusst deine mentale Gesundheit, Energie und Entscheidungsfähigkeit.",
                                examples = listOf(
                                    "Mangel an Schlaf → Schwierigkeit, Emotionen zu regulieren",
                                    "Besserer Schlaf → Verbesserte Angstbewältigung"
                                )
                            )
                        )
                    },

                    takeawaysBuilder = {
                        listOf(
                            "Schlafhygiene ist trainierbar und verbesserbar",
                            "Kleine Änderungen können große Effekte haben",
                            "Konsistenz ist wichtiger als Perfektion"
                        )
                    }
                ),

                PsychoeducationModuleBuilder.createChapter(
                    id = "ch2",
                    title = "Praktische Tipps für besseren Schlaf",
                    content = "Konkrete Strategien, die du heute umsetzen kannst.",

                    sectionsBuilder = {
                        listOf(
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Schlafenszeit-Routine",
                                text = "Eine regelmäßige Routine 30 Minuten vor dem Schlafengehen.",
                                examples = listOf(
                                    "Licht dimmen",
                                    "Bildschirme ausschalten",
                                    "Entspannungstechniken nutzen"
                                )
                            ),
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Umgebungsfaktoren",
                                text = "Dein Schlafzimmer sollte dunkel, kühl und ruhig sein.",
                                examples = listOf(
                                    "Temperatur: 16-19°C",
                                    "Dunkelheit: Keine Lichtquellen",
                                    "Lärm: Ruhig oder weißes Rauschen"
                                )
                            )
                        )
                    },

                    takeawaysBuilder = {
                        listOf(
                            "Routine schafft Gewohnheiten",
                            "Deine Umgebung beeinflusst deinen Schlaf",
                            "Geduld: Es dauert 2-4 Wochen, Verbesserungen zu sehen"
                        )
                    }
                )
            )
        }
    )
}

// ============================================================
// CHECKLISTE VOR VERÖFFENTLICHUNG
// ============================================================

/**
 * CHECKLISTE FÜR NEUE MODULE:
 *
 * INHALTE:
 * ☐ Mindestens 2 Kapitel
 * ☐ Jedes Kapitel hat mindestens 2 Sektionen
 * ☐ 3-5 Key Takeaways pro Kapitel
 * ☐ Mindestens 2-3 Beispiele pro Sektion
 * ☐ Texte sind max. 200 Wörter pro Sektion
 * ☐ Sprache ist klar und verständlich
 *
 * DESIGN:
 * ☐ Eindeutige, aussagekräftige Farbe gewählt
 * ☐ Icon ist relevant zum Thema
 * ☐ Titel ist prägnant (2-3 Wörter)
 * ☐ Subtitle ist aussagekräftig (5-10 Wörter)
 *
 * PSYCHOLOGIE:
 * ☐ Inhalte sind evidenzbasiert
 * ☐ Praktische Anwendung vorhanden
 * ☐ Sprache ist nicht moralisierend
 * ☐ Selbstmitgefühl wird gefördert
 *
 * TECHNISCH:
 * ☐ IDs sind eindeutig und lowercase
 * ☐ Modul ist in getPsychoeducationModules() hinzugefügt
 * ☐ Build kompiliert ohne Fehler
 * ☐ In der App sichtbar und funktionsfähig
 *
 * TESTING:
 * ☐ Alle Sektionen sind lesbar
 * ☐ Expandierbare Sektionen funktionieren
 * ☐ Text ist auf allen Geräten lesbar
 * ☐ Navigation funktioniert (Vor/Zurück zwischen Kapiteln)
 */

