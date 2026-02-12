# 🔧 Kompilierungs- und Test-Guide

## Pre-Build Checklist

### 1. Neue Dateien überprüfen
```
✅ Dimensions.kt - Design Tokens
✅ ReusableComponents.kt - UI Komponenten
✅ ErrorHandling.kt - Error Management
✅ AccessibilityHelpers.kt - A11y Utilities
✅ EnhancedButtons.kt - Button Komponenten
```

### 2. Modifizierte Dateien überprüfen
```
✅ AttentionSwitchingExercise.kt - Audio-Fix, Spacing
✅ SelectiveAttentionExercise.kt - Audio-Fix
✅ SharedAttentionExercise.kt - Audio-Fix
✅ TodayScreen.kt - Dimensions
✅ ActivityPlannerScreen.kt - Validation
✅ ProfileScreen.kt - UI Redesign
✅ SettingsScreen.kt - Spacing
```

---

## Build Steps

### Terminal in Projektordner ausführen:

```bash
# 1. Clean & Sync
./gradlew clean
./gradlew sync

# 2. Kompilieren
./gradlew compileDebugKotlin

# 3. Build
./gradlew assembleDebug

# 4. (Optional) Tests
./gradlew test
```

---

## Expected Output

```
BUILD SUCCESSFUL in XYZs
XX actionable tasks: X executed, XX up-to-date
```

---

## Mögliche Fehler und Lösungen

### Error: "Cannot find symbol: Dimensions"
**Lösung:** Stelle sicher, dass Dimensions.kt im korrekten Package liegt:
```
com/empiriact/app/ui/theme/Dimensions.kt
```

### Error: "Cannot find symbol: ReusableComponents"
**Lösung:** Import Statement hinzufügen:
```kotlin
import com.empiriact.app.ui.common.TimerDisplay
import com.empiriact.app.ui.common.StepProgressIndicator
```

### Error: "Cannot find symbol: SoundManager"
**Lösung:** Import hinzufügen:
```kotlin
import com.empiriact.app.ui.common.SoundManager
```

---

## Runtime Testing

### Test 1: AttentionSwitchingExercise
1. App öffnen → Navigation zu Resources/Exercises
2. "Aufmerksamkeitswechsel" Übung starten
3. **Erwartung:** Gong-Sound spielt ab bei Timer-Ende
4. **Erwartung:** Besseres Spacing, TimerDisplay funktioniert

### Test 2: ActivityPlannerScreen
1. Navigation zu Values/Planner
2. Leeres Textfeld eingeben
3. **Erwartung:** Button ist deaktiviert
4. Mehr als 150 Zeichen eingeben
5. **Erwartung:** Error-Text zeigt Max-Länge
6. Gültige Aktivität eingeben
7. **Erwartung:** Button ist aktiviert, Item wird hinzugefügt

### Test 3: TodayScreen
1. Today Screen öffnen
2. **Erwartung:** Konsistentes Spacing zwischen Cards
3. Activity Eintrag öffnen
4. **Erwartung:** Card-Padding stimmt überein

### Test 4: ProfileScreen
1. Navigation zu Profile
2. **Erwartung:** 3 Info-Cards sind sichtbar
3. **Erwartung:** Icons und Farben stimmen überein
4. **Erwartung:** Responsive Layout

### Test 5: SettingsScreen
1. Navigation zu Settings
2. **Erwartung:** Besseres Spacing
3. **Erwartung:** Button-Text ist gekürzt ("CSV" statt "Als CSV exportieren")

---

## Accessibility Testing

### 1. Mit Android Studio Accessibility Scanner
```
1. Tools → Lint → Run Inspection
2. Wähle Accessibility Probleme
3. Überprüfe auf Warnings
```

### 2. Mit TalkBack (Android Feature)
```
1. Settings → Accessibility → TalkBack → Enable
2. Durchlaufe jeden Screen
3. Überprüfe Content Descriptions
```

### 3. Manuelle Checks
- [ ] Alle Buttons mindestens 48x48dp
- [ ] Text auf Hintergrund hat genug Kontrast
- [ ] Icons haben contentDescription

---

## Performance Testing

### Memory Check
```bash
./gradlew connectedAndroidTest
```

### Recomposition Logging
```kotlin
// In Theme.kt hinzufügen (nur für Debug)
@Composable
fun DebugRecomposition() {
    val recomposeCount = remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        recomposeCount.value++
    }
    // Log recomposeCount für Performance
}
```

---

## Deployment Checklist

Bevor zum Main-Branch mergen:

- [ ] Alle neuen Files vorhanden
- [ ] Alle Imports korrekt
- [ ] Build erfolgreich (./gradlew build)
- [ ] Lint Warnings überprüfen
- [ ] Accessibility Scanner durchlaufen lassen
- [ ] 3-4 Screens manuell testen
- [ ] Dokumentation aktualisiert
- [ ] Code Review durchgeführt

---

## Debugging Tips

### Spacing Issues
1. Überprüfe ob Dimensions.kt importiert ist
2. Verwende Modifier.background(Color.Red) für visuelle Debugging
3. LazyColumn contentPadding überprüfen

### Component Issues
1. Check imports für ReusableComponents
2. Überprüfe Modifier parameter (z.B. fillMaxWidth())
3. Log State changes mit remember + DisposableEffect

### Audio Issues
1. SoundManager.initialize() wird vor playGongSound() aufgerufen
2. Überprüfe Permissions (POST_NOTIFICATIONS für Android 13+)
3. Test auf echtem Gerät, nicht nur Emulator

---

## Nächste Schritte nach Build

1. **In 1-2 Tagen:** Loading States implementieren
2. **In 1 Woche:** Mehr Validation Screens
3. **In 2 Wochen:** Accessibility Audit durchführen
4. **In 1 Monat:** Skeleton Screens & Animations

---

**Status:** Ready for Build ✅  
**Datum:** Januar 2026
