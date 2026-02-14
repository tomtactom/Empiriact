package com.empiriact.app.ui.navigation

sealed class Route(val route: String) {
    object Entry : Route("entry")
    object Onboarding : Route("onboarding")

    object Today : Route("today")
    object Overview : Route("overview")
    object Profile : Route("profile")
    object Values : Route("values")
    object ActivityPlanner : Route("activity_planner/{valueName}") {
        fun createRoute(valueName: String) = "activity_planner/$valueName"
    }
    object Reflect : Route("reflect")
    object Resources : Route("resources")
    object Skills : Route("skills")
    object RuminationExercise : Route("rumination_exercise")
    object Evaluations : Route("evaluations")
    object ChooseEvaluation : Route("choose_evaluation")
    object TakeEvaluation : Route("take_evaluation/{type}") {
        fun createRoute(type: String) = "take_evaluation/$type"
    }
    object Settings : Route("settings")

    object FiveFourThreeTwoOneExercise : Route("five_four_three_two_one_exercise/{from}") {
        fun createRoute(from: String) = "five_four_three_two_one_exercise/$from"
    }

    object ExerciseRating : Route("exercise_rating/{exerciseId}/{from}") {
        fun createRoute(exerciseId: String, from: String) = "exercise_rating/$exerciseId/$from"
    }

    object ValuesCompassExercise : Route("values_compass_exercise")
    object FlowChartExercise : Route("flow_chart_exercise")
    object SituationalAttentionRefocusingExercise : Route("situational_attention_refocusing_exercise/{from}") {
        fun createRoute(from: String) = "situational_attention_refocusing_exercise/$from"
    }

    object SelectiveAttentionExercise : Route("selective_attention_exercise/{from}") {
        fun createRoute(from: String) = "selective_attention_exercise/$from"
    }

    object AttentionSwitchingExercise : Route("attention_switching_exercise/{from}") {
        fun createRoute(from: String) = "attention_switching_exercise/$from"
    }

    object SharedAttentionExercise : Route("shared_attention_exercise/{from}") {
        fun createRoute(from: String) = "shared_attention_exercise/$from"
    }

    object DistractionSkillExercise : Route("distraction_skill_exercise/{from}") {
        fun createRoute(from: String) = "distraction_skill_exercise/$from"
    }

    object PsychoeducationActivationFoundations : Route("psychoeducation_activation_foundations")

    companion object {
        fun fromExerciseId(exerciseId: String, from: String = "overview"): String? {
            return when (exerciseId) {
                "rumination_exercise" -> RuminationExercise.route
                "five_four_three_two_one", "five_four_three_two_one_exercise" -> FiveFourThreeTwoOneExercise.createRoute(from)
                "flow_chart", "flow_chart_exercise" -> FlowChartExercise.route
                "values_compass", "values_compass_exercise" -> ValuesCompassExercise.route
                "situational_attention_refocusing", "situational_attention_refocusing_exercise" -> SituationalAttentionRefocusingExercise.createRoute(from)
                "selective_attention", "selective_attention_exercise" -> SelectiveAttentionExercise.createRoute(from)
                "attention_switching", "attention_switching_exercise" -> AttentionSwitchingExercise.createRoute(from)
                "shared_attention", "shared_attention_exercise" -> SharedAttentionExercise.createRoute(from)
                "distraction_skill", "distraction_skill_exercise" -> DistractionSkillExercise.createRoute(from)
                else -> null
            }
        }
    }
}
