# Modularisierungs-Roadmap

## Zielbild
Das Projekt wird schrittweise von einer monolithischen App-Struktur zu einer modularen, wartbaren Architektur entwickelt.

## Geplante Modulgruppen

### `modules/core/`
Wiederverwendbare, plattformnahe und domänenübergreifende Bausteine.

- `common`: Basisklassen, Utilities, Result-/Error-Typen, Coroutines-Konventionen.
- `model`: Stabile Domain-Modelle und Interfaces.
- `ui`: Design Tokens, wiederverwendbare UI-Bausteine, Theme-Abstraktionen.

### `modules/features/`
Fachliche Features mit klaren Grenzen und eigener Verantwortlichkeit.

- `onboarding`
- `today`
- `insights`
- `settings`

Jedes Feature soll perspektivisch enthalten:
- `api/` (öffentliche Schnittstelle)
- `impl/` (interne Implementierung)
- ggf. `data/` und `ui/` innerhalb der Implementierung

### `modules/platform/`
Technische Adapter zu Android-/Infrastruktur-Themen.

- `database`
- `notifications`
- `export`

## Migrationsprinzipien

1. **Vertical Slice statt Big Bang**: Pro Feature schrittweise migrieren.
2. **API first**: Feature-Kommunikation nur über explizite Interfaces.
3. **Abhängigkeiten reduzieren**: Feature-Module kennen sich nicht direkt.
4. **Testbarkeit erhöhen**: Fachlogik aus UI/ViewModel extrahieren.
5. **Stabile Kernmodule**: `core/*` ist möglichst frei von Android-Details.

## Nächste konkrete Schritte

1. Erstes Feature (`today`) als Pilot in ein eigenes Modul auslagern.
2. Gemeinsame Modelle und Utility-Klassen in `core/model` und `core/common` verschieben.
3. Notifications-/Export-Integrationen hinter Interfaces aus `platform/*` kapseln.
4. Build-Konfiguration modularisieren (Gradle includes + Version Catalog beibehalten).
5. CI-Checks je Modul aktivieren.
