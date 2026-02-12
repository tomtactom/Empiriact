package com.empiriact.app.ui.common

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * A custom modifier that detects a long press gesture and triggers an action.
 * This provides a consistent and reusable way to finish exercises or close screens.
 *
 * @param onFinish The lambda to be executed on a long press.
 */
fun Modifier.longPressToFinish(onFinish: () -> Unit): Modifier = this.then(
    pointerInput(Unit) {
        detectTapGestures(onLongPress = { onFinish() })
    }
)
