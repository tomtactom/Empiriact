# Empiriact – Vollständige KI-optimierte Entwicklungsdokumentation

> Ziel dieses Dokuments: **Ein einziges, verlässliches Arbeitsdokument** für Codex und andere KI-Entwicklungsmodelle (z. B. GPT-basierte Coder, Claude Code, Gemini Code Assist), damit Änderungen schnell, konsistent und testbar umgesetzt werden können.

## 1) Projektüberblick

**Empiriact** ist eine Android-App (Kotlin + Jetpack Compose), die Übungen, Check-ins und Reflexionsfunktionen für mentale Gesundheit, Achtsamkeit und Verhaltensänderung bereitstellt.

### Kern-Stack
- Kotlin 2.2.10
- Android Gradle Plugin 9.0.0
- Jetpack Compose (BOM 2026.01.00)
- Navigation Compose
- Room (KSP)
- DataStore Preferences
- WorkManager
- Ktor + Kotlinx Serialization

### Plattformziele
- `minSdk = 33`
- `targetSdk = 36`
- `compileSdk = 36`

---

## 2) Schnellstart für KI-Agenten (empfohlenes Standardprotokoll)

### 2.1 Einmaliger Setup-Check
```bash
./gradlew --version
./gradlew tasks --all > /tmp/empiriact-gradle-tasks.txt
```

### 2.2 Standard-Validierung bei Änderungen
```bash
./gradlew compileDebugKotlin
./gradlew test
```

### 2.3 Wenn UI/Navigation betroffen ist
```bash
./gradlew assembleDebug
```

### 2.4 Wenn Datenpersistenz betroffen ist (Room/Repository)
```bash
./gradlew testDebugUnitTest
```

---

## 3) Repository-Karte (relevante Bereiche)

## Root
- `README.md`: Kurzbeschreibung
- `README_PROJECT.md`: ausführlicheres Projekt-README
- `BUILD_AND_TEST_GUIDE.md`, `TESTING_GUIDE.md`: Test- und Build-Hinweise
- `DOCUMENTATION_INDEX.md`: Dokumentationsverzeichnis

## App-Modul
- `app/src/main/java/com/empiriact/app/MainActivity.kt`: Einstiegspunkt Activity
- `app/src/main/java/com/empiriact/app/EmpiriactApplication.kt`: Application + zentrale Repositories + ViewModelFactory
- `app/src/main/java/com/empiriact/app/EmpiriactApp.kt`: Compose-App-Wurzel
- `app/src/main/java/com/empiriact/app/ui/navigation/`: Routing/NavGraph
- `app/src/main/java/com/empiriact/app/ui/screens/`: Feature-Screens + ViewModels
- `app/src/main/java/com/empiriact/app/data/`: Data Layer (inkl. teils historischer Strukturen)
- `app/src/main/java/com/empiriact/app/data/db/`: Room-Entities, DAOs, Datenbank

---

## 4) Laufzeitarchitektur (mental model für KI)

1. `MainActivity` startet Compose-UI und holt `EmpiriactApplication.viewModelFactory`.
2. `EmpiriactApp` rendert `EmpiriactNavGraph` innerhalb `MaterialTheme`.
3. `EmpiriactNavGraph` steuert Bottom-Navigation + Screen-Routen.
4. Screens arbeiten mit ViewModels.
5. ViewModels beziehen Repositories/Services aus `ViewModelFactory`.
6. Datenzugriff erfolgt über Room-DAOs und ggf. Assets/JSON/Services.

### Wichtige Implikation
Wenn du als KI einen Feature-Fix machst, ist die häufigste Änderungskette:

`Screen` → `ViewModel` → `Repository` → `DAO/Entity` (optional)

---

## 5) Navigation & zentrale Routen

Die Route-Definitionen liegen in `ui/navigation/Route.kt`.

Top-Level-Navigation (Bottom Bar):
- `today`
- `checkin`
- `overview`
- `learn`
- `leo`
- `resources`

Weitere wichtige Routen:
- `questionnaire_detail/{questionnaireId}`
- `exercise_rating/{exerciseId}/{from}`
- Übungen mit Herkunftsparameter `{from}` (z. B. selective/shared attention)

### KI-Hinweis
- Nutze bei neuen Übungen möglichst bestehende Route-Muster mit `createRoute(...)`.
- Für Rücknavigation immer den `from`-Kontext durchreichen, damit UX-Flows stabil bleiben.

---

## 6) Datenebene: Struktur und Vorsichtspunkte

### 6.1 Aktive Datenanbindung
`EmpiriactApplication` verwendet die Room-DB aus `data/db/EmpiriactDatabase.kt` und erstellt u. a.:
- `ActivityLogRepository`
- `ExerciseRepository`
- `GratitudeRepository`
- `CheckinRepository`
- `CourseRepository`

### 6.2 Historische/duplizierte Strukturen im Repository
Im Projekt existieren parallel Dateien mit ähnlichen Namen unter:
- `data/`
- `data/db/`
- `data/repo/`

Für KI-Agenten gilt:
1. **Immer zuerst den tatsächlichen Laufzeitpfad prüfen** (`EmpiriactApplication`, `ViewModelFactory`, konkrete Imports in ViewModel/Screen).
2. Nicht „blind“ Klassen mit gleichem Namen ändern.
3. Bei Unsicherheit zuerst Call-Sites mit `rg` prüfen.

---

## 7) Entwicklungsregeln für Codex/andere KI-Modelle

## 7.1 Änderungsprinzipien
- Kleine, klar isolierte Patches bevorzugen.
- Vorhandene Patterns wiederverwenden (Theme, Komponenten, Navigation, Repository-Stil).
- Keine großen Umstrukturierungen ohne explizite Anforderung.

