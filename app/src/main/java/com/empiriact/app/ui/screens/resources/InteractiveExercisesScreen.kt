package com.empiriact.app.ui.screens.resources

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

// ============== Interaktive Übungs-Module ==============

private data class InteractiveExercise(
    val id: String,
    val title: String,
    val description: String,
    val duration: Int, // Minuten
    val steps: List<ExerciseStep>,
    val difficulty: String,
    val category: String,
    val benefits: List<String>
)

private data class ExerciseStep(
    val number: Int,
    val title: String,
    val instruction: String,
    val duration: Int, // Sekunden
    val guidance: String,
    val tips: List<String>
)

private fun getInteractiveExercises(): List<InteractiveExercise> {
    return listOf(
        InteractiveExercise(
            id = "grounding_555",
            title = "5-4-3-2-1 Erdungsübung",
            description = "Eine schnelle Technik, um aus Gedankenschleifen in die Gegenwart zu kommen.",
            duration = 5,
            difficulty = "Anfänger",
            category = "Erdung",
            steps = listOf(
                ExerciseStep(
                    number = 1,
                    title = "Schritt 1: Sieh 5 Dinge",
                    instruction = "Schau dich um und benenne 5 Dinge, die du siehst.",
                    duration = 60,
                    guidance = "Nimm dir Zeit, um wirklich zu beobachten. Es können kleine Details sein.",
                    tips = listOf(
                        "Farben, Formen, Texturen",
                        "Was zieht deine Aufmerksamkeit an?",
                        "Sei konkret und präzise"
                    )
                ),
                ExerciseStep(
                    number = 2,
                    title = "Schritt 2: Höre 4 Dinge",
                    instruction = "Konzentriere dich auf 4 Geräusche, die du hören kannst.",
                    duration = 60,
                    guidance = "Höre hin. Es können subtile Geräusche sein – Wind, Vögel, dein Atem.",
                    tips = listOf(
                        "Nahe und ferne Geräusche",
                        "Musik oder Stille?",
                        "Was ist im Hintergrund?"
                    )
                ),
                ExerciseStep(
                    number = 3,
                    title = "Schritt 3: Berühre 3 Dinge",
                    instruction = "Berühre 3 verschiedene Gegenstände und beschreibe ihre Textur.",
                    duration = 60,
                    guidance = "Verlangsame dich. Fühle die Temperatur, Rauhheit, Glätte.",
                    tips = listOf(
                        "Deine Kleidung, ein Gegenstand, deine Haut",
                        "Kalt oder warm?",
                        "Hart oder weich?"
                    )
                ),
                ExerciseStep(
                    number = 4,
                    title = "Schritt 4: Rieche 2 Dinge",
                    instruction = "Identifiziere 2 Gerüche in deiner Umgebung.",
                    duration = 60,
                    guidance = "Atme bewusst. Geruchssinn ist sehr erdend.",
                    tips = listOf(
                        "Dein Körper, die Luft, Essen",
                        "Angenehm oder neutral?",
                        "Intensiv oder subtil?"
                    )
                ),
                ExerciseStep(
                    number = 5,
                    title = "Schritt 5: Schmecke 1 Ding",
                    instruction = "Konzentriere dich auf 1 Geschmack, den du hast.",
                    duration = 60,
                    guidance = "Langsam kauen oder lutschen hilft. Achte auf Nuancen.",
                    tips = listOf(
                        "Kaugummi, Bonbon, dein natürlicher Speichel",
                        "Süß, sauer, salzig?",
                        "Wie veränderst sich der Geschmack?"
                    )
                ),
                ExerciseStep(
                    number = 6,
                    title = "Abschluss: Körperbewusstsein",
                    instruction = "Mache jetzt eine kurze Körperscan. Wie fühlst du dich?",
                    duration = 60,
                    guidance = "Merke die Veränderung. Du bist jetzt präsent und geerdet.",
                    tips = listOf(
                        "Weniger Angst?",
                        "Mehr Klarheit?",
                        "Das ist normal und wirksam"
                    )
                )
            ),
            benefits = listOf(
                "Schnelle Rückkehr zur Gegenwart",
                "Reduziert Angstsymptome",
                "Aktiviert alle Sinne"
            )
        ),
        InteractiveExercise(
            id = "progressive_relaxation",
            title = "Progressive Muskelentspannung",
            description = "Spanne und entspanne Muskelgruppen systematisch.",
            duration = 10,
            difficulty = "Anfänger",
            category = "Entspannung",
            steps = listOf(
                ExerciseStep(
                    number = 1,
                    title = "Vorbereitung",
                    instruction = "Setze dich bequem hin oder leg dich hin.",
                    duration = 30,
                    guidance = "Finde eine Position, die angenehm ist. Keine Eile.",
                    tips = listOf(
                        "Lockere Kleidung",
                        "Ruhige Umgebung",
                        "Telefon ausschalten"
                    )
                ),
                ExerciseStep(
                    number = 2,
                    title = "Füße und Unterschenkel",
                    instruction = "Spanne deine Fußmuskeln für 5 Sekunden an, dann entspanne für 10 Sekunden.",
                    duration = 60,
                    guidance = "Du solltest eine klare Anspannung fühlen, gefolgt von Entspannung.",
                    tips = listOf(
                        "Wiederhole 2x",
                        "Achte auf Unterschenkel",
                        "Spüre die Wärme der Entspannung"
                    )
                ),
                ExerciseStep(
                    number = 3,
                    title = "Oberschenkel und Gesäß",
                    instruction = "Spanne deine Oberschenkel-Muskeln an. Halten. Entspannen.",
                    duration = 60,
                    guidance = "Die größten Muskeln bringen die tiefste Entspannung.",
                    tips = listOf(
                        "Wiederhole 2x",
                        "Großer Effekt auf Gesamtkörper",
                        "Verwende alle Muskelgruppen"
                    )
                ),
                ExerciseStep(
                    number = 4,
                    title = "Bauch und Brust",
                    instruction = "Spanne deinen Bauch an. Halte. Lass los.",
                    duration = 60,
                    guidance = "Dies sind oft Ansammlungsorte für Spannung.",
                    tips = listOf(
                        "Tief atmen während Entspannung",
                        "Wiederhole 2x",
                        "Spüre die Öffnung"
                    )
                ),
                ExerciseStep(
                    number = 5,
                    title = "Arme und Hände",
                    instruction = "Balle deine Fäuste und spanne deine Armmuskeln.",
                    duration = 60,
                    guidance = "Halte die Spannung, dann lasse sie langsam fließen.",
                    tips = listOf(
                        "Wiederhole 2x",
                        "Achte auf Hände und Unterarme",
                        "Tiefes Ausatmen beim Loslassen"
                    )
                ),
                ExerciseStep(
                    number = 6,
                    title = "Hals, Kinn und Gesicht",
                    instruction = "Spanne die Nackenmuskulatur und die Kinnmuskulatur an.",
                    duration = 60,
                    guidance = "Das Gesicht trägt oft Spannung. Sei sanft.",
                    tips = listOf(
                        "Augen zusammenkneifen",
                        "Mund spitzen",
                        "Wiederhole 2x"
                    )
                ),
                ExerciseStep(
                    number = 7,
                    title = "Endscan und Integrieren",
                    instruction = "Scanne deinen Körper von oben bis unten. Spüre die Ruhe.",
                    duration = 120,
                    guidance = "Verweile in diesem entspannten Zustand. Genieße es.",
                    tips = listOf(
                        "Dies ist dein natürlicher Entspannungszustand",
                        "Du kannst jederzeit hierher zurückkehren",
                        "Dein Körper merkt sich diese Erfahrung"
                    )
                )
            ),
            benefits = listOf(
                "Tiefe körperliche Entspannung",
                "Reduziert chronische Spannungen",
                "Verbessert Körperbewusstsein"
            )
        ),
        InteractiveExercise(
            id = "thought_labeling",
            title = "Gedanken-Etikettierung",
            description = "Lerne, Gedanken als mentale Ereignisse zu behandeln.",
            duration = 7,
            difficulty = "Fortgeschrittene",
            category = "Kognitive Defusion",
            steps = listOf(
                ExerciseStep(
                    number = 1,
                    title = "Einen Gedanken erkennen",
                    instruction = "Achte auf einen Gedanken, der dir gerade kommt.",
                    duration = 60,
                    guidance = "Es kann ein Sorgengedanke, ein Gedanke über dich, oder ein alltäglicher Gedanke sein.",
                    tips = listOf(
                        "Nicht erzwingen",
                        "Gedanken kommen natürlicherweise",
                        "Sei neugierig, nicht kritisch"
                    )
                ),
                ExerciseStep(
                    number = 2,
                    title = "Den Gedanken benennen",
                    instruction = "Klassifiziere den Gedanken: Ist es ein Planungs-, Sorgen- oder Wertungsgedanke?",
                    duration = 60,
                    guidance = "Das Etikettieren schafft Abstand zum Gedanken.",
                    tips = listOf(
                        "Planung: \"Ich sollte...\"",
                        "Sorge: \"Was wenn...?\"",
                        "Wertung: \"Ich bin schlecht...\""
                    )
                ),
                ExerciseStep(
                    number = 3,
                    title = "Etikett sprechen",
                    instruction = "Sage laut oder leise: \"Ich habe den Gedanken, dass [Gedanke]\"",
                    duration = 60,
                    guidance = "Dies unterscheidet dich vom Gedanken.",
                    tips = listOf(
                        "Du bist nicht der Gedanke",
                        "Der Gedanke ist ein Ereignis",
                        "Du kannst ihn beobachten"
                    )
                ),
                ExerciseStep(
                    number = 4,
                    title = "Neutraler werden",
                    instruction = "Versuche, dem Gedanken eine weitere Form von Etikettierung hinzuzufügen.",
                    duration = 60,
                    guidance = "Zum Beispiel: \"Mein Gehirn dreht gerade ein Angstfilm.\"",
                    tips = listOf(
                        "Mit Humor und Mitgefühl",
                        "\"Oh, da ist dieser Gedanke wieder\"",
                        "Mit Neugier, nicht Widerstand"
                    )
                ),
                ExerciseStep(
                    number = 5,
                    title = "Loslassen und weitermachen",
                    instruction = "Beobachte, wie der Gedanke natürlicherweise vorbeizieht.",
                    duration = 120,
                    guidance = "Wenn du den Gedanken nicht fütterst, verliert er an Kraft.",
                    tips = listOf(
                        "Keine Anstrengung erforderlich",
                        "Wie eine Wolke am Himmel",
                        "Er kommt, er geht"
                    )
                )
            ),
            benefits = listOf(
                "Reduziert Gedankenkontrolle",
                "Erhöht mentale Flexibilität",
                "Fördert Akzeptanz von Gedanken"
            )
        )
    )
}

