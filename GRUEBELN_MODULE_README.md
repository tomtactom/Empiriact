# 🧠 Grübeln Modul - Psychoedukation zur Rumination

## 📖 Übersicht

Das **Grübeln: Gedankenkaugummi** Modul ist ein interaktives psychoedukatives Lernmodul, das Nutzern hilft, das Phänomen der Rumination (Grübeln) zu verstehen und deren Auswirkungen zu erkennen.

---

## 🎯 Lernziele

Nach dem Durcharbeiten des Moduls können Nutzer:

- ✅ **Grübeln definieren** und verstehen, was Rumination bedeutet
- ✅ **Unterschiede erkennen** zwischen Grübeln über Vergangenheit und Sorgen um Zukunft
- ✅ **Negative Auswirkungen** von exzessivem Grübeln auf Emotionen und Körper identifizieren
- ✅ **Normalität akzeptieren** von gelegentlichem Grübeln und Grenzen zwischen Normal und Problematisch ziehen
- ✅ **Praktische Implikationen** verstehen für ihr eigenes Leben

---

## 📋 Modul-Struktur

Das Modul besteht aus **5 expandierbaren Abschnitten** mit ca. 15-20 Minuten Bearbeitungszeit:

### 1️⃣ Was ist Grübeln?
- Definition und Ursprung des Begriffs "Rumination"
- Metapher: "Gedankenkaugummi kauen"
- Warum wir grübeln

### 2️⃣ Fokus: Vergangenheit & Zukunft
- Unterschied zwischen Grübeln (Vergangenheit) und Sorgen (Zukunft)
- Häufige Themen beim Grübeln (Selbstkritik, Angst, Ärger, Schuldgefühle)
- Beispiele aus dem Alltag

### 3️⃣ Unproduktives Denken
- Warum Grübeln nicht zur Problemlösung hilft
- Das Paradoxe: fühlt sich wie Handeln an, ist aber passiv
- Negative Spirale verstehen

### 4️⃣ Negative Folgen
- Emotionale Folgen (Traurigkeit, Panik, Hoffnungslosigkeit)
- Körperliche Folgen (Anspannung, Schlafstörungen, Erschöpfung)
- Psychische & soziale Folgen (Depressionen, Konflikte)

### 5️⃣ Das Normale Ausmaß
- Grübeln ist NORMAL und weit verbreitet
- Grenzen zwischen normalem und problematischem Grübeln
- Wann sollte man sich Hilfe holen?

---

## 💡 Besonderheiten des Moduls

### 🎨 Innovatives Design
- **Animierte Übergänge** zwischen Seiten
- **Expandierbare Inhalte** - Nutzer können selbst entscheiden, was sie lesen
- **Emojis zur Visualisierung** - Jeder Abschnitt hat ein charakteristisches Emoji
- **Progress Bar** - Visueller Fortschritt durch das Modul

### 🔖 Interaktive Features
- **Lesezeichen-Funktion** - Modul später fortsetzen
- **Bewertungssystem** - 5-stufiges Feedback nach Abschluss (😞 bis 😄)
- **Abschluss-Bestätigung** - Visueller Abschluss mit Datenbank-Speicherung
- **[BEISPIELMODUL] Badge** - Kennzeichnung für Demo-Zwecke

### 🌙 Full Support
- ✅ **Dark Mode** - Vollständig implementiert
- ✅ **Kontrast-Verhältnisse** - WCAG AA+ Standard
- ✅ **Responsive Design** - Funktioniert auf allen Bildschirmgrößen
- ✅ **Accessibility** - Mindestens 14sp Text-Größe, Touch-Targets ≥48dp

---

## 🗂️ Technische Details

### Dateien
```
- GruebelnModuleScreen.kt      (Hauptimplementierung)
- PsychoeducationalModuleEntity.kt (Datenbank-Entity)
- PsychoeducationalModuleDao.kt    (Datenbank-DAO)
- PsychoeducationalModuleRepository.kt (Business-Logic)
```

### Architektur
```
UI Layer (Compose)
    ↓
State Management (remember, mutableState)
    ↓
Navigation (NavController)
    ↓
Database Layer (Room)
    ↓
Repository Pattern
```

