package com.empiriact.app.ui.common

/**
 * Accessibility Content Descriptions
 * Zentrale Verwaltung von Beschreibungen für Screen Readers
 * Hilft bei der Konsistenz und macht Code wartbar
 */
object ContentDescriptions {
    // Navigation
    const val BACK_BUTTON = "Zurück"
    const val SETTINGS_BUTTON = "Einstellungen öffnen"
    const val PROFILE_BUTTON = "Profil öffnen"
    const val CLOSE_DIALOG = "Dialog schließen"
    const val CLOSE_MENU = "Menü schließen"

    // Common Actions
    const val SAVE = "Speichern"
    const val DELETE = "Löschen"
    const val EDIT = "Bearbeiten"
    const val ADD = "Hinzufügen"
    const val CANCEL = "Abbrechen"
    const val CONFIRM = "Bestätigen"
    const val SUBMIT = "Absenden"
    const val EXPORT = "Exportieren"

    // Exercise & Activity Related
    const val START_EXERCISE = "Übung starten"
    const val NEXT_STEP = "Nächster Schritt"
    const val PREVIOUS_STEP = "Vorheriger Schritt"
    const val PAUSE_TIMER = "Timer pausieren"
    const val RESUME_TIMER = "Timer fortsetzen"
    const val STOP_EXERCISE = "Übung beenden"

    // Status & Feedback
    const val LOADING = "Lädt Daten..."
    const val ERROR_OCCURRED = "Ein Fehler ist aufgetreten"
    const val SUCCESS = "Erfolgreich abgeschlossen"
    const val DISMISS_ERROR = "Fehlermeldung ausblenden"

    // Timer & Time Related
    const val TIMER_RUNNING = "Timer läuft"
    const val TIMER_PAUSED = "Timer pausiert"
    const val TIME_REMAINING = "Verbleibende Zeit"

    // Buttons & Controls
    const val EXPAND_SECTION = "Bereich ausklappen"
    const val COLLAPSE_SECTION = "Bereich einklappen"
    const val TOGGLE_SWITCH = "Schalter umschalten"

    // Icons (Generic)
    const val ICON_INFO = "Informationssymbol"
    const val ICON_WARNING = "Warnsymbol"
    const val ICON_SUCCESS = "Erfolgssymbol"
    const val ICON_ERROR = "Fehlersymbol"
    const val ICON_HEART = "Herzsymbol"
    const val ICON_STAR = "Sternsymbol"

    // User Input
    const val MOOD_SELECTOR = "Stimmung auswählen"
    const val ACTIVITY_INPUT = "Aktivität eingeben"
    const val RATING_SLIDER = "Bewertung schieben"
    const val DROPDOWN_MENU = "Menü öffnen"

    // Values & Categories
    const val SELECT_VALUE = "Wert auswählen"
    const val IMPORTANCE_LEVEL = "Wichtigkeitsstufe"
    const val IMPLEMENTATION_LEVEL = "Umsetzungsstufe"
}

/**
 * Accessibility Extensions für häufige Muster
 */

/**
 * Gibt eine aussagekräftige Beschreibung basierend auf dem Wert zurück
 */
fun getValenceDescription(valence: Int): String {
    return when (valence) {
        in 1..2 -> "Sehr negativ"
        in 3..4 -> "Negativ"
        in 5..6 -> "Neutral"
        in 7..8 -> "Positiv"
        in 9..10 -> "Sehr positiv"
        else -> "Neutral"
    }
}

/**
 * Gibt eine Emoji-basierte Stimmungs-Beschreibung zurück
 */
fun getValenceEmoji(valence: Int): String {
    return when (valence) {
        in 1..2 -> "😢"
        in 3..4 -> "☹️"
        in 5..6 -> "😐"
        in 7..8 -> "🙂"
        in 9..10 -> "😄"
        else -> "😐"
    }
}

/**
 * Kombiniert Emoji und Beschreibung
 */
fun formatValenceWithEmoji(valence: Int): String {
    return "${getValenceEmoji(valence)} ${getValenceDescription(valence)}"
}

/**
 * Format für Stunden im HH:MM Format
 */
fun formatHourRange(hour: Int): String {
    return String.format("%02d:00 - %02d:59", hour, hour)
}

/**
 * Format für Minuten und Sekunden (MM:SS)
 */
fun formatTimerDisplay(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
