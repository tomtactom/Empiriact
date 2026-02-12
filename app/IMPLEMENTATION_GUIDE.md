# Implementation Guide - Neue Features

## 🔧 Was wurde implementiert

### 1. OverviewScreen - Swipe-Navigation
**Datei:** `app/src/main/java/com/empiriact/app/ui/screens/overview/OverviewScreen.kt`

**Änderungen:**
- ✅ `HorizontalPager` statt `when`-Ausdruck
- ✅ `rememberPagerState` für Page-Tracking
- ✅ `PrimaryTabRow` mit `animateScrollToPage`
- ✅ Coroutine Scope für smooth animations

**Neue Imports:**
```kotlin
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch
```

**Code Pattern:**
```kotlin
@OptIn(ExperimentalFoundationApi::class)
val pagerState = rememberPagerState(pageCount = { tabs.size })
val coroutineScope = rememberCoroutineScope()

HorizontalPager(state = pagerState) { page ->
    when (page) { /* tab content */ }
}
```

---

### 2. LearnScreen - Subsections mit Swipe
**Datei:** `app/src/main/java/com/empiriact/app/ui/screens/learn/LearnScreen.kt`

**Änderungen:**
- ✅ 3 neue Tabs: "Module", "Mein Fortschritt", "Ressourcen"
- ✅ HorizontalPager-Navigation
- ✅ Separate Composable-Funktionen für jede Tab
- ✅ ModulesTab zeigt bestehende Module
- ✅ ProgressTab & ResourcesTab als Basis implementiert

**Neue Tabs:**
1. **ModulesTab** - Zeigt LearningModules List
2. **ProgressTab** - Placeholder für Lernfortschritt
3. **ResourcesTab** - Placeholder für zusätzliche Ressourcen

---

### 3. OnboardingScreen - Leo-Inhalte + Permissions
**Datei:** `app/src/main/java/com/empiriact/app/ui/screens/onboarding/OnboardingScreen.kt`

**Komplett umgeschrieben mit:**

#### A. Leo-Content Pages (10 Seiten)
```kotlin
data class OnboardingPage(
    val title: String,
    val text: String,
    val icon: ImageVector
)
```

**Pages:**
1. "Herzlich willkommen!" - Intro zu Empiract
2. "Du als Forscher*in" - Selbsterforschung
3. "Werteorientierte Aktivierung" - Methode
4. "Du bist Expert*in" - Eigenverantwortung
5. "Beobachten als erster Schritt" - Start
6. "Ehrlichkeit dir selbst gegenüber" - Authentizität
7. "Sinn statt positiv" - Werte-Fokus
8. "Werte als Kompass" - Orientierung
9. "Deine zentrale Rolle" - Empowerment
10. "Einladung zum Entdecken" - Call to Action

#### B. Notification Permission Page
- Icon: `Icons.Default.Notifications`
- Button: "Aktivieren" → `POST_NOTIFICATIONS` Permission
- Android 13+ aware (nur für TIRAMISU+)

#### C. Battery Optimization Page
- Icon: `Icons.Default.BatteryChargingFull`
- Button: "Einstellungen öffnen" → System Settings Intent
- OutlinedButton: "Fertig" → Zum Haupt-App

**Neue Imports:**
```kotlin
import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
```

---

### 4. AndroidManifest.xml - Permissions
**Datei:** `app/src/main/AndroidManifest.xml`

**Hinzugefügt:**
```xml
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
```

---

## 🎯 UX/Usability Features

### Intuitive Navigation
- 🔄 Horizontale Swipe-Gesten (wie Instagram, TikTok)
- 📍 Visuelles Feedback über PrimaryTabRow
- ⚡ Smooth Animations (Coroutine-basiert)

### Modern Design
- 📱 Material Design 3 Standard
- 🎨 Dynamische Icons & Farben
- ✨ Compose-Best-Practices

### User Empowerment
- 📖 Umfassende Leo-Einführung (10 Seiten)
- 🔔 Klare Permission-Erklärungen
- ⚙️ Optional: Battery Optimization

### Accessibility
- ♿ Touch-Targets 48dp+ (Material Design)
- 🎯 Clear Labels & Descriptions
- 🎨 High Contrast Icons

---

## 🧪 Testing Guide

### 1. Swipe-Navigation testen

