package com.empiriact.app.ui.screens.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ViewModelFactory
import com.empiriact.app.ui.navigation.Route

/**
 * Konvertiert die exerciseId zu einem lesbaren Ressourcennamen
 * MUSS identisch mit getExerciseName() in OverviewScreen.kt sein!
 */
private fun getExerciseName(exerciseId: String): String {
    return when (exerciseId) {
        "rumination_exercise" -> "Ruminations-Übung"
        "five_four_three_two_one" -> "5-4-3-2-1 Übung"
        "selective_attention" -> "Selektive Aufmerksamkeit"
        "attention_switching" -> "Aufmerksamkeitswechsel"
        "shared_attention" -> "Geteilte Aufmerksamkeit"
        else -> exerciseId
    }
}

@Composable
fun ExerciseRatingScreen(
    navController: NavController,
    exerciseId: String,
    from: String,
    factory: ViewModelFactory
) {
    val viewModel: ExerciseRatingViewModel = viewModel(factory = factory)
    var selectedRating by remember { mutableStateOf<Int?>(null) }

    // Ressourcennamen extrahieren und anzeigen
    val exerciseName = getExerciseName(exerciseId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = exerciseName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Wie hilfreich war diese Übung für dich?",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RatingButton(text = "--", isSelected = selectedRating == -2) { selectedRating = -2 }
            RatingButton(text = "-", isSelected = selectedRating == -1) { selectedRating = -1 }
            RatingButton(text = "0", isSelected = selectedRating == 0) { selectedRating = 0 }
            RatingButton(text = "+", isSelected = selectedRating == 1) { selectedRating = 1 }
            RatingButton(text = "++", isSelected = selectedRating == 2) { selectedRating = 2 }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                selectedRating?.let {
                    viewModel.saveRating(exerciseId, it)
                    val destination = if (from == "overview") Route.Overview.route else Route.Resources.route
                    navController.navigate(destination) {
                        popUpTo(destination) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            enabled = selectedRating != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Bewertung speichern")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                val destination = if (from == "overview") Route.Overview.route else Route.Resources.route
                navController.navigate(destination) {
                    popUpTo(destination) { inclusive = false }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Überspringen")
        }
    }
}

@Composable
private fun RatingButton(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = text)
    }
}
