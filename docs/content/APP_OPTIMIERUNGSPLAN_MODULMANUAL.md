# Optimierungsplan aus dem Modulmanual (Grübeln)

## Ausgangspunkt
Dieser Plan leitet konkrete App-Optimierungen aus dem Modulmanual „Modulreihe Grübeln“ ab und richtet sie auf das App-Ziel aus: **Grübeln erkennen, unterbrechen und in werteorientiertes Handeln überführen**.

Die Empfehlungen kombinieren:
- moderne Android-Entwicklung (Compose, Android 14+/15 Patterns, Data/AI-ready Architektur)
- verhaltenstherapeutische Qualität (CBT/ACT-Fidelity)
- Usability Engineering
- Layout-/Interaktionsdesign
- Kommunikationswissenschaft (Motivations- und Verständlichkeitsprinzipien)

---

## 1) Priorisierte Produkt-Optimierungen (Roadmap)

### P0 – Sofort (hoher Impact, geringes Risiko)

1. **Lektionsabschluss mit „Mikro-Commitment“ standardisieren**
   - Für jede Lektion ein identisches Abschlussmuster einführen:
     - „1 Satz Erkenntnis“
     - „Wenn-Dann-Plan“
     - „Nächster Schritt in < 2 Minuten“
   - Begründung: Direkter Transfer von Psychoedukation in Verhalten; reduziert Absicht-Handlungs-Lücke.

2. **Vorher/Nachher-Selbstrating verpflichtend, aber ultrakurz**
   - 2 Slider (Belastung 0–10, Handlungsfähigkeit 0–10) vor und nach jeder Übung.
   - Im Insights-Bereich als Verlauf sichtbar machen.
   - Begründung: Messbarkeit aus Modulmanual + unmittelbare Selbstwirksamkeit.

3. **Akutmodus „90 Sekunden aus der Schleife“ als globaler Einstieg**
   - Floating/Top-Aktion auf Today-Screen, direkt aus jeder Hauptansicht erreichbar.
   - Script: Atemfokus + Aufmerksamkeitswechsel + „Was ist der nächste konkrete Schritt?“
   - Begründung: Modul G3 fordert schnelle Unterbrechung im Alltag; hoher Nutzen bei Krisenspitzen.

4. **Sprachliche Vereinfachung auf B1-Niveau + validierende Tonalität**
   - Kurze Sätze, aktive Verben, eine Kernaussage pro Screen.
   - „Normalisieren ohne Verharmlosen“ als Copy-Regel.
   - Begründung: Höhere Verständlichkeit und niedrigere Drop-off-Raten bei belasteten Nutzer:innen.

### P1 – Kurzfristig (strukturelle Qualitätssteigerung)

5. **Adaptiver Lernpfad pro Modulfortschritt**
   - Regeln:
     - Wenn hohe Grübelintensität: zuerst Unterbrechung (G3), dann Analyse (G2/G1).
     - Wenn niedrige Intensität + hohe Vermeidung: Fokus auf Werte-Handeln (G5).
   - Technisch: rule-based Personalization (on-device, transparent).

6. **Funktionale Analyse (AB 4–6) als geführter Wizard statt Freitextblock**
   - Schrittweise Erfassung: Situation → Gedanken/Bilder → Körper/Gefühl → Verhalten → kurz/langfristige Konsequenzen.
   - Zwischenzusammenfassungen + „Ist das konkret genug?“ Prompt.
   - Begründung: Kognitive Last sinkt, Datenqualität steigt.

7. **Rückfallprophylaxe als „Notfallkarte“ systemweit verfügbar**
   - One-tap Zugriff aus Notification und Home.
   - Enthält: Frühwarnzeichen, Stopp-Satz, 2 Kurzinterventionen, 1 Kontaktoption.
   - Begründung: Direkte Ableitung aus Modul G6, besonders wirksam in Peak-Momenten.

8. **Kommunikative Feedback-Loops**
   - Nach jeder Übung: 
     - Ergebnisfeedback („Du hast X abgeschlossen“)
     - Prozessfeedback („Trotz Belastung gehandelt“)
     - Feedforward („Als Nächstes hilft dir ...“)
   - Begründung: Verstärkt Motivation nach Lern- und Kommunikationspsychologie.

### P2 – Mittelfristig (Skalierung und Differenzierung)

9. **Nutzer:innentypen für passgenaue Interventionen**
   - Cluster (on-device): z. B. „abends grübelnd“, „perfektionistisch-vermeidend“, „sozial getriggert“.
   - Unterschiedliche Reihenfolge von Übungen und Notification-Texten.

10. **JITAI-ähnliche Prompts (Just-in-Time Adaptive Interventions)**
    - Trigger: Tageszeit, bisherige Nutzung, selbstberichtete Belastung.
    - Beispiel: Hohe Belastung + Abendzeit → kurze G3-Intervention statt langer Reflexion.

11. **Therapeutische Allianz digital stärken**
    - Konsistente Coach-Persona mit Mikro-Dialogen und Rückbezug auf frühere Fortschritte.
    - Klare Haltung: validierend, direktiv-light, ressourcenorientiert.

