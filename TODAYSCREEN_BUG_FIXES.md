# TodayScreen Bug Fixes - Logische Überarbeitung

## Problem-Analyse

Der `HourEntry`-Composable hatte mehrere kritische Bugs, die verhinderten, dass Daten dauerhaft in der Datenbank gespeichert wurden:

### 1. **Zustand-Initialisierungsfehler**
- Die Variablen wurden mit `remember { mutableStateOf("") }` ohne Keys initialisiert
- Dies führte dazu, dass beim Öffnen einer Stunde **immer leere Werte geladen wurden**
- Die `LaunchedEffect` versuchte später, die Daten zu laden, aber es gab Race Conditions

### 2. **Inkonsistentes Cache-Management**
- Der `onCacheChanged`-Callback wurde an vielen Stellen aufgerufen, aber mit unterschiedlichen/unvollständigen Parametern
- Das Cache-System war unzuverlässig und verlor Daten

### 3. **Auto-Save beim Schließen funktionierte nicht**
- Die `LaunchedEffect` versuchte, Daten zu speichern, wenn `isExpanded = false` wurde
- Dies war aber zu früh und mit fehlerhafter Validierung

## Implementierte Lösungen

### 1. **Richtige Zustand-Initialisierung mit Keys**
```kotlin
var activityInputText by remember(hour, isExpanded) { mutableStateOf(initialActivityInput) }
var activityChips by remember(hour, isExpanded) { mutableStateOf(initialChips) }
var valence by remember(hour, isExpanded) { mutableStateOf(initialValence) }
var peopleInputText by remember(hour, isExpanded) { mutableStateOf(initialPeopleInput) }
var peopleChips by remember(hour, isExpanded) { mutableStateOf(initialPeopleChips) }
```

**Ziele:**
- Mit den Keys `(hour, isExpanded)` wird der Zustand **neu initialisiert**, wenn sich die Stunde oder der Expanded-Status ändert
- Die Initialwerte werden **korrekt aus Cache oder Log geladen**
- Keine Race Conditions mehr

### 2. **Zentralisiertes Cache-Update**
```kotlin
val updateCache = {
    onCacheChanged(
        HourEntryCache(
            activities = activityChips,
            valence = valence,
            inputText = activityInputText,
            peopleText = "",
            peopleChips = peopleChips,
            peopleInputText = peopleInputText
        )
    )
}
```

**Ziele:**
- **Eine einzige Quelle der Wahrheit** für Cache-Updates
- Alle Parameter werden konsistent gefüllt
- Keine doppelten oder unvollständigen Updates mehr

### 3. **Explizite Speichern/Abbrechen-Logik**
```kotlin
val handleSave = {
    if (activityChips.isNotEmpty()) {
        val activityText = activityChips.joinToString(", ")
        val peopleText = if (peopleChips.isNotEmpty()) peopleChips.joinToString(", ") else ""
        onSave(activityText, valence, peopleText)
    }
    onExpand()  // Collapse after save
}

val handleCancel = {
    // Reset to original state and collapse
    activityInputText = initialActivityInput
    activityChips = initialChips
    valence = initialValence
    peopleInputText = initialPeopleInput
    peopleChips = initialPeopleChips
    onExpand()
}
```

**Ziele:**
- **Explizites Speichern** - Der Benutzer klickt "Speichern" und nur dann werden Daten gespeichert
- **Abbrechen funktioniert** - Bei Klick auf "Abbrechen" werden Änderungen verworfen und zu ursprünglichen Werten zurückgekehrt
- **Keine unbeabsichtigten Auto-Saves** - Vermeidet Datenverlust durch automatische Speicherung

## Benutzerfluss (Intuitive UX)

### Szenario 1: Neue Aktivität eingeben und speichern
1. Benutzer klickt auf eine Stunde
2. Gibt 1-3 Aktivitäten ein
3. Wählt Stimmung (Emoji)
4. Gibt optional Personen ein
5. Klickt **"Speichern"**
6. ✅ Daten werden in Datenbank gespeichert → Stunde collpased
7. Beim nächsten Öffnen sind die Daten noch da

### Szenario 2: Änderungen verwerfen
1. Benutzer klickt auf eine Stunde (hat bereits Daten)
2. Verändert die Aktivitäten
3. Klickt **"Abbrechen"**
4. ✅ Alle Änderungen werden verworfen
5. Ursprüngliche Daten bleiben erhalten

### Szenario 3: Teilweise Eingabe + Schließen (neue Logik)
1. Benutzer klickt auf eine Stunde
2. Gibt einige Daten ein aber **klickt nicht "Speichern"**
3. Klickt auf eine andere Stunde
4. Die Daten sind **nicht verloren** - sie sind im Cache gespeichert
5. Wenn er die erste Stunde erneut öffnet, sind seine Eingaben noch da

## Technische Verbesserungen

| Aspekt | Vorher | Nachher |
|--------|--------|---------|
| **State Initialization** | Immer leer, Race Condition | Mit Keys, deterministische Initialisierung |
| **Cache Updates** | Doppelt, unterschiedliche Parameter | Zentralisiert, konsistent |
| **Auto-Save** | Unklar, fehlerhaft | Entfernt, explizites Speichern |
| **Cancel-Button** | Setzt nur lokale Variablen | Resettet zu ursprünglichen Werten |
| **Data Persistence** | Nicht dauerhaft | ✅ Dauerhaft nach "Speichern" |

## Verbleibende Tests

- [ ] Daten eingeben → Speichern → App schließen → App öffnen → Daten vorhanden?
- [ ] Mehrere Stunden mit verschiedenen Daten → Jede richtig speichern/laden?
- [ ] Abbrechen-Button → Änderungen wirklich verworfen?
- [ ] Personen → Sind sie in der Datenbank?
- [ ] Cache → Funktioniert das bei schnellem Wechsel zwischen Stunden?

