# Psychoedukatives Modul - UX/Design Dokumentation

## Überblick der User Experience

Das psychoedukative Modul wurde mit modernstem UX-Design-Thinking entwickelt, um maximale Engagement und Lerneffektivität zu erreichen.

---

## 🎯 Design-Philosophie

### 1. **Progressive Disclosure** (Schrittweise Offenbarung)
Nutzer sollten nicht mit Informationen überfordert werden. Das Modul nutzt mehrere Strategien:

- **Kapitel-basierte Struktur**: Inhalte sind in verdaubare Einheiten aufgeteilt
- **Expandierbare Sektionen**: Nutzer können wählen, wie tief sie einsteigen
- **Step-by-Step Übungen**: Jeder Schritt ist zeitlich begrenzt (30-120 Sekunden)
- **Visuelle Progression**: Progress-Balken zeigt, wie weit der Nutzer ist

### 2. **Cognitive Load Reduction** (Mentale Belastung reduzieren)
Die Cognitive Load Theory besagt, dass das Kurzzeitgedächtnis begrenzt ist.

**Implementierung:**
- Kurze, prägnante Texte (max. 2-3 Sätze pro Absatz)
- Bullet Points für Hervorhebung von Punkten
- Klare visuelle Hierarchie durch Größe und Farbe
- Icons für schnelle Erkennbarkeit
- Beispiele in eigenen, abgetrennten Boxen

### 3. **Intrinsic Motivation** (Innere Motivation)
Nutzer sollten intrinsisch motiviert sein, nicht durch externe Belohnung:

- **Autonomie**: Nutzer wählen, welche Module und Übungen
- **Kompetenz**: Fortschritt ist sichtbar, Erfolg ist greifbar
- **Relatedness**: Inhalte sind relevant für persönliche Werte
- **Mastery**: Schwierigkeitsstufen ermöglichen schrittwise Verbesserung

### 4. **Emotional Design** (Emotionales Design)
Psychoedukation ist emotional sensibel. Das Modul nutzt:

- **Warme Farben**: Indigo, Grün, Pink für Vertrautheit
- **Sanfte Animationen**: Nicht aufdringlich, unterstützend
- **Positive Sprache**: "Du kannst lernen und wachsen"
- **Selbstmitgefühl**: Tipps sind ermutigend, nicht richtend

---

## 🎨 Visual Design System

### Farbpalette

| Farbe | Hex Code | Nutzung | Psychologie |
|-------|----------|---------|-------------|
| **Indigo** | #6366F1 | Emotionsregulation | Beruhigend, Vertrauenswürdig |
| **Bernstein** | #F59E0B | Angststörungen | Warnung, Aufmerksamkeit |
| **Grün** | #10B981 | Kognitive Defusion | Hoffnung, Wachstum |
| **Pink** | #EC4899 | Werteorientierung | Liebe, Mitgefühl |
| **Grau** | #6B7280 | Neutrale Inhalte | Professionell, Seriös |

### Typography

```
Headlines:
- Headline Small: 24sp, Bold (Modul-Titel)
- Title Medium: 16sp, SemiBold (Kapitel-Titel)
- Title Small: 14sp, SemiBold (Sektions-Überschrift)

Body:
- Body Large: 16sp (Haupttext)
- Body Medium: 14sp (Sekundärtext)
- Body Small: 12sp (Kleine Notizen)

Labels:
- Label Large: 12sp, SemiBold (Badges, Labels)
- Label Small: 11sp (Status, Unterstützungstext)
```

### Spacing-System

```kotlin
// Standard Padding/Margin
val spacing_2 = 2.dp   // Micro
val spacing_4 = 4.dp   // Minimal
val spacing_8 = 8.dp   // Small
val spacing_12 = 12.dp // Medium
val spacing_16 = 16.dp // Standard
val spacing_20 = 20.dp // Large
val spacing_24 = 24.dp // Extra Large
val spacing_32 = 32.dp // XXL
```

Das Modul nutzt primär **16dp** als Standard, mit **12dp** für kompaktere Bereiche.

---

## 🖼️ Layout-Patterns

### 1. **Card-basierte Hierarchie**
Jede Sektion ist in einer Card. Dies hilft:
- Scannen (Augen folgen natürlich den Blöcken)
- Fokus (Jede Card ist ein Thema)
- Touch-targets (Mehr Platz zum Drücken)

```kotlin
Card(
    shape = RoundedCornerShape(12.dp),
    modifier = Modifier
        .fillMaxWidth()
        .shadow(2.dp, RoundedCornerShape(12.dp))
)
```

