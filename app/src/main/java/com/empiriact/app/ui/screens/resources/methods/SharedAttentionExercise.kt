package com.empiriact.app.ui.screens.resources.methods

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions
import com.empiriact.app.ui.navigation.Route
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

private enum class SharedAttentionScreenState { INTRO, INSTRUCTION, SETUP, EXERCISE, SUMMARY }
private enum class SharedAttentionPhase { BROAD, DUAL, INTEGRATED }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedAttentionExercise(navController: NavController, from: String) {
    var state by remember { mutableStateOf(SharedAttentionScreenState.INTRO) }
    var intervalDuration by remember { mutableIntStateOf(45) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Geteilte Aufmerksamkeit") },
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
                SharedAttentionScreenState.INTRO -> SaIntroScreen { state = SharedAttentionScreenState.INSTRUCTION }
                SharedAttentionScreenState.INSTRUCTION -> SaInstructionScreen { state = SharedAttentionScreenState.SETUP }
                SharedAttentionScreenState.SETUP -> SaSetupScreen { duration ->
                    intervalDuration = duration
                    state = SharedAttentionScreenState.EXERCISE
                }
                SharedAttentionScreenState.EXERCISE -> SaExerciseScreen(intervalDuration) {
                    state = SharedAttentionScreenState.SUMMARY
                }
                SharedAttentionScreenState.SUMMARY -> SaSummaryScreen {
                    navController.navigate(Route.ExerciseRating.createRoute("shared_attention", from))
                }
            }
        }
    }
}

@Composable
private fun SaIntroScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Weite deinen Blick", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text("Diese Übung hilft dir, aus dem Tunnelblick auszubrechen und eine offene, flexible Wahrnehmung zu kultivieren.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onNext) { Text("So geht's") }
    }
}

@Composable
private fun SaInstructionScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Drei Phasen des Sehens", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text("1. BREIT: Nimm alles gleichzeitig wahr.\n2. DUAL: Halte einen Punkt im Fokus, aber bleibe offen für den Rest.\n3. INTEGRIERT: Lass Fokus und Weite zu einer Einheit verschmelzen.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onNext) { Text("Verstanden") }
    }
}

@Composable
private fun SaSetupScreen(onStart: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Wie lange pro Phase üben?", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { onStart(30) }) { Text("30 Sek") }
            Button(onClick = { onStart(45) }) { Text("45 Sek") }
            Button(onClick = { onStart(60) }) { Text("60 Sek") }
        }
    }
}

@Composable
private fun SaExerciseScreen(duration: Int, onFinish: () -> Unit) {
    var phase by remember { mutableStateOf(SharedAttentionPhase.BROAD) }
    var timeRemaining by remember { mutableIntStateOf(duration) }

    LaunchedEffect(phase) {
        timeRemaining = duration
        while (timeRemaining > 0) {
            delay(1000)
            timeRemaining--
        }
        playGongSound()
        when (phase) {
            SharedAttentionPhase.BROAD -> phase = SharedAttentionPhase.DUAL
            SharedAttentionPhase.DUAL -> phase = SharedAttentionPhase.INTEGRATED
            SharedAttentionPhase.INTEGRATED -> onFinish()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        AnimatedContent(targetState = phase, label = "PhaseAnimation") { currentPhase ->
            Text(text = currentPhase.name, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(Modifier.height(32.dp))
        FocusLandscape(phase)
        Spacer(Modifier.height(32.dp))
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(progress = { timeRemaining.toFloat() / duration }, modifier = Modifier.size(100.dp))
            Text(timeRemaining.toString(), style = MaterialTheme.typography.headlineLarge)
        }
    }
}

@Composable
private fun FocusLandscape(phase: SharedAttentionPhase) {
    val pulse by animateFloatAsState(targetValue = 1f, animationSpec = tween(durationMillis = 2000), label = "Pulse")
    val primaryColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.size(250.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        when (phase) {
            SharedAttentionPhase.BROAD -> {
                val brush = Brush.radialGradient(colors = listOf(primaryColor.copy(alpha = 0.1f * pulse), Color.Transparent), center = center)
                drawCircle(brush, radius = size.width / 2)
            }
            SharedAttentionPhase.DUAL -> {
                val brush = Brush.radialGradient(colors = listOf(primaryColor.copy(alpha = 0.1f), Color.Transparent), center = center)
                drawCircle(brush, radius = size.width / 2)
                drawCircle(primaryColor, radius = 10.dp.toPx(), center = center)
            }
            SharedAttentionPhase.INTEGRATED -> {
                val brush = Brush.radialGradient(colors = listOf(primaryColor.copy(alpha = 0.3f * pulse), Color.Transparent), center = center)
                drawCircle(brush, radius = size.width / 2 * pulse)
            }
        }
    }
}

@Composable
private fun SaSummaryScreen(onFinish: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Stark!", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text("Du hast gelernt, deine Aufmerksamkeit bewusst zu weiten und zu integrieren. Nutze diese Fähigkeit, um aus dem Tunnelblick auszubrechen.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onFinish) { Text("Übung abschließen") }
    }
}

private fun playGongSound() {
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
        val value = (sin(2 * PI * 440.0 * time) * (Short.MAX_VALUE) * exp(-5.0 * time)).toInt()
        buffer[i] = value.toShort()
    }

    audioTrack.write(buffer, 0, buffer.size)
    audioTrack.play()
}
