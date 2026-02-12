# 🎭 Geteilte Aufmerksamkeit - Implementierungsdokumentation

## Übersicht
Die **Geteilte Aufmerksamkeit** (Shared/Distributed Attention) ist eine neue Ressourcen-Übung im ATT-Modul, die Nutzern hilft, ihren Aufmerksamkeitsraum zu **weiten** statt zu **verengen**. Dies ist komplementär zur Selektiven Aufmerksamkeit.

---

## 📁 Implementierte Komponenten

### 1. **SharedAttentionExercise.kt** (NEU)
**Pfad:** `app/src/main/java/com/empiriact/app/ui/screens/resources/methods/`

#### Funktionalität:
- Adaptive 9-Stufen-Übung mit progressivem Aufmerksamkeits-Training
- 3 × 45-Sekunden Trainingszyklen mit verschiedenen Wahrnehmungs-Modi
- Timer mit Gong-Sound bei jedem Zyklus-Ende
- Reflexions-TextField (optional)
- Farbliche Timer-Unterscheidung (läuft vs. fertig)

#### Struktur:
```
Schritt 0: Willkommen
Schritt 1: Verständnis der Übung
Schritt 2: Vorbereitung
Schritt 3: Trainings-Phase Anleitung
─────────────────────────────────────
Schritt 4: BREITE PERIPHERE WAHRNEHMUNG (45 Sek)
Schritt 5: FOKUS + PERIPHERIE (45 Sek)
Schritt 6: INTEGRATIVE WAHRNEHMUNG (45 Sek)
─────────────────────────────────────
Schritt 7: Reflexion (Optional)
Schritt 8: Abschluss & Erkenntnis
```

#### Features:
- 🎭 **Expansion statt Fokus:** Breiter werden statt enger
- ⏱️ **45-Sekunden-Zyklen:** Längere Trainingsphase als selektive Aufmerksamkeit
- 🎯 **3 Wahrnehmungs-Modi:**
  1. Breite periphere Wahrnehmung (alles gleichzeitig)
  2. Fokus + Peripherie (duales Bewusstsein)
  3. Integrative Wahrnehmung (alles als Eins)
- 🔔 **Gong-Sound** bei Zyklus-Ende
- 📝 **Optionale Reflexion** mit TextField
- 📊 **Fortschrittsanzeige**

---

## 🧠 Therapeutische Konzepte

### Breite Periphere Wahrnehmung:
- Gleichzeitiges Wahrnehmen ALLER Reize
- Ohne Priorisierung von einzelnen
- Wie ein "breites Wahrnehmungsfeld"
- Relativiert dominante Reize

### Fokus + Peripherie:
- Verbindung von fokussierter + breiter Wahrnehmung
- Einen Punkt deutlich, Umgebung diffus
- Duales Bewusstsein trainieren
- Flexible Aufmerksamkeit

### Integrative Wahrnehmung:
- Alles als zusammenhängendes Ganzes
- Keine Unterscheidung zwischen Zentrum und Peripherie
- "Eins-Bewusstsein"
- Maximale Flexibilität

---

## 🔧 Integration

### 2. **Route.kt** - Navigation
```kotlin
object SharedAttentionExercise : Route("shared_attention_exercise/{from}") {
    fun createRoute(from: String) = "shared_attention_exercise/$from"
}
```

### 3. **ResourcesScreen.kt** - Ressourcenliste
Neue Übung zur Liste hinzugefügt:
```kotlin
ResourceExercise(
    title = "Geteilte Aufmerksamkeit",
    description = "Lerne, deine Aufmerksamkeit zu weiten und mehrere Reize gleichzeitig wahrzunehmen. Entwickle einen breiten Aufmerksamkeitsraum und relativiere dominante Reize.",
    route = Route.SharedAttentionExercise
)
```

Navigation angepasst:
```kotlin
is Route.SharedAttentionExercise -> {
    navController.navigate(Route.SharedAttentionExercise.createRoute(from = "resources"))
}
```

### 4. **ExerciseRatingScreen.kt** - Bewertung
```kotlin
"shared_attention" -> "Geteilte Aufmerksamkeit"
```

### 5. **EmpiriactNavGraph.kt** - Navigation Graph
Import und Route registriert:
```kotlin
import com.empiriact.app.ui.screens.resources.methods.SharedAttentionExercise

composable(
    route = Route.SharedAttentionExercise.route,
    arguments = listOf(navArgument("from") { type = NavType.StringType })
) { backStackEntry ->
    val from = backStackEntry.arguments?.getString("from")!!
    SharedAttentionExercise(navController, from)
}
```

---

## ⏱️ Timer-Verhalten

