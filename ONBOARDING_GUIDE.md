# 🎓 Empiriact Onboarding Guide für neue Entwickler

**Status:** Phase 1 Complete  
**Datum:** Januar 2026  
**Ziel:** In 2 Stunden production-ready sein

---

## ⏰ Timeline Overview

```
Hour 1: Grundlagen verstehen
  ├─ 15 min: Project Overview
  ├─ 15 min: Architecture verstehen
  ├─ 15 min: Tech Stack durchgehen
  └─ 15 min: Erste Komponente nutzen

Hour 2: Praktisch arbeiten
  ├─ 30 min: Build & Run
  ├─ 30 min: Code navigieren
  ├─ 30 min: Kleine Änderung machen
  └─ 30 min: Debugging & Testing
```

---

## 🚀 Schnellstart (15 min)

### 1. Repository klonen
```bash
git clone <repo-url>
cd Empiriact
```

### 2. Projekt öffnen
```
Android Studio → Open → Wähle Empiriact Folder
```

### 3. Gradle Sync starten
```
File → Sync Now
(oder Ctrl+Shift+S)
```

### 4. Build starten
```bash
./gradlew assembleDebug
# oder: Build → Build Bundle(s) / APK(s) → Build APK(s)
```

### 5. Auf Gerät/Emulator installieren
```bash
./gradlew installDebug
# Gerät anschließen oder Emulator starten
```

### 6. App öffnen
```
Emulator/Gerät: App "Empiriact" starten
```

**Fertig!** Du hast Phase 1 erfolgreich deployed. ✅

---

## 📚 Lernpfad (2 Stunden)

### 🟢 Level 1: Verständnis (30 min)

**Lese diese 3 Dateien:**

1. **README_PROJECT.md** (15 min)
   - Projekt-Übersicht
   - Architecture
   - Tech Stack
   - Key Features

2. **DOCUMENTATION_INDEX.md** (10 min)
   - Wo alles zu finden ist
   - Schnelle Navigation
   - FAQ

3. **COMPONENT_LIBRARY_GUIDE.md** (Intro) (5 min)
   - Übersicht der 11 Komponenten
   - Welche Komponente wofür?

**Quiz:**
- [ ] Was ist das Ziel der Empiriact App?
- [ ] Welche 3 Aufmerksamkeits-Übungen gibt es?
- [ ] Welche 3 Main Screens gibt es?

---

### 🟡 Level 2: Komponenten (30 min)

**Praktische Übung: Eine Komponente verwenden**

#### Aufgabe 1: TimerDisplay verwenden
```kotlin
// Öffne eine beliebige Datei im screens/ Ordner
import com.empiriact.app.ui.common.TimerDisplay

@Composable
fun MyExercise() {
    var timeRemaining by remember { mutableStateOf(30) }
    
    TimerDisplay(
        timeString = "${timeRemaining}s",
        isRunning = true,
        modifier = Modifier.fillMaxWidth()
    )
}
```

#### Aufgabe 2: Input Validation hinzufügen
```kotlin
import com.empiriact.app.ui.theme.Dimensions
import com.empiriact.app.ui.common.InputValidationFeedback

var inputText by remember { mutableStateOf("") }
val isValid = inputText.isNotBlank() && inputText.length <= 150

Column(modifier = Modifier.padding(Dimensions.paddingMedium)) {
    OutlinedTextField(
        value = inputText,
        onValueChange = { inputText = it },
        isError = !isValid,
        supportingText = { Text("${inputText.length}/150") }
    )
    
    InputValidationFeedback(
        isValid = isValid,
        errorMessage = if (inputText.length > 150) "Zu lang" else ""
    )
}
```

#### Aufgabe 3: Error Handling implementieren
```kotlin
import com.empiriact.app.ui.common.ErrorBanner
import com.empiriact.app.ui.common.tryCatch

var errorMessage by remember { mutableStateOf("") }

LaunchedEffect(Unit) {
    tryCatch(
        { viewModel.loadData() },
        "Fehler beim Laden",
        onError = { errorMessage = it }
    )
}

Column {
    ErrorBanner(
        message = errorMessage,
        onDismiss = { errorMessage = "" }
    )
    // Content
}
```

**Komplett?** Du kannst jetzt 3 Komponenten nutzen! ✅

---

### 🟠 Level 3: Navigation (30 min)

**Lese diese Teile:**

1. **COMPONENT_LIBRARY_GUIDE.md** (Komplett)
   - Alle 11 Komponenten
   - Alle Code-Beispiele
   - Best Practices

