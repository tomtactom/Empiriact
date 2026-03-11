# 📚 Psychoedukatives Modul Baukastensystem v2 - Neue Implementierung

## 🚀 Neuer Ansatz: Modernes Design mit vollständiger UX

Zusätzlich zur bestehenden Module Builder Methode gibt es jetzt einen **neuen, modernen Ansatz** für psychoedukative Module mit innovativem UI/UX Design und Datenbank-Persistierung.

---

## 📊 Vergleich: Alter vs. Neuer Ansatz

| Aspekt | Module Builder (Alt) | Screen-basiert (Neu) |
|--------|---------------------|----------------------|
| Struktur | Deklarativ, Builder-Pattern | Composable Screen |
| Content | Kapitel + Sektionen | Seite-basiert mit Progress |
| UI Layout | Vordefinierteres Template | Vollständig anpassbar |
| Datenbank | Keine persistente Speicherung | ✅ Volle DB-Integration |
| Lesezeichen | ❌ Nicht unterstützt | ✅ Unterstützt |
| Rating-System | Einfach (0-5 Sterne) | ✅ Erweitert (-2 bis +2) |
| Dark Mode | ✅ Supported | ✅ Full Support |
| Komplexität | Einfacher für Standard-Inhalte | Mächtig für Custom-Designs |
| Kontrastanpassung | Teilweise | ✅ Vollständig |

---

## 🏗️ Neue Architektur

```
GruebelnModuleScreen.kt (Beispiel)
├── Data Classes
│   ├── ModuleSection (Title, Icon, Content)
│   └── RumationRating (Enum mit Emoji)
├── State Management
│   ├── currentStep (Seite-Navigation)
│   ├── isBookmarked (Lesezeichen)
│   ├── selectedRating (Nutzer-Feedback)
│   └── expandedSection (Expandable Content)
├── UI Components
│   ├── ExpandableSection (Wiederverwendbar)
│   ├── RatingButton (Wiederverwendbar)
│   └── Header + Navigation
└── Database Integration (Optional)
    ├── PsychoeducationalModuleEntity
    ├── PsychoeducationalModuleRepository
    └── DAO Queries
```

---

## 📋 Schritt-für-Schritt Anleitung

### Schritt 1: Dateistruktur erstellen

```
src/main/java/com/empiriact/app/ui/screens/modules/
├── GruebelnModuleScreen.kt (✅ Beispiel - kopiere als Template!)
├── [NewTheme]ModuleScreen.kt (Dein neues Modul)
└── PsychoeducationModulesScreen.kt (Modul-Übersicht)
```

### Schritt 2: Neue Modul-Datei erstellen

Verwende `GruebelnModuleScreen.kt` als Vorlage und kopiere es als `[ThemeName]ModuleScreen.kt`.

**Beispiel-Template:**

```kotlin
package com.empiriact.app.ui.screens.modules

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.material.icons.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// 1. Define Rating Enum
enum class [ThemeName]Rating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}

// 2. Define Content Structure
data class ModuleSection(
    val title: String,
    val icon: String,  // Unicode Emoji
    val content: String
)

// 3. Main Composable
@Composable
fun [ThemeName]ModuleScreen(onBack: () -> Unit) {
    // State
    var currentStep by remember { mutableStateOf(0) }
    var isBookmarked by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf<[ThemeName]Rating?>(null) }
    var expandedSection by remember { mutableStateOf(-1) }
    
    // Content Definition
    val sections = listOf(
        ModuleSection(
            title = "Abschnitt 1",
            icon = "🧠",
            content = "Hier kommt der Inhalt..."
        ),
        // ... weitere Abschnitte
    )
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Header mit Lesezeichen
        // Progress Bar
        // Expandierbare Inhalts-Abschnitte
        // Navigation Buttons
        // Rating Section (am Ende)
    }
}

// 4. Reusable Components
@Composable
private fun ExpandableSection(
    section: ModuleSection,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    isCurrent: Boolean
) {
    // Implementation
}

@Composable
private fun RatingButton(
    rating: [ThemeName]Rating,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Implementation
}
```

### Schritt 3: Inhalte strukturieren