**OverviewScreen:**
```
1. Launch App → Overview Tab
2. Swipe left → "Analyse" sollte sichtbar sein
3. Swipe left → "Ressourcen" sollte sichtbar sein
4. Swipe right → "Protokoll" sollte sichtbar sein
5. Click "Analyse" Tab → Swipe-Position sollte sich updaten
```

**LearnScreen:**
```
1. Navigate zu Learn Screen
2. Swipe left → "Mein Fortschritt" sollte sichtbar sein
3. Swipe left → "Ressourcen" sollte sichtbar sein
4. Swipe right → "Module" sollte sichtbar sein
```

### 2. Onboarding testen

```
1. Clear App Data / Fresh Install
2. App starten → OnboardingScreen sollte zeigen
3. Swipe durch 10 Leo-Pages
4. Permission Page → "Aktivieren" klicken
5. Battery Optimization Page → "Einstellungen öffnen" / "Fertig"
6. Hauptapp sollte laden
```

### 3. Permissions testen

```
Android 13+:
- POST_NOTIFICATIONS wird korrekt angefragt
- REQUEST_IGNORE_BATTERY_OPTIMIZATIONS funktioniert

Android 12:
- Permissions werden ignoriert (Build.VERSION.SDK_INT Check)
- Onboarding läuft trotzdem
```

---

## 📋 Integration Checklist

- [x] OverviewScreen mit HorizontalPager
- [x] LearnScreen mit 3 Tabs
- [x] OnboardingScreen mit Leo + Permissions
- [x] AndroidManifest.xml Permissions
- [x] UX Documentation
- [x] Implementation Guide
- [ ] Runtime Testing (Simulator/Device)
- [ ] Compile Check
- [ ] Permission Testing Android 12/13+

---

## 🚀 Nächste Schritte

### Unmittelbar (Fehlerbehoben):
1. Projekt kompilieren & Fehler beheben
2. Onboarding Flow testen
3. Swipe-Navigation validieren
4. Permissions auf Device testen

### Kurzfristig (Enhancements):
1. Swipe-Indikatoren (Dots) hinzufügen
2. Haptic Feedback bei Page Changes
3. Gesture-Tutorials
4. Progress-Tracking in "Mein Fortschritt"

### Mittelfristig (Polish):
1. Accessibility Testing
2. Offline-Funktionalität
3. Animation Refinement
4. Device-Specific Testing

---

## ⚠️ Known Issues & Considerations

### Permissions
- Android 13+ nur POST_NOTIFICATIONS
- Battery Optimization ist optional (Skip möglich)
- SCHEDULE_EXACT_ALARM für zukünftige Features

### Navigation
- HorizontalPager hat page=0 als default
- Swipe-Geschwindigkeit ist OS-abhängig
- Tactile feedback nur auf Devices mit Vibrator

### Performance
- 10 Leo-Pages könnte auf alten Devices laggen
- Lazy-Loading könnte optimiert werden
- Coroutine-Scope muss korrekt lifecycle-managed sein

---

## 📖 Referenzen & Links

### Jetpack Compose
- [HorizontalPager Docs](https://developer.android.com/jetpack/androidx/releases/compose-foundation)
- [Pager State](https://developer.android.com/reference/kotlin/androidx/compose/foundation/pager/PagerState)

### Material Design 3
- [Tabs Component](https://m3.material.io/components/tabs)
- [Navigation Patterns](https://m3.material.io/patterns/navigation)

### Android Permissions
- [Runtime Permissions](https://developer.android.com/training/permissions/requesting)
- [Notification Permissions (Android 13)](https://developer.android.com/develop/ui/views/notifications/notification-permission)
- [Battery Optimization](https://developer.android.com/training/monitoring-device-state/doze-standby)

---

## ✅ Completion Status

| Feature | Status | Notes |
|---------|--------|-------|
| Swipe Navigation OverviewScreen | ✅ Implementiert | HorizontalPager + PrimaryTabRow |
| Swipe Navigation LearnScreen | ✅ Implementiert | 3 Tabs mit Swiping |
| Leo-Inhalte in Onboarding | ✅ Implementiert | 10 Pages zum Swipen |
| Notification Permission | ✅ Implementiert | Android 13+ aware |
| Battery Optimization | ✅ Implementiert | Settings Intent + Skip |
| UX Documentation | ✅ Dokumentiert | Vollständig |
| Testing Guide | ✅ Dokumentiert | Schritt-für-Schritt |

