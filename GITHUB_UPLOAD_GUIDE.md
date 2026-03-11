# 🚀 Wie man das Projekt auf GitHub hochlädt

## Schnellstart

### 1. GitHub Repository erstellen

1. Gehe zu https://github.com/new
2. Gib einen **Repository-Namen** ein (z.B. `Empiriact` oder `Empiriact-Psychoedukation`)
3. Wähle **Private** oder **Public**
4. **NICHT** "Initialize with README" anhaken (wir haben bereits Dateien)
5. Klick **Create repository**

### 2. Git lokal konfigurieren

Öffne PowerShell im Projektordner und führe aus:

```powershell
# Globale Git-Konfiguration (einmalig)
git config --global user.name "Dein Name"
git config --global user.email "deine.email@beispiel.de"

# Im Projekt-Verzeichnis:
cd "C:\Users\Tom Uni\StudioProjects\Empiriact"

# Initialisiere Git Repository
git init

# Definiere den Remote (ersetze USERNAME und REPO-NAME)
git remote add origin https://github.com/USERNAME/REPO-NAME.git

# Überprüfe die Konfiguration
git remote -v
```

### 3. .gitignore erstellen

```powershell
# Erstelle .gitignore-Datei
New-Item -ItemType File -Name ".gitignore" -Force
```

Füge folgendes hinzu (`.gitignore` Datei):

```
# Build-Ordner
build/
.gradle/
.idea/
*.iml
out/

# Local configuration file
local.properties

# IDE
.idea/
*.iws
*.ipr
*.iml
*.sublime-*

# Android-spezifisch
.gradle
/local.properties
/.idea
/build
*.apk
*.ap_
*.dex
*.class
bin/
gen/

# Gradle files
.gradle/
build/

# Maven
target/
pom.xml.tag
pom.xml.release
pom.xml.backup

# IDE-spezifisch
.vscode/
.DS_Store
*.log

# Sensible Daten
.env
secrets.properties
```

### 4. Dateien hinzufügen und committen

```powershell
# Alle Dateien zum Staging hinzufügen
git add .

# Initial Commit
git commit -m "Initial commit: Grübeln-Modul und psychoedukatives System implementiert"

# Zum Remote pushen (bei erstem Mal Branch erstellen)
git branch -M main
git push -u origin main
```

### 5. Weitere Commits

```powershell
# Nach Änderungen:
git add .
git commit -m "Aussagekräftige Commit-Nachricht"
git push origin main
```

---

## 📝 Gute Commit-Nachrichten

**Format:** `[Typ]: Kurze Beschreibung`

### Typen:
- `feat:` - Neue Funktion (z.B. `feat: Grübeln-Modul hinzugefügt`)
- `fix:` - Bugfix (z.B. `fix: Lesezeichen-Funktionalität korrigiert`)
- `docs:` - Dokumentation (z.B. `docs: README aktualisiert`)
- `refactor:` - Code-Umstrukturierung
- `perf:` - Performance-Verbesserung
- `test:` - Tests hinzugefügt

### Beispiele:
```
feat: Psychoedukatives Modul-System implementiert
fix: Dark Mode Kontrast-Verhältnisse verbessert
docs: Module Builder Guide erstellt
feat: Bookmark-Funktion zu Modulen hinzugefügt
```

---

## 🔐 GitHub Authentication

### Option 1: HTTPS (einfacher)
```powershell
# Git speichert Token automatisch
git push origin main
# Beim ersten Mal wird ein Browser-Fenster geöffnet
```

### Option 2: SSH (sicherer)
```powershell
# SSH-Key generieren (einmalig)
ssh-keygen -t ed25519 -C "deine.email@beispiel.de"

# Füge public key zu GitHub hinzu (Settings > SSH Keys)
# Dann verwende SSH-URL statt HTTPS
git remote set-url origin git@github.com:USERNAME/REPO-NAME.git
```

---

## 📊 Projekt-Struktur auf GitHub

```
Empiriact/
├── README.md                           (Projekt-Übersicht)
├── .gitignore
├── app/
│   ├── src/main/java/com/empiriact/
│   │   └── ui/screens/modules/
│   │       └── GruebelnModuleScreen.kt  ✨ NEU
│   └── ...
├── PSYCHOEDUCATION_MODULE_SYSTEM_NEW.md ✨ NEU (Developer Guide)
├── GRUEBELN_MODULE_README.md             ✨ NEU (Modul-Dokumentation)
├── build.gradle.kts
├── settings.gradle.kts
└── ... (weitere bestehende Dateien)
```

---

## ✅ Checkliste vor dem Pushen

- [ ] **Code kompiliert** ohne Fehler
- [ ] **Tests laufen** (falls vorhanden)
- [ ] **.gitignore ist vorhanden** und korrekt konfiguriert
- [ ] **Keine sensiblen Daten** in Commits (API-Keys, Passwörter)
- [ ] **README.md** ist aktualisiert
- [ ] **Dokumentation** ist korrekt (Typos überprüft)
- [ ] **Commits sind sauber** und aussagekräftig
- [ ] **Branch ist aktuell** (git pull origin main)

---

## 🐛 Hilfreiche Git-Befehle

```powershell
# Status überprüfen
git status

# Letzte Commits sehen
git log --oneline -n 10

# Unterschiede sehen
git diff

# Änderungen rückgängig machen (vor commit)
git reset HEAD datei.kt

# Letzten Commit ändern (VORSICHT!)
git commit --amend

# In eine bestimmte Branch wechseln
git checkout branch-name

# Neue Branch erstellen
git checkout -b feature/new-module

# Branch merge
git merge feature/new-module
```

---

## 📚 Zusätzliche Ressourcen

- **GitHub Docs:** https://docs.github.com/
- **Git Tutorial:** https://git-scm.com/book/de/v2
- **GitHub Desktop:** https://desktop.github.com/ (GUI-Alternative)
- **GitKraken:** https://www.gitkraken.com/ (Advanced GUI)

---

## 🎯 Best Practices

1. **Regelmäßig committen** - Kleine, logische Commits sind besser als eine große am Ende
2. **Aussagekräftige Nachrichten** - Andere Entwickler sollen verstehen, was sich geändert hat
3. **Nie Secrets commiten** - `.gitignore` ist dein Freund
4. **Pull vor Push** - Immer die neuesten Änderungen abholen
5. **Branches für Features** - `main` bleibt stabil, neue Features in separaten Branches

---

## ❓ Häufige Probleme

### Problem: "fatal: not a git repository"
**Lösung:** `git init` im Projektverzeichnis ausführen

### Problem: "Permission denied (publickey)"
**Lösung:** SSH-Keys überprüfen oder HTTPS verwenden

### Problem: Merge-Konflikte
**Lösung:** In Konflikt-Dateien manuell edieren oder `git mergetool` verwenden

### Problem: "Rejected non-fast-forward"
**Lösung:** `git pull origin main` vor dem Push

---

**Version:** 1.0  
**Letztes Update:** 2026-02-19  
**Status:** ✅ Bereit für Production

