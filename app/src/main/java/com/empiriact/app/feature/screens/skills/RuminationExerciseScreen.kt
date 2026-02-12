package com.empiriact.app.ui.screens.skills

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.empiriact.app.ui.common.ContentDescriptions
import com.empiriact.app.ui.common.ResourceExercise
import kotlinx.coroutines.delay

enum class ExerciseStep {
    INITIAL, TIMER, DEFUSION, ACTION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuminationExerciseScreen(onFinish: () -> Unit) {
    var step by remember { mutableStateOf(ExerciseStep.INITIAL) }
    var worryText by remember { mutableStateOf("") }

    ResourceExercise(onFinish = onFinish) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Grübel-Stopp") },
                    navigationIcon = {
                        IconButton(onClick = onFinish) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = ContentDescriptions.CLOSE_DIALOG
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (step) {
                    ExerciseStep.INITIAL -> {
                        Text("Was beschäftigt dich gerade?", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(16.dp))
                        Text("Schreibe den Gedanken auf, der sich im Kreis dreht, und starte dann die Grübelzeit.", textAlign = TextAlign.Center)
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = worryText,
                            onValueChange = { worryText = it },
                            label = { Text("Mein Gedanke...") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { step = ExerciseStep.TIMER }, enabled = worryText.isNotBlank()) {
                            Text("Grübelzeit starten")
                        }
                    }
                    ExerciseStep.TIMER -> {
                        WorryTimer(onTimerFinish = { step = ExerciseStep.DEFUSION })
                    }
                    ExerciseStep.DEFUSION -> {
                        Text("Zeit ist um!", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(16.dp))
                        Text("Stell dir vor, der Gedanke ist eine Wolke. Beobachte, wie sie am Himmel vorbeizieht. Du musst sie nicht festhalten.", textAlign = TextAlign.Center)
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { step = ExerciseStep.ACTION }) {
                            Text("Weiter")
                        }
                    }
                    ExerciseStep.ACTION -> {
                        Text("Gut gemacht!", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(16.dp))
                        Text("Richte deine Aufmerksamkeit jetzt auf das Hier und Jetzt. Was ist eine kleine, konkrete Handlung, die du als Nächstes tun kannst?", textAlign = TextAlign.Center)
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = onFinish) {
                            Text("Übung beenden")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorryTimer(durationSeconds: Int = 120, onTimerFinish: () -> Unit) {
    var timeLeft by remember { mutableStateOf(durationSeconds) }
    val progress by animateFloatAsState(
        targetValue = timeLeft / durationSeconds.toFloat(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = ""
    )

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        onTimerFinish()
    }

    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(progress = { progress }, modifier = Modifier.size(200.dp), strokeWidth = 8.dp)
        Text("$timeLeft s", style = MaterialTheme.typography.headlineLarge)
    }
    Spacer(Modifier.height(16.dp))
    Text("Erlaube dir, für diese Zeit intensiv über das Thema nachzudenken.", textAlign = TextAlign.Center)
}
