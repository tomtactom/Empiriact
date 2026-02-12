# 🧪 Testing & Validation Guide

## ✅ Pre-Build Checks

### 1. Syntax & Import Validierung

Überprüfe, ob alle neuen Imports korrekt sind:

**OverviewScreen.kt:**
```kotlin
✓ import androidx.compose.foundation.ExperimentalFoundationApi
✓ import androidx.compose.foundation.pager.HorizontalPager
✓ import androidx.compose.foundation.pager.rememberPagerState
✓ import kotlinx.coroutines.launch
```

**LearnScreen.kt:**
```kotlin
✓ import androidx.compose.foundation.ExperimentalFoundationApi
✓ import androidx.compose.foundation.pager.HorizontalPager
✓ import androidx.compose.foundation.pager.rememberPagerState
✓ import androidx.compose.runtime.rememberCoroutineScope
```

**OnboardingScreen.kt:**
```kotlin
✓ import android.Manifest
✓ import android.content.Context
✓ import android.content.Intent
✓ import android.os.Build
✓ import android.provider.Settings
✓ import androidx.activity.compose.rememberLauncherForActivityResult
✓ import androidx.activity.result.contract.ActivityResultContracts
✓ import androidx.compose.foundation.ExperimentalFoundationApi
✓ import androidx.compose.foundation.pager.HorizontalPager
✓ import androidx.compose.foundation.pager.rememberPagerState
```

---

## 🔨 Build & Compile Steps

### Step 1: Projekt neu laden
```bash
# Terminal im Projekt-Root
./gradlew clean
./gradlew build
```

**Erwarteter Output:**
```
BUILD SUCCESSFUL in Xs
37 actionable tasks: 8 executed, 29 up-to-date
```

### Step 2: Kompilierung überprüfen
```bash
./gradlew compileDebugKotlin
```

**Erwartetes Ergebnis:** Keine Fehler, ggf. Warnungen zu @OptIn

---

## 📱 Runtime Testing Steps

### Test 1: Erstes App-Öffnen (Onboarding)

```
1. App clearen (Einstellungen → Apps → Empiriact → Storage → Clear Data)
2. App öffnen
3. ERWARTUNG: OnboardingScreen sollte zeigen

4. Seite lesen (Leo-Intro)
5. Swipe nach links → nächste Leo-Seite sollte smooth loaded sein
6. Repeat bis 10 Seiten durch
7. ERWARTUNG: Alle 10 Leo-Pages sind navigierbar

8. Nach Seite 10 → Notification Permission Page
9. Button "Aktivieren" klicken
10. Android: Permission Popup sollte erscheinen (nur Android 13+)
11. Genehmigen oder Ablehnen (beide sollten funktionieren)

12. Battery Optimization Page sollte zeigen
13. "Einstellungen öffnen" → Android Einstellungen sollten öffnen
14. Oder "Fertig" → zum Haupt-App

15. ERWARTUNG: App startet normal, TodayScreen zeigt
```

**Validation Kriterien:**
- ✅ Swipe-Navigation funktioniert (links/rechts)
- ✅ Keine Crashes während Swipen
- ✅ Animations sind smooth
- ✅ Permission-Requests funktionieren
- ✅ App startet nach Onboarding

---

### Test 2: Overview Screen Swipe-Navigation

```
1. Zur Overview-Seite navigieren (Tab "Übersicht")
2. Starte bei "Protokoll" Tab
3. ERWARTUNG: Protokoll-Inhalte sichtbar

4. Swipe nach links
5. ERWARTUNG: 
   - Smooth Transition
   - "Analyse" Tab wird jetzt aktiv
   - PrimaryTabRow zeigt "Analyse" als selected
   - Analyse-Inhalte sichtbar

6. Swipe nach links nochmal
7. ERWARTUNG:
   - "Ressourcen" Tab wird aktiv
   - Ressourcen-Inhalte sichtbar

8. Swipe nach rechts
9. ERWARTUNG: Zurück zu "Analyse"

10. Klick auf "Protokoll" Tab (ohne zu swipen)
11. ERWARTUNG: 
    - Direkt zu Protokoll-Seite (nicht linear)
    - Keine Animation nötig (instant)

12. Rapid Swipes durchführen (5x schnell)
13. ERWARTUNG:
    - Keine Crashes
    - App sollte responsive bleiben
    - Letzte Swipe-Richtung gewinnt
```

**Validation Kriterien:**
- ✅ Swipe funktioniert in beide Richtungen
- ✅ Tab-Klicks funktionieren
- ✅ Animations sind smooth (keine Ruckler)
- ✅ Rapid Interactions führen zu keinem Crash
- ✅ State ist konsistent

---

### Test 3: Learn Screen Swipe-Navigation

```
1. Zur Learn-Seite navigieren (Bottom Nav "Lernen")
2. ERWARTUNG: ModulesTab zeigt Lernmodule

3. Swipe nach links
4. ERWARTUNG:
   - "Mein Fortschritt" Tab wird aktiv
   - Fortschritt-Inhalte zeigen (Basis-Placeholder)

5. Swipe nach links nochmal
6. ERWARTUNG:
   - "Ressourcen" Tab wird aktiv
   - Ressourcen-Inhalte zeigen

7. Tap "Module" Tab
8. ERWARTUNG: Zurück zu Module-Liste

9. Klick auf ein Modul (z.B. "Grundlagen")
10. ERWARTUNG: Navigation zum LearnBasicsScreen funktioniert
11. Zurück zum LearnScreen
12. ERWARTUNG: Still bei "Module" Tab
```

