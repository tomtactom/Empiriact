# 🎯 Quick Reference: Grübeln Modul

## 🚀 Schneller Überblick

**Was:** Ein psychoedukatives Modul zum Verständnis von Rumination (Grübeln)  
**Wo:** App > "Module" Tab > "Grübeln: Gedankenkaugummi"  
**Zeit:** ~15 Minuten  
**Typ:** [BEISPIELMODUL] - Template für zukünftige Module  

---

## 📱 In der App

### Navigation
1. Öffne die App
2. Klick auf "Module" in der unteren Navigationsleiste
3. Wähle "Grübeln: Gedankenkaugummi [BEISPIELMODUL]"
4. Lese die 5 expandierbaren Abschnitte
5. Navigiere mit "Weiter" / "Zurück" Buttons
6. Am Ende: Bewerte das Modul (😞 bis 😄)

### Features
- 📖 **Lesen:** 5 Abschnitte zu Rumination
- 📖 **Expandierbar:** Klick auf einen Abschnitt um zu lesen/zu verstecken
- 🔖 **Lesezeichen:** Klick auf Icon oben rechts
- ⭐ **Rating:** Nach Abschluss 5-stufiges Feedback-System
- 🌙 **Dark Mode:** Automatisch angepasst

---

## 💻 Für Entwickler

### Dateien
| Datei | Rolle |
|-------|-------|
| `GruebelnModuleScreen.kt` | Hauptimplementierung des Moduls |
| `PsychoeducationalModuleEntity.kt` | Datenbank-Entity |
| `PsychoeducationalModuleDao.kt` | Datenbank-Access |
| `PsychoeducationalModuleRepository.kt` | Business-Logic |

### Wichtige Code-Snippets

#### State definieren
```kotlin
var currentStep by remember { mutableStateOf(0) }
var isBookmarked by remember { mutableStateOf(false) }
var selectedRating by remember { mutableStateOf<RumationRating?>(null) }
```

#### Inhalts-Abschnitte definieren
```kotlin
val sections = listOf(
    ModuleSection(
        title = "Titel",
        icon = "🧠",
        content = "Dein Inhalt..."
    ),
    // ...
)
```

#### Navigation registrieren
```kotlin
// In Route.kt:
object GruebelnModule : Route("gruebeln_module")

// In EmpiriactNavGraph.kt:
composable(Route.GruebelnModule.route) {
    GruebelnModuleScreen(onBack = { navController.popBackStack() })
}

// In PsychoeducationModulesScreen.kt:
ModuleItem(
    title = "Grübeln: Gedankenkaugummi",
    description = "[BEISPIELMODUL] ...",
    icon = Icons.Default.School,
    color = Color(0xFFEC4899),
    route = "gruebeln_module",
    estimatedTime = "~15 min"
)
```

---

## 🎨 Design Quick Reference

### Farben
```kotlin
// Primär (Rosa/Magenta)
Color(0xFFEC4899)

// Alternativ
Color(0xFF6366F1)  // Indigo
Color(0xFF10B981)  // Grün
Color(0xFFF59E0B)  // Amber
```

### Spacing
```kotlin
// Horizontal
padding = 16.dp

// Vertical
verticalArrangement = Arrangement.spacedBy(12.dp)
```

### Typography
```kotlin
// Überschrift
style = MaterialTheme.typography.titleSmall
fontWeight = FontWeight.Bold

// Body
style = MaterialTheme.typography.bodySmall
color = MaterialTheme.colorScheme.onSurface
```

---

## 📊 Rating System

```kotlin
enum class RumationRating(val value: Int, val label: String, val emoji: String) {
    STRONGLY_NEGATIVE(-2, "Sehr negativ", "😞"),
    NEGATIVE(-1, "Negativ", "😕"),
    NEUTRAL(0, "Neutral", "😐"),
    POSITIVE(1, "Positiv", "🙂"),
    STRONGLY_POSITIVE(2, "Sehr positiv", "😄")
}
```

---

## 🔐 Datenbank

### Entity
```kotlin
@Entity(tableName = "psychoeducational_modules")
data class PsychoeducationalModuleEntity(
    @PrimaryKey val moduleId: String,
    val moduleTitle: String,
    val category: String,
    val isCompleted: Boolean = false,
    val rating: Int? = null,  // -2 bis +2
    val completedAt: Long? = null,
    val startedAt: Long = System.currentTimeMillis(),
    val isBookmarked: Boolean = false,
    val isExample: Boolean = false
)
```

