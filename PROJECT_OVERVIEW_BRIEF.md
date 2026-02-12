# Empiriact – Projektüberblick (Engineer Kickoff)

## 1) Rahmen
- **Produkttyp:** Offline-first Android-App für Wellness/mentale Selbstbeobachtung mit Schwerpunkt auf Aufmerksamkeitstraining, Lerninhalten, Tagesprotokollen und wertebasierter Reflexion.
- **Plattform:** Android (Kotlin + Jetpack Compose), lokale Persistenz via Room, Einstellungen via DataStore, Hintergrundjobs via WorkManager.
- **Navigation:** Mehrere Hauptbereiche über Bottom Navigation (u.a. Heute, Check-in, Übersicht, Lernen, Leo, Ressourcen).

## 2) Rahmendaten
- **App-ID/Version:** `com.empiriact.app`, Version `0.01`.
- **SDK-Stand:** compileSdk 36, minSdk 33, targetSdk 36.
- **Runtime:** Java 17.
- **Architektur:** MVVM + StateFlow, Repository-Pattern.
- **Persistenz:** Room-DB mit mehreren Entitäten (u.a. ActivityLog, Routine, Evaluation, UserValue, ExerciseRating, Gratitude).
- **Privacy-by-design:** Primär lokale Datenspeicherung; Export als CSV/JSON ist vorgesehen; optionale Data Donation ist technisch vorbereitet.

## 3) Prozessziel (fachlich)
Die App führt Nutzer:innen entlang eines Verhaltensaktivierungs-/Selbstbeobachtungsprozesses:
1. **Beobachten:** Stündliches Protokollieren von Aktivitäten + Stimmung/Valenz.
2. **Verstehen:** Musteranalyse (Mood Booster/Damper) aus Verlaufsdaten.
3. **Intervenieren:** Übungen (u.a. Selective, Shared, Attention Switching) und Lernmodule.
4. **Ausrichten:** Werteklärung + werteorientierte Planung.
5. **Reflektieren:** Check-ins, Bewertungen und Erkenntnisse zur Stabilisierung.

## 4) Zielvision
- **Kurzfristig:** Alltagstaugliche, niedrigschwellige Unterstützung zur Selbstregulation und Stimmungsstabilisierung.
- **Mittelfristig:** Kontinuierliche Lern- und Reflexionsschleifen, die individuelle Wirksamkeit sichtbar machen.
- **Langfristig:** Eine robuste, privacy-fokussierte Begleit-App für bewusstes, werteorientiertes Handeln im Alltag.

## 5) Engineer-Startpunkt (empfohlen)
1. Build/Run lokal (`assembleDebug`, `installDebug`).
2. Hauptfluss testen: Heute → Übersicht (Analyse) → Ressourcen/Übungen → Lernen → Check-in.
3. Datenfluss nachvollziehen: ViewModel ↔ Repository ↔ Room.
4. Risiko-/Gap-Check: DataDonation Endpoint Placeholder, TODOs/Phase-2-Punkte, Dokumentations-Konsistenz.