**Validation Kriterien:**
- ✅ Alle 3 Tabs sind swipebar
- ✅ Module sind klickbar & navigierbar
- ✅ State bleibt bei Navigation erhalten
- ✅ Keine Crashes

---

### Test 4: Permission Edge Cases

```
Android 13+:
1. Clear App Data
2. App öffnen → Onboarding
3. Navigiere zu Notification Page
4. "Aktivieren" → Android Dialog
5. ALLOW → Erwarte erfolgreiche Requests
6. DENY → App sollte trotzdem funktionieren

Android 12:
1. Clear App Data
2. App öffnen → Onboarding
3. Notification Page: Button sollte trotzdem existieren
4. Battery Page: Sollte normal funktionieren
5. Kein Permission-Dialog sollte zeigen
```

**Validation Kriterien:**
- ✅ Permissions werden auf korrekter Android-Version gefragt
- ✅ Denial führt zu Graceful Degradation (kein Crash)
- ✅ Allow funktioniert korrekt

---

### Test 5: Performance & Memory

```
1. Onboarding durchlaufen (Leo 10 Pages)
2. Memory Monitor beobachten:
   - Sollte stabil bei ~150-200MB sein
   - Keine extremen Sprünge

3. Swipe 50x durchführen in Overview
4. Memory sollte gleich bleiben
5. App sollte responsive bleiben

6. Öffne LearnScreen
7. Swipe 50x durch
8. Memory sollte stabil sein

9. Keine Warnings im Logcat
```

**Validation Kriterien:**
- ✅ Kein Memory Leak (stabile Nutzung)
- ✅ Smooth Performance auch nach vielen Swipes
- ✅ Keine Warnings oder Crashes

---

### Test 6: Orientation Changes

```
1. Overview Screen öffnen
2. Device rotieren (Portrait → Landscape)
3. ERWARTUNG:
   - Layout sollte sich anpassen
   - Aktive Tab sollte erhalten bleiben
   - Keine Crashes

4. Während Swipe rotieren
5. App sollte graceful damit umgehen

6. LearnScreen → rotieren
7. Alle 3 Tabs sollten weiterhin funktionieren
```

**Validation Kriterien:**
- ✅ Rotation wird korrekt gehandhabt
- ✅ State bleibt erhalten
- ✅ Keine UI-Glitches

---

## 🐛 Common Issues & Solutions

### Issue 1: "ExperimentalFoundationApi not found"
**Problem:** Import fehlt oder verschieben
**Lösung:** `@OptIn(ExperimentalFoundationApi::class)` vor Composable
```kotlin
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyScreen() { ... }
```

### Issue 2: "rememberPagerState() Type Mismatch"
**Problem:** pageCount Parameter wird falsch übergeben
**Lösung:** pageCount als Lambda mit Braces übergeben
```kotlin
// FALSCH
rememberPagerState(pageCount = tabs.size)

// RICHTIG
rememberPagerState(pageCount = { tabs.size })
```

### Issue 3: "Coroutine Scope not found"
**Problem:** rememberCoroutineScope() wurde nicht remembered
**Lösung:** 
```kotlin
val coroutineScope = rememberCoroutineScope()
// Dann später
coroutineScope.launch {
    pagerState.animateScrollToPage(index)
}
```

### Issue 4: "Permission Launcher returns null"
**Problem:** Permission wurde nicht gegeben
**Lösung:** Graceful degradation
```kotlin
notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
// Trotzdem funktioniert App auch ohne Permission
```

---

## ✔️ Final Validation Checklist

- [ ] Projekt kompiliert ohne Fehler
- [ ] Keine Red-Squigglies im IDE
- [ ] OnboardingScreen zeigt auf frischem Start
- [ ] Alle 10 Leo-Pages sind swipebar
- [ ] Notification Permission Page funktioniert
- [ ] Battery Optimization Page funktioniert
- [ ] Overview Screen ist swipebar (3 Tabs)
- [ ] LearnScreen ist swipebar (3 Tabs)
- [ ] Alle Buttons funktionieren
- [ ] Keine Crashes bei rapid interactions
- [ ] Memory ist stabil
- [ ] Orientierungswechsel funktioniert
- [ ] Android 12 und 13+ kompatibel
- [ ] Graceful Degradation funktioniert

---

## 📊 Test Report Template

```
Datum: 2026-01-24
Tester: [Name]
Device: [Device Model]
Android Version: [Version]

RESULTS:
- Onboarding: ✅/❌ [Notes]
- Overview Swipe: ✅/❌ [Notes]
- Learn Swipe: ✅/❌ [Notes]
- Permissions: ✅/❌ [Notes]
- Performance: ✅/❌ [Notes]
- Orientation: ✅/❌ [Notes]

Issues Found:
1. [Issue 1]
2. [Issue 2]

Overall Status: ✅ PASS / ⚠️ ISSUES / ❌ FAIL
```

---

## 🚀 Ready for Production?

Sobald alle Tests bestanden sind:
- ✅ Commit Code zu Main Branch
- ✅ Update Version in gradle
- ✅ Erstelle Release-Notes
- ✅ Build Release APK
- ✅ Deploy zu Play Store / TestFlight
- ✅ Monitor Crash-Reports

---

**Viel Erfolg beim Testing!** 🎉