### Operationen
```kotlin
// Modul erstellen
moduleRepository.createModule("gruebeln_module", "Grübeln", "Psychoedukation", isExample = true)

// Lesezeichen togglen
moduleRepository.toggleBookmark("gruebeln_module", true)

// Als abgeschlossen markieren
moduleRepository.markAsCompleted("gruebeln_module")

// Bewertung speichern
moduleRepository.setModuleRating("gruebeln_module", 1)
```

---

## ✅ Checkliste: Neues Modul erstellen

```
Schritt 1: Vorlage kopieren
- [ ] GruebelnModuleScreen.kt als Template nutzen
- [ ] Umbenennen zu [ThemeName]ModuleScreen.kt

Schritt 2: Inhalte einfügen
- [ ] Rating Enum definieren
- [ ] 3-5 ModuleSection Objekte erstellen
- [ ] Texte mit deinem Inhalt ausfüllen

Schritt 3: Design anpassen
- [ ] Farbe wählen (aus Palette)
- [ ] Icon/Emoji für jeden Abschnitt
- [ ] Estimated Time aktualisieren

Schritt 4: Navigation
- [ ] Route in Route.kt hinzufügen
- [ ] composable() in EmpiriactNavGraph.kt hinzufügen
- [ ] ModuleItem in PsychoeducationModulesScreen.kt hinzufügen

Schritt 5: Testen
- [ ] App kompiliert ohne Fehler
- [ ] Modul ist in der Liste sichtbar
- [ ] Navigation funktioniert
- [ ] Dark Mode sieht gut aus
- [ ] Text ist lesbar
```

---

## 🎬 Beispiel: Ein neues Modul in 5 Minuten

1. **Kopiere die Datei:**
   ```
   GruebelnModuleScreen.kt → AchtsamkeitModuleScreen.kt
   ```

2. **Ändere den Paket-Namen & Funktionsnamen:**
   ```kotlin
   fun AchtsamkeitModuleScreen(onBack: () -> Unit)
   ```

3. **Definiere dein Rating-System:**
   ```kotlin
   enum class AchtsamkeitRating(...)
   ```

4. **Erstelle Inhalts-Abschnitte:**
   ```kotlin
   val sections = listOf(
       ModuleSection("Was ist Achtsamkeit?", "🧘", "..."),
       ModuleSection("Wie übe ich?", "🙏", "..."),
       // ...
   )
   ```

5. **Registriere in Routes & Navigation** (siehe oben)

**Fertig! Dein Modul ist live.** 🚀

---

## 🧪 Testing

### Manuelles Testen
```
1. Öffne App
2. Navigiere zu "Module"
3. Klick auf dein Modul
4. Überprüfe:
   - Alle Abschnitte expandieren
   - Text ist lesbar
   - Navigation funktioniert
   - Rating-Buttons funktionieren
   - Dark Mode sieht gut aus
```

### Automatische Tests (noch zu implementieren)
```kotlin
// ViewModel Test
@Test
fun testModuleCompletion() { ... }

// UI Test
@Test
fun testRatingButtonClick() { ... }
```

---

## 🔗 Verwandte Dokumente

- **Umfassender Guide:** `PSYCHOEDUCATION_MODULE_SYSTEM_NEW.md`
- **Modul-Details:** `GRUEBELN_MODULE_README.md`
- **Implementierungs-Übersicht:** `IMPLEMENTATION_SUMMARY.md`
- **GitHub-Guide:** `GITHUB_UPLOAD_GUIDE.md`

---

## 💬 FAQ

**F: Wo finde ich das Grübeln-Modul?**  
A: App → "Module" Tab → "Grübeln: Gedankenkaugummi"

**F: Kann ich mein eigenes Modul erstellen?**  
A: Ja! Siehe "Quick Reference: Neues Modul in 5 Minuten"

**F: Wird meine Bewertung gespeichert?**  
A: Noch nicht. Das wird in der nächsten Version implementiert.

**F: Funktioniert das Modul im Dark Mode?**  
A: Ja! Vollständig automatisch.

**F: Kann ich das Modul als PDF exportieren?**  
A: Noch nicht. Auf der Roadmap.

---

**Version:** 1.0 Quick Reference  
**Status:** ✅ Aktiv  
**Letzte Aktualisierung:** 2026-02-19

