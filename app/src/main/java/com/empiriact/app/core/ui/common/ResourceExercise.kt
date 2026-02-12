package com.empiriact.app.ui.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * This is the PARENT layout for all resource exercises. It provides a root `Box`
 * that fills the entire screen and reliably detects a long press gesture to finish the exercise.
 *
 * Child composables (the actual exercises) are placed inside this layout. The gesture detection
 * is applied to a transparent overlay Box, ensuring it catches all non-consumed touch events.
 *
 * @param onFinish The lambda to be executed on a long press.
 * @param modifier The modifier to be applied to the root Box.
 * @param content The specific UI of the resource method (the CHILD element).
 */
@Composable
fun ResourceExercise(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()
        // This transparent Box acts as an overlay. It covers the whole screen and
        // will catch any long-press gestures that are not consumed by the UI elements
        // underneath it (like Buttons).
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = { onFinish() })
                }
        )
    }
}
