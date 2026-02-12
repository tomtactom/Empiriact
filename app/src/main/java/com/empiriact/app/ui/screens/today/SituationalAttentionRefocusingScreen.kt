
package com.empiriact.app.ui.screens.today

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.empiriact.app.ui.common.ContentDescriptions
import com.empiriact.app.ui.common.ResourceExercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SituationalAttentionRefocusingScreen(navController: NavController) {
    ResourceExercise(onFinish = { navController.popBackStack() }) {
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
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Situational Attention Refocusing Screen - Coming Soon!")
            }
        }
    }
}
