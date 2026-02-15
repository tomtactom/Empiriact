# 🎨 Psychoedukatives Modul - Visuelle Architektur

## Gesamt-Struktur

```
┌─────────────────────────────────────────────────────────────────┐
│              PSYCHOEDUCATION SYSTEM ARCHITECTURE                 │
└─────────────────────────────────────────────────────────────────┘

                          ┌──────────────────┐
                          │   Empiriact App  │
                          └────────┬─────────┘
                                   │
                    ┌──────────────┼──────────────┐
                    │              │              │
        ┌───────────▼───┐  ┌──────▼──────┐  ┌───▼────────────┐
        │  Psycho-      │  │ Interactive │  │  Resource      │
        │  education    │  │  Exercises  │  │  Browser &     │
        │  Screen       │  │  Screen     │  │  Learning Path │
        └───────────────┘  └─────────────┘  └────────────────┘
             (4 Modules)       (3 Exercises)    (10+ Resources)
```

---

## 📚 Modul-System (Psychoeducation)

```
PSYCHOEDUCATION SCREEN
│
├─ MODULE LIST SCREEN
│  │
│  ├─ [Module Card 1] ──┐
│  │   Emotionsregulation
│  │   Anfänger | 8 min
│  │
│  ├─ [Module Card 2] ──┐
│  │   Angststörungen
│  │   Fortgeschrittene | 10 min
│  │
│  ├─ [Module Card 3] ──┐
│  │   Kognitive Defusion
│  │   Anfänger | 7 min
│  │
│  └─ [Module Card 4] ──┐
│      Werteorientierung
│      Alle | 9 min
│
└─ MODULE DETAIL SCREEN
   │
   ├─ Header
   │  ├─ Title
   │  ├─ Progress Bar (0%, 50%, 100%)
   │  └─ Chapter Indicator (1/2, 2/2)
   │
   ├─ Content
   │  │
   │  ├─ Chapter Title
   │  ├─ Intro Card
   │  │
   │  └─ Sections (Scrollable)
   │     │
   │     ├─ [Section 1 - Expandable]
   │     │  ├─ Heading
   │     │  ├─ Text
   │     │  └─ Examples (if isExpandable)
   │     │
   │     └─ [Section 2 - Static]
   │        ├─ Heading
   │        ├─ Text
   │        └─ Examples (always visible)
   │
   │  ├─ Key Takeaways Card
   │  │  ├─ Icon
   │  │  └─ Bullet Points (3x)
   │
   └─ Footer
      ├─ [← Back Button]
      └─ [Next Button →]
```

---

## 💪 Übungs-System (Interactive Exercises)

```
EXERCISE LIST SCREEN
│
├─ [Exercise Card 1]
│  │ 5-4-3-2-1 Grounding
│  │ 5 min | Anfänger | Erdung
│  └─ [Open →]
│
├─ [Exercise Card 2]
│  │ Progressive Relaxation
│  │ 10 min | Anfänger | Entspannung
│  └─ [Open →]
│
└─ [Exercise Card 3]
   │ Thought Labeling
   │ 7 min | Fortgeschrittene | Defusion
   └─ [Open →]

                          ↓
                          
EXERCISE PREVIEW SCREEN
│
├─ Title + Icon
├─ Info Cards
│  ├─ Duration: 5 min
│  ├─ Steps: 6
│  └─ Level: Anfänger
│
├─ Description
├─ Benefits (3x)
│  ├─ ✓ Benefit 1
│  ├─ ✓ Benefit 2
│  └─ ✓ Benefit 3
│
├─ Steps Overview (6x)
│  ├─ Step 1: Titel
│  ├─ Step 2: Titel
│  └─ ...
│
└─ [▶ Start Button]

                          ↓
                          
EXERCISE PROGRESS SCREEN
│
├─ Header
│  ├─ Progress Bar (1/6 → 2/6 → ... → 6/6)
│  └─ Step Indicator (Step 1 of 6)
│
├─ Content
│  │
│  ├─ Big Step Number (1)
│  ├─ Step Title
│  ├─ Step Instruction
│  │
│  ├─ Timer Box
│  │  └─ "60 Sekunden"
│  │
│  ├─ Guidance Card (Green)
│  │  ├─ 💡 Anleitung
│  │  └─ "Detaillierte Erklärung..."
│  │
│  └─ Tips Card (Orange)
│     ├─ Tipp 1
│     ├─ Tipp 2
│     └─ Tipp 3
│
└─ Footer
   ├─ [← Back] (disabled on step 1)
   └─ [Next →] / [Fertig ✓]
```

