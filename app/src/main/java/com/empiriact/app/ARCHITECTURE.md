# Empiriact – Modulare Dateisystem-Struktur

Diese Ordnerstruktur ist auf **langfristige Modularisierung** ausgelegt und trennt Verantwortlichkeiten klar nach technischer Rolle und Feature-Kontext.

## Zielbild

- `core/`
  - Wiederverwendbare, feature-unabhängige Bausteine.
  - Beispiele: Datenzugriff, Navigation, Theme, plattformnahe Services.
- `feature/`
  - Fachliche/produktnahe Screens und Flows, nach Features gruppiert.

## Aktuelle Struktur

```text
com/empiriact/app
├── core
│   ├── data
│   │   ├── db
│   │   ├── datastore
│   │   ├── model
│   │   └── repo
│   ├── platform
│   │   ├── notifications
│   │   └── services
│   └── ui
│       ├── common
│       ├── navigation
│       └── theme
└── feature
    └── screens
```

## Architektur-Regeln (ab jetzt)

1. **Neue Business-Logik gehört in `feature/...`** und darf nur über definierte Abhängigkeiten auf `core` zugreifen.
2. **`core` kennt keine Feature-spezifischen UI-Flows.**
3. **Neue persistente Modelle/DAOs** zuerst in `core/data` einordnen und dann über Repositories in Features verwenden.
4. **Cross-cutting Services** (Export, Scheduling, Notification) in `core/platform` platzieren.
5. Bei größeren Erweiterungen: Unterordner pro Feature anlegen, z. B. `feature/checkin`, `feature/today`, `feature/values` statt zentralem `feature/screens`.

## Nächster Schritt (empfohlen)

Im nächsten Refactor sollten die Paketnamen schrittweise an die neue Ordnerstruktur angepasst werden
(z. B. `com.empiriact.app.feature.checkin`), um die physische und logische Modulgrenze vollständig anzugleichen.
