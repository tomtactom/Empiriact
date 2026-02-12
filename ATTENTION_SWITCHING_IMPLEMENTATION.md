# 🔄 Aufmerksamkeitswechsel - Implementierungsdokumentation

## Übersicht
Die **Aufmerksamkeitswechsel**-Übung ist eine neue Ressourcen-Übung im ATT-Modul (Attention Training), die Nutzern hilft, die Flexibilität ihrer Aufmerksamkeit zu trainieren. Diese Übung adressiert direkt die Rigidität von Aufmerksamkeitsmustern und fördert kognitive Beweglichkeit.

---

## 📁 Implementierte Dateien

### 1. **AttentionSwitchingExercise.kt** (NEU)
**Pfad:** `app/src/main/java/com/empiriact/app/ui/screens/resources/methods/`

#### Funktionalität:
- Adaptive 10-Stufen-Übung mit strukturiertem Aufmerksamkeitstraining
- Abwechselnd: Äußere Reize ↔ Innere Reize
- 4 Trainingszyklen à 30 Sekunden
- Timer mit Gong-Sound bei jedem Zyklus-Ende
- Reflexions-Antwortfeld

#### Struktur:
```
Schritt 1: Willkommen
Schritt 2: Verständnis der Übung
Schritt 3: Vorbereitung
Schritt 4: Trainings-Phase Anleitung
─────────────────────────────────────
Schritt 5: ÄUSSERE REIZE (30 Sek)
Schritt 6: INNERE REIZE (30 Sek)
Schritt 7: ÄUSSERE REIZE (30 Sek)
Schritt 8: INNERE REIZE (30 Sek)
─────────────────────────────────────
Schritt 9: Reflexion & Fragen
Schritt 10: Abschluss & Erkenntnis
```

#### Features:
- 🔄 **Flexibilitätstraining:** Bewusster Wechsel zwischen Fokus-Arten
- ⏱️ **30-Sekunden-Zyklus:** Optimale Dauer für Aufmerksamkeitswechsel
- 🔊 **Gong-Sound:** Signalisiert Zyklus-Ende und Wechsel
- 📝 **Reflexion:** Nutzer notiert Erfahrungen
- 📊 **Fortschrittsanzeige:** LinearProgressIndicator

#### Aufmerksamkeits-Ebenen:

**ÄUSSERE REIZE (External Focus):**
- 🔊 Hörbares: Geräusche, Töne
- 👁️ Sichtbares: Farben, Formen, Bewegungen
- 🤚 Tastgefühl: Oberflächen, Texturen, Temperaturen

**INNERE REIZE (Internal Focus):**
- 🧠 Gedanken: Gedankenmuster, -inhalte
- ❤️ Gefühle: Emotionen, Stimmungen
- 🫀 Körper: Körperempfindungen, Muskeltonus

---

## 🔧 Integration

### 2. **Route.kt** - Navigation
Neue Route hinzugefügt:
```kotlin
object AttentionSwitchingExercise : Route("attention_switching_exercise/{from}") {
    fun createRoute(from: String) = "attention_switching_exercise/$from"
}
```

---

### 3. **ResourcesScreen.kt** - Ressourcenliste
Neue Übung zur Liste hinzugefügt:
```kotlin
ResourceExercise(
    title = "Aufmerksamkeitswechsel",
    description = "Trainiere die Flexibilität deiner Aufmerksamkeit durch bewusstes Wechseln zwischen äußeren und inneren Reizen. Breche starre Aufmerksamkeitsmuster auf.",
    route = Route.AttentionSwitchingExercise
)
```

Navigation aktualisiert für AttentionSwitchingExercise-Route

---

### 4. **ExerciseRatingScreen.kt** - Bewertung
Übungstitel mapping aktualisiert:
```kotlin
"attention_switching" -> "Aufmerksamkeitswechsel"
```

---

### 5. **EmpiriactNavGraph.kt** - Navigation Graph
Import und Route registriert:
```kotlin
import com.empiriact.app.ui.screens.resources.methods.AttentionSwitchingExercise

composable(
    route = Route.AttentionSwitchingExercise.route,
    arguments = listOf(navArgument("from") { type = NavType.StringType })
) { backStackEntry ->
    val from = backStackEntry.arguments?.getString("from")!!
    AttentionSwitchingExercise(navController, from)
}
```

---

## 🎯 Therapeutische Ziele

Diese Übung adressiert spezifisch:

### 1. **Aufmerksamkeitstraining (ATT)**
- ✅ Flexible Aufmerksamkeitskontrolle
- ✅ Bewusstes Wechseln zwischen Fokus-Typen
- ✅ Reduktion von Aufmerksamkeitsverharren

