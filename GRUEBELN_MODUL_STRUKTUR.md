# Grübeln-Module Struktur & Integration

## Übersicht

Die 6 Grübel-Module wurden sinnvoll fusioniert und unter "Psychoedukation" in der Navigation integriert. Dies schafft eine kohärente Lernreihenfolge.

## Fusionierte Module

### 1. **Grübeln & Rumination** (gruebeln_module)
**Fusioniert aus:**
- "Grübeln: Gedankenkaugummi" (Definition, Wiederkäuen, normale Ausprägungen)
- "Grübeln & Negative Folgen" (Emotionale/Körperliche/Soziale Auswirkungen, Kreislauf)

**Ziel:** 
- Definition und Verständnis von Grübeln
- Erkennung der negativen Konsequenzen
- Verständnis des Grübel-Kreislaufs

**Inhalte:**
- Definition: Grübeln = Wiederkäuen belastender Gedanken
- Normalität von Grübeln in Maßen
- Häufige Themen (Selbstkritik, Zukunftsängste, Ärger)
- Unproduktivität des Grübelns
- Negative Folgen (Stimmung, körperliche Zustände, psychische Erkrankungen, soziale Probleme)
- Verstärkungsmechanismus durch Grübeln

---

### 2. **Denkstile verstehen** (denkstile_module)
**Basiert auf:**
- "Haben Sie sich einen schlechten Denkstil angewöhnt?" 
- Vergleich: Ungünstige vs. Günstige Denkstile

**Ziel:**
- Unterscheidung zwischen produktivem und unproduktivem Denken
- Erkennung von Denkmustern

**Inhalte:**
- **Ungünstige Denkstile:**
  - Automatisiert, plötzlich auftretend, kaum kontrollierbar
  - Sich wiederholend, sprunghaft/weitschweifig
  - Negativ, extrem, irrational
  - Unproduktiv
  - Abstrakt und verallgemeinernd
  - Starr und demotivierend

- **Günstige Denkstile:**
  - Kontrollierbar und lösungsorientiert
  - Fokussiert
  - Ausgewogen, rational, logisch
  - Produktiv
  - Konkret und situationsbezogen
  - Flexibel und motivierend

---

### 3. **RND verstehen & regulieren** (rnd_module)
**Fusioniert aus:**
- "Repetitives negatives Denken verstehen und regulieren" (Konzepte, Funktionen, Problematik)
- "Repetitives negatives Denken – unterschiedliche Inhalte, gleicher Prozess" (Verschiedene Ausprägungen)
- "Grübeln als ungünstige Gewohnheit – ein veränderbarer Denkstil" (Regulationsmöglichkeiten, Gewohnheitswechsel)

**Ziel:**
- Tieferes Verständnis von RND als transdiagnostischem Prozess
- Erkennung der verschiedenen Ausdrucksformen
- Strategien zur Regulation und Veränderung

**Inhalte:**
- Definition: RND = Wiederkehrende negative Gedanken
- Unterscheidung: Grübeln vs. Sorgen
- Zwölf häufige Ausprägungen (selbstbezogen, zukunftsfokussiert, sozial, etc.)
- Ursprüngliche Funktionen (Warnfunktion, Relevanzfunktion, etc.)
- Problematische Aspekte
- **Wichtiger Schwerpunkt: Gewohnheitswechsel**
  - RND als erlernte Denkgewohnheit (nicht als Persönlichkeit!)
  - Entstehung durch Wiederholung
  - Gegenkonditionierung
  - Neue Gewohnheiten durch Übung
  - Die Hoffnungsbotschaft: Veränderbarkeit

---

## Struktur in der Anwendung

### Navigation: `PsychoeducationModulesScreen`
```
Module
├── Psychoedukation
│   ├── Grübeln & Rumination (gruebeln_module)
│   ├── Denkstile verstehen (denkstile_module)
│   └── RND verstehen & regulieren (rnd_module)
├── Interaktive Übungen
│   ├── 5-4-3-2-1 Erdungsübung
│   ├── Progressive Muskelentspannung
│   ├── Gedanken-Etikettierung
│   ├── Roten Faden finden (interaktiv, Wolle-Metapher)
│   └── 3-Fragen-Daumenregel (Gedankenexperiment)
└── Weitere Inhalte
    ├── Ressourcen-Bibliothek
    └── Lernpfade
```

---

## Empfohlene Lernreihenfolge

1. **Grübeln & Rumination** (verstehen, was Grübeln ist)
2. **Denkstile verstehen** (unterscheiden guter/schlechter Denkstile)
3. **RND verstehen & regulieren** (tieferes Verständnis + Strategien)
4. **Interaktive Übungen** (praktische Anwendung)

---

## UX/Design-Überlegungen

### Datenklassen (öffentlich, nicht private!)
```kotlin
data class ModuleItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val route: String,
    val estimatedTime: String
)

data class ModuleSection(
    val sectionTitle: String,
    val sectionDescription: String,
    val sectionModules: List<ModuleItem>
)
```

### Farbcodierung
- Grübeln & Rumination: 🟣 Indigo (0xFF6366F1)
- Denkstile: 🟣 Violett (0xFF8B5CF6)
- RND: 🔵 Blau (0xFF3B82F6)

### Zeit-Schätzungen
- Grübeln & Rumination: ~45 min
- Denkstile: ~20 min
- RND verstehen & regulieren: ~35 min

---

## Kompilatoren-Status

✅ **Keine Redeclaration-Fehler**
✅ **Alle Routes korrekt definiert**
✅ **Datenklassen sind öffentlich (nicht private)**
✅ **Imports korrekt**

---

## Zukünftige Entwicklungen

Dieses Struktur-Schema kann als **Baukastensystem** für neue psychoedukative Module verwendet werden:

1. Erstelle einen neuen Screen (z.B. `AnxietyModuleScreen.kt`)
2. Definiere die Route in `Route.kt`
3. Registriere die Route in `EmpiriactNavGraph.kt`
4. Füge das Modul in `PsychoeducationModulesScreen.kt` hinzu
5. Verwende das einheitliche Design mit Icons, Farben und Time-Estimates

---

## Notiz für Developer

Das System ist modular und skalierbar. Neue Module folgen diesem Pattern:
- **Klare Lernziele** definieren
- **Sinnvolle Inhalte-Fusionierung** (nicht zu viele Module mit Überschneidungen)
- **Einheitliche UX** mit Card-basierten Navigation
- **Konsistente Farben und Icons**
- **Realistische Zeit-Schätzungen** für Nutzer-Erwartungen