---

## 📚 Ressourcen-System (Resource Browser)

```
RESOURCE BROWSER SCREEN
│
├─ Header with Search
│  ├─ 🔍 Suchbar
│  └─ 👁 View Mode Toggle
│
├─ Category Filter
│  ├─ [All] ← selected
│  ├─ [Anxiety]
│  ├─ [Relaxation]
│  ├─ [Cognitive]
│  └─ ...
│
├─ Difficulty Filter
│  ├─ [All]
│  ├─ [Beginner] ← selected
│  └─ [Advanced]
│
├─ Results: 10 Resources Found
│
└─ Resource List (Scrollable)
   │
   ├─ [Resource Card 1]
   │  │ 📖 Title
   │  │ Description
   │  │ [Article] [Beginner] ⏱ 5 min
   │  │ Category: Anxiety
   │  │ [Open Button]
   │  └─ 🔖 Bookmark Icon
   │
   ├─ [Resource Card 2]
   │  │ 🏋️ Title
   │  │ Description
   │  │ [Exercise] [Beginner] ⏱ 10 min
   │  │ Category: Relaxation
   │  │ [Open Button]
   │  └─ 🔖 Bookmark Icon
   │
   └─ ...

                          ↓
                          
LEARNING PATH SCREEN
│
├─ Header
│  └─ "Dein Lernpfad"
│
├─ Overall Progress
│  │ 50% abgeschlossen
│  │ 6 of 12 Modules
│  └─ [████░░░░░░░░░░░░] Progress Bar
│
├─ Learning Path 1: "Angstabbau 101"
│  │ Von Grundlagen zu praktischen Strategien
│  │ [●●●●○] 75% Progress
│  │ 3/4 Modules completed
│  │ "CBT Fundamentals" ← Next Step
│
├─ Learning Path 2: "Emotionale Bewältigung"
│  │ Emotionen verstehen und regulieren
│  │ [●●○○○] 40% Progress
│  │ 2/5 Modules completed
│  │ "Emotional Regulation Skills" ← Next Step
│
├─ Learning Path 3: "Wertorientiertes Leben"
│  │ Deine Werte definieren und leben
│  │ [●○○○○] 20% Progress
│  │ 1/5 Modules completed
│  │ "Values Clarification" ← Next Step
│
└─ Recommended Next Steps
   ├─ "Cognitive Restructuring" (8 min, Advanced)
   └─ "ACT Exercise: Values" (10 min, Beginner)
```

---

## 🎨 Farbcodierung & Visuelles System

```
FARB-MAPPING
────────────────────────────────────────────────────────

Emotionsregulation (Module 1)
┌──────────────────────┐
│ ███ INDIGO #6366F1   │  ← Primary Color
│ Emotionsregulation   │  ← Secondary Text
│ Anfänger, 8 min      │  ← Meta
└──────────────────────┘

Angststörungen (Module 2)
┌──────────────────────┐
│ ███ BERNSTEIN #F59E0B │ ← Primary Color
│ Angststörungen       │  ← Secondary Text
│ Fortgeschrittene     │  ← Meta
└──────────────────────┘

Kognitive Defusion (Module 3)
┌──────────────────────┐
│ ███ GRÜN #10B981     │  ← Primary Color
│ Kognitive Defusion   │  ← Secondary Text
│ Anfänger, 7 min      │  ← Meta
└──────────────────────┘

Werteorientierung (Module 4)
┌──────────────────────┐
│ ███ PINK #EC4899     │  ← Primary Color
│ Werteorientierung    │  ← Secondary Text
│ Alle, 9 min          │  ← Meta
└──────────────────────┘

GRADIENT BEISPIEL
┌─────────────────────────────────────┐
│ ▓▓▓▓▓▓▓▓▓▓▓▓▓░░░░░░░░░░░░░░░░░░░ │ ← Vertical Gradient
│ (Primary Color → transparent)        │
│ (Für Hintergründe & Akzente)        │
└─────────────────────────────────────┘
```