2. **BUILD_AND_TEST_GUIDE.md**
   - Wie man testet
   - Debugging Tips
   - Common Errors

**Praktische Übung:**
- [ ] Öffne TodayScreen.kt
- [ ] Finde die Komponenten die du kennst
- [ ] Verstehe wie sie zusammenpassen
- [ ] Ändere ein Spacing mit Dimensions

---

### 🔴 Level 4: Produktion (30 min)

**Lese:**
1. **ACCESSIBILITY_AND_UX_IMPROVEMENTS.md**
   - Best Practices
   - Accessibility Guidelines
   - Testing

2. **Code Review:**
   - Schaue dir 3 modifizierte Screens an
   - Verstehe die Patterns
   - Beobachte Best Practices

**Praktische Aufgabe:**
1. Erstelle einen neuen Screen mit:
   - ✅ Dimensions.paddingMedium Padding
   - ✅ Input Validation mit InputValidationFeedback
   - ✅ Error Handling mit ErrorBanner
   - ✅ Button mit ActionButton
   - ✅ Content Descriptions für Icons

2. Test auf echtem Gerät oder Emulator

3. Code Review mit Senior Developer

**Fertig?** Du bist nun ein Production-Ready Entwickler! 🚀

---

## 📖 Referenz-Guide

### Häufigste Aufgaben

#### "Ich möchte eine neue Komponente erstellen"
1. Lese: COMPONENT_LIBRARY_GUIDE.md
2. Erstelle in: `common/MyNewComponent.kt`
3. Teste mit: Code Example im Guide
4. Dokumentiere wie andere Komponenten

#### "Ich möchte einen neuen Screen machen"
1. Erstelle: `screens/myfeature/MyScreen.kt`
2. Verwende: Dimensions für Spacing
3. Nutze: Reusable Components
4. Validiere: Mit InputValidationFeedback
5. Fehlerhandlung: Mit ErrorBanner

#### "Ich möchte einen Bug fixen"
1. Reproduziere: Mit BUILD_AND_TEST_GUIDE
2. Debugge: Mit Android Studio Debugger
3. Fixen: Minimal change
4. Teste: Mit Unit & UI Tests
5. Dokumentiere: Code Review

#### "Ich verstehe den Code nicht"
1. Lese: README_PROJECT.md (Architecture)
2. Suche: Im DOCUMENTATION_INDEX.md
3. Frage: Im Code Comments oder Senior Dev
4. Debugge: Mit Logcat & Breakpoints

---

## 🛠️ Wichtige Befehle

### Build & Run
```bash
# Debug Build
./gradlew assembleDebug

# Debug auf Gerät
./gradlew installDebug

# Clean & Build
./gradlew clean assembleDebug

# Full Sync
./gradlew sync
```

### Testing
```bash
# Unit Tests
./gradlew test

# UI Tests
./gradlew connectedAndroidTest

# Lint Check
./gradlew lint
```

### Debugging
```
Android Studio:
  • Breakpoints setzen: Klick auf Zeile
  • Step Over: F10
  • Step Into: F11
  • Resume: F9
  • Logcat: Alt+6
```

---

## 🎯 Best Practices (MUST KNOW)

### 1. Verwende Dimensions statt Magic Numbers
```kotlin
❌ FALSCH:  .padding(16.dp)
✅ RICHTIG: .padding(Dimensions.paddingMedium)
```

### 2. Content Descriptions für Icons
```kotlin
❌ FALSCH:  Icon(Icons.Default.Settings, "Icon")
✅ RICHTIG: Icon(Icons.Default.Settings, ContentDescriptions.SETTINGS_BUTTON)
```

### 3. Input Validation immer
```kotlin
❌ FALSCH:  if (text.isNotBlank()) save()
✅ RICHTIG: 
  val isValid = text.isNotBlank() && text.length <= 150
  Button(enabled = isValid) { save() }
```

### 4. Error Handling Framework
```kotlin
❌ FALSCH:  try { load() } catch(e: Exception) { }
✅ RICHTIG: tryCatch({ load() }, onError = { showError(it) })
```

### 5. Komponenten Reuse
```kotlin
❌ FALSCH:  Box { Text(...) } (Timer hardcoded)
✅ RICHTIG: TimerDisplay(timeString, isRunning)
```

---

## 🐛 Häufige Fehler

### "Cannot find symbol: Dimensions"
**Lösung:**
```kotlin
import com.empiriact.app.ui.theme.Dimensions
```

### "Cannot find symbol: TimerDisplay"
**Lösung:**
```kotlin
import com.empiriact.app.ui.common.TimerDisplay
```