// ============== UI für interaktive Übungen ==============

@Composable
fun InteractiveExercisesScreen(onBack: () -> Unit) {
    var selectedExerciseId by rememberSaveable { mutableStateOf<String?>(null) }
    val exercises = remember { getInteractiveExercises() }

    AnimatedContent(
        targetState = selectedExerciseId,
        label = "ExerciseTransition"
    ) { exerciseId ->
        if (exerciseId == null) {
            ExerciseListScreen(
                exercises = exercises,
                onSelectExercise = { selectedExerciseId = it },
                onBack = onBack
            )
        } else {
            val exercise = exercises.find { it.id == exerciseId }
            if (exercise != null) {
                ExerciseDetailScreen(
                    exercise = exercise,
                    onBack = { selectedExerciseId = null }
                )
            }
        }
    }
}

@Composable
private fun ExerciseListScreen(
    exercises: List<InteractiveExercise>,
    onSelectExercise: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Zurück")
            }
            Text(
                text = "Übungen",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Box(modifier = Modifier.size(40.dp))
        }

        // Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF10B981).copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Geführte, schrittweise Übungen",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Jede Übung ist zeitlich geplant und mit Anleitungen ausgestattet.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exercise List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 16.dp)
        ) {
            itemsIndexed(exercises) { index, exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onClick = { onSelectExercise(exercise.id) }
                )
            }
        }
    }
}