### 2. **Linear Reading Flow**
Der Inhalt folgt einem Z-Muster:
1. Kopf (Title, Progress)
2. Inhalt (Scrollable)
3. Aktionen (Buttons unten)

Dies ist natürlich für unsere Leserichtung (links-nach-rechts, oben-nach-unten).

### 3. **Icon Utilization**
Icons werden strategisch platziert:
- **Leading Icons**: Für Kategorisierung (z.B. Timer-Icon)
- **Trailing Icons**: Für Aktion (z.B. Expand-Arrow)
- **Hero Icons**: Große Icons für Modul-Identifikation
- **Status Icons**: Checkmarks für Completion

---

## 🎬 Animation & Micro-Interactions

### Transition Patterns

#### 1. **Screen-to-Screen (Crossfade)**
```kotlin
Crossfade(
    targetState = selectedModuleId,
    animationSpec = tween(300)
)
```
**Nutzen**: Smooth, nicht ablenkend, zeigt Zustandswechsel

#### 2. **Content Expansion (ExpandVertically)**
```kotlin
AnimatedVisibility(
    visible = isExpanded,
    enter = expandVertically(tween(300)) + fadeIn(),
    exit = shrinkVertically(tween(300)) + fadeOut()
)
```
**Nutzen**: Zeigt progressive Offenbarung, nicht überwältigend

#### 3. **Color Changes (animateColorAsState)**
```kotlin
val buttonColor by animateColorAsState(
    targetValue = if (isFavorite) Color.Gold else Color.Gray
)
```
**Nutzen**: Subtil, zeigt Statuswechsel

### Timing

- **Schnelle Animationen** (150-200ms): UI-Feedback (Button-Klick)
- **Mittlere Animationen** (300ms): Screen-Übergänge
- **Langsame Animationen** (500ms+): Keine (zu distrahierend beim Lernen)

---

## 📱 Responsive Design

Das Modul passt sich verschiedenen Bildschirmgrößen an:

### Phone (360-480dp)
- Single Column Layout
- Größere Touch-Targets (min. 48dp)
- Weniger seitliches Padding (12dp statt 16dp)

### Tablet (600dp+)
- Zwei-Spalten-Layout möglich
- Mehr Whitespace
- Größere Text-Größen

Die aktuelle Implementierung passt sich automatisch durch:
```kotlin
Row(modifier = Modifier.fillMaxWidth()) {
    // Responsive Content
}
```

---

## ♿ Accessibility (Barrierefreiheit)

### Screen-Reader Support
```kotlin
Icon(
    imageVector = Icons.Default.CheckCircle,
    contentDescription = "Modul abgeschlossen"  // <-- Wichtig!
)

Box(modifier = Modifier.semantics {
    contentDescription = "Seite 1 von 3"
})
```

### Color Contrast
- Text auf Light Background: Mindestens 4.5:1 Kontrast
- Das Modul nutzt Material3 ColorScheme, das bereits optimiert ist

### Touch Targets
- Minimum: 48dp x 48dp
- Das Modul hat überall mind. 40dp x 40dp

### Text Readability
- Zeilenhöhe: 1.5 (Material3 Standard)
- Keine zu langen Zeilen (max. 50 Zeichen)
- Serifenlose Fonts (bessere Lesbarkeit)

---

## 🧠 Cognitive Psychology Principles

### 1. **Chunking**
Information ist in "Chunks" organisiert:
- Ein Kapitel pro Konzept
- 3-5 Bullet Points pro Card
- 5-7 Schritte pro Übung

### 2. **Spaced Repetition**
Konzepte werden mehrfach präsentiert:
- Intro-Seite
- Detaillierte Erklärung
- Beispiele
- "Important Takeaways"

### 3. **Active Learning**
Nutzer sind nicht passiv:
- Expandierbare Sektionen (Nutzer kontrolliert)
- Interaktive Übungen (Hands-on)
- Wahl der Lernpfade (Autonomie)

### 4. **Scaffolding**
Jede Übung bietet Unterstützung:
- Schritt-Titels (Was mache ich?)
- Anweisungen (Wie mache ich es?)
- Guidance (Warum mache ich es?)
- Tips (Wie mache ich es besser?)

---

## 📊 User Flow Diagramme

### Main Navigation

