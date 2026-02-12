package com.empiriact.app.ui.screens.insights

data class RankedActivity(val name: String, val averageRating: Double)

data class ActivityAnalysis(
    val topActivities: List<RankedActivity>,
    val bottomActivities: List<RankedActivity>,
    val problematicTriggers: List<String>
)
