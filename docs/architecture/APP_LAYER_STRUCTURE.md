# App-Layer-Struktur

Die App ist jetzt in klaren Ebenen organisiert, damit Features leichter erweiterbar bleiben.

## 1) Einleitung (Intro-Layer)
- Route: `entry`, `onboarding`
- Verantwortlich für den Erststart-Flow
- Prüft `onboarding_completed` aus `SettingsRepository`

## 2) Navigation & Menü
- Zentrale Steuerung in `ui/navigation/EmpiriactNavGraph.kt`
- Bottom-Navigation ist nur außerhalb des Intro-Layers sichtbar

## 3) Statische Seiten
- Heute (`today`)
- Check-in (`checkin` + Detailroute)
- Übersicht (`overview`)
- Einstellungen (`settings`)

## 4) Modulare Seiten
- Lernen (`learn` + Unterseiten)
- Ressourcen (`resources` + Übungen)
- Erweiterbare Modul-Slots (z. B. Diagnostik/Evaluations)

## 5) Support-Ebene (technisch)
- Persistenz und App-Zustand über `SettingsRepository`
- Route-Metadaten in `ui/navigation/AppLayer.kt`

Damit ist die Trennung zwischen Start-Logik, Navigation, statischen Bereichen und modularen Funktionen klar dokumentiert.
