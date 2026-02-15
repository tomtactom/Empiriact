# 🏗️ Psychoedukation Module Builder - Developer Guide

## Übersicht

Das **Module Builder System** ist ein flexibles Baukastensystem zur schnellen und konsistenten Erstellung neuer psychoedukativer Module. Es abstrahiert die komplexe UI-Logik und konzentriert sich auf den Inhalt.

## 🎯 Wie es funktioniert

### Konzept
```
Dein Inhalt (Definition)
        ↓
Module Builder (Strukturierung)
        ↓
PsychoeducationScreen (Darstellung)
        ↓
Benutzer sieht Module
```

## 📋 Schritt-für-Schritt Guide

### Schritt 1: Verstehe die Struktur

Ein Modul hat diese Hierarchie:

```
Module
├── Kapitel 1
│   ├── Sektion 1 (expandierbar oder statisch)
│   │   └── 2-3 Beispiele
│   ├── Sektion 2
│   └── 3-5 Key Takeaways
│
└── Kapitel 2
    └── ...
```

### Schritt 2: Schreibe dein Modul

Verwende die Builder-Funktionen aus `PsychoeducationModuleBuilder.kt`:

```kotlin
fun createMyNewModule(): PsychoeducationModuleDefinition {
    return PsychoeducationModuleBuilder.createModule(
        id = "module_id",
        title = "Modul Titel",
        subtitle = "Kurzbeschreibung",
        icon = Icons.Default.School,
        color = Color(0xFF6366F1),
        estimatedReadTime = 8,
        difficulty = "Anfänger",
        category = "Kategorie",
        
        chaptersBuilder = {
            listOf(
                PsychoeducationModuleBuilder.createChapter(
                    id = "ch1",
                    title = "Kapitel 1",
                    content = "Intro-Text...",
                    
                    sectionsBuilder = {
                        listOf(
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Abschnitt Titel",
                                text = "Erklärung...",
                                examples = listOf("Beispiel 1", "Beispiel 2")
                            )
                        )
                    },
                    
                    takeawaysBuilder = {
                        listOf(
                            "Erkenntnisse 1",
                            "Erkenntnisse 2",
                            "Erkenntnisse 3"
                        )
                    }
                )
            )
        }
    )
}
```

### Schritt 3: Registriere das Modul

Öffne `PsychoeducationScreen.kt` und finde die Funktion `getPsychoeducationModules()`:

```kotlin
private fun getPsychoeducationModules(): List<PsychoeducationModule> {
    return listOf(
        createMyNewModule(),  // ← FÜGE HIER EIN
        // ... bestehende Module
    )
}
```

### Schritt 4: Teste

```bash
./gradlew compileDebugKotlin  # Kompiliere
# Öffne App → Module Tab → Dein Modul sollte erscheinen!
```

## 💡 Best Practices

### 1. Schreibe klare, prägnante Inhalte
```kotlin
// ✅ GUT
content = "Angst ist ein natürliches System, das manchmal überreagiert.",

// ❌ SCHLECHT
content = "Das System der Angst ist ein komplexes psychobiologisches Phänomen, das sich manifestiert..."
```

### 2. Nutze emoji für Visuelle Klarheit
```kotlin
examples = listOf(
    "🧠 Körper: Erhöhter Puls",
    "💭 Gedanke: Automatische Überzeugungen",
    "🎯 Verhalten: Fluchtimpulse"
)
```

### 3. Wähle aussagekräftige Farben
```kotlin
color = Color(0xFF6366F1), // Indigo - Emotionsregulation
color = Color(0xFFF59E0B), // Bernstein - Angststörungen
color = Color(0xFF10B981), // Grün - Kognitive Defusion
color = Color(0xFFEC4899), // Pink - Werteorientierung
```

### 4. Balanciere Theorie und Praxis
```kotlin
// Struktur sollte sein:
// 1. Erklären (Was ist es?)
// 2. Beispiel (Wie sieht das aus?)
// 3. Aktion (Was kann ich tun?)
```

## 📐 Templates

### Template: Grundlagen-Modul (2 Kapitel, Anfänger)

```kotlin
fun createNewModule(): PsychoeducationModuleDefinition {
    return PsychoeducationModuleBuilder.createModule(
        id = "new_module",
        title = "Modulname",
        subtitle = "Kurzbeschreibung",
        icon = Icons.Default.School,
        color = Color(0xFF3B82F6),
        estimatedReadTime = 6,
        difficulty = "Anfänger",
        category = "Kategorie",
        
        chaptersBuilder = {
            listOf(
                // KAPITEL 1: Grundlagen & Theorie
                PsychoeducationModuleBuilder.createChapter(
                    id = "ch1",
                    title = "Was ist [Konzept]?",
                    content = "Einführung ins Thema...",
                    
                    sectionsBuilder = {
                        listOf(
                            PsychoeducationModuleBuilder.createStaticSection(
                                heading = "Definition",
                                text = "Prägnante Erklärung..."
                            ),
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Warum ist das wichtig?",
                                text = "Relevanz erklären...",
                                examples = listOf("Beispiel 1", "Beispiel 2")
                            )
                        )
                    },
                    
                    takeawaysBuilder = {
                        listOf("Takeaway 1", "Takeaway 2", "Takeaway 3")
                    }
                ),
                
                // KAPITEL 2: Praktische Anwendung
                PsychoeducationModuleBuilder.createChapter(
                    id = "ch2",
                    title = "Praktische Anwendung",
                    content = "Wie du [Konzept] umsetzen kannst...",
                    
                    sectionsBuilder = {
                        listOf(
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Erste Schritte",
                                text = "Was zu tun ist...",
                                examples = listOf("Schritt 1", "Schritt 2")
                            )
                        )
                    },
                    
                    takeawaysBuilder = {
                        listOf("Takeaway 1", "Takeaway 2", "Takeaway 3")
                    }
                )
            )
        }
    )
}
```