---

## 📐 Layout-Grid (Responsive)

```
PHONE (360-480dp)
┌───────────────────┐
│ ║ HEADER ║       │  ← 16dp padding
│ ║               ║  │
│ ║ CONTENT ║      │  ← Single Column
│ ║               ║  │
│ ║ FOOTER  ║      │
└───────────────────┘
   Min width: 320dp
   Card spacing: 12dp
   Touch target: 48dp

TABLET (600dp+)
┌─────────────────────────────────────┐
│ ║ HEADER ║                        │  │
│ ║     LEFT PANEL     ║  RIGHT ║     │
│ ║ (300dp)            ║ (300dp) ║    │
│ ║                    ║         ║    │
│ ║ FOOTER ║                        │  │
└─────────────────────────────────────┘
   Two Column Layout
   More whitespace
   Larger touch targets
```

---

## 🔄 State & Navigation Flow

```
GLOBAL STATE
│
├─ selectedModuleId: String?
│  │ (null = List, "id" = Detail)
│  │
│  ├─ Transition: Crossfade(300ms)
│  └─ Effect: Navigate List ↔ Detail
│
├─ currentStepIndex: Int
│  │ (0-5 for 6-step exercise)
│  │
│  ├─ Transition: Smooth
│  └─ Effect: Show next step
│
└─ currentChapterIndex: Int
   │ (0-2 for 3-chapter module)
   │
   ├─ Transition: Smooth
   └─ Effect: Show next chapter


ANIMATION TIMELINE
──────────────────────────────────────

Screen Transition (Crossfade)
0ms: ════════════════════════════════
     Start: Opacity 100% (Screen 1)
     
150ms: ═════════════════════════════
      Mid: Opacity 50% (Both screens)
      
300ms: ════════════════════════════
      End: Opacity 100% (Screen 2)


Section Expansion (ExpandVertically)
0ms: ════════════════════════════════
     Start: Height 0dp
     
150ms: ═════════════════════════════
      Mid: Height 50%
      
300ms: ════════════════════════════
      End: Full Height
```

---

## 📊 Data Model Hierarchy

```
PsychoeducationModule (1)
└── chapters: List[Chapter] (2-3)
    ├── Chapter (1)
    │   └── sections: List[Section] (2-4)
    │       ├── Section (1)
    │       │   └── examples: List[String] (0-3)
    │       │
    │       └── Section (2)
    │           └── examples: List[String] (2-4)
    │
    └── keyTakeaways: List[String] (3)


InteractiveExercise (1)
└── steps: List[ExerciseStep] (5-7)
    ├── ExerciseStep (1)
    │   └── tips: List[String] (2-4)
    │
    └── ExerciseStep (2)
        └── tips: List[String] (3)


PsychologicalResource (1-10+)
├── category: String
├── difficulty: String
├── type: String (article, exercise, questionnaire)
└── estimatedTime: Int
```

---

## 🎬 Animation Timing

```
STANDARD TIMING GUIDE
─────────────────────

Fast Interactions (UI Feedback)
    Duration: 150-200ms
    Easing: LinearOutSlowInEasing
    Use: Button clicks, state changes

Medium Transitions (Screen Changes)
    Duration: 300ms
    Easing: LinearOutSlowInEasing
    Use: Screen fades, list updates

Slow Animations (Drawer/Modal)
    Duration: 500ms+
    Easing: LinearOutSlowInEasing
    Use: Navigation drawers (not used here)

NO Animations
    Learning content should not distract
    Subtle is always better


EASING FUNCTION EXAMPLE
─────────────────────────
```

