# Empiriact (com.empiriact.app)

Grundgerüst für eine Offline-Android-App (Kotlin, Compose, Room, DataStore, WorkManager).

## Öffnen
- Android Studio: "Open" -> Projektordner auswählen
- Gradle Sync starten


## Stabiler Build-Setup
- Das Repository enthält ein robustes `gradlew`-Skript, das automatisch die in `gradle/wrapper/gradle-wrapper.properties` konfigurierte Gradle-Version lädt, falls lokal eine falsche Version installiert ist.
- Lege für lokale Builds eine `local.properties` Datei an (siehe `local.properties.example`) und setze dort deinen `sdk.dir`.
- So vermeiden wir maschinenspezifische Pfade im Repository und reduzieren Build-Fehler im Team/CI.

## Hinweis
- Benachrichtigungen: WorkManager plant einen Impuls um :50 und plant sich danach erneut.
- Eingabe-Fenster: :50 bis :10, Logik in TodayScreen.computePromptHour()


## KI-optimierte Dokumentation
- Vollständige KI-Developer-Doku: `AI_DEVELOPMENT_DOCUMENTATION.md`

## Modulare Zielstruktur (neu)
- Neue vorbereitende Modul-Struktur unter `modules/` für schrittweise Entkopplung.
- Architektur- und Migrationsplan unter `docs/architecture/MODULARISIERUNG_ROADMAP.md`.
- Ziel: feature-orientierte Entwicklung, klare Verantwortlichkeiten, bessere Wartbarkeit.
