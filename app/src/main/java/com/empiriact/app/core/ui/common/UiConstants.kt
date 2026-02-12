package com.empiriact.app.ui.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object UiConstants {
    // Paddings and Spacings
    val PADDING_SMALL = 4.dp
    val PADDING_MEDIUM = 8.dp
    val PADDING_LARGE = 16.dp

    // Arrangements
    val ARRANGEMENT_SPACING_MEDIUM = 12.dp
    val ARRANGEMENT_SPACING_LARGE = 16.dp

    // Elevations
    val CARD_ELEVATION = 2.dp

    // Component Sizes
    val RATING_BAR_ITEM_SIZE = 20.dp

    // Corner Shapes
    val RATING_BAR_CORNER_SHAPE = 4.dp
    
    // Colors
    val RATING_FILLED_COLOR = Color(0xFFFFB800)
    val RATING_UNFILLED_COLOR = Color.LightGray
    val POSITIVE_CHANGE_COLOR = Color(0xFF2E7D32)
    val NEGATIVE_CHANGE_COLOR = Color(0xFFC62828)

    // Row Weights
    const val PROTOCOL_ROW_TIME_WEIGHT = 0.2f
    const val PROTOCOL_ROW_ACTIVITY_WEIGHT = 0.6f
    const val PROTOCOL_ROW_VALENCE_WEIGHT = 0.2f
}