Teile deinen psychoedukativen Inhalt in 3-5 **Abschnitte** auf:

```kotlin
val sections = listOf(
    ModuleSection(
        title = "Was ist [Thema]?",
        icon = "❓",
        content = "Einführung und Definition..."
    ),
    ModuleSection(
        title = "Wie wirkt es sich aus?",
        icon = "⚡",
        content = "Auswirkungen und Folgen..."
    ),
    ModuleSection(
        title = "Was kann ich tun?",
        icon = "✨",
        content = "Praktische Tipps und Strategien..."
    ),
)
```

### Schritt 4: Route registrieren

#### 4.1 `Route.kt` aktualisieren:

```kotlin
object [ThemeName]Module : Route("[themename]_module")
```

#### 4.2 `EmpiriactNavGraph.kt` aktualisieren:

```kotlin
// Im Import:
import com.empiriact.app.ui.screens.modules.[ThemeName]ModuleScreen

// Im modularGraph():
composable(Route.[ThemeName]Module.route) { 
    [ThemeName]ModuleScreen(onBack = { navController.popBackStack() })
}
```

### Schritt 5: In Modulübersicht eintragen

Bearbeite `PsychoeducationModulesScreen.kt`:

```kotlin
ModuleItem(
    title = "[Dein Modultitel]",
    description = "[BEISPIELMODUL] Kurze Beschreibung",
    icon = Icons.Default.[IconName],
    color = Color(0xFF[HexColor]),
    route = "[themename]_module",
    estimatedTime = "~15 min"
)
```

### Schritt 6: (Optional) Datenbank-Integration

Falls du Fortschritt, Lesezeichen und Bewertungen speichern möchtest:

```kotlin
// 1. Erstelle ViewModel
class [ThemeName]ModuleViewModel(
    private val moduleRepo: PsychoeducationalModuleRepository
) : ViewModel() {
    fun saveProgress(moduleId: String, rating: Int) {
        viewModelScope.launch {
            moduleRepo.markAsCompleted(moduleId)
            moduleRepo.setModuleRating(moduleId, rating)
        }
    }
}

// 2. Verwende in Screen
@Composable
fun [ThemeName]ModuleScreen(
    onBack: () -> Unit,
    viewModel: [ThemeName]ModuleViewModel = hiltViewModel()
) {
    // ...
}
```

---

## 🎨 Design-Guidelines

### Farbpalette

```kotlin
// Primäre Farben für Module
val Colors = mapOf(
    "Rosa/Magenta" to Color(0xFFEC4899),
    "Indigo" to Color(0xFF6366F1),
    "Grün" to Color(0xFF10B981),
    "Amber" to Color(0xFFF59E0B),
    "Cyan" to Color(0xFF06B6D4),
    "Orange" to Color(0xFFF97316)
)
```

### Typography & Contrast

```kotlin
// Überschriften
Text(
    text = "...",
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurface  // ✅ Contrast checked
)

// Body Text
Text(
    text = "...",
    style = MaterialTheme.typography.bodySmall,
    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),  // ✅ WCAG AA
    lineHeight = 20.sp
)

// Labels
Text(
    text = "...",
    style = MaterialTheme.typography.labelSmall,
    color = MaterialTheme.colorScheme.onSurfaceVariant  // ✅ Subtle but readable
)
```

### Spacing Standards

```kotlin
// Horizontal Padding
Modifier.padding(horizontal = 16.dp)  // Standard horizontal

// Vertical Gaps
verticalArrangement = Arrangement.spacedBy(12.dp)  // Sections
verticalArrangement = Arrangement.spacedBy(8.dp)   // Elemente

// Card/Box Padding
Modifier.padding(16.dp)  // Standard für Cards
```

### Dark Mode Support

Material3 mit `isSystemInDarkTheme()` automatisch angepasst:

```kotlin
color = MaterialTheme.colorScheme.onSurface  // ✅ Auto light/dark
background = MaterialTheme.colorScheme.surface  // ✅ Auto light/dark
```

---

## 💾 Datenspeicherung

### Datenbank-Schema

