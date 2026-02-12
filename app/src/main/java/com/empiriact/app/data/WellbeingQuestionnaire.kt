package com.empiriact.app.data

import androidx.compose.ui.graphics.vector.ImageVector

// 1. DATENMODELL: Eine umfassende Struktur für Fragebögen

/**
 * Definiert eine einzelne Frage (Item) innerhalb eines Fragebogens.
 * @param id Eindeutige ID der Frage.
 * @param text Der Fragetext, der dem Nutzer angezeigt wird.
 */
data class QuestionnaireItem(
    val id: String,
    val text: String,
)

/**
 * Definiert die Optionen für eine Likert-Skala.
 * @param lowAnchor Beschriftung für den niedrigsten Wert (z. B. "Trifft gar nicht zu").
 * @param highAnchor Beschriftung für den höchsten Wert (z. B. "Trifft voll und ganz zu").
 * @param steps Die Anzahl der Antwortmöglichkeiten (z. B. 5 für eine 5-stufige Skala).
 */
data class LikertScale(
    val lowAnchor: String,
    val highAnchor: String,
    val steps: Int = 5
)

/**
 * Definiert die Interpretation eines erreichten Punktwerts.
 * @param scoreRange Der Punktebereich, für den diese Interpretation gilt.
 * @param title Eine kurze, prägnante Überschrift (z. B. "Niedriges Wohlbefinden").
 * @param interpretation Eine detaillierte, VT-sensitive Erklärung der Bedeutung.
 * @param recommendation Eine konstruktive, aktivierende Empfehlung.
 */
data class ScoreInterpretation(
    val scoreRange: IntRange,
    val title: String,
    val interpretation: String,
    val recommendation: String
)

/**
 * Das vollständige Datenmodell für einen Fragebogen.
 * @param id Eindeutige ID des Fragebogens.
 * @param title Der Titel des Fragebogens.
 * @param description Eine kurze Beschreibung, die in der Vorschau angezeigt wird.
 * @param icon Ein optionales Icon für die Vorschau.
 * @param instruction Eine detaillierte Anleitung für den Nutzer vor Beginn.
 * @param items Die Liste der Fragen.
 * @param scale Die für diesen Fragebogen verwendete Antwortskala.
 * @param interpretations Eine Liste der möglichen Auswertungen.
 */
data class WellbeingQuestionnaire(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector? = null,
    val instruction: String,
    val items: List<QuestionnaireItem>,
    val scale: LikertScale,
    val interpretations: List<ScoreInterpretation>
)