### Trainings-Phasen (Steps 4-6):
```
Step 4 (Breite periphere Wahrnehmung)
├─ Timer angezeigt: 45 → 44 → ... → 00
├─ Farbe: 🟦 BLAU ("Nimm alles wahr...")
├─ Button: "Timer läuft..." (DEAKTIVIERT)
└─ Nach 45 Sek:
   ├─ 🔔 GONG! Sound
   ├─ Farbe: 🟩 GRÜN ("Fertig! Klick auf Weiter")
   └─ Button: "Weiter" (AKTIVIERT)
       ↓ [Nutzer klickt]

Step 5 (Fokus + Peripherie)
├─ Timer RESETET: 45 → 44 → ... → 00
├─ [Gleicher Prozess]
```

---

## 🎯 Therapeutische Ziele

Diese Übung adressiert spezifisch:

### 1. **Aufmerksamkeitstraining (ATT)**
- ✅ Flexible Aufmerksamkeitskontrolle (Erweiterung)
- ✅ Bewusstes Weiten des Aufmerksamkeitsraums
- ✅ Alternative zu Fokus-Training

### 2. **Relativierung dominanter Reize**
- ✅ Erlebnis: "Dieser Reiz ist nicht der Einzige"
- ✅ Bedrohungsreize werden weniger dominant
- ✅ Perspektiv-Erweiterung

### 3. **Distanz zu inneren Inhalten**
- ✅ Durch Expansion statt Fokus
- ✅ Reduktion von Grübelschleifen
- ✅ "Alles als Ganzes" statt "einzelner Gedanke"

### 4. **Kognitive Beweglichkeit**
- ✅ Wechsel zwischen Fokus und Peripherie
- ✅ Flexible Aufmerksamkeits-Muster
- ✅ Reduktion von Aufmerksamkeitsverharren

---

## 🔄 Unterschiede zu anderen Übungen

### Selektive Aufmerksamkeit:
- ❌ Fokussiert auf EINEN Reiz
- ❌ Enge Aufmerksamkeit
- ✅ Gut für Grübelschleifen-Unterbrechung

### Geteilte Aufmerksamkeit:
- ✅ Fokussiert auf MEHRERE Reize gleichzeitig
- ✅ Breite Aufmerksamkeit
- ✅ Gut für Relativierung dominanter Reize

### Aufmerksamkeitswechsel:
- ❌ Sequentielle Wechsel (nacheinander)
- ❌ Zwischen verschiedenen Fokus-Typen

### Geteilte Aufmerksamkeit:
- ✅ Gleichzeitige Wahrnehmung (parallel)
- ✅ Breiter Raum statt Wechsel

---

## 🧪 Test-Szenarios

### Szenario 1: Normaler Durchlauf
```
1. Start bei Schritt 3
2. [Weiter] → Schritt 4, Timer startet (45 Sek)
3. Warte 45 Sekunden
4. GONG! Farbe ändert sich, Button wird aktiv
5. [Weiter] → Schritt 5, Neuer Timer startet
6. [Gleicher Prozess] (45 Sekunden)
7. [Weiter] → Schritt 6 (letzte Trainingsphase)
8. [Gleicher Prozess] (45 Sekunden)
9. Reflexion (optional) → [Weiter]
10. Abschluss → [Abschließen]
11. Rating Screen
```

### Szenario 2: Timer überspringen nicht möglich
```
- Timer läuft bei Step 4
- Button ist deaktiviert (nicht klickbar)
- MUSS 45 Sekunden warten
- Dann kann Weiter geklickt werden
```

### Szenario 3: Reflexion optional
```
- Bei Step 7 (Reflexion)
- Nutzer schreibt NICHTS
- [Weiter] Button ist trotzdem aktiv
- Kann direkt zu Step 8 gehen
```

---

## 📊 Datenbankintegration

### Gespeicherte Daten:
```
exercise_ratings
├── exerciseId: "shared_attention"
├── rating: -2 bis +2
└── timestamp: Speicher-Zeit

exercise_reflections (optional)
├── exerciseId: "shared_attention"
├── reflection: Nutzer-Text
└── timestamp: Speicher-Zeit
```

---

## ✅ Qualitätssicherung

- [x] Timer-Logik funktioniert (45 Sekunden)
- [x] Gong-Sound bei Timer-Ende
- [x] Farbliche Unterscheidung (läuft vs. fertig)
- [x] Button wird nach Timer freigegeben
- [x] Neuer Timer startet mit Weiter-Klick
- [x] Reflexion ist optional
- [x] Navigation zu Rating-Screen funktioniert
- [x] Übungstitel "Geteilte Aufmerksamkeit" wird angezeigt
- [x] Keine Breaking Changes

---

## 🚀 Status

**✅ IMPLEMENTIERUNG ABGESCHLOSSEN**

Die Übung ist sofort einsatzbereit und kann von Nutzern im Ressourcen-Menü aufgerufen werden.