## 🔍 Qualitäts-Checkliste

Vor dem Commit überprüfe:

- [ ] **Inhalte**
  - [ ] Mindestens 2 Kapitel
  - [ ] Jedes Kapitel mindestens 2 Sektionen
  - [ ] 3-5 Key Takeaways pro Kapitel
  - [ ] 2-3 Beispiele pro Sektion
  - [ ] Texte sind max. 200 Wörter

- [ ] **Stil**
  - [ ] "Du"-Form (persönlich)
  - [ ] Kurze, prägnante Sätze
  - [ ] Keine Jargon oder erklärt
  - [ ] Supportive, nicht moralisierend

- [ ] **Psychologie**
  - [ ] Evidenzbasiert
  - [ ] Praktisch anwendbar
  - [ ] Selbstmitgefühl-fokussiert
  - [ ] Klinisch korrekt

- [ ] **Technisch**
  - [ ] IDs sind eindeutig (lowercase)
  - [ ] Build kompiliert
  - [ ] In der App sichtbar
  - [ ] Navigation funktioniert

## 🧪 Testing

### Manual Test
1. Öffne App → Module Tab
2. Dein Modul sollte erscheinen
3. Klick auf Modul
4. Navigiere durch Kapitel (Vor/Zurück)
5. Öffne/schließe expandierbare Sektionen
6. Back-Button sollte zurück zur Modul-Liste führen

### Code Test
```bash
./gradlew compileDebugKotlin  # Keine Fehler?
./gradlew build                # Voller Build erfolgreich?
```

## 📚 Beispiel: "Achtsamkeit" Modul

Hier ist ein vollständiges Beispiel-Modul, das du als Template verwenden kannst:

```kotlin
fun createMindfulnessModule(): PsychoeducationModuleDefinition {
    return PsychoeducationModuleBuilder.createModule(
        id = "mindfulness",
        title = "Achtsamkeit",
        subtitle = "Präsent sein im Hier und Jetzt",
        icon = Icons.Default.School,
        color = Color(0xFF3B82F6),
        estimatedReadTime = 8,
        difficulty = "Anfänger",
        category = "Achtsamkeit",
        
        chaptersBuilder = {
            listOf(
                PsychoeducationModuleBuilder.createChapter(
                    id = "ch1",
                    title = "Was ist Achtsamkeit?",
                    content = "Achtsamkeit ist die Fähigkeit, mit voller Aufmerksamkeit im gegenwärtigen Moment zu sein – ohne Urteile.",
                    
                    sectionsBuilder = {
                        listOf(
                            PsychoeducationModuleBuilder.createStaticSection(
                                heading = "Die Kernelemente",
                                text = "Achtsamkeit hat drei wichtige Komponenten..."
                            ),
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Vorurteile über Achtsamkeit",
                                text = "Viele Missverständnisse halten Menschen davon ab, Achtsamkeit zu praktizieren.",
                                examples = listOf(
                                    "❌ 'Ich muss meinen Geist leeren' → ✅ 'Ich bemerke einfach, was ist'",
                                    "❌ 'Das braucht 1 Stunde pro Tag' → ✅ '5 Minuten reichen aus'"
                                )
                            )
                        )
                    },
                    
                    takeawaysBuilder = {
                        listOf(
                            "Achtsamkeit ist eine Fähigkeit, die du trainieren kannst",
                            "Dein Gehirn widerstrebt dem gegenwärtigen Moment – das ist normal",
                            "Mit Übung wird Achtsamkeit leichter und natürlicher"
                        )
                    }
                ),
                
                PsychoeducationModuleBuilder.createChapter(
                    id = "ch2",
                    title = "Praktische Übungen",
                    content = "Achtsamkeit ist nicht nur Theorie – sie ist eine Praxis.",
                    
                    sectionsBuilder = {
                        listOf(
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Body Scan Meditation",
                                text = "Eine einfache 5-Minuten-Übung zur Körper-Achtsamkeit...",
                                examples = listOf("Schritt 1", "Schritt 2", "Schritt 3")
                            ),
                            PsychoeducationModuleBuilder.createExpandableSection(
                                heading = "Achtsamkeit im Alltag",
                                text = "Du brauchst nicht meditieren – Achtsamkeit ist überall möglich...",
                                examples = listOf(
                                    "🍽️ Essen: Schmecke jeden Bissen",
                                    "🚶 Gehen: Spüre deine Füße"
                                )
                            )
                        )
                    },
                    
                    takeawaysBuilder = {
                        listOf(
                            "Achtsamkeit braucht keine spezielle Umgebung oder Zeit",
                            "Kleine, regelmäßige Praktiken sind wirksamer als lange, seltene",
                            "Mit Geduld wird Achtsamkeit deine Lebensqualität verbessern"
                        )
                    }
                )
            )
        }
    )
}
```

## 🎓 Nächste Schritte

1. **Lese** `PsychoeducationModuleBuilder.kt` für alle verfügbaren Builder-Funktionen
2. **Kopiere** das Template für dein neues Modul
3. **Schreibe** deine Inhalte
4. **Registriere** das Modul in `getPsychoeducationModules()`
5. **Teste** dein Modul in der App
6. **Commit** und fertig! 🎉

Viel Erfolg beim Erstellen deiner Module!