@Composable
private fun ExerciseCard(exercise: InteractiveExercise, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(2.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Titel und Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = exercise.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(Color(0xFF3B82F6).copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = exercise.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF3B82F6)
                    )
                }
            }

            // Beschreibung
            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Meta-Infos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${exercise.duration} min",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Difficulty
                Box(
                    modifier = Modifier
                        .background(
                            color = if (exercise.difficulty == "Anfänger") Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFF59E0B).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                ) {
                    Text(
                        text = exercise.difficulty,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (exercise.difficulty == "Anfänger") Color(0xFF10B981) else Color(0xFFF59E0B)
                    )
                }
            }

            // Benefits
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(minOf(2, exercise.benefits.size)) {
                    Text(
                        text = "✓ ${exercise.benefits[it]}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseDetailScreen(
    exercise: InteractiveExercise,
    onBack: () -> Unit
) {
    var currentStepIndex by rememberSaveable { mutableIntStateOf(0) }
    var isStarted by rememberSaveable { mutableStateOf(false) }
    val currentStep = exercise.steps[currentStepIndex]

    if (!isStarted) {
        ExercisePreviewScreen(
            exercise = exercise,
            onStart = { isStarted = true },
            onBack = onBack
        )
    } else {
        ExerciseProgressScreen(
            exercise = exercise,
            currentStep = currentStep,
            currentStepIndex = currentStepIndex,
            onNextStep = {
                if (currentStepIndex < exercise.steps.size - 1) {
                    currentStepIndex++
                } else {
                    onBack()
                }
            },
            onPreviousStep = {
                if (currentStepIndex > 0) currentStepIndex--
            },
            onFinish = onBack
        )
    }
}

@Composable
private fun ExercisePreviewScreen(
    exercise: InteractiveExercise,
    onStart: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.Close, contentDescription = "Schließen")
            }
            Box(modifier = Modifier.weight(1f))
            IconButton(onClick = {}) {
                Icon(Icons.Default.EmojiEvents, contentDescription = "Favorit")
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Titel
            Text(
                text = exercise.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Card mit Infos
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3B82F6).copy(alpha = 0.08f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoItem(label = "Dauer", value = "${exercise.duration} min")
                    InfoItem(label = "Schritte", value = "${exercise.steps.size}")
                    InfoItem(label = "Level", value = exercise.difficulty)
                }
            }

            // Beschreibung
            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium
            )

            // Benefits
            Text(
                text = "Nutzen",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                exercise.benefits.forEach { benefit ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = benefit, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Anleitung-Preview
            Text(
                text = "Übersicht der Schritte",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                exercise.steps.forEach { step ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color(0x1F3B82F6), RoundedCornerShape(50.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${step.number}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF3B82F6)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = step.title,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${step.duration} Sek.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                    Text("Übung starten")
                }
            }
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Abbrechen")
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ExerciseProgressScreen(
    exercise: InteractiveExercise,
    currentStep: ExerciseStep,
    currentStepIndex: Int,
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onFinish: () -> Unit
) {
    var showTimeoutDialog by rememberSaveable { mutableStateOf(false) }
    var timeRemaining by rememberSaveable { mutableIntStateOf(currentStep.duration) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF3B82F6).copy(alpha = 0.08f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Progress-Kopf
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onFinish) {
                        Icon(Icons.Default.Close, contentDescription = "Beenden")
                    }
                    Text(
                        text = exercise.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Box(modifier = Modifier.size(40.dp))
                }

                // Schritt-Fortschritt
                LinearProgressIndicator(
                    progress = { (currentStepIndex + 1) / exercise.steps.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF3B82F6)
                )

                Text(
                    text = "Schritt ${currentStepIndex + 1} von ${exercise.steps.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Schritt-Inhalt
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Großer Schritt-Nummer-Kreis
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(80.dp)
                        .background(Color(0xFF3B82F6).copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${currentStep.number}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3B82F6)
                    )
                }

                // Titel und Anweisung
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = currentStep.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = currentStep.instruction,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Timer
                TimerBox(
                    duration = currentStep.duration,
                    onTimeComplete = onNextStep
                )

                // Anleitung
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF10B981).copy(alpha = 0.08f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Anleitung",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                        }
                        Text(
                            text = currentStep.guidance,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Tipps
                if (currentStep.tips.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF59E0B).copy(alpha = 0.08f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Tipps",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF59E0B)
                            )
                            currentStep.tips.forEach { tip ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "•",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFF59E0B)
                                    )
                                    Text(
                                        text = tip,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onPreviousStep,
                    modifier = Modifier.weight(1f),
                    enabled = currentStepIndex > 0
                ) {
                    Text("← Zurück")
                }
                Button(
                    onClick = onNextStep,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (currentStepIndex == exercise.steps.size - 1) "Fertig ✓" else "Weiter →")
                }
            }
        }
    }
}

@Composable
private fun TimerBox(duration: Int, onTimeComplete: () -> Unit) {
    var secondsRemaining by rememberSaveable { mutableIntStateOf(duration) }

    // Simulate timer (in real app, use LaunchedEffect with timer)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3B82F6).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = secondsRemaining.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B82F6)
            )
            Text(
                text = "Sekunden",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

