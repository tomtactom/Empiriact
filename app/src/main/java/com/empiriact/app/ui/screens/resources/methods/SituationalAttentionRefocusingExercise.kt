package com.empiriact.app.ui.screens.resources.methods

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions
import com.empiriact.app.ui.navigation.Route

private enum class SarSense { SEEING, HEARING, FEELING }
private sealed class SarScreenState {
    object INTRO : SarScreenState()
    object SENSE_CHOICE : SarScreenState()
    data class EXERCISE(val sense: SarSense) : SarScreenState()

    companion object {
        val Saver: Saver<SarScreenState, String> = Saver(
            save = { state ->
                when (state) {
                    is EXERCISE -> "EXERCISE:${state.sense.name}"
                    INTRO -> "INTRO"
                    SENSE_CHOICE -> "SENSE_CHOICE"
                }
            },
            restore = { saved ->
                when {
                    saved.startsWith("EXERCISE:") -> EXERCISE(SarSense.valueOf(saved.removePrefix("EXERCISE:")))
                    saved == "INTRO" -> INTRO
                    saved == "SENSE_CHOICE" -> SENSE_CHOICE
                    else -> throw IllegalArgumentException("Unknown screen state: $saved")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SituationalAttentionRefocusingExercise(
    navController: NavController,
    from: String
) {
    var screenState: SarScreenState by rememberSaveable(stateSaver = SarScreenState.Saver) { mutableStateOf(SarScreenState.INTRO) }
    var completedSenses by rememberSaveable { mutableStateOf(emptySet<SarSense>()) }

    fun onSenseComplete(completedSense: SarSense) {
        val newCompletedSenses = completedSenses + completedSense
        completedSenses = newCompletedSenses
        val remainingSenses = SarSense.values().toSet() - newCompletedSenses

        if (remainingSenses.isEmpty()) {
            navController.navigate(Route.ExerciseRating.createRoute("situational_attention_refocusing", from))
        } else {
            screenState = SarScreenState.SENSE_CHOICE
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Refokussierung nach außen") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = ContentDescriptions.CLOSE_DIALOG
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val state = screenState) {
                is SarScreenState.INTRO -> SarIntroScreen(
                    onNext = { screenState = SarScreenState.SENSE_CHOICE }
                )
                is SarScreenState.SENSE_CHOICE -> {
                    val availableSenses = SarSense.values().toSet() - completedSenses
                    if (availableSenses.size == 1) {
                        screenState = SarScreenState.EXERCISE(availableSenses.first())
                    } else {
                        SarSenseChoiceScreen(
                            onSenseSelected = { screenState = SarScreenState.EXERCISE(it) },
                            onFinish = { navController.navigate(Route.ExerciseRating.createRoute("situational_attention_refocusing", from)) },
                            availableSenses = availableSenses,
                            isFirstChoice = completedSenses.isEmpty()
                        )
                    }
                }
                is SarScreenState.EXERCISE -> when (state.sense) {
                    SarSense.SEEING -> SarSenseExerciseScreen(
                        title = "Was siehst du?",
                        question = "Finde 5 Dinge in deiner Umgebung und tippe für jedes auf einen Kreis.",
                        onComplete = { onSenseComplete(SarSense.SEEING) }
                    )
                    SarSense.HEARING -> SarSenseExerciseScreen(
                        title = "Was hörst du?",
                        question = "Konzentriere dich auf 3 Geräusche und tippe für jedes auf einen Kreis.",
                        count = 3,
                        onComplete = { onSenseComplete(SarSense.HEARING) }
                    )
                    SarSense.FEELING -> SarSenseExerciseScreen(
                        title = "Was fühlst du?",
                        question = "Berühre 1 Gegenstand bewusst und tippe dann auf den Kreis.",
                        count = 1,
                        onComplete = { onSenseComplete(SarSense.FEELING) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SarIntroScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Willkommen zur Sinnesreise", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text(
            "Diese Übung hilft dir, deine Aufmerksamkeit von belastenden Gedanken wegzulenken und dich im Hier und Jetzt zu verankern. Wähle gleich den Sinn, mit dem du starten möchtest.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(32.dp))
        Button(onClick = onNext) {
            Text("Starten")
        }
    }
}

@Composable
private fun SarSenseChoiceScreen(
    onSenseSelected: (SarSense) -> Unit,
    onFinish: () -> Unit,
    availableSenses: Set<SarSense>,
    isFirstChoice: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val title = if (isFirstChoice) "Womit möchtest du beginnen?" else "Womit möchtest du weitermachen?"
        Text(title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        if (SarSense.SEEING in availableSenses) {
            SarSenseChoiceCard(icon = Icons.Default.Visibility, text = "Sehen", onClick = { onSenseSelected(SarSense.SEEING) })
            Spacer(Modifier.height(16.dp))
        }
        if (SarSense.HEARING in availableSenses) {
            SarSenseChoiceCard(icon = Icons.Default.Hearing, text = "Hören", onClick = { onSenseSelected(SarSense.HEARING) })
            Spacer(Modifier.height(16.dp))
        }
        if (SarSense.FEELING in availableSenses) {
            SarSenseChoiceCard(icon = Icons.Default.TouchApp, text = "Fühlen", onClick = { onSenseSelected(SarSense.FEELING) })
        }

        Spacer(Modifier.weight(1f))
        Button(onClick = onFinish) {
            Text("Übung beenden")
        }
    }
}

@Composable
private fun SarSenseChoiceCard(icon: ImageVector, text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
            Text(text, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun SarSenseExerciseScreen(
    title: String,
    question: String,
    count: Int = 5,
    onComplete: () -> Unit
) {
    var completedItems by rememberSaveable { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
        Text(question, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(count) {
                val isCompleted = it < completedItems
                SarTappableCircle(isCompleted) {
                    if (it == completedItems) completedItems++
                }
            }
        }

        Spacer(Modifier.weight(1f))

        if (completedItems == count) {
            Button(onClick = onComplete) {
                Text("Weiter")
            }
        }
    }
}

@Composable
private fun SarTappableCircle(isCompleted: Boolean, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(48.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable(onClick = onTap),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(Icons.Default.Check, contentDescription = "Completed", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
        }
    }
}