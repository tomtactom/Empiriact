package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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

private data class GroundingStep(val count: Int, val prompt: String, val icon: ImageVector)

private val groundingSteps = listOf(
    GroundingStep(5, "Finde 5 Dinge, die du sehen kannst", Icons.Default.Visibility),
    GroundingStep(4, "Spüre 4 Dinge, die du fühlen kannst", Icons.Default.TouchApp),
    GroundingStep(3, "Höre 3 Dinge, die du hören kannst", Icons.Default.Hearing),
    GroundingStep(2, "Rieche 2 Dinge, die du riechen kannst", Icons.Default.Sensors),
    GroundingStep(1, "Schmecke 1 Ding, das du schmecken kannst", Icons.Default.Sms)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiveFourThreeTwoOneExerciseScreen(
    navController: NavController,
    from: String
) {
    var currentStepIndex by remember { mutableIntStateOf(0) }
    var stepProgress by remember { mutableIntStateOf(0) }
    val currentStep = groundingSteps[currentStepIndex]

    val progress by animateFloatAsState((currentStepIndex + 1) / groundingSteps.size.toFloat(), label = "Progress")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("5-4-3-2-1 Übung") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = ContentDescriptions.CLOSE_DIALOG)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier.weight(1f).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(currentStep.icon, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(24.dp))
                Text(currentStep.prompt, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
                Spacer(Modifier.height(32.dp))
                TappableCircles(currentStep.count, stepProgress) { newProgress ->
                    stepProgress = newProgress
                }
            }
            Button(
                onClick = {
                    if (stepProgress == currentStep.count) {
                        if (currentStepIndex < groundingSteps.lastIndex) {
                            currentStepIndex++
                            stepProgress = 0
                        } else {
                            navController.navigate(Route.ExerciseRating.createRoute("five_four_three_two_one", from))
                        }
                    }
                },
                enabled = stepProgress == currentStep.count,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Text(if (currentStepIndex < groundingSteps.lastIndex) "Nächster Schritt" else "Übung abschließen")
            }
        }
    }
}

@Composable
private fun TappableCircles(count: Int, currentProgress: Int, onProgressChange: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(count) {
            val isCompleted = it < currentProgress
            TappableCircle(isCompleted) {
                if (it == currentProgress) {
                    onProgressChange(currentProgress + 1)
                }
            }
        }
    }
}

@Composable
private fun TappableCircle(isCompleted: Boolean, onTap: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(40.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable(onClick = onTap),
        contentAlignment = Alignment.Center
    ) {
        if (isCompleted) {
            Icon(Icons.Default.Check, contentDescription = "Abgeschlossen", tint = MaterialTheme.colorScheme.primary)
        }
    }
}