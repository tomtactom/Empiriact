package com.empiriact.app.ui.screens.resources

/**
 * INTEGRATION GUIDE - Psychoeducation Module
 *
 * Dieses Paket enthält drei Haupt-Screens für psychoedukative Inhalte:
 * 1. PsychoeducationScreen - Lernmodule mit Kapiteln
 * 2. InteractiveExercisesScreen - Geführte Übungen
 * 3. ResourceBrowserScreen - Ressourcen-Bibliothek
 * 4. LearningPathScreen - Strukturierte Lernwege
 *
 * ============================================================
 * BASIC USAGE
 * ============================================================
 *
 * In deinem Navigation Graph:
 *
 * ```kotlin
 * composable(
 *     route = "psychoeducation/{moduleId}",
 *     arguments = listOf(navArgument("moduleId") { type = NavType.StringType })
 * ) { backStackEntry ->
 *     val moduleId = backStackEntry.arguments?.getString("moduleId")
 *     PsychoeducationScreen(
 *         onBack = { navController.popBackStack() }
 *     )
 * }
 *
 * composable("exercises") {
 *     InteractiveExercisesScreen(
 *         onBack = { navController.popBackStack() }
 *     )
 * }
 *
 * composable("resources") {
 *     ResourceBrowserScreen(
 *         onBack = { navController.popBackStack() }
 *     )
 * }
 *
 * composable("learning-path") {
 *     LearningPathScreen(
 *         onBack = { navController.popBackStack() }
 *     )
 * }
 * ```
 *
 * ============================================================
 * FEATURES ÜBERBLICK
 * ============================================================
 *
 * ### PsychoeducationScreen
 * - 4 vollständig entwickelte Module:
 *   1. Emotionsregulation (Anfänger, 8 min)
 *   2. Angststörungen verstehen (Fortgeschrittene, 10 min)
 *   3. Kognitive Defusion (Anfänger, 7 min)
 *   4. Werteorientiertes Leben (Alle Levels, 9 min)
 *
 * - Jedes Modul hat:
 *   * 2-3 Kapitel mit progressivem Aufbau
 *   * Expandierbare Sektionen für tiefe Information
 *   * Konkrete Beispiele und Anwendungsszenarien
 *   * "Wichtige Erkenntnisse" am Ende jedes Kapitels
 *
 * - UX Features:
 *   * Vertikale Navigation mit Vor/Zurück-Buttons
 *   * Progress-Bar zeigt Kapitel-Fortschritt
 *   * Farbcodierung nach Modul-Typ
 *   * Icon-Set für schnelle Erkennung
 *   * Smooth animations beim Expandieren/Zusammenziehen
 *
 * ### InteractiveExercisesScreen
 * - 3 praktische, zeitgesteuerter Übungen:
 *   1. 5-4-3-2-1 Erdungsübung (5 min)
 *   2. Progressive Muskelentspannung (10 min)
 *   3. Gedanken-Etikettierung (7 min)
 *
 * - Für jede Übung:
 *   * Preview-Screen mit Details vor dem Start
 *   * Step-by-Step Anleitung mit Timer
 *   * Kontextbezogene Tipps und Weisungen
 *   * Benefis und Lernziele klar kommuniziert
 *
 * - UX Features:
 *   * Großer visueller Schritt-Indikator (Nummer in Kreis)
 *   * Farbcodierte Info-Boxen (Anleitung, Tipps)
 *   * Leichte Navigation zwischen Schritten
 *   * Abbruch-Option jederzeit
 *   * Satisfying completion-Feedback
 *
 * ### ResourceBrowserScreen
 * - 10+ kuratierte psychologische Ressourcen
 * - Filterbare Ansicht nach:
 *   * Kategorie (Angststörungen, Achtsamkeit, etc.)
 *   * Schwierigkeitsgrad (Anfänger/Fortgeschrittene)
 *   * Ressourcentyp (Artikel, Übung, Fragebogen)
 *   * Suchtext (Volltextsuche)
 *
 * - Features:
 *   * Favoritensystem (Bookmark)
 *   * Zeitschätzung sichtbar
 *   * View-Mode Toggle (Grid/List)
 *   * Empty State mit Hilfetext
 *
 * ### LearningPathScreen
 * - Strukturierte Lernwege:
 *   * Angstabbau 101 (4 Module)
 *   * Emotionale Bewältigung (5 Module)
 *   * Wertorientiertes Leben (5 Module)
 *
 * - Features:
 *   * Gesamtfortschritt mit Prozentangabe
 *   * Pro Pfad: Progress-Tracking
 *   * Personalisierte nächste Schritte
 *   * Visuelle Schwierigkeitsgrade
 *
 * ============================================================
 * CUSTOMIZATION
 * ============================================================
 *
 * ### Neue Module hinzufügen
 *
 * 1. In getPsychoeducationModules():
 * ```kotlin
 * PsychoeducationModule(
 *     id = "custom_module",
 *     title = "Dein Modul-Titel",
 *     subtitle = "Kurzbeschreibung",
 *     icon = Icons.Default.YourIcon,
 *     color = Color(0xFFXXXXXX),
 *     estimatedReadTime = 8,
 *     difficulty = "Anfänger",
 *     chapters = listOf(
 *         Chapter(
 *             id = "ch1",
 *             title = "Kapiteltitel",
 *             content = "Einführungstext",
 *             sections = listOf(
 *                 Section(
 *                     heading = "Überschrift",
 *                     text = "Inhaltstext",
 *                     examples = listOf("Beispiel 1", "Beispiel 2"),
 *                     isExpandable = true
 *                 )
 *             ),
 *             keyTakeaways = listOf("Erkenntnisse...")
 *         )
 *     )
 * )
 * ```
 *
 * ### Neue Übungen hinzufügen
 *
 * In getInteractiveExercises():
 * ```kotlin
 * InteractiveExercise(
 *     id = "custom_exercise",
 *     title = "Übungstitel",
 *     description = "Was macht diese Übung?",
 *     duration = 10,
 *     difficulty = "Anfänger",
 *     category = "Kategorie",
 *     steps = listOf(
 *         ExerciseStep(
 *             number = 1,
 *             title = "Schritt 1",
 *             instruction = "Anweisung",
 *             duration = 60,
 *             guidance = "Anleitung",
 *             tips = listOf("Tipp 1", "Tipp 2")
 *         )
 *     ),
 *     benefits = listOf("Nutzen 1", "Nutzen 2")
 * )
 * ```
 *
 * ============================================================
 * STYLING & BRANDING
 * ============================================================
 *
 * Das Modul verwendet ein konsistentes Farbschema:
 * - Primary: MaterialTheme.colorScheme.primary (für Buttons, Highlights)
 * - Container: MaterialTheme.colorScheme.primaryContainer (für Hintergründe)
 * - Surface: MaterialTheme.colorScheme.surface (für Karten)
 *
 * Modul-spezifische Farben (können angepasst werden):
 * - Emotionsregulation: #6366F1 (Indigo)
 * - Angststörungen: #F59E0B (Bernstein)
 * - Defusion: #10B981 (Grün)
 * - Werte: #EC4899 (Pink)
 *
 * ============================================================
 * PERFORMANCE NOTES
 * ============================================================
 *
 * - LazyColumn ist für große Listen optimiert
 * - remember{} cacht Daten zwischen Recompositions
 * - rememberSaveable{} erhält State bei Config-Changes
 * - Crossfade nutzt effiziente Transitions
 *
 * Wenn du viel mehr Inhalte hinzufügst:
 * 1. Überprüfe, dass LazyColumn für Listen genutzt wird
 * 2. Paginariere große Datensets
 * 3. Nutze Coil für Image Loading (falls Bilder hinzugefügt)
 *
 * ============================================================
 * TESTING
 * ============================================================
 *
 * Beispiel für Preview:
 * ```kotlin
 * @Preview
 * @Composable
 * fun PsychoeducationScreenPreview() {
 *     PsychoeducationScreen(onBack = {})
 * }
 *
 * @Preview
 * @Composable
 * fun InteractiveExercisesScreenPreview() {
 *     InteractiveExercisesScreen(onBack = {})
 * }
 *
 * @Preview
 * @Composable
 * fun ResourceBrowserScreenPreview() {
 *     ResourceBrowserScreen(onBack = {})
 * }
 * ```
 *
 * ============================================================
 * HÄUFIGE FRAGEN
 * ============================================================
 *
 * Q: Wie kann ich Backend-Integration hinzufügen?
 * A: Ersetze die in-memory Listen mit Room Database oder API-Aufrufe.
 *    Speichere Favoriten, Fortschritt und Nutzer-Antworten dort ab.
 *
 * Q: Kann ich die Timer deaktivieren?
 * A: Ja, entferne einfach die TimerBox oder mache sie optional.
 *    Nutzer können Schritte mit einem Button überspringen.
 *
 * Q: Wie kann ich Quiz/Tests hinzufügen?
 * A: Füge ein neues Datenmodell \"Quiz\" hinzu und zeige nach Modulen
 *    ein Quiz-Screen mit Multiple-Choice-Fragen.
 *
 * Q: Kann ich Bilder/Videos hinzufügen?
 * A: Ja, ergänze die Datenmodelle mit URL-Feldern und nutze
 *    AsyncImage (von Coil) oder Video-Composables.
 *
 * ============================================================
 */

// Diese Datei dient nur der Dokumentation.
// Die eigentliche Implementation ist in den anderen Dateien.

