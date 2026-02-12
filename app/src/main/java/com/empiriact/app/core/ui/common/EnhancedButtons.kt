package com.empiriact.app.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.empiriact.app.ui.theme.Dimensions

/**
 * Enhanced Button mit Loading und Success State
 */
@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isSuccess: Boolean = false,
    isError: Boolean = false,
    enabled: Boolean = true,
    successText: String = "Erfolgreich!",
    errorText: String = "Fehler"
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .height(Dimensions.buttonHeight)
            .animateContentSize()
    ) {
        when {
            isLoading -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(text)
                }
            }
            isSuccess -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = ContentDescriptions.SUCCESS,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(successText)
                }
            }
            isError -> {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = ContentDescriptions.ERROR_OCCURRED,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(errorText)
                }
            }
            else -> Text(text)
        }
    }
}

/**
 * Tonal Button mit Icon
 */
@Composable
fun IconTonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(Dimensions.buttonHeightSmall)
    ) {
        if (icon != null) {
            Icon(
                icon,
                contentDescription = text,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = Dimensions.spacingSmall)
            )
        }
        Text(text)
    }
}

/**
 * Rating Button
 */
@Composable
fun RatingButton(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacingSmall)
    ) {
        repeat(maxRating) { index ->
            val ratingValue = index + 1
            Button(
                onClick = { onRatingChange(ratingValue) },
                modifier = Modifier
                    .width(40.dp)
                    .height(Dimensions.buttonHeightSmall),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (ratingValue <= rating) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Text(ratingValue.toString(), fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * Confirm Button
 */
@Composable
fun ConfirmButton(
    onClick: () -> Unit,
    onConfirmingChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Löschen",
    isConfirming: Boolean = false,
    confirmingText: String = "Nochmal tippen zum Bestätigen"
) {
    Button(
        onClick = {
            if (isConfirming) {
                onClick()
                onConfirmingChange(false)
            } else {
                onConfirmingChange(true)
            }
        },
        modifier = modifier.height(Dimensions.buttonHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isConfirming) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    ) {
        Text(if (isConfirming) confirmingText else text)
    }
}