```kotlin
@Entity(tableName = "psychoeducational_modules")
data class PsychoeducationalModuleEntity(
    @PrimaryKey val moduleId: String,
    val moduleTitle: String,
    val category: String,
    val isCompleted: Boolean = false,
    val rating: Int? = null,  // -2 bis +2
    val feedback: String? = null,
    val completedAt: Long? = null,
    val startedAt: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = false,
    val isExample: Boolean = false
)
```

### Beispiel: Fortschritt speichern

```kotlin
// Am Ende des Moduls:
Button(onClick = {
    viewModelScope.launch {
        moduleRepository.markAsCompleted("gruebeln_module")
        moduleRepository.setModuleRating("gruebeln_module", selectedRating.value)
    }
    navController.popBackStack()
}) {
    Text("Fertig!")
}
```

---

## ✅ Quality Checklist

- [ ] **Struktur:** Modul-Datei erstellt mit klarer Hierarchie
- [ ] **Inhalte:** 3-5 expandierbare Abschnitte mit klarem Inhalt
- [ ] **UI:** Header, Progress Bar, Seiten-Navigation, Rating
- [ ] **Navigation:** Route + NavGraph + ModuleItem eingetragen
- [ ] **Design:** 
  - [ ] Farbschema gewählt (aus Palette)
  - [ ] Kontrast überprüft (Text lesbar auf Background)
  - [ ] Emojis für visuelle Unterscheidung
  - [ ] Spacing konsistent
- [ ] **Dark Mode:** Mit dunklem Theme getestet
- [ ] **Accessibility:**
  - [ ] Text-Größen ausreichend (14.sp minimum)
  - [ ] Kontrast WCAG AA+ (4.5:1 für Text)
  - [ ] Touch-Targets ≥48.dp
- [ ] **Performance:** Keine laggy Animationen
- [ ] **Testing:** Auf verschiedenen Bildschirmen getestet
- [ ] **[BEISPIELMODUL] Tag:** Falls Beispielmodul, kennzeichnen

---

## 📊 Fortgeschrittene Features

### Feature: Lesezeichen

```kotlin
IconButton(onClick = { isBookmarked = !isBookmarked }) {
    Icon(
        imageVector = if (isBookmarked) Icons.Default.BookmarkAdded 
                      else Icons.Default.BookmarkAdd,
        contentDescription = "Lesezeichen",
        tint = if (isBookmarked) MaterialTheme.colorScheme.primary 
               else MaterialTheme.colorScheme.onSurfaceVariant
    )
}
```

### Feature: Expandable Content

```kotlin
AnimatedVisibility(
    visible = isExpanded,
    enter = expandVertically(),
    exit = shrinkVertically()
) {
    Text(section.content)
}
```

### Feature: Rating mit Emoji

```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
    [RatingEnum].entries.forEach { rating ->
        Column(
            modifier = Modifier
                .clickable { selectedRating = rating }
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(rating.emoji, fontSize = 24.sp)
            Text(rating.label, style = MaterialTheme.typography.labelSmall)
        }
    }
}
```

---

## 🔗 Verwandte Dateien

- **Beispiel-Modul:** `GruebelnModuleScreen.kt`
- **Datenbank:** `PsychoeducationalModuleEntity.kt`, `PsychoeducationalModuleDao.kt`
- **Repository:** `PsychoeducationalModuleRepository.kt`
- **Navigation:** `Route.kt`, `EmpiriactNavGraph.kt`
- **UI Übersicht:** `PsychoeducationModulesScreen.kt`

---

## 🎯 Fazit

Das neue System bietet:
- ✅ **Moderne UI/UX** mit Animations und Interaktionen
- ✅ **Volle Kontrolle** über das Design jedes Moduls
- ✅ **Persistierung** von Nutzer-Daten (Fortschritt, Bewertungen)
- ✅ **Accessibility** mit Material3 und Dark Mode Support
- ✅ **Skalierbarkeit** für zukünftige Module

Viel Erfolg beim Erstellen neuer psychoedukativer Module! 🚀

---

**Versio** 2.0 (Neu)  
**Status:** ✅ Produktionsreif  
**Letzte Aktualisierung:** 2026-02-19

