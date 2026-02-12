package com.empiriact.app.ui.screens.resources.methods

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ValuesCompassExercise(navController: NavController) {
    Scaffold {
 padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Werte-Kompass", style = MaterialTheme.typography.headlineMedium)
            Text("Diese Übung hilft dir, deine Entscheidungen an deinen Werten auszurichten. (Hier wird die Übung implementiert)", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
