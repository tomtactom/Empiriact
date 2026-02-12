# Swipe Navigation & UX Flow - Visuelle Übersicht

## 📱 Swipe-Navigation Flows

### 1. OverviewScreen - Drei Subsections

```
┌─────────────────────────────────────┐
│         OVERVIEW SCREEN             │
├─────────────────────────────────────┤
│  [Protokoll | Analyse | Ressourcen] │ ← PrimaryTabRow
├─────────────────────────────────────┤
│                                     │
│       Aktuell: PROTOKOLL            │
│    (Aktivitäten für heute)          │
│                                     │
│  ← Swipe Left ← oder ← Swipe Right  │
│                                     │
└─────────────────────────────────────┘

Beim Swipe nach Links:
┌─────────────────────────────────────┐
│  [Protokoll | Analyse | Ressourcen] │ ← Tab-Indicator aktualisiiert
├─────────────────────────────────────┤
│                                     │
│       Aktuell: ANALYSE              │
│    (Stimmungsaufheller/Dämpfer)     │
│                                     │
└─────────────────────────────────────┘

Nächster Swipe:
┌─────────────────────────────────────┐
│  [Protokoll | Analyse | Ressourcen] │
├─────────────────────────────────────┤
│                                     │
│      Aktuell: RESSOURCEN            │
│   (Übungsbewertungen + Navigation)  │
│                                     │
└─────────────────────────────────────┘
```

**Interaktionen:**
- 👉 Swipe Left/Right: Seite wechseln
- 🖱️ Tap auf Tab: Direkt zu dieser Seite springen
- ⚡ Smooth Animation während Transition

---

### 2. LearnScreen - Drei Subsections (Neu!)

```
┌─────────────────────────────────────┐
│          LEARN SCREEN               │
├─────────────────────────────────────┤
│  [Module | Fortschritt | Ressourcen]│ ← PrimaryTabRow
├─────────────────────────────────────┤
│                                     │
│       Aktuell: MODULE               │
│  • Grundlagen                       │
│  • Fortgeschritten                  │
│  • Praktische Übungen               │
│  • Testmodul                        │
│                                     │
│  ← Swipe Links → Fortschritt        │
│                                     │
└─────────────────────────────────────┘

Swipe nach Links:
┌─────────────────────────────────────┐
│  [Module | Fortschritt | Ressourcen]│
├─────────────────────────────────────┤
│                                     │
│    Aktuell: MEIN FORTSCHRITT        │
│  (Placeholder für Statistiken)      │
│                                     │
│  Grundlagen: ████████░░ 80%         │
│  Fortgeschritten: ██░░░░░░░░ 20%    │
│                                     │
└─────────────────────────────────────┘

Nächster Swipe:
┌─────────────────────────────────────┐
│  [Module | Fortschritt | Ressourcen]│
├─────────────────────────────────────┤
│                                     │
│      Aktuell: RESSOURCEN            │
│   (Zusätzliche Lernmaterialien)     │
│                                     │
└─────────────────────────────────────┘
```

---

### 3. OnboardingScreen - Full Flow (Erweitert!)

```
App starten → OnboardingScreen

┌─────────────────────────────────────┐
│          ONBOARDING - LEO            │
│                                     │
│    🧠  Herzlich Willkommen!         │
│                                     │
│  Mein Name ist Leo und ich bin      │
│  hier, um dich auf deinem Weg zu    │
│  unterstützen...                   │
│                                     │
│  [←──────────────────────────────→] │
│   Swipe für nächste Seite           │
│                                     │
└─────────────────────────────────────┘

      ↓ Swipe Links (9x)

┌─────────────────────────────────────┐
│          ONBOARDING - LEO            │
│          (Page 2 von 10)             │
│                                     │
│    🧠  Du als Forscher*in           │
│                                     │
│  In der nächsten Zeit kannst du     │
│  erkunden, wie du deinen Alltag     │
│  so gestalten kannst...            │
│                                     │
│  [←──────────────────────────────→] │
│                                     │
└─────────────────────────────────────┘

    ... (8 weitere Pages) ...

      ↓ Nach 10 Leo-Pages

┌─────────────────────────────────────┐
│      BENACHRICHTIGUNGEN             │
│                                     │
│    🔔                               │
│                                     │
│  Damit wir dich an wichtige         │
│  Momente erinnern können...         │
│                                     │
│  ┌──────────────────────────────┐   │
│  │     AKTIVIEREN              │   │
│  └──────────────────────────────┘   │
│                                     │
└─────────────────────────────────────┘

      ↓ Nach Permission Request

┌─────────────────────────────────────┐
│   AKKUOPTIMIERUNG DEAKTIVIEREN      │
│                                     │
│    🔋                               │
│                                     │
│  Damit die App im Hintergrund       │
│  zuverlässig läuft...              │
│                                     │
│  ┌──────────────────────────────┐   │
│  │  EINSTELLUNGEN ÖFFNEN        │   │
│  └──────────────────────────────┘   │
│                                     │
│  ┌──────────────────────────────┐   │
│  │       FERTIG                 │   │
│  └──────────────────────────────┘   │
│                                     │
└─────────────────────────────────────┘

      ↓ Fertig geklickt

    → Hauptapp lädt (TodayScreen)
```

---

## 🎨 Design Pattern Übersicht

### Swipe-Navigations Pattern
```
Komponente:        HorizontalPager
State Management:  PagerState
Animation:         animateScrollToPage() + Coroutine
Visual Feedback:   PrimaryTabRow zeigt aktive Page
Touch Target:      Full-Screen Swipe Area (optimal)
Gesture:           Horizontal Drag (Swipe)
```

