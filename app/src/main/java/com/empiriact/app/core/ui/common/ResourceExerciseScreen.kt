package com.empiriact.app.ui.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * A generic screen container for all resource methods (exercises).
 * This is the PARENT element. It provides a consistent structure and behavior,
 * such as the top app bar and a long-press-to-finish gesture that works reliably across the entire screen.
 *
 * @param title The title of the resource method, displayed in the top app bar.
 * @param onFinish The action to perform when the exercise is finished (e.g., by pressing the close button or long-pressing).
 * @param content The specific UI of the resource method (the CHILD element).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceExerciseScreen(
    title: String,
    onFinish: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.pointerInput(Unit) { // Apply gesture detection to the whole screen
            detectTapGestures(onLongPress = { onFinish() })
        },
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onFinish) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Übung schließen"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}
