# 🎨 Empiriact UI Components & Utilities - Developer Guide

**Version:** 1.0  
**Status:** Production Ready ✅

---

## 📚 Inhaltsverzeichnis

1. [Design Tokens (Dimensions)](#design-tokens)
2. [Reusable Components](#reusable-components)
3. [Error Handling](#error-handling)
4. [Accessibility Helpers](#accessibility-helpers)
5. [Enhanced Buttons](#enhanced-buttons)
6. [Verwendungsbeispiele](#verwendungsbeispiele)

---

## Design Tokens

**Datei:** `com.empiriact.app.ui.theme.Dimensions`

### Spacing
```kotlin
Dimensions.spacingSmall     // 8dp
Dimensions.spacingMedium    // 16dp
Dimensions.spacingLarge     // 24dp
Dimensions.spacing2/4/12/20/32/48  // Weitere Größen
```

### Padding
```kotlin
Dimensions.paddingSmall     // 8dp
Dimensions.paddingMedium    // 16dp
Dimensions.paddingLarge     // 24dp
```

### Touch Targets
```kotlin
Dimensions.minTouchTarget   // 48dp (Material Design Standard)
Dimensions.buttonHeight     // 48dp
Dimensions.buttonHeightSmall // 40dp
```

### Corner Radius
```kotlin
Dimensions.cornerRadiusSmall   // 8dp
Dimensions.cornerRadiusMedium  // 12dp
Dimensions.cornerRadiusLarge   // 16dp
```

### Icons
```kotlin
Dimensions.iconSizeSmall    // 20dp
Dimensions.iconSizeMedium   // 24dp
Dimensions.iconSizeLarge    // 32dp
Dimensions.iconSizeXLarge   // 48dp
```

### Verwendungsbeispiel
```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(Dimensions.paddingMedium),
    verticalArrangement = Arrangement.spacedBy(Dimensions.spacingMedium)
) {
    // Content
}
```

---

## Reusable Components

**Datei:** `com.empiriact.app.ui.common.ReusableComponents`

### 1. ErrorBanner
Zeigt Fehler mit Dismiss-Option an.

```kotlin
ErrorBanner(
    message = "Fehler beim Speichern",
    onDismiss = { /* dismiss */ }
)
```

**Features:**
- Automatisches Fade-In/Out
- Dismissible mit Button
- Rote Fehlerfarbe aus Theme
- Error-Icon

---

### 2. LoadingIndicator
Zeigt Loading State mit Text an.

```kotlin
LoadingIndicator(
    message = "Speichert Daten...",
    modifier = Modifier.fillMaxSize()
)
```

**Features:**
- Zentriert positioniert
- CircularProgressIndicator
- Beschreibender Text

---

### 3. ConfirmationDialog
Dialog für wichtige Bestätigungen.

```kotlin
ConfirmationDialog(
    title = "Aktivität löschen?",
    message = "Diese Aktion kann nicht rückgängig gemacht werden",
    onConfirm = { /* delete */ },
    onCancel = { /* close */ },
    confirmButtonText = "Löschen",
    cancelButtonText = "Abbrechen"
)
```

**Features:**
- Zwei konfigurierbare Buttons
- Material Design 3 Styling
- Klare Information Architecture

---

### 4. StepProgressIndicator
Zeigt Fortschritt in Multi-Step Prozessen.

```kotlin
StepProgressIndicator(
    currentStep = 3,
    totalSteps = 5
)
```

**Features:**
- Visuelle Fortschrittsbalken
- Text "Schritt 3 von 5"
- Gefüllte/ungefüllte Segmente

---

### 5. TimerDisplay
Große Timer-Anzeige mit Status-Feedback.

```kotlin
TimerDisplay(
    timeString = "02:30",
    isRunning = true,
    modifier = Modifier.fillMaxWidth()
)
```

**Features:**
- Großer, gut lesbarer Text
- Farbliche Unterscheidung (läuft vs. fertig)
- Status-Text ("Timer läuft..." oder "Fertig!")

**Replaces:** 4 verschiedene Timer-Implementierungen

---

### 6. InputValidationFeedback
Zeigt Validierungsfehler an.

```kotlin
InputValidationFeedback(
    isValid = isInputValid,
    errorMessage = "Fehler: Text zu lang"
)
```

**Features:**
- Nur sichtbar wenn ungültig
- Animierte Sichtbarkeit
- Rote Fehlerfarbe

---

## Error Handling

**Datei:** `com.empiriact.app.ui.common.ErrorHandling`

### ErrorState
Zentrale Fehlerverwaltung.

```kotlin
// In ViewModel oder Composable
val errorState = ErrorState()

// Fehler anzeigen
errorState.showError("Fehler beim Laden")

// Fehler löschen
errorState.clearError()

// Auto-Löschen nach 3 Sekunden
errorState.clearErrorAfterDelay(coroutineScope)
```

---

### tryCatch Helper
Vereinfachtes Error Handling in Funktionen.

```kotlin
val result = tryCatch(
    block = { viewModel.saveData() },
    errorMessage = "Fehler beim Speichern",
    onError = { errorState.showError(it) }
)
```

---

### safeLaunch
Sichere Coroutine Execution.

```kotlin
coroutineScope.safeLaunch(
    errorHandler = { errorState.showError(it) }
) {
    // Async Code hier
    val data = fetchDataFromDb()
}
```

---

## Accessibility Helpers

**Datei:** `com.empiriact.app.ui.common.AccessibilityHelpers`

### Content Descriptions
30+ zentral verwaltete Beschreibungen.

```kotlin
Icon(
    Icons.Default.Settings,
    contentDescription = ContentDescriptions.SETTINGS_BUTTON
)

// Alle verfügbaren:
ContentDescriptions.BACK_BUTTON
ContentDescriptions.START_EXERCISE
ContentDescriptions.TIMER_RUNNING
ContentDescriptions.DISMISS_ERROR
// ... und 26 weitere
```

---

### Formatting Utilities

#### getValenceDescription()
```kotlin
getValenceDescription(7) // "Positiv"
getValenceDescription(2) // "Sehr negativ"
```

#### getValenceEmoji()
```kotlin
getValenceEmoji(7) // "🙂"
getValenceEmoji(2) // "😢"
```

#### formatValenceWithEmoji()
```kotlin
formatValenceWithEmoji(7) // "🙂 Positiv"
```

#### formatTimerDisplay()
```kotlin
formatTimerDisplay(150) // "02:30"
```

#### formatHourRange()
```kotlin
formatHourRange(14) // "14:00 - 14:59"
```

---

## Enhanced Buttons

**Datei:** `com.empiriact.app.ui.common.EnhancedButtons`

### 1. ActionButton
Button mit Loading/Success/Error States.

```kotlin
ActionButton(
    text = "Speichern",
    onClick = { /* save */ },
    isLoading = isSaving,
    isSuccess = savedSuccessfully,
    isError = saveFailed
)
```

**States:**
- Normal: Text
- Loading: Spinner + Text
- Success: ✓ + "Erfolgreich!"
- Error: ✗ + "Fehler"

---

### 2. ActionButtonWithFeedback
Async Button mit automatischem Feedback.

```kotlin
ActionButtonWithFeedback(
    text = "Hochladen",
    onClick = suspend { uploadFile() }
)
```

**Features:**
- Auto-Handling von Loading State
- Success für 2 Sekunden angezeigt
- Error für 3 Sekunden angezeigt

---

### 3. IconTonalButton
Tonal Button mit Icon.

```kotlin
IconTonalButton(
    text = "Bearbeiten",
    onClick = { /* edit */ },
    icon = Icons.Default.Edit
)
```

---

### 4. RatingButton
Für 1-5 Bewertungen.

```kotlin
RatingButton(
    rating = 3,
    maxRating = 5,
    onRatingChange = { newRating -> /* update */ }
)
```

**Features:**
- Visuelle Buttons für jede Bewertung
- Gefüllte Buttons für ausgewählte Werte
- Einfaches Tap-Interface

---

### 5. ConfirmButton
Für destruktive Aktionen.

```kotlin
var isConfirming by remember { mutableStateOf(false) }

ConfirmButton(
    text = "Löschen",
    onClick = { /* delete */ },
    isConfirming = isConfirming,
    onConfirmingChange = { isConfirming = it }
)
```

**Features:**
- Double-Confirmation Pattern
- Text ändert sich ("Nochmal tippen zum Bestätigen")
- Rote Farbe bei Bestätigung

---

## Verwendungsbeispiele

### Beispiel 1: Activity Planner mit Validation
```kotlin
@Composable
fun ActivityPlannerScreen(valueName: String) {
    var newActivityText by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf("") }
    
    val maxActivityLength = 150
    val isInputValid = newActivityText.isNotBlank() && 
                       newActivityText.length <= maxActivityLength

    Column(modifier = Modifier.padding(Dimensions.paddingMedium)) {
        OutlinedTextField(
            value = newActivityText,
            onValueChange = { newText ->
                newActivityText = newText
                validationError = when {
                    newText.isEmpty() -> ""
                    newText.length > maxActivityLength -> "Zu lang"
                    else -> ""
                }
            },
            isError = validationError.isNotEmpty(),
            supportingText = { Text("${newActivityText.length}/$maxActivityLength") }
        )
        
        InputValidationFeedback(
            isValid = validationError.isEmpty(),
            errorMessage = validationError
        )
        
        Button(
            onClick = { /* add */ },
            enabled = isInputValid
        ) {
            Text("Hinzufügen")
        }
    }
}
```

---

### Beispiel 2: Exercise mit Timer
```kotlin
@Composable
fun ExerciseScreen() {
    var timeRemaining by remember { mutableStateOf(30) }
    var isRunning by remember { mutableStateOf(false) }
    
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeRemaining > 0) {
                delay(1000)
                timeRemaining--
            }
            isRunning = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.paddingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerDisplay(
            timeString = formatTimerDisplay(timeRemaining),
            isRunning = isRunning
        )
        
        Spacer(modifier = Modifier.height(Dimensions.spacingLarge))
        
        ActionButton(
            text = if (isRunning) "Pausieren" else "Starten",
            onClick = { isRunning = !isRunning }
        )
    }
}
```

---

### Beispiel 3: Error Handling
```kotlin
@Composable
fun DataScreen() {
    val viewModel = viewModel<DataViewModel>()
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        tryCatch(
            block = { viewModel.loadData() },
            errorMessage = "Fehler beim Laden",
            onError = { errorMessage = it }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ErrorBanner(
            message = errorMessage,
            onDismiss = { errorMessage = "" }
        )
        
        // Content
    }
}
```

---

## Best Practices

1. **Verwende immer Dimensions statt Magic Numbers**
   ```kotlin
   ✅ Dimensions.paddingMedium
   ❌ 16.dp
   ```

2. **Nutze ContentDescriptions aus Accessibility Helpers**
   ```kotlin
   ✅ ContentDescriptions.START_EXERCISE
   ❌ "Button"
   ```

3. **Verwende TimerDisplay statt eigene Implementierung**
   ```kotlin
   ✅ TimerDisplay(timeString, isRunning)
   ❌ Box(...) { Text(...) }
   ```

4. **Error Handling mit Framework**
   ```kotlin
   ✅ tryCatch({ ... }, onError = { ... })
   ❌ try { ... } catch (e: Exception) { ... }
   ```

5. **Input Validation immer**
   ```kotlin
   ✅ OutlinedTextField(isError = ..., supportingText = ...)
   ❌ OutlinedTextField(...)
   ```

---

## Troubleshooting

### "Cannot find symbol: Dimensions"
→ Import: `import com.empiriact.app.ui.theme.Dimensions`

### "Cannot find symbol: ReusableComponents"
→ Import: `import com.empiriact.app.ui.common.TimerDisplay`

### Timer zeigt falsche Zeit
→ Verwende `formatTimerDisplay(seconds)` nicht `String.format()`

### Button wird nicht deaktiviert
→ Verwende `enabled = isInputValid` auf dem Button

### ErrorBanner nicht sichtbar
→ Stelle sicher, dass `errorMessage.isNotEmpty()`

---

## Weitere Ressourcen

- [Material Design 3](https://m3.material.io/)
- [Compose Accessibility](https://developer.android.com/jetpack/compose/accessibility)
- [Android UX Guidelines](https://developer.android.com/design)

---

**Status:** ✅ Production Ready  
**Last Updated:** Januar 2026
