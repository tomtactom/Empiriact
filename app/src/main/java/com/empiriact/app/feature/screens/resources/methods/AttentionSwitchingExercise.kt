package com.empiriact.app.ui.screens.resources.methods

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions
import com.empiriact.app.ui.navigation.Route
import kotlinx.coroutines.delay

private enum class AsSwitchState { INTRO, INSTRUCTION, SETUP, EXERCISE, SUMMARY }
private enum class AsFocusTarget { EXTERNAL, INTERNAL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttentionSwitchingExercise(navController: NavController, from: String) {
    var state by remember { mutableStateOf(AsSwitchState.INTRO) }
    var totalReps by remember { mutableIntStateOf(8) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aufmerksamkeitswechsel") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = ContentDescriptions.CLOSE_DIALOG)
                    }
                }
            )
        }
    ) { padding ->
        AnimatedContent(targetState = state, modifier = Modifier.padding(padding), label = "StateAnimation") { targetState ->
            when (targetState) {
                AsSwitchState.INTRO -> AsIntroScreen { state = AsSwitchState.INSTRUCTION }
                AsSwitchState.INSTRUCTION -> AsInstructionScreen { state = AsSwitchState.SETUP }
                AsSwitchState.SETUP -> AsSetupScreen(
                    onStart = { reps ->
                        totalReps = reps
                        state = AsSwitchState.EXERCISE
                    }
                )
                AsSwitchState.EXERCISE -> AsExerciseScreen(
                    totalReps = totalReps,
                    onFinish = { state = AsSwitchState.SUMMARY }
                )
                AsSwitchState.SUMMARY -> AsSummaryScreen(totalReps) {
                    navController.navigate(Route.ExerciseRating.createRoute("attention_switching", from))
                }
            }
        }
    }
}

@Composable
private fun AsIntroScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Warum den Fokus wechseln?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text("Das bewusste Wechseln deiner Aufmerksamkeit zwischen der inneren und äußeren Welt ist wie Training für dein Gehirn. Es stärkt deine Fähigkeit, aus Grübelschleifen auszusteigen und gibt dir die Kontrolle zurück.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onNext) { Text("Verstanden") }
    }
}

@Composable
private fun AsInstructionScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Wie funktioniert das Training?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text("Du wirst für 30-Sekunden-Intervalle abwechselnd deinen Fokus nach INNEN (Gedanken, Gefühle) und nach AUSSEN (Sehen, Hören, Fühlen) richten. Ein Gong signalisiert das Ende jedes Intervalls.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onNext) { Text("Los geht's") }
    }
}

@Composable
private fun AsSetupScreen(onStart: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Wie viele Wechsel möchtest du trainieren?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { onStart(4) }) { Text("4 Wechsel") }
            Button(onClick = { onStart(8) }) { Text("8 Wechsel") }
            Button(onClick = { onStart(12) }) { Text("12 Wechsel") }
        }
    }
}

@Composable
private fun AsExerciseScreen(totalReps: Int, onFinish: () -> Unit) {
    var currentRep by remember { mutableIntStateOf(1) }
    var focusTarget by remember { mutableStateOf(AsFocusTarget.EXTERNAL) }
    var timeRemaining by remember { mutableIntStateOf(30) }
    val context = LocalContext.current

    LaunchedEffect(currentRep) {
        timeRemaining = 30
        while (timeRemaining > 0) {
            delay(1000)
            timeRemaining--
        }
        playGongSound(context)
        if (currentRep < totalReps) {
            currentRep++
            focusTarget = if (focusTarget == AsFocusTarget.EXTERNAL) AsFocusTarget.INTERNAL else AsFocusTarget.EXTERNAL
        } else {
            onFinish()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Wechsel $currentRep / $totalReps", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        AnimatedContent(targetState = focusTarget, label = "FocusAnimation") { target ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (target == AsFocusTarget.EXTERNAL) {
                    Icon(Icons.Default.Public, contentDescription = "Fokus nach Außen", modifier = Modifier.size(100.dp))
                    Text("Fokus: AUSSEN", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 8.dp))
                    Text("Sehen, Hören, Fühlen", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
                } else {
                    Icon(Icons.Default.SelfImprovement, contentDescription = "Fokus nach Innen", modifier = Modifier.size(100.dp))
                    Text("Fokus: INNEN", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 8.dp))
                    Text("Gedanken, Gefühle, Körper", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
        Spacer(Modifier.height(32.dp))
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(progress = { timeRemaining / 30f }, modifier = Modifier.size(120.dp))
            Text(timeRemaining.toString(), style = MaterialTheme.typography.displaySmall)
        }
    }
}

@Composable
private fun AsSummaryScreen(completedReps: Int, onFinish: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Stark gemacht!", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text("Du hast $completedReps erfolgreiche Wechsel durchgeführt und deine kognitive Flexibilität trainiert.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onFinish) { Text("Übung abschließen") }
    }
}

private fun playGongSound(context: Context) {
    val sampleRate = 44100
    val duration = 1 // second
    val numSamples = duration * sampleRate
    val buffer = ShortArray(numSamples)
    val audioTrack = AudioTrack(
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build(),
        AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build(),
        buffer.size,
        AudioTrack.MODE_STATIC,
        1 // session ID
    )

    for (i in 0 until numSamples) {
        val time = i.toDouble() / sampleRate
        val value = (Math.sin(2 * Math.PI * 440.0 * time) * (Short.MAX_VALUE) * Math.exp(-5.0 * time)).toInt()
        buffer[i] = value.toShort()
    }

    audioTrack.write(buffer, 0, buffer.size)
    audioTrack.play()
}