animateColorAsState(
    targetValue = targetColor,
    animationSpec = tween(
        durationMillis = 300,
        easing = LinearOutSlowInEasing
    )
)
```

---

## ♿ Accessibility Map

```
SCREEN READER SUPPORT
─────────────────────

Icon Navigation (contentDescription required)
┌──────────────────────┐
│ 🔙 "Zurück Button"   │
│ (← Screen Reader reads)
└──────────────────────┘

Button with Text (automatic)
┌──────────────────────┐
│ [Next Module →]      │
│ (← Auto read: "Next Module Button")
└──────────────────────┘

Box with Semantic (required)
┌──────────────────────┐
│ ○ ○ ○ ○ ○            │
│ (← Semantics: "Page 3 of 5")
└──────────────────────┘

CONTRAST REQUIREMENTS
──────────────────────
Normal Text:    4.5:1
Large Text:     3:1
UI Components:  3:1
(Material3 handles this automatically)

TOUCH TARGET SIZES
───────────────────
Minimum:     48dp x 48dp
Recommended: 56dp x 56dp
Safe Area:   64dp x 64dp
(All buttons in this module are 48dp+)
```

---

## 📈 Performance Metrics

```
RENDERING PERFORMANCE
──────────────────────

LazyColumn (for Lists)
    Time to interactive: ~200ms
    Memory per item: ~2-4KB
    Smooth scrolling: 60 FPS

remember{} (for cached data)
    First render: Normal
    Recomposition: ~50ms (instant)
    Memory: One instance in RAM

rememberSaveable{} (for important state)
    Survives config changes
    Memory: Minimal
    Latency: <10ms

Crossfade Transition
    Duration: 300ms
    Frame rate: 60 FPS
    GPU acceleration: Yes
```

---

## 🗂️ File Organization

```
app/src/main/java/com/empiriact/app/ui/screens/resources/
│
├── PsychoeducationScreen.kt
│   ├── data models
│   ├── PsychoeducationScreen (main)
│   ├── PsychoeducationListScreen
│   ├── PsychoeducationDetailScreen
│   ├── ExpandableSection
│   ├── KeyTakeawaysCard
│   └── PagerDots
│
├── InteractiveExercisesScreen.kt
│   ├── data models
│   ├── InteractiveExercisesScreen (main)
│   ├── ExerciseListScreen
│   ├── ExerciseDetailScreen
│   ├── ExercisePreviewScreen
│   ├── ExerciseProgressScreen
│   ├── TimerBox
│   └── InfoItem
│
├── ResourceBrowserScreen.kt
│   ├── data models
│   ├── ResourceBrowserScreen (main)
│   ├── ResourceListScreen
│   ├── ResourceCard
│   ├── LearningPathScreen
│   ├── LearningPathCard
│   ├── NextStepCard
│   └── EmptyResourceState
│
└── INTEGRATION_GUIDE.kt
    └── Documentation only
```

---

## 🚀 Deployment Architecture

```
DEVELOPMENT → STAGING → PRODUCTION

┌─────────────────────────────────────┐
│ Source Code (GitHub)                │
│ ├── PsychoeducationScreen.kt        │
│ ├── InteractiveExercisesScreen.kt   │
│ └── ResourceBrowserScreen.kt        │
└─────────────────────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│ Build Process (Gradle)              │
│ └── Compile → Package → Sign        │
└─────────────────────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│ Testing (Optional)                  │
│ ├── Unit Tests                      │
│ ├── UI Tests                        │
│ └── Accessibility Tests             │
└─────────────────────────────────────┘
              │
              ↓
┌─────────────────────────────────────┐
│ Distribution                        │
│ ├── Google Play Store               │
│ ├── Internal Testing                │
│ └── Beta Distribution               │
└─────────────────────────────────────┘
```

---

**Generated:** 2026-02-15 | **Version:** 1.0 | **Status:** Complete

