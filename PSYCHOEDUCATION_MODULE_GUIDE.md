# Psychoedukatives Modul - Implementierungsguide

## Überblick

Das psychoedukative Modul bietet eine umfassende, wissenschaftlich fundierte Lernplattform mit modernem UX-Design. Es besteht aus drei Hauptkomponenten:

### 1. **PsychoeducationScreen** - Interaktive Lernmodule
- **4 Kernmodule**: Emotionsregulation, Angststörungen verstehen, Kognitive Defusion, Werteorientiertes Leben
- **Multi-kapitel Struktur**: Jedes Modul hat 2-3 Kapitel mit progressivem Aufbau
- **Interaktive Elemente**: Expandierbare Sektionen, Beispielboxen, Zusammenfassungen
- **Progress-Tracking**: Visueller Fortschrittsbalken und Kapitel-Navigation

**Lokation:**
```
com.empiriact.app.ui.screens.resources.PsychoeducationScreen
```

### 2. **InteractiveExercisesScreen** - Geführte Übungen
- **3 praktische Übungen**:
  - 5-4-3-2-1 Erdungsübung (5 min) - Anfänger
  - Progressive Muskelentspannung (10 min) - Anfänger
  - Gedanken-Etikettierung (7 min) - Fortgeschrittene
- **Schritt-für-Schritt Anleitung**: Zeitgesteuerter Prozess mit Tipps und Weisungen
- **Vorschau-Bildschirm**: Nutzer können die Übung vorher erkunden
- **Timer-Integration**: Jeder Schritt hat eine definierte Dauer
- **Kontextbezogene Hilfe**: Anleitung und Tipps für jeden Schritt

**Lokation:**
```
com.empiriact.app.ui.screens.resources.InteractiveExercisesScreen
```

### 3. **ResourceBrowserScreen + LearningPathScreen** - Ressourcen-Verwaltung
- **Ressourcen-Bibliothek**: 10+ kuratierte psychologische Ressourcen
- **Intelligente Filter**: Nach Kategorie, Schwierigkeitsgrad, Typ
- **Suchfunktion**: Volltextsuche über Titel und Beschreibung
- **Favoritensystem**: Lesezeichen für häufig besuchte Ressourcen
- **Lernpfade**: Strukturierte Lernwege basierend auf psychologischen Themen
- **Progress-Tracking**: Fortschrittsanzeige für jeden Lernpfad
- **Personalisierte Empfehlungen**: Nächste empfohlene Schritte

**Lokation:**
```
com.empiriact.app.ui.screens.resources.ResourceBrowserScreen
com.empiriact.app.ui.screens.resources.LearningPathScreen
```

---

## UX/UI Design-Features

### 🎨 Moderne Design-Techniken