---

## 2) Konkrete UX-/Layout-Verbesserungen je Modultyp

1. **Psychoedukation (G1/G4)**
   - Karten mit „1 Kernidee + 1 Beispiel + 1 Transferfrage“.
   - Progressive Disclosure statt langer Fließtexte.

2. **Mustererkennung (G2)**
   - Timeline- und Trigger-Heatmap (wann, wo, mit wem, Thema).
   - Schnellerfassung per Chips statt Freitext.

3. **Unterbrechung (G3)**
   - Vollbild-Modus mit stark reduzierter UI, großem CTA, optional Audio.
   - Haptik-Signal bei Phasenwechsel (Start/Fokus/Ende).

4. **Werteorientierung (G5)**
   - „Wert → 10-Minuten-Aktion“-Generator.
   - Tagesplan-Integration mit Realitätscheck (Zeit/Ort/Hürde/Plan B).

5. **Stabilisierung (G6)**
   - Wochenreview mit Ampelstatus: Stabil / Wackelig / Risiko.
   - Bei Risiko automatisch Notfallkarte + Mini-Plan anbieten.

---

## 3) Android-Engineering-Empfehlungen (State of the Art)

1. **Compose-Qualitätssystem ausbauen**
   - UI-State strikt unidirektional, Screen-States als sealed models.
   - Reusable „ExerciseStepScaffold“ für alle Arbeitsblatt-Flows.

2. **Performance & Startzeit**
   - Baseline Profiles + Macrobenchmark für Today/Exercise-Flows.
   - Ziel: flüssige Interaktionen auf Midrange-Geräten.

3. **Resiliente Offline-First-Datenhaltung**
   - Event-basierte Speicherung für Übungen (Start, Schritt, Abschluss, Rating).
   - Verbesserte Insights und robustes Recovery bei App-Abbruch.

4. **Accessibility by default**
   - Dynamische Schriftgrößen, semantische Labels, Screenreader-optimierte Reihenfolge.
   - Kontrast-Checks und Fokusführung für kritische CTAs.

5. **Experimentierbarkeit ohne Risiko**
   - Feature Flags für neue Flows (z. B. Wizard vs. Freitext).
   - A/B-Tests lokal konfigurierbar (ohne cloudpflichtige Infrastruktur).

---

## 4) Verhaltentherapeutische Qualitätssicherung

1. **Fidelity-Checkliste pro Lektion**
   - Enthält: funktionale Perspektive, konkrete Verhaltensalternative, Transferauftrag, Selbstbeobachtung.

2. **„Keine iatrogene Verstärkung“ als Designregel**
   - Keine endlosen Reflexionsschleifen ohne Handlungsabschluss.
   - Nach max. 2 Reflexionsschritten immer Verhaltenseinheit anbieten.

3. **Belastungssensitive Dosissteuerung**
   - Hohe Belastung = kurze, körpernahe, konkrete Übungen.
   - Niedrige Belastung = mehr Analyse, Reframing, Wertearbeit.

---

## 5) Kommunikations- und Motivationsdesign

1. **Message Framing**
   - Gain-Framing für Fortschritt („Du gewinnst Handlungsraum“),
   - Loss-Framing nur sparsam bei Rückfallprävention.

2. **Persuasive Sequencing**
   - Erst Empathie → dann Klarheit → dann konkrete Handlung.

3. **Selbstwirksamkeitsanker**
   - Fortschrittsnarrativ pro Woche: „Was du bereits kannst“ statt nur Defizittracking.

4. **Soziale Anschlussfähigkeit**
   - Optionales Teilen von „Notfallkarte“ oder „Unterstützungswunsch“ mit Vertrauensperson (datenschutzsensibel).

---

## 6) Metriken zur Wirkungskontrolle

### Primäre Outcomes
- Veränderung Belastungsrating (vor/nach Übungen)
- Abschlussrate pro Lektion/Modul
- Anteil Tage mit aktiver Unterbrechungsübung

### Sekundäre Outcomes
- Zeit bis zur ersten Aktion nach Grübelerkennen
- Wiederaufnahmerate nach Unterbrechung (7/30 Tage)
- Drop-off pro Screen-Typ (Info, Reflexion, Übung)

### Qualitätsindikatoren
- Verständlichkeit (1-Item nach Lektion)
- Gefühl von Unterstützung/Validierung
- wahrgenommene Umsetzbarkeit im Alltag

---

## 7) Umsetzungsreihenfolge (empfohlen)

1. P0 vollständig (Mikro-Commitment, 2-Item-Ratings, Akutmodus, Copy-Standard).
2. AB 4–6 Wizard + Notfallkarte (P1 Kern).
3. Adaptiver Lernpfad + Feedback-Loops.
4. Performance/Accessibility-Härtung.
5. JITAI-Logik und Typisierung (P2).

Damit wird das Modulmanual nicht nur inhaltlich abgebildet, sondern **verhaltenswirksam in der App-Interaktion operationalisiert**.