## 7.2 Compose- und UI-Regeln
- Material 3 Stil beibehalten.
- Bestehende gemeinsame Komponenten in `ui/common/` priorisieren.
- Accessibility beachten (Labels/ContentDescription, Touch-Targets, Kontrast).

## 7.3 Daten- und Persistenzregeln
- Schema-/Entity-Änderungen nur wenn zwingend nötig.
- DAO-Signaturen konsistent zu Repository und Tests halten.
- Bei Datenänderungen Unit-Tests im `app/src/test` Bereich ergänzen/anpassen.

## 7.4 Fehlervermeidung für KI
- Vor Rename/Move: alle Referenzen prüfen (`rg "ClassName|functionName" ...`).
- Navigation-Änderungen immer auf Argumente (`{from}`, IDs) validieren.
- Keine "Dead imports" oder ungenutzte Codepfade zurücklassen.

---

## 8) Vorgehens-Playbooks (für typische KI-Aufgaben)

## A) Neue Übung hinzufügen
1. Übungs-Screen in `ui/screens/resources/methods/` ergänzen.
2. Route in `Route.kt` ergänzen (`object ... : Route(...)`).
3. Route im `EmpiriactNavGraph` registrieren.
4. Einstiegspunkt in `ResourcesScreen` oder passender Quelle ergänzen.
5. Falls Bewertung benötigt: Übergang zu `exercise_rating/{exerciseId}/{from}` ergänzen.
6. Build + Tests ausführen.

## B) Bestehenden Screen verbessern
1. Screen-Datei + zugehöriges ViewModel identifizieren.
2. UI-State-Fluss prüfen.
3. Änderung minimal umsetzen.
4. `compileDebugKotlin` + relevante Tests laufen lassen.

## C) Datenproblem (Room/Repository)
1. Tatsächlich verwendete DAO/Entity ermitteln (Imports prüfen).
2. Repository-Methode anpassen.
3. Unit-Test für Fehlerfall + Erfolgsfall ergänzen.
4. Testlauf durchführen.

---

## 9) Qualitäts-Gate (Definition of Done für KI-Patches)

Ein Patch gilt als „fertig“, wenn:
1. Projekt kompiliert (`compileDebugKotlin` oder stärker).
2. Betroffene Tests laufen.
3. Keine offensichtlichen Navigation-/State-Regressions.
4. Dokumentation angepasst wurde (bei Feature-/Architekturänderung).

Empfohlener Minimal-Check:
```bash
./gradlew compileDebugKotlin
./gradlew test
```

---

## 10) Prompt-Vorlagen für KI-Modelle

## 10.1 Bugfix-Prompt (robust)
```text
Du arbeitest im Android-Projekt Empiriact (Kotlin, Compose, Room).
Ziel: [Bug kurz beschreiben].
Bitte:
1) Analysiere zuerst den Laufzeitpfad (Activity → NavGraph → Screen/ViewModel → Repository/DAO).
2) Implementiere einen minimalen Fix ohne Architekturumbau.
3) Führe compileDebugKotlin + relevante Tests aus.
4) Gib eine Zusammenfassung mit geänderten Dateien und Risiken.
```

## 10.2 Feature-Prompt (neuer Screen/Funktion)
```text
Implementiere in Empiriact das Feature: [Beschreibung].
Bedingungen:
- Bestehende UI-Patterns und Navigation beibehalten.
- Falls neue Route nötig: in Route.kt + EmpiriactNavGraph eintragen.
- Falls Datenpersistenz nötig: Repository/DAO konsistent erweitern.
- Danach compileDebugKotlin und test ausführen.
```

## 10.3 Refactor-Prompt (sicher)
```text
Führe einen konservativen Refactor in Empiriact durch: [Bereich].
Nicht erlaubt: Verhalten ändern, große Dateiverschiebungen, API-Brüche.
Erlaubt: Lesbarkeit, Namensverbesserung, Entkopplung kleiner Hilfsfunktionen.
Validierung: compileDebugKotlin + test.
```

---

## 11) Bekannte Risiken / technische Schulden

- Parallele Datenstrukturen (`data/` vs `data/db/` vs `data/repo/`) erhöhen Verwechslungsgefahr.
- Einzelne Dateien wirken historisch/ungenutzt; vor Löschungen immer Referenzen prüfen.
- Navigation enthält mehrere parametrisierte Übungsrouten; inkonsistente `from`-Werte können Rücksprünge brechen.

---

## 12) Empfohlene nächste Dokumentationsschritte

1. Eine „Source-of-Truth“-Liste für **aktive** DAOs/Repositories ergänzen.
2. Architekturdiagramm (Mermaid) für App-Flow hinzufügen.
3. Modulweise Test-Matrix (Screen ↔ ViewModel ↔ Repository) dokumentieren.
4. Optional: `docs/ai/`-Ordner mit task-spezifischen Runbooks aufbauen.

---

## 13) Kurzreferenz (für schnelle KI-Läufe)

```bash
# Projekt prüfen
./gradlew compileDebugKotlin

# Unit-Tests
./gradlew test

# Debug-Build
./gradlew assembleDebug

# Nur App-Modul-Tests
./gradlew :app:testDebugUnitTest
```

---

## 14) Maintainer-Notiz

Dieses Dokument ist bewusst auf **maschinenlesbare, handlungsorientierte Abschnitte** ausgelegt:
- klare Checklisten,
- reproduzierbare Befehle,
- eindeutige Dateipfade,
- risikoarme Arbeitsabläufe.

Damit eignet es sich sowohl für menschliche Entwickler als auch für agentische KI-Workflows.
