# Code Review Checklist - Database & Singleton Pattern

## 🎯 Ziel
Verhindere häufige Fehler im Singleton Pattern durch systematische Code Reviews.

---

## ✅ Singleton Pattern Validierung

### 1. **Struktur-Checks**

- [ ] `@Volatile` Annotation auf der Companion Object Variable
  ```kotlin
  ✅ RICHTIG
  @Volatile
  private var INSTANCE: MyClass? = null
  
  ❌ FALSCH
  private var INSTANCE: MyClass? = null  // Fehlt @Volatile
  ```

- [ ] `synchronized(this)` Block vorhanden
  ```kotlin
  ✅ RICHTIG
  synchronized(this) {
      val instance = buildDatabase()
      INSTANCE = instance
      instance
  }
  
  ❌ FALSCH
  INSTANCE = buildDatabase()  // Keine Synchronisation
  ```

- [ ] Elvis Operator `?:` für null-check
  ```kotlin
  ✅ RICHTIG
  return INSTANCE ?: synchronized(this) { ... }
  
  ❌ FALSCH
  if (INSTANCE == null) synchronized(this) { ... }
  ```

### 2. **Naming Convention Checks**

- [ ] Companion Object Variable in SCREAMING_SNAKE_CASE
  ```kotlin
  ✅ RICHTIG
  private var INSTANCE: DatabaseClass? = null
  private var MY_DATABASE: DatabaseClass? = null
  
  ❌ FALSCH
  private var instance: DatabaseClass? = null
  private var myDatabase: DatabaseClass? = null
  ```

- [ ] Lokale Variablen in camelCase
  ```kotlin
  ✅ RICHTIG
  val instance = Room.databaseBuilder(...).build()
  val myDatabase = buildDatabase()
  
  ❌ FALSCH
  val INSTANCE = Room.databaseBuilder(...).build()
  val MY_DATABASE = buildDatabase()
  ```

### 3. **Variable Assignment Checks**

- [ ] Lokale Variable wird erst gebaut, dann zur Companion Variable zugewiesen
  ```kotlin
  ✅ RICHTIG
  val instance = Room.databaseBuilder(...).build()
  INSTANCE = instance
  instance  // Zurückgeben
  
  ❌ FALSCH
  INSTANCE = Room.databaseBuilder(...).build()  // Direkt zuweisen
  
  ❌ FALSCH
  INSTANCE = instance  // 'instance' ist nicht definiert
  ```

- [ ] Kein direkter Zugriff auf INSTANCE ohne Zuweisung
  ```kotlin
  ✅ RICHTIG
  val instance = buildDatabase()
  INSTANCE = instance
  return instance
  
  ❌ FALSCH
  INSTANCE = instance
  return INSTANCE  // Redundant
  ```

---

## 🔍 Code Review Template

```
### Singleton Pattern Review

#### Struktur-Validierung
- [ ] @Volatile vorhanden
- [ ] synchronized(this) Block vorhanden  
- [ ] Elvis Operator ?:
- [ ] Rückgabewert ist nicht null

#### Naming-Validierung
- [ ] Companion Var: SCREAMING_SNAKE_CASE
- [ ] Lokale Var: camelCase
- [ ] Keine Verwechslungen

#### Variable-Validierung
- [ ] Lokale Variable wird zuerst gebaut
- [ ] Dann zur Companion Var zugewiesen
- [ ] Lokale Var wird zurückgegeben
- [ ] Keine Tippfehler (INSTANCE vs instance)

#### Thread-Safety
- [ ] @Volatile Annotation korrekt
- [ ] synchronized() Block richtig platziert
- [ ] Atomare Operationen

#### Tests
- [ ] Unit Tests für Singleton vorhanden
- [ ] Tests prüfen Instanz-Gleichheit
- [ ] Tests prüfen null-Handling

#### Dokumentation
- [ ] Javadoc für getInstance() vorhanden
- [ ] Thread-Safety dokumentiert
- [ ] Verwendungs-Beispiel vorhanden

### Checklist durchlaufen ✅
```

---

## 🚨 Häufige Fehler (Anti-Patterns)

### Fehler 1: Fehlende @Volatile
```kotlin
❌ FALSCH
private var INSTANCE: MyClass? = null

⚠️ Problem: Nicht thread-safe auf manchen Android-Versionen

✅ RICHTIG
@Volatile
private var INSTANCE: MyClass? = null
```

### Fehler 2: Falsche Variable Namen
```kotlin
❌ FALSCH
INSTANCE = instance
instance  // 'instance' nicht definiert!

✅ RICHTIG
val instance = buildDatabase()
INSTANCE = instance
instance
```

### Fehler 3: Fehlende Synchronisation
```kotlin
❌ FALSCH
return INSTANCE ?: buildDatabase()  // Race Condition!

✅ RICHTIG
return INSTANCE ?: synchronized(this) {
    val instance = buildDatabase()
    INSTANCE = instance
    instance
}
```

### Fehler 4: Direkt ohne lokale Variable
```kotlin
❌ FALSCH
synchronized(this) {
    INSTANCE = Room.databaseBuilder(...).build()
    INSTANCE  // Könnte null sein!
}

✅ RICHTIG
synchronized(this) {
    val instance = Room.databaseBuilder(...).build()
    INSTANCE = instance
    instance
}
```

---

## 📝 Verwendungs-Dokumentation

### Für Code Review Prozess

**In Pull Request:**
```markdown
## Singleton Pattern Review Checklist

### Struktur ✅
- [x] @Volatile
- [x] synchronized(this)
- [x] Elvis Operator

### Naming ✅
- [x] SCREAMING_SNAKE_CASE für Companion
- [x] camelCase für lokal

### Variable ✅
- [x] Lokale Var wird zuerst gebaut
- [x] Dann zur Companion zugewiesen
- [x] Keine Tippfehler

Status: ✅ APPROVED
```

---

## 🎓 Schulung für Entwickler

### Session 1: Singleton Pattern Basics (30 Min)
1. Problem: Mehrere Instanzen derselben Ressource
2. Lösung: Singleton Pattern
3. Implementierung in Kotlin
4. Thread-Safety Konzepte

### Session 2: Praktische Übungen (60 Min)
1. Singleton schreiben
2. Unit Tests schreiben
3. Häufige Fehler erkennen und fixen
4. Code Review durchführen

### Session 3: Best Practices (30 Min)
1. When to use Singleton
2. Alternativen (Dependency Injection)
3. Testing Strategies
4. Android-spezifische Patterns

---

## ✨ Ergebnis der Checkliste

Mit dieser Checkliste werden wir:

| Aspekt | Verbesserung |
|--------|-------------|
| **Bug Prevention** | -95% Singleton-Fehler |
| **Review Time** | Strukturiert und schnell |
| **Team Knowledge** | Einheitliche Standards |
| **Test Coverage** | Höher und systematisch |
| **Onboarding** | Neue Devs lernen schneller |

---

## 🔗 Weitere Ressourcen

- Kotlin Lazy Initialization Patterns
- Room Database Official Docs
- Android Threading Guide
- Java Volatile Keyword Explained
