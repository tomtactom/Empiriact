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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import kotlin.math.roundToInt

private enum class SarSense { SEEING, HEARING, FEELING }
private sealed class SarScreenState {
    object PRE_CHECKIN : SarScreenState()
    object INTRO : SarScreenState()
    object SENSE_CHOICE : SarScreenState()
    data class EXERCISE(val sense: SarSense) : SarScreenState()
    object POST_CHECKIN : SarScreenState()
    object MICRO_COMMITMENT : SarScreenState()

    companion object {
        val Saver: Saver<SarScreenState, String> = Saver(
            save = { state ->
                when (state) {
                    is EXERCISE -> "EXERCISE:${state.sense.name}"
                    PRE_CHECKIN -> "PRE_CHECKIN"
                    INTRO -> "INTRO"
                    SENSE_CHOICE -> "SENSE_CHOICE"
                    POST_CHECKIN -> "POST_CHECKIN"
                    MICRO_COMMITMENT -> "MICRO_COMMITMENT"
                }
            },
            restore = { saved ->
                when {
                    saved.startsWith("EXERCISE:") -> {
                        val senseName = saved.removePrefix("EXERCISE:")
                        val sense = SarSense.entries.firstOrNull { it.name == senseName }
                        if (sense != null) EXERCISE(sense) else INTRO
                    }
                    saved == "PRE_CHECKIN" -> PRE_CHECKIN
                    saved == "INTRO" -> INTRO
                    saved == "SENSE_CHOICE" -> SENSE_CHOICE
                    saved == "POST_CHECKIN" -> POST_CHECKIN
                    saved == "MICRO_COMMITMENT" -> MICRO_COMMITMENT
                    else -> INTRO
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
    var screenState: SarScreenState by rememberSaveable(stateSaver = SarScreenState.Saver) { mutableStateOf(SarScreenState.PRE_CHECKIN) }
    var completedSenses by rememberSaveable { mutableStateOf(emptySet<SarSense>()) }
    var preBurden by rememberSaveable { mutableStateOf(6f) }
    var preAgency by rememberSaveable { mutableStateOf(4f) }
    var postBurden by rememberSaveable { mutableStateOf(5f) }
    var postAgency by rememberSaveable { mutableStateOf(5f) }
    var insight by rememberSaveable { mutableStateOf("") }
    var ifThenPlan by rememberSaveable { mutableStateOf("") }
    var nextStep by rememberSaveable { mutableStateOf("") }

    fun onSenseComplete(completedSense: SarSense) {
        val newCompletedSenses = completedSenses + completedSense
        completedSenses = newCompletedSenses
        val remainingSenses = SarSense.values().toSet() - newCompletedSenses

        if (remainingSenses.isEmpty()) {
            screenState = SarScreenState.POST_CHECKIN
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
                is SarScreenState.PRE_CHECKIN -> PrePostCheckinScreen(
                    title = "Kurzer Start-Check-in",
                    subtitle = "Du bist nicht falsch, wenn du gerade festhängst. Wir schauen nur kurz auf deinen aktuellen Zustand.",
                    burden = preBurden,
                    agency = preAgency,
                    primaryButtonLabel = "Akutmodus starten",
                    onBurdenChange = { preBurden = it },
                    onAgencyChange = { preAgency = it },
                    onContinue = { screenState = SarScreenState.INTRO }
                )
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
                is SarScreenState.POST_CHECKIN -> PrePostCheckinScreen(
                    title = "Nach der Übung",
                    subtitle = "Schon kleine Veränderungen zählen. Du trainierst gerade flexible Aufmerksamkeit.",
                    burden = postBurden,
                    agency = postAgency,
                    primaryButtonLabel = "Weiter zum Transfer",
                    onBurdenChange = { postBurden = it },
                    onAgencyChange = { postAgency = it },
                    onContinue = { screenState = SarScreenState.MICRO_COMMITMENT }
                )
                is SarScreenState.MICRO_COMMITMENT -> MicroCommitmentScreen(
                    insight = insight,
                    ifThenPlan = ifThenPlan,
                    nextStep = nextStep,
                    onInsightChange = { insight = it },
                    onIfThenChange = { ifThenPlan = it },
                    onNextStepChange = { nextStep = it },
                    onFinish = {
                        navController.navigate(Route.ExerciseRating.createRoute("situational_attention_refocusing", from))
                    }
                )
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
            "Diese 90-Sekunden-Übung hilft dir, Grübelschleifen zu unterbrechen und wieder handlungsfähig zu werden. Wähle gleich den Sinn, mit dem du starten möchtest.",
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
private fun PrePostCheckinScreen(
    title: String,
    subtitle: String,
    burden: Float,
    agency: Float,
    primaryButtonLabel: String,
    onBurdenChange: (Float) -> Unit,
    onAgencyChange: (Float) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))

        RatingSlider(
            label = "Belastung (0-10)",
            value = burden,
            onValueChange = onBurdenChange
        )
        Spacer(Modifier.height(16.dp))
        RatingSlider(
            label = "Handlungsfähigkeit (0-10)",
            value = agency,
            onValueChange = onAgencyChange
        )

        Spacer(Modifier.height(24.dp))
        Button(onClick = onContinue) {
            Text(primaryButtonLabel)
        }
    }
}

@Composable
private fun RatingSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("$label: ${value.roundToInt()}")
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..10f,
            steps = 9
        )
    }
}

@Composable
private fun MicroCommitmentScreen(
    insight: String,
    ifThenPlan: String,
    nextStep: String,
    onInsightChange: (String) -> Unit,
    onIfThenChange: (String) -> Unit,
    onNextStepChange: (String) -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Transfer in den Alltag", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Stark, dass du trotz Belastung gehandelt hast. Abschluss mit drei Mini-Schritten:",
            style = MaterialTheme.typography.bodyMedium
        )

        Text("1) 1 Satz Erkenntnis")
        Text("2) Wenn-Dann-Plan")
        Text("3) Nächster Schritt < 2 Minuten")

        OutlinedTextField(
            value = insight,
            onValueChange = onInsightChange,
            label = { Text("1 Satz Erkenntnis") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = ifThenPlan,
            onValueChange = onIfThenChange,
            label = { Text("Wenn ich Grübeln bemerke, dann ...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = nextStep,
            onValueChange = onNextStepChange,
            label = { Text("Nächster Schritt in unter 2 Minuten") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.weight(1f))
        Button(
            onClick = onFinish,
            enabled = insight.isNotBlank() && ifThenPlan.isNotBlank() && nextStep.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Akutmodus abschließen")
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