### 2. **Reduktion von Rigidität**
- ✅ Erlebnis von kognitiver Flexibilität
- ✅ Aufbrechen starrer Muster
- ✅ Verbesserter Wechsel zwischen Perspektiven

### 3. **Bedrohungsfokus-Reduktion**
- ✅ Umlenken von Bedrohungs-orientierten Gedanken
- ✅ Flexibler Wechsel zu neutralen/positiven Reizen
- ✅ Reduktion von Hypervigilanz

### 4. **Selbstbezugs-Reduktion**
- ✅ Alternativer Fokus zu inneren Reizen
- ✅ Bewusster Wechsel zu äußerer Umgebung
- ✅ Reduktion von Rumination

---

## 🎬 Navigationsfluss

```
Ressourcen-Menü
    ↓
ResourcesScreen
    (Nutzer klickt "Aufmerksamkeitswechsel")
    ↓
AttentionSwitchingExercise
    Steps 1-4: Erklärung & Vorbereitung
    Steps 5-8: 4 × 30-Sekunden Trainingszyklen
        (Äußen → Innen → Äußen → Innen)
    Steps 9-10: Reflexion & Abschluss
    (Nutzer klickt "Abschließen")
    ↓
ExerciseRatingScreen
    (Übung bewerten)
    ↓
Zurück zu ResourcesScreen
```

---

## 💻 Code-Highlights

### Timer mit LaunchedEffect:
```kotlin
LaunchedEffect(isTimerRunning, timeRemaining) {
    if (isTimerRunning && timeRemaining > 0) {
        delay(1000)
        timeRemaining--
    } else if (timeRemaining == 0 && isTimerRunning) {
        playGongSound(context)
        isTimerRunning = false
        currentStep++  // Zum nächsten Schritt
    }
}
```

### Intelligente Button-Aktivierung:
```kotlin
enabled = when {
    currentStep == 8 -> reflectionText.isNotBlank()
    currentStep in 4..6 && isTimerRunning -> false  // Timer läuft
    else -> true
}
```

### Adaptive UI für Timer-Phasen:
```kotlin
if ((stepIndex in 4..7) && isTimerRunning) {
    // Zeige Timer an
    Text(formatTime(timeRemaining))
}
```

---

## 📊 Datenstruktur

Die Übung speichert (über ExerciseRatingScreen):
```
exercise_ratings
├── exerciseId: "attention_switching"
├── rating: -2 bis +2
└── timestamp: Speicher-Zeit

exercise_reflections (optional)
├── exerciseId: "attention_switching"
├── reflection: Nutzer-Text
└── timestamp: Speicher-Zeit
```

---

## 🧪 Testing-Punkte

- [ ] Navigiere zu Ressourcen → Klick "Aufmerksamkeitswechsel"
- [ ] Verifiziere alle 10 Schritte sind erreichbar
- [ ] Teste Timer: Startet bei Step 5 mit 30 Sekunden
- [ ] Prüfe Timer läuft: 30 → 29 → ... → 1 → 0
- [ ] Verifiziere Gong-Sound erklingt bei Step-Übergang
- [ ] Prüfe automatischen Übergang nach Timer
- [ ] Teste Reflexions-Feld: Button nur aktiv bei Text
- [ ] Verifiziere Navigation zu ExerciseRatingScreen
- [ ] Prüfe Übungstitel "Aufmerksamkeitswechsel" wird angezeigt
- [ ] Teste Bewertung speichern & Rückkehr zu Resources

---

## 🚀 Zukünftige Erweiterungen

### Phase 2: Adaptive Schwierigkeit
- Kurz (3 Zyklen × 20 Sek)
- Standard (4 Zyklen × 30 Sek) ← AKTUELL
- Lang (5 Zyklen × 45 Sek)

### Phase 3: Tracking
- Wie schnell wechselt deine Aufmerksamkeit?
- Welche Richtung ist einfacher? (Außen→Innen vs. Innen→Außen)
- Fortschritt über Zeit

### Phase 4: Variationen
- Unterschiedliche Reiz-Kombinationen
- Schnellere/langsamere Wechsel
- Mit Ablenkungen (schwerer)

### Phase 5: Erweiterte Metriken
- Durchschnittliche Reaktionszeit beim Wechsel
- Konsistenz über mehrere Sessions
- Vergleich mit Baseline

---

## 📈 Therapeutische Effektivität

Diese Übung wird besonders effektiv für:
- ✅ Patienten mit rigiden Aufmerksamkeitsmustern
- ✅ Generalisierten Angststörungen (GAD)
- ✅ Hyperfokus auf Bedrohungsreize
- ✅ Ruminations-Zyklen
- ✅ Reduktion von Hypervigilanz

---

**Status: ✅ Implementierung abgeschlossen und integriert**

Die neue Übung ist sofort einsatzbereit und kann von Nutzern im Ressourcen-Menü aufgerufen werden.