### Permission Pattern
```
Komponente:        rememberLauncherForActivityResult
Permission Type:   POST_NOTIFICATIONS (Android 13+)
Fallback:          Skip möglich
UX:                Separate dedizierte Page
Feedback:          Status-Button ("Aktivieren" → "✓ Aktiviert")
```

### Settings Navigation Pattern
```
Komponente:        Intent mit ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
Package:           Settings App
UX:                "Einstellungen öffnen" Button
Fallback:          "Fertig" Button für Skip
Graceful:          App funktioniert auch ohne diese Optimierung
```

---

## 🔄 State & Animation Flow

### Page Transition Animation
```
Swipe-Start
    ↓
Gesture-Detection (HorizontalPager)
    ↓
animateScrollToPage() aufgerufen
    ↓
Coroutine lädt neue Page
    ↓
Smoothe Animation (300ms default)
    ↓
Page-Transition abgeschlossen
    ↓
PrimaryTabRow aktualisiert sich
```

### State Management
```
var pagerState = rememberPagerState(pageCount = { tabs.size })
    ↓
pagerState.currentPage → aktuelle Page
    ↓
pagerState.settledPage → letzte stabile Page
    ↓
HorizontalPager nutzt diese State
    ↓
Tab-Clicks triggern: pagerState.animateScrollToPage(index)
```

---

## 📐 Layout Dimensionen

### Responsive Design
```
┌─────────────────────────────────────┐
│    24.dp Padding (bei Onboarding)  │
│  ┌─────────────────────────────┐    │
│  │                             │    │
│  │  Content Area               │    │
│  │  (fillMaxSize)              │    │
│  │                             │    │
│  └─────────────────────────────┘    │
│    24.dp Padding                   │
└─────────────────────────────────────┘

HorizontalPager:
- Full-Width für Swipe
- Page-zu-Page Transition
- Dynamische Höhe basierend auf Content
```

### Touch Target Sizes
```
Tabs:           48.dp minimum (Material Design 3)
Icons:          64.dp (Onboarding)
Buttons:        48.dp minimum Höhe
Swipe Area:     Full-Screen
```

---

## ♿ Accessibility Features

### Touch Targets
- 🎯 Alle Buttons: mindestens 48dp × 48dp
- 📱 Swipe-Area: Full-Screen für einfache Nutzung
- 🎨 Contrast: High-Contrast Icons & Text

### Labels & Descriptions
- 📝 Alle Icons haben `contentDescription`
- 🗣️ Tabs haben Text-Labels
- 🔊 Screen Reader Support (automatisch via Compose)

### Keyboard Navigation
- ⌨️ Tab-Navigation unterstützt
- 🎯 Enter zum Aktivieren von Buttons
- 🔄 Swipe-Shortcuts wo möglich

---

## 🧪 Testing Scenarios

### Scenario 1: Normale Nutzung
```
1. App öffnen
2. Onboarding-Pages durchswipen (10x)
3. Permissions akzeptieren
4. Hauptapp laden
5. OverviewScreen öffnen
6. Swipen zwischen Protokoll/Analyse/Ressourcen
7. LearnScreen öffnen
8. Swipen zwischen Module/Fortschritt/Ressourcen
```

### Scenario 2: Permission Denials
```
1. App öffnen
2. Notification Permission → Ablehnen
3. Battery Page → Fertig klicken (ohne zu öffnen)
4. App sollte trotzdem funktionieren
```

### Scenario 3: Repeated Swipes
```
1. Rapid Swipes durchführen (100ms Intervalle)
2. App sollte nicht crashen
3. Animations sollten smooth sein
4. State sollte konsistent sein
```

---

## 🎯 Expected User Behaviors

### Positive Behaviors (erwünscht)
✅ Intuitives Swipen zwischen Sections
✅ Klare Visual Feedback bei Page Changes
✅ Schnelle Navigation per Tabs
✅ Verständnis der Permission-Gründe

### Potential Friction Points
⚠️ Zu schnelle Swipes könnten übershooten
⚠️ Permission-Requests könnten abgelehnt werden
⚠️ Neue Nutzer verstehen Swipe-Geste initial nicht

### Mitigation Strategies
✅ Onboarding erklärt Swipe-Navigation
✅ Graceful Degradation bei Permission-Denial
✅ Tab-Klicks als Alternative zu Swipes
✅ Smooth Animations helfen Verständnis

---

## 📊 Metriken zum Tracken

Nach Implementierung sollten gemessen werden:

**Engagement:**
- 📈 Swipe-Rate (Nutzer die swipen vs. Tab-Klicks)
- ⏱️ Zeit pro Section
- 🔄 Rückkehr zu vorherigen Sections

**Onboarding:**
- ✅ Completion Rate
- ⏱️ Time to Completion
- 📍 Drop-off Points
- 🔔 Permission Acceptance Rate

**Performance:**
- ⚡ Animation FPS
- 📱 Memory Usage während Paging
- 🎯 Touch-Response Time

---

## ✨ Polish & Refinement Ideas

### Quick Wins
- [ ] Swipe-Indikatoren (Dots unten bei Onboarding)
- [ ] Haptic Feedback bei Swipes
- [ ] Page-Transition Sounds (optional)

### Medium Effort
- [ ] Gesture Tutorial in Onboarding
- [ ] Predictive Page Pre-Loading
- [ ] Custom Swipe Sensitivity

### High Impact
- [ ] Vertical Swipe für Sub-Navigation
- [ ] Gesture Customization Settings
- [ ] Offline Page Caching

---

**Ziel:** Intuitive, moderne Navigation mit hohem User Delight! 🎉
