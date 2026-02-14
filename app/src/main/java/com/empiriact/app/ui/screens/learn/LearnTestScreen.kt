package com.empiriact.app.ui.screens.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ActionButton
import com.empiriact.app.ui.common.ContentDescriptions

data class TestQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)

private val testQuestions = listOf(
    TestQuestion(
        "Welche Aussage trifft Rumination am besten?",
        listOf(
            "Konkretes, zeitlich begrenztes Problemlösen",
            "Wiederholtes negatives Kreisen ohne klaren Handlungsschritt",
            "Entspannungstechnik zur Emotionsregulation",
            "Neutrales Erinnern ohne Belastung"
        ),
        1
    ),
    TestQuestion(
        "Worin liegt der zentrale therapeutische Fokus in Modul 1?",
        listOf(
            "Nur positive Gedanken denken",
            "Denkinhalte komplett vermeiden",
            "Das Wie des Denkens (Denkstil) verändern",
            "Ausschließlich Schlaf verbessern"
        ),
        2
    ),
    TestQuestion(
        "Welche Funktion hat die 3-Fragen-Daumenregel?",
        listOf(
            "Diagnosen ersetzen",
            "Akut erkennen, ob hilfreiches Denken oder Grübeln vorliegt",
            "Nur Therapieaufgaben planen",
            "Gefühle unterdrücken"
        ),
        1
    ),
    TestQuestion(
        "Wozu dient die Funktionsanalyse (AB 4-6) primär?",
        listOf(
            "Ausschließlich Rückschau ohne Konsequenzen",
            "Analyse von Auslösern, Reaktionen, Konsequenzen und Aufrechterhaltung",
            "Sofortige Konfrontation ohne Verständnis der Muster",
            "Vergleich mit anderen Personen"
        ),
        1
    ),
    TestQuestion(
        "Was wird mit AB 8 (Therapietracker) unterstützt?",
        listOf(
            "Nur tägliche To-do-Listen",
            "Verlaufsevaluation von Leidensdruck, Häufigkeit, Dauer, Triggern und Skills",
            "Ausschließlich Medikamentenplanung",
            "Einmalige Abschlussbewertung"
        ),
        1
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnTestScreen(navController: NavController) {
    val currentQuestion = remember { mutableStateOf(0) }
    val score = remember { mutableStateOf(0) }
    val showResults = remember { mutableStateOf(false) }
    val selectedAnswer = remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wissenscheck: Modul 1") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ContentDescriptions.BACK_BUTTON
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (showResults.value) {
            TestResultsScreen(
                score = score.value,
                totalQuestions = testQuestions.size,
                onRetry = {
                    currentQuestion.value = 0
                    score.value = 0
                    showResults.value = false
                    selectedAnswer.value = null
                },
                onBack = { navController.navigateUp() }
            )
        } else {
            val question = testQuestions[currentQuestion.value]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Frage ${currentQuestion.value + 1} von ${testQuestions.size}",
                    style = MaterialTheme.typography.labelMedium
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = question.question,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    question.options.forEachIndexed { index, option ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedAnswer.value = index }
                                .background(
                                    if (selectedAnswer.value == index) {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    } else {
                                        Color.Transparent
                                    }
                                )
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (currentQuestion.value > 0) {
                        Button(
                            onClick = {
                                currentQuestion.value--
                                selectedAnswer.value = null
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Zurück")
                        }
                    }

                    ActionButton(
                        onClick = {
                            if (selectedAnswer.value == question.correctAnswer) {
                                score.value++
                            }

                            if (currentQuestion.value < testQuestions.size - 1) {
                                currentQuestion.value++
                                selectedAnswer.value = null
                            } else {
                                showResults.value = true
                            }
                        },
                        enabled = selectedAnswer.value != null,
                        text = if (currentQuestion.value == testQuestions.size - 1) {
                            "Fertig"
                        } else {
                            "Weiter"
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TestResultsScreen(
    score: Int,
    totalQuestions: Int,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    val percentage = (score * 100) / totalQuestions
    val resultColor = when {
        percentage >= 80 -> Color.Green
        percentage >= 60 -> Color(0xFFFFA500)
        else -> Color.Red
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Wissenscheck abgeschlossen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .size(150.dp)
                .background(resultColor.copy(alpha = 0.2f), RoundedCornerShape(75.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.headlineLarge,
                color = resultColor
            )
        }

        Text(
            text = "Ergebnis: $score von $totalQuestions richtig",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        val feedbackText = when {
            percentage >= 80 -> "Hervorragende Leistung! Du hast die Inhalte sehr gut verstanden."
            percentage >= 60 -> "Gute Arbeit! Ein paar Bereiche könnten noch wiederholt werden."
            else -> "Bitte wiederhole die Inhalte und versuche es erneut."
        }

        Text(
            text = feedbackText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                onClick = onBack,
                text = "Zurück",
                modifier = Modifier.weight(1f)
            )
            ActionButton(
                onClick = onRetry,
                text = "Erneut versuchen",
                modifier = Modifier.weight(1f)
            )
        }
    }
}
