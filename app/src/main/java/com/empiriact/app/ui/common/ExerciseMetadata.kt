package com.empiriact.app.ui.common

import com.empiriact.app.ui.navigation.Route

fun getExerciseDisplayName(exerciseId: String): String {
    return when (exerciseId) {
        "rumination_exercise" -> "Ruminations-Übung"
        "five_four_three_two_one", "five_four_three_two_one_exercise" -> "5-4-3-2-1 Übung"
        "selective_attention", "selective_attention_exercise" -> "Selektive Aufmerksamkeit"
        "attention_switching", "attention_switching_exercise" -> "Aufmerksamkeitswechsel"
        "shared_attention", "shared_attention_exercise" -> "Geteilte Aufmerksamkeit"
        "distraction_skill", "distraction_skill_exercise" -> "Ablenkung als situativer Skill"
        "situational_attention_refocusing", "situational_attention_refocusing_exercise" -> "Refokussierung nach außen (Situational Attention Refocusing)"
        "flow_chart", "flow_chart_exercise" -> "Flow-Landkarte"
        "values_compass", "values_compass_exercise" -> "Wertekompass"
        else -> exerciseId.replace('_', ' ')
    }
}

fun getExerciseRoute(exerciseId: String, from: String = "overview"): String? {
    return when (exerciseId) {
        "rumination_exercise" -> Route.RuminationExercise.route
        "five_four_three_two_one", "five_four_three_two_one_exercise" -> Route.FiveFourThreeTwoOneExercise.createRoute(from)
        "flow_chart", "flow_chart_exercise" -> Route.FlowChartExercise.route
        "values_compass", "values_compass_exercise" -> Route.ValuesCompassExercise.route
        "situational_attention_refocusing", "situational_attention_refocusing_exercise" -> Route.SituationalAttentionRefocusingExercise.createRoute(from)
        "selective_attention", "selective_attention_exercise" -> Route.SelectiveAttentionExercise.createRoute(from)
        "attention_switching", "attention_switching_exercise" -> Route.AttentionSwitchingExercise.createRoute(from)
        "shared_attention", "shared_attention_exercise" -> Route.SharedAttentionExercise.createRoute(from)
        "distraction_skill", "distraction_skill_exercise" -> Route.DistractionSkillExercise.createRoute(from)
        else -> null
    }
}
