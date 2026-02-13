# Module Workspace

Dieser Ordner dient als strukturierter Zielraum für die schrittweise Modularisierung.

## Struktur

- `core/`: Wiederverwendbare, feature-unabhängige Basismodule.
- `features/`: Fachliche Features als getrennte, evolvierbare Einheiten.
- `platform/`: Infrastruktur- und Android-nahe Adapter.

## Konventionen

- Klare Modulverantwortung pro Ordner.
- Öffentliche APIs explizit markieren; interne Implementierungen kapseln.
- Keine zyklischen Abhängigkeiten zwischen Feature-Modulen.
- Cross-Cutting Code zentral in `core/*` oder `platform/*` halten.