### "Gradle sync failed"
**Lösung:**
```bash
./gradlew clean
./gradlew sync
```

### "App crasht beim Starten"
**Lösung:**
1. Schau Logcat (Alt+6)
2. Suche rote ERROR-Zeilen
3. Google error message
4. Frag einen Senior Developer

### "UI sieht komisch aus"
**Lösung:**
1. Überprüfe Spacing (Dimensions)
2. Überprüfe Modifier (fillMaxWidth, etc)
3. Überprüfe Column/Row Alignment
4. Nutze Preview für schnelle Tests

---

## 📋 Checkliste vor dem Commit

- [ ] Code kompiliert ohne Fehler
- [ ] Keine neuen Warnings im Lint
- [ ] Verwendet Dimensions statt Magic Numbers
- [ ] Hat Input Validation wenn nötig
- [ ] Hat Error Handling
- [ ] Icons haben Content Descriptions
- [ ] Tests geschrieben/aktualisiert
- [ ] Dokumentation aktualisiert
- [ ] Code Review angefordert

---

## 🆘 Hilfe & Support

### Schnelle Fragen
```
Android Studio → Help → Find Action (Ctrl+Shift+A)
→ Suche "Material Design"
→ Viele Guides verfügbar
```

### Komponenten-Fragen
```
→ COMPONENT_LIBRARY_GUIDE.md
→ Code Examples für alle Komponenten
→ Troubleshooting am Ende
```

### Architektur-Fragen
```
→ README_PROJECT.md
→ Architecture Sektion
→ Tech Stack erklärt
```

### Build/Test-Fragen
```
→ BUILD_AND_TEST_GUIDE.md
→ Step-by-step Anleitung
→ Expected Output erklärt
```

### Onboarding-Fragen
```
→ Dieses Dokument (Onboarding Guide)
→ FAQ Sektion unten
→ Quick Reference oben
```

---

## ❓ FAQ

**F: Wie lange dauert das Onboarding?**
A: 2 Stunden für Basics. 1 Woche bis voll produktiv.

**F: Kann ich sofort Code schreiben?**
A: Nach Level 1 (30 min) ja. Aber Level 2-4 sind wichtig.

**F: Was wenn ich einen Bug findet?**
A: Großartig! Report im Issue Tracker mit Details.

**F: Wo finde ich Code-Standards?**
A: Code Review Checklist & Best Practices oben.

**F: Kann ich Komponenten anpassen?**
A: Ja, aber koordiniere mit Team wenn es API ändert.

**F: Wie oft muss ich dokumentation lesen?**
A: Initial: Alle. Später: Nur was du brauchst.

**F: Wie melde ich einen Bug?**
A: GitHub Issues → Template ausfüllen → Assign to PM

**F: Wie bekomme ich Code Review?**
A: GitHub Pull Request → Assign Reviewer → Warte auf Feedback

**F: Was ist die Coding Convention?**
A: Kotlin Google Style Guide + Android Best Practices

**F: Wie stelle ich sicher mein Code ist gut?**
A: Lint, Tests, Code Review, Testing auf echtem Gerät

---

## 🎓 Weiterführende Ressourcen

### Material Design
- https://m3.material.io/

### Jetpack Compose
- https://developer.android.com/jetpack/compose

### Android Architecture
- https://developer.android.com/topic/architecture

### Kotlin
- https://kotlinlang.org/docs/

### Accessibility
- https://developer.android.com/guide/topics/ui/accessibility

---

## 📅 Nächste Schritte nach Onboarding

**Day 1-2:**
- [ ] Setup complete
- [ ] First component understood
- [ ] Can navigate codebase

**Week 1:**
- [ ] Written first PR
- [ ] Code Review passed
- [ ] Merged to main
- [ ] Deployed to staging

**Week 2:**
- [ ] Understand architecture deeply
- [ ] Know all 11 components
- [ ] Can debug issues
- [ ] Writing good tests

**Week 3+:**
- [ ] Leading features
- [ ] Reviewing code
- [ ] Mentoring others
- [ ] Contributing to docs

---

## ✨ Du schaffst das!

Willkommen im Empiriact Team! 🎉

```
    _______________
   /               \
  /   WELCOME TO    \
  |  EMPIRIACT      |
  |   TEAM!  🚀     |
   \               /
    \_____________/

You are now Production-Ready! 💪
```

---

**Next Step:** Start with Level 1 right now!

**Estimated Time:** 2 hours  
**Outcome:** Production-ready developer ✅  
**Questions?** Siehe Support section oben

---

**Onboarding Guide Version:** 1.0  
**Date:** Januar 2026  
**Status:** ✅ Complete