1. **Farbcodierung nach Modultyp**
   - Emotionsregulation: Indigo (#6366F1)
   - Angststörungen: Bernstein (#F59E0B)
   - Kognitive Defusion: Grün (#10B981)
   - Werte: Pink (#EC4899)

2. **Adaptive Animationen**
   - CrossFade für Navigation zwischen Screens
   - ExpandVertically für expandierbare Inhalte
   - animateColorAsState für Status-Änderungen
   - Smooth transitions mit tween(300)

3. **Hierarchische Information**
   - Große Überschriften mit klarem Kontrast
   - Subtitle-Text für Kontextualisierung
   - Badges für Kategorien und Status
   - Progressive Offenbarung durch expandierbare Sektionen

4. **Visueller Feedback**
   - Progress-Balken für Modul-Fortschritt
   - Durchschreitbare Punkte-Indikatoren
   - Icon-Codes für Übungstypen
   - Farbige Status-Indikatoren

5. **Raumnutzung**
   - Großzügige Padding und Margin (16dp als Standard)
   - Visuelle Hierarchie durch Spacing
   - Kartenbasiertes Layout für Skandierbarkeit
   - Vertikale Scroll für vertiefte Inhalte

6. **Barrierefreiheit**
   - semantics{} für Screen-Reader
   - Ausreichender Kontrast in allen Elementen
   - Large touch-targets (mindestens 48dp)
   - Descriptive contentDescription bei Icons

---

## Psychoedukative Inhalte - Wissenschaftlicher Hintergrund

### Module 1: Emotionsregulation

**Kapitel 1: Was sind Emotionen wirklich?**
- Basiert auf: Emotion Science und Psychophysiology
- Inhalte: 4-Säulen-Modell (Körper, Gedanke, Verhalten, Expression)
- Praktische Anwendung: Emotionen als Informationssystem verstehen

**Kapitel 2: Strategien zur Emotionsregulation**
- RAIN-Methode: Accept & Commit Therapy (Hayes)
- ABC-Modell: Kognitive Verhaltenstherapie (Ellis, Beck)
- Anwendungsbereich: Akute Emotionsregulation

### Module 2: Angststörungen verstehen

**Basiert auf:**
- Fear & Anxiety Models (Foa & Kozak)
- Threat Detection System
- Exposure & Response Prevention (ERP)

**Inhalte:**
- Fight-Flight-Freeze-Neurobiology
- Anxiety Maintenance Cycles
- Evidence-based treatments (CBT, Exposure Therapy)

### Module 3: Kognitive Defusion

**Basiert auf:**
- Acceptance and Commitment Therapy (Hayes, Strosahl, Wilson)
- Cognitive Fusion vs. Cognitive Defusion

**Praktische Techniken:**
- Thought Labeling
- Metaphorical Distancing
- Values-Aligned Thought Management

### Module 4: Werteorientiertes Leben

**Basiert auf:**
- Values Clarification (ACT)
- Meaning-Making in Psychology
- Behavioral Activation

**Inhalte:**
- Values vs. Goals Distinction
- Value Alignment in Daily Life
- Small Steps zur Werteintegration

---

## Integration in Navigation

### Beispiel: Navigation in der App integrieren

```kotlin
// In deiner Navigation (z.B. NavGraph.kt)
composable("psychoeducation") {
    PsychoeducationScreen(
        onBack = { navController.popBackStack() }
    )
}

composable("exercises") {
    InteractiveExercisesScreen(
        onBack = { navController.popBackStack() }
    )
}

composable("resources") {
    ResourceBrowserScreen(
        onBack = { navController.popBackStack() }
    )
}

composable("learning-path") {
    LearningPathScreen(
        onBack = { navController.popBackStack() }
    )
}
```

### Beispiel: In einem Menü oder Button-Set hinzufügen

```kotlin
// In deinem Hauptbildschirm (z.B. TodayScreen)
Button(onClick = { navController.navigate("psychoeducation") }) {
    Icon(Icons.Default.SchoolOutlined, contentDescription = null)
    Text("Psychoedukation")
}

Button(onClick = { navController.navigate("exercises") }) {
    Icon(Icons.Default.Lightbulb, contentDescription = null)
    Text("Übungen")
}

Button(onClick = { navController.navigate("resources") }) {
    Icon(Icons.Default.BookmarkBorder, contentDescription = null)
    Text("Ressourcen")
}
```

---

## Datenmodelle und Struktur

### Datenmodelle

```kotlin
// Psychoedukatives Modul
data class PsychoeducationModule(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val chapters: List<Chapter>,
    val estimatedReadTime: Int,
    val difficulty: String
)

// Kapitel mit verschachtelten Inhalten
data class Chapter(
    val id: String,
    val title: String,
    val content: String,
    val sections: List<Section>,
    val keyTakeaways: List<String>
)

// Section mit optionalen expandierbaren Inhalten
data class Section(
    val heading: String,
    val text: String,
    val examples: List<String> = emptyList(),
    val isExpandable: Boolean = false
)

// Interaktive Übung mit Schritt-für-Schritt-Anleitung
data class InteractiveExercise(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int,
    val steps: List<ExerciseStep>,
    val difficulty: String,
    val category: String,
    val benefits: List<String>
)

// Übungs-Schritt mit zeitgesteuerter Anleitung
data class ExerciseStep(
    val number: Int,
    val title: String,
    val instruction: String,
    val duration: Int,
    val guidance: String,
    val tips: List<String>
)

// Ressource für Bibliothek
data class PsychologicalResource(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val type: String,
    val estimatedTime: Int,
    val isFavorite: Boolean = false
)
```

---

## Erweiterungsmöglichkeiten

### Zukünftige Features

1. **Backend-Integration**
   - Speichere Favoriten und Fortschritt in der Datenbank
   - Synchronisiere Benutzerfortschritt über Geräte

2. **Personalisierung**
   - Adaptive Lernpfade basierend auf Nutzerantworten
   - Quiz nach jedem Modul
   - Dynamische Content-Empfehlungen

3. **Offline-Unterstützung**
   - Download von Modulen zur Offline-Nutzung
   - Caching von Ressourcen

4. **Social Features**
   - Teile Fortschritt mit Therapeuten
   - Gruppenlernpfade

5. **Gamification**
   - Badges für abgeschlossene Module
   - Streaks für regelmäßiges Lernen
   - Leaderboards

6. **Multimedia-Integration**
   - Video-Tutorials
   - Audio-Meditationen
   - Interaktive Quizze

---

## Best Practices für Nutzung

### Für Endnutzer

1. **Start mit Anfänger-Modulen**
   - Emotionsregulation ist ein guter Einstieg
   - 5-4-3-2-1 Erdungsübung für schnelle Ergebnisse

2. **Struktur dein Lernen**
   - Nutze Lernpfade als Orientierung
   - Ein Modul pro Woche für optimale Integration

3. **Praktiziere regelmäßig**
   - Übungen sind am wirksamsten mit wiederholter Praxis
   - Nutze die Favoritenfunktion für häufige Ressourcen

### Für Therapeuten/App-Manager

1. **Inhalt kuratieren**
   - Update Module basierend auf Nutzer-Feedback
   - Füge neue Forschungsergebnisse hinzu

2. **Progress-Tracking**
   - Überwache Nutzer-Engagement
   - Identifiziere, welche Module am hilfreichsten sind

3. **Feedback-Schleifen**
   - Sammle Nutzer-Feedback nach jedem Modul
   - Verwende Daten, um Inhalte zu verbessern

---

## Technische Details

### Dependencies

Das Modul benötigt nur Standard-Compose-Dependencies:
- `androidx.compose.foundation`
- `androidx.compose.material3`
- `androidx.compose.runtime`
- `androidx.compose.ui`

### Performance-Optimierungen

- LazyColumn für effiziente Listenrendering
- remember{} für Daten, die nicht neu berechnet werden sollen
- rememberSaveable{} für State-Persistenz bei Config-Changes
- Crossfade statt AnimatedContent für einfachere Animationen

### Code-Struktur

Alle Komponenten sind in Packages organisiert:
```
com.empiriact.app.ui.screens.resources/
├── PsychoeducationScreen.kt (4 Lernmodule)
├── InteractiveExercisesScreen.kt (3 Übungen)
└── ResourceBrowserScreen.kt (Ressourcen + Lernpfade)
```

---

## Lizenz & Anerkennung

Die psychologischen Konzepte basieren auf:
- Cognitive-Behavioral Therapy (Beck, Ellis)
- Acceptance & Commitment Therapy (Hayes)
- Emotion Science (Gross, Ekman)
- Exposure & Response Prevention (Foa)

**Wichtig**: Die Inhalte sind für psychoedukative Zwecke und ersetzen keine professionelle psychologische Behandlung.

---

## Support

Bei Fragen zur Integration oder Erweiterung, siehe:
- AI_DEVELOPMENT_DOCUMENTATION.md
- COMPONENT_LIBRARY_GUIDE.md
- UX_DESIGN_DOCUMENTATION.md

