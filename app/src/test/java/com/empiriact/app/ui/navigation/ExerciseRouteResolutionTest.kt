package com.empiriact.app.ui.navigation

import com.empiriact.app.ui.common.getExerciseRoute
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseRouteResolutionTest {

    @Test
    fun ruminationExerciseRoute_isResolvedConsistently() {
        assertEquals(Route.RuminationExercise.route, getExerciseRoute("rumination_exercise"))
        assertEquals(Route.RuminationExercise.route, Route.fromExerciseId("rumination_exercise"))
    }
}