```
┌─────────────────────┐
│   Psychoeducation   │
├─────────────────────┤
│  [Module List]      │
│  ┌───────────────┐  │
│  │ Module 1      │  │
│  │ Module 2      │  │
│  │ Module 3      │  │
│  │ Module 4      │  │
│  └───────────────┘  │
└──────────┬──────────┘
           │
        [Tap]
           │
      ┌────▼──────┐
      │   Chapter 1       │
      │   [Expand Sections] │
      │   [Key Takeaways]   │
      │   [Next Button]     │
      └────┬──────┘
           │
        [Next]
           │
      ┌────▼──────┐
      │   Chapter 2       │
      └────┬──────┘
           │
        [Finish]
           │
           ▼
      [Back to List]
```

### Exercise Flow

```
┌──────────────────┐
│ Exercise List    │
├──────────────────┤
│ [Exercise 1]     │
│ [Exercise 2]     │
│ [Exercise 3]     │
└────┬─────────────┘
     │
  [Tap]
     │
  ┌──▼──────────────┐
  │ Exercise Preview │
  │ [Title]         │
  │ [Description]   │
  │ [Duration]      │
  │ [Benefits]      │
  │ [Start Button]  │
  └──┬──────────────┘
     │
  [Start]
     │
  ┌──▼──────────────┐
  │ Step 1/6        │
  │ [Timer: 60s]    │
  │ [Guidance]      │
  │ [Tips]          │
  │ [Next Button]   │
  └──┬──────────────┘
     │
  [Next] ...repeat...
     │
  ┌──▼──────────────┐
  │ Completion      │
  │ "Gut gemacht!"  │
  │ [Back Button]   │
  └─────────────────┘
```

---

## 🚀 Performance & Best Practices

### Rendering Performance
- LazyColumn ist verwendet für lange Listen
- remember{} cacht teure Berechnungen
- Crossfade statt ComplexAnimations

### Memory Usage
- Daten sind in-memory (können zu Room migriert werden)
- Keine unbegrenzten Listen
- rememberSaveable erhält nur notwendige State

### Battery Impact
- Keine continuous Animationen
- Einfache Transitions
- Keine ständigen API-Calls

---

## 🔧 Customization Guide

### Farben ändern
```kotlin
// In getPsychoeducationModules():
color = Color(0xFFFFFFFF) // Neue Farbe
```

### Text-Inhalte ändern
```kotlin
// Alle Texte sind in den data classes definiert
// Einfach die Strings ändern:
title = "Neuer Titel"
content = "Neuer Inhalt"
```

### Icons ändern
```kotlin
icon = Icons.Default.NewIcon // Aus Material Icons wählen
```

### Neue Komponenten hinzufügen
```kotlin
// Neue Card Type:
@Composable
private fun CustomCard(...) {
    Card(...) { ... }
}
```

---

## 📈 Analytics & Tracking

Empfohlene Metriken zur Verfolgung:

```kotlin
// Module View
analytics.logEvent("module_viewed", mapOf(
    "module_id" to moduleId,
    "module_name" to moduleName
))

// Chapter Completion
analytics.logEvent("chapter_completed", mapOf(
    "module_id" to moduleId,
    "chapter_number" to chapterIndex
))

// Exercise Started/Completed
analytics.logEvent("exercise_started", mapOf(
    "exercise_id" to exerciseId
))

analytics.logEvent("exercise_completed", mapOf(
    "exercise_id" to exerciseId,
    "duration_seconds" to elapsedTime
))

// Resource Bookmarked
analytics.logEvent("resource_bookmarked", mapOf(
    "resource_id" to resourceId,
    "resource_type" to type
))
```

---

## ✅ Testing Checklist

- [ ] Alle Module laden schnell
- [ ] Expandierbare Sektionen funktionieren reibungslos
- [ ] Navigation ist intuitiv
- [ ] Text ist lesbar auf allen Bildschirmgrößen
- [ ] Icons sind erkennbar
- [ ] Farben haben ausreichend Kontrast
- [ ] Touch-Targets sind mindestens 48dp
- [ ] Screen-Reader funktioniert mit Inhalten
- [ ] Animationen sind nicht ablenkend
- [ ] Favoriten-Funktion funktioniert
- [ ] Suchfunktion ist präzise
- [ ] Filter funktionieren korrekt
- [ ] Progress wird gespeichert (beim Implementieren)

---

## 📚 Weitere Ressourcen

- Material Design 3: https://m3.material.io/
- Cognitive Load Theory: Sweller, 1988
- Emotional Design: Norman, 2004
- ACT & Values: Hayes et al., 2006
- WCAG 2.1 Accessibility: https://www.w3.org/WAI/WCAG21/quickref/

---

**Version**: 1.0
**Letzte Aktualisierung**: 2026-02-15
**Autor**: AI Development Team

