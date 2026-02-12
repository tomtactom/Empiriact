# 🎯 Selektive Aufmerksamkeit - Implementierungsdokumentation

## Übersicht
Die **Selektive Aufmerksamkeit** ist eine neue Ressourcenübung im Modul ATT (Attention Training), die Nutzer dabei hilft, ihre Fähigkeit zur gezielten Aufmerksamkeitskontrolle zu trainieren. Diese Übung unterbricht automatische Grübelschleifen durch bewusstes Fokussieren auf einen einzelnen Reiz.

---

## 📁 Implementierte Dateien

### 1. **SelectiveAttentionExercise.kt**
**Pfad:** `app/src/main/java/com/empiriact/app/ui/screens/resources/methods/`

#### Funktionalität:
- Interaktive, adaptive 7-Stufen-Übung
- Schritt-für-Schritt Anleitung mit Visualisierungen
- Fokus-Auswahl durch den Nutzer
- Timer-Simulation für Fokusphase

#### Struktur:
```
Schritt 1: Willkommen & Erklärung
Schritt 2: Fokus-Auswahl (4 Optionen)
  - 👂 Geräusche um dich herum
  - 👁️ Visuelle Details
  - 🫁 Atem
  - 🦶 Körperliche Empfindungen
Schritt 3: Vorbereitung
Schritt 4: Übungsanleitung
Schritt 5: Fokusphase (aktive Übung)
Schritt 6: Reflexion
Schritt 7: Abschluss & Erkenntnis
```

#### Features:
- 🎨 **Visuelles Design:** Emojis für jeden Schritt
- 📊 **Fortschrittsanzeige:** LinearProgressIndicator zeigt Position an
- 🎯 **Interaktivität:** Benutzer wählt seinen Fokus
- ⏱️ **Timer:** Zeigt verbleibende Zeit während Fokusphase
- 🔄 **Navigation:** Zurück/Weiter Buttons mit Validierung

---

## 🔧 Integration

### 2. **Route.kt** - Navigation
Neue Route hinzugefügt:
```kotlin
object SelectiveAttentionExercise : Route("selective_attention_exercise/{from}") {
    fun createRoute(from: String) = "selective_attention_exercise/$from"
}
```

**Parameter:**
- `from`: Gibt an, von wo die Übung aufgerufen wurde (z.B. "resources")

---

### 3. **ResourcesScreen.kt** - Ressourcenliste
Neue Übung zur Liste hinzugefügt:
```kotlin
ResourceExercise(
    title = "Selektive Aufmerksamkeit",
    description = "Trainiere deine Fähigkeit, deine Aufmerksamkeit gezielt auf einen Reiz zu lenken und Grübelschleifen zu unterbrechen.",
    route = Route.SelectiveAttentionExercise
)
```

**Navigation aktualisiert:**
- Erkannt den `SelectiveAttentionExercise`-Route-Typ
- Übergibt `from = "resources"`-Parameter beim Navigieren

---

### 4. **EmpiriactNavGraph.kt** - Navigation Graph
Navigation-Komposition hinzugefügt:
```kotlin
composable(
    route = Route.SelectiveAttentionExercise.route,
    arguments = listOf(navArgument("from") { type = NavType.StringType })
) { backStackEntry ->
    val from = backStackEntry.arguments?.getString("from")!!
    SelectiveAttentionExercise(navController, from)
}
```

---

## 🎬 Navigationsfluss

```
Ressourcen-Menü
    ↓
ResourcesScreen
    (Nutzer klickt "Selektive Aufmerksamkeit")
    ↓
SelectiveAttentionExercise
    Step 1-7 durchlaufen
    (Nutzer klickt "Abschließen")
    ↓
ExerciseRatingScreen
    (Übung bewerten)
    ↓
Zurück zu ResourcesScreen
```

---

## 📱 User Experience

### Schritt 1: Willkommen
- Erklärt Zweck und Ziel der Übung
- Beruhigender Ton, ohne Druck

### Schritt 2: Fokus-Auswahl
- 4 Optionen für unterschiedliche Lernstile
- Button-Validierung: "Weiter" nur aktiv nach Auswahl

### Schritt 3-4: Vorbereitung & Anleitung
- Detaillierte Erklärung des Prozesses
- Normalisierung von abschweifenden Gedanken

### Schritt 5: Fokusphase
- Timer-Anzeige (2 Minuten)
- Nutzer führt Übung eigenständig durch
- Automatischer Übergang nach 2 Sekunden (Demo-Modus)

### Schritt 6: Reflexion
- Guided Reflection mit 3 Fragen
- Selbstwahrnehmung fördern

### Schritt 7: Abschluss
- Zusammenfassung der Lernziele
- Verknüpfung zu Grübelschleifen-Unterbrechung
- Bestärkung von Selbstkontrolle

---

## 🎯 Therapeutische Ziele

Diese Übung adressiert:

1. **ATT (Attention Training)**
   - ✅ Fokussierte Aufmerksamkeit
   - ✅ Bewusste Aufmerksamkeitskontrolle
   - ✅ Trainierbarkeit von Aufmerksamkeit

2. **Grübelschleifen-Unterbrechung**
   - ✅ Ablenkung von automatischen Gedanken
   - ✅ Gegenwartsorientierung
   - ✅ Mentale Flexibilität

3. **Selbstwirksamkeit**
   - ✅ Erlebnis von Selbststeuerung
   - ✅ Erfolgserlebnis durch trainierbare Fähigkeit
   - ✅ Wiederholbarkeit und Verbesserung

---

## 🔮 Mögliche Zukünftige Erweiterungen

### Phase 2: Adaptive Schwierigkeit
- Kurz (1 Min) / Mittel (2 Min) / Lang (5 Min)
- Wahl basierend auf Nutzerpräferenz

### Phase 3: Echte Timer
- Haptisches Feedback bei Ende
- Tatsächliche 2-Minuten-Messung
- Progress-Tracking über Tage

### Phase 4: Fortgeschrittene Modi
- Mehrere Reize (schwieriger)
- Ablenkungen hinzufügen
- Gamification (Streak-Counter)

### Phase 5: Datenerfassung
- Erfasse wie oft Aufmerksamkeit abschweifte
- Vergleiche über Zeit
- Zeige Fortschritt

---

## ✅ Testing-Punkte

- [ ] Navigiere zu Ressourcen → Klick "Selektive Aufmerksamkeit"
- [ ] Verifiziere alle 7 Schritte sind durchlaufen
- [ ] Prüfe Fokus-Auswahl ist erforderlich vor Weiter
- [ ] Prüfe Timer läuft in Fokusphase
- [ ] Verifiziere Navigation zu ExerciseRatingScreen
- [ ] Prüfe Zurück-Button funktioniert in allen Steps
- [ ] Prüfe Bewertung führt zurück zu ResourcesScreen

---

## 📊 Code-Metriken

| Aspekt | Wert |
|--------|------|
| Zeilen Code | ~249 |
| Komponentgröße | Mittel |
| Komplexität | Moderat |
| Abhängigkeiten | 4 neue Imports |
| Dateien geändert | 4 |
| Neue Dateien | 1 |

