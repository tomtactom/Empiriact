package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions
import com.empiriact.app.ui.navigation.Route
import kotlinx.coroutines.delay

// --- State Management ---
private enum class SaScreenState { INTRO, FOCUS_CHOICE, FOCUS_ARENA, REFLECTION, SUMMARY }
private enum class SaFocusType { VISUAL, AUDITORY, SENSORY }
private data class SaFocusOption(val type: SaFocusType, val title: String, val icon: ImageVector)

private val saFocusOptions = listOf(
    SaFocusOption(SaFocusType.VISUAL, "Visuell", Icons.Default.Visibility),
    SaFocusOption(SaFocusType.AUDITORY, "Auditiv", Icons.Default.Hearing),
    SaFocusOption(SaFocusType.SENSORY, "Körper", Icons.Default.SelfImprovement)
)

// --- Main Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectiveAttentionExercise(navController: NavController, from: String) {
    var state by rememberSaveable { mutableStateOf(SaScreenState.INTRO) }
    var focusType by rememberSaveable { mutableStateOf(SaFocusType.VISUAL) } 
    var durationInSeconds by rememberSaveable { mutableIntStateOf(120) }
    var thoughtsCaught by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selektive Aufmerksamkeit") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = ContentDescriptions.CLOSE_DIALOG)
                    }
                }
            )
        }
    ) { padding ->
        AnimatedContent(targetState = state, modifier = Modifier.padding(padding), label = "ScreenAnimation") { targetState ->
            when (targetState) {
                SaScreenState.INTRO -> IntroScreen { state = SaScreenState.FOCUS_CHOICE }
                SaScreenState.FOCUS_CHOICE -> FocusChoiceScreen(
                    onStart = { type, duration ->
                        focusType = type
                        durationInSeconds = duration
                        thoughtsCaught = 0
                        state = SaScreenState.FOCUS_ARENA
                    }
                )
                SaScreenState.FOCUS_ARENA -> FocusArenaScreen(
                    duration = durationInSeconds,
                    onThoughtCaught = { thoughtsCaught++ },
                    onFinish = { state = SaScreenState.REFLECTION }
                )
                SaScreenState.REFLECTION -> ReflectionScreen(
                    thoughtsCaught = thoughtsCaught,
                    onFinish = { state = SaScreenState.SUMMARY }
                )
                SaScreenState.SUMMARY -> SummaryScreen {
                    navController.navigate(Route.ExerciseRating.createRoute("selective_attention", from))
                }
            }
        }
    }
}

// --- Screen Composables ---

@Composable
private fun IntroScreen(onNext: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        Text("Trainiere deinen Fokus", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Text("Diese Übung stärkt deine Fähigkeit, die Aufmerksamkeit bewusst zu lenken und Grübeln zu stoppen.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.height(32.dp))
        Button(onClick = onNext) { Text("Starten") }
    }
}

@Composable
private fun FocusChoiceScreen(onStart: (SaFocusType, Int) -> Unit) {
    var selectedType by remember { mutableStateOf(SaFocusType.VISUAL) }
    var selectedDuration by remember { mutableIntStateOf(120) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
        Text("Wähle deinen Fokus", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            saFocusOptions.forEach { option ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { selectedType = option.type }) {
                    Icon(option.icon, contentDescription = option.title, modifier = Modifier.size(48.dp), tint = if (selectedType == option.type) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                    Text(option.title, color = if (selectedType == option.type) MaterialTheme.colorScheme.primary else Color.Unspecified)
                }
            }
        }

        Text("Wie lange möchtest du üben?", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf(60, 120, 300).forEach { duration ->
                Button(onClick = { selectedDuration = duration }, enabled = selectedDuration != duration) {
                    Text("${duration / 60} min")
                }
            }
        }

        Button(onClick = { onStart(selectedType, selectedDuration) }, modifier = Modifier.fillMaxWidth()) {
            Text("Fokus-Training starten")
        }
    }
}

@Composable
private fun FocusArenaScreen(duration: Int, onThoughtCaught: () -> Unit, onFinish: () -> Unit) {
    var timeRemaining by remember { mutableIntStateOf(duration) }
    val progress by animateFloatAsState(targetValue = timeRemaining.toFloat() / duration, label = "Progress")
    val pulse by animateFloatAsState(
        targetValue = if (timeRemaining > 0) 1.2f else 1f,
        animationSpec = infiniteRepeatable(tween(1500)), label = "Pulse"
    )

    LaunchedEffect(timeRemaining) {
        if (timeRemaining > 0) {
            delay(1000)
            timeRemaining--
        } else {
            onFinish()
        }
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = primaryColor, radius = size.minDimension / 4 * pulse, alpha = 0.1f)
                drawCircle(color = primaryColor, radius = size.minDimension / 5 * pulse, alpha = 0.2f)
            }
            CircularProgressIndicator(progress = { progress }, modifier = Modifier.size(200.dp), strokeWidth = 8.dp)
            Text(formatTime(timeRemaining), style = MaterialTheme.typography.displayMedium)
        }

        Button(onClick = onThoughtCaught) {
            Text("Gedanke bemerkt")
        }
    }
}

@Composable
private fun ReflectionScreen(thoughtsCaught: Int, onFinish: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Gut gemacht!", style = MaterialTheme.typography.headlineMedium)
        Text("Du hast deine Aufmerksamkeit trainiert.", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.height(24.dp))
        Text("Du hast $thoughtsCaught Mal bemerkt, wie deine Gedanken abgeschweift sind. Jeder einzelne Moment ist ein Erfolg!", textAlign = TextAlign.Center)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onFinish) { Text("Weiter zur Zusammenfassung") }
    }
}

@Composable
private fun SummaryScreen(onFinish: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.WorkspacePremium, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Text("Starke Leistung!", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp))
        Text("Jedes Training stärkt deinen 'Aufmerksamkeits-Muskel'. Du hast die Fähigkeit, zu wählen, worauf du dich konzentrierst.", textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.height(32.dp))
        Button(onClick = onFinish) { Text("Übung abschließen") }
    }
}

// --- Helper Functions ---

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%02d:%02d".format(minutes, remainingSeconds)
}
