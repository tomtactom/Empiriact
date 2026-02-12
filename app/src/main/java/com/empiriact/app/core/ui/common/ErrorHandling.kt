package com.empiriact.app.ui.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Error State Holder - zentrale Verwaltung von Fehlern in der App
 * Ermöglicht konsistentes Error Handling über alle ViewModels
 */
class ErrorState {
    private var _errorMessage by mutableStateOf("")
    val errorMessage: String
        get() = _errorMessage

    fun showError(message: String) {
        _errorMessage = message
    }

    fun clearError() {
        _errorMessage = ""
    }

    fun clearErrorAfterDelay(scope: CoroutineScope, delayMs: Long = 3000) {
        scope.launch {
            kotlinx.coroutines.delay(delayMs)
            clearError()
        }
    }
}

/**
 * Error Handler Extension - vereinfacht Try-Catch Blöcke
 * Beispiel:
 * viewModel.tryCatch({
 *     // Code
 * }, "Fehler beim Speichern")
 */
inline fun <T> tryCatch(
    block: () -> T,
    errorMessage: String = "Ein Fehler ist aufgetreten",
    onError: (String) -> Unit = {}
): T? {
    return try {
        block()
    } catch (e: Exception) {
        val message = errorMessage + (if (e.message != null) ": ${e.message}" else "")
        onError(message)
        null
    }
}

/**
 * Safe Coroutine Launcher - führt Code sicher in einer Coroutine aus
 */
fun CoroutineScope.safeLaunch(
    errorHandler: (String) -> Unit = {},
    block: suspend () -> Unit
) {
    launch {
        try {
            block()
        } catch (e: Exception) {
            errorHandler(e.message ?: "Ein unbekannter Fehler ist aufgetreten")
        }
    }
}