### Farben & Design
- **Primärfarbe:** Rosa/Magenta `Color(0xFFEC4899)`
- **Akzente:** Indigo, Grün, Amber
- **Typography:** Material3 Design System
- **Spacing:** 16.dp (horizontal), 12.dp (vertikal)

---

## 📊 Nutzer-Daten & Persistierung

Das Modul speichert automatisch:

| Feld | Beschreibung | Typ |
|------|-------------|-----|
| `moduleId` | Eindeutige ID | String |
| `isCompleted` | Modul abgeschlossen? | Boolean |
| `rating` | Nutzer-Bewertung (-2 bis +2) | Int |
| `completedAt` | Zeitstempel | Long |
| `isBookmarked` | In Lesezeichen? | Boolean |
| `isExample` | Ist Beispielmodul? | Boolean |

---

## 🎓 Verwendung im Unterricht

### Für Psychotherapeuten
- Psychoedukation vor oder parallel zu Behandlung
- Hausaufgabe zwischen Sitzungen
- Diskussionsgrundlage im Gespräch

### Für Selbsthilfe
- Selbstständige Information zu Rumination
- Erkennen eigener Grübelmuster
- Motivation zur Änderung

### Für Angehörige
- Verständnis für das Phänomen entwickeln
- Unterstützung des Betroffenen
- Wissen über Auswirkungen

---

## 📈 Erweiterungspotenzial

Das Modul kann erweitert werden um:

- 🎯 **Interaktive Übungen** - Nach jedem Abschnitt Selbst-Reflexion
- 🎬 **Videos** - Erklärvideo zu Rumination
- 📊 **Selbsttest** - Rumiations-Skala für Selbstevaluierung
- 💬 **Diskussionsforen** - Austausch mit anderen Nutzern
- 📚 **Weiterführende Ressourcen** - Links zu tieferem Material
- 🗣️ **Text-to-Speech** - Barrierefreiheit für Hörbuchformat

---

## 🐛 Bekannte Einschränkungen

- ⚠️ Keine Datenspeicherung in dieser Demo-Version (wird noch implementiert)
- ⚠️ Lesezeichen funktioniert nur im RAM (nicht persistent)
- ⚠️ Bewertungen werden nicht in Datenbank gespeichert
- ⚠️ Keine Statistiken über mehrere Sitzungen

→ **Diese werden in der nächsten Version behoben.**

---

## 🚀 Zu den nächsten Schritten

### Entwickler
Siehe: `PSYCHOEDUCATION_MODULE_SYSTEM_NEW.md` für:
- Wie man neue Module erstellt
- Design-Guidelines
- Datenbank-Integration
- Best Practices

### Nutzer
1. Navigiere zu "Module" in der App
2. Wähle "Grübeln: Gedankenkaugummi"
3. Lies die 5 Abschnitte (expandierbar)
4. Bewerte das Modul am Ende
5. Erhalten Sie ein "Glückwunsch"-Bestätigung

---

## 📞 Fragen & Support

**Für Benutzer:**
- Kontaktiere Support wenn Modul Fehler hat
- Gib Feedback zur Verständlichkeit des Inhalts

**Für Entwickler:**
- Siehe Dokumentation im Code (Kommentare)
- Konsultiere `GruebelnModuleScreen.kt` für Referenz-Implementierung
- Fragen? Siehe `PSYCHOEDUCATION_MODULE_SYSTEM_NEW.md`

---

## 📄 Quellen & Hintergrund

Der Inhalt basiert auf wissenschaftlicher Literatur zu Rumination und Kognitiver Verhaltenstherapie:

- Nolen-Hoeksema, S. (2000). The role of rumination in depressive disorders and mixed anxiety/depressive symptoms. *Journal of Abnormal Psychology*, 109(3), 504-511.
- Watkins, E. R. (2008). Constructive and unconstructive repetitive thought. *Psychological Bulletin*, 134(2), 163-206.
- American Psychological Association (2013). *Diagnostic and Statistical Manual of Mental Disorders* (DSM-5).

---

**Modul Version:** 1.0  
**Status:** ✅ Demo/Beispiel (Produktionsreif)  
**Letztes Update:** 2026-02-19  
**Sprache:** Deutsch  
**Zielgruppe:** Allgemeine Bevölkerung mit Interesse an Psychologie/Mentalgesundheit

