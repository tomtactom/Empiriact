package com.empiriact.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.empiriact.app.EmpiriactApplication
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.ui.common.ViewModelFactory
import com.empiriact.app.ui.screens.checkin.CheckinScreen
import com.empiriact.app.ui.screens.checkin.QuestionnaireDetailScreen
import com.empiriact.app.ui.screens.learn.LearnAdvancedScreen
import com.empiriact.app.ui.screens.learn.LearnBasicsScreen
import com.empiriact.app.ui.screens.learn.LearnPracticeScreen
import com.empiriact.app.ui.screens.learn.LearnScreen
import com.empiriact.app.ui.screens.learn.LearnTestScreen
import com.empiriact.app.ui.screens.leo.LeoScreen
import androidx.compose.runtime.collectAsState
import com.empiriact.app.ui.screens.onboarding.OnboardingScreen
import com.empiriact.app.ui.screens.overview.OverviewScreen
import com.empiriact.app.ui.screens.rating.ExerciseRatingScreen
import com.empiriact.app.ui.screens.resources.ResourcesScreen
import com.empiriact.app.ui.screens.resources.methods.AttentionSwitchingExercise
import com.empiriact.app.ui.screens.resources.methods.DistractionSkillExercise
import com.empiriact.app.ui.screens.resources.methods.FiveFourThreeTwoOneExerciseScreen
import com.empiriact.app.ui.screens.resources.methods.SelectiveAttentionExercise
import com.empiriact.app.ui.screens.resources.methods.SharedAttentionExercise
import com.empiriact.app.ui.screens.resources.methods.SituationalAttentionRefocusingExercise
import com.empiriact.app.ui.screens.resources.methods.ValuesCompassExercise
import com.empiriact.app.ui.screens.settings.SettingsScreen
import com.empiriact.app.ui.screens.today.FlowChartExerciseScreen
import com.empiriact.app.ui.screens.today.TodayScreen

@Composable
fun EmpiriactNavGraph(factory: ViewModelFactory, settingsRepository: SettingsRepository) {
    val navController = rememberNavController()
    val onboardingCompleted by settingsRepository.onboardingCompleted.collectAsState(initial = false)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(Route.Today, "Heute", Icons.Default.Today),
        BottomNavItem(Route.Checkin, "Check-in", Icons.Default.CheckCircle),
        BottomNavItem(Route.Overview, "Übersicht", Icons.Default.Assessment),
        BottomNavItem(Route.Learn, "Lernen", Icons.Default.School),
        BottomNavItem(Route.Leo, "Leo", Icons.Default.Person),
        BottomNavItem(Route.Resources, "Ressourcen", Icons.Default.AutoStories),
    )

    val showBottomBar = AppStructure.intro.none { meta ->
        currentDestination?.hierarchy?.any { it.route == meta.route.route } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == item.route.route } == true
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (isSelected && item.route == Route.Checkin) {
                                    navController.popBackStack("checkin_list", inclusive = false)
                                } else if (isSelected) {
                                    navController.popBackStack(item.route.route, inclusive = false)
                                } else {
                                    navigateToTopLevel(navController, item.route)
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Entry.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            introGraph(navController, settingsRepository, onboardingCompleted)
            staticGraph(factory, navController)
            modularGraph(factory, navController)
        }
    }
}

private fun NavGraphBuilder.introGraph(
    navController: NavController,
    settingsRepository: SettingsRepository,
    onboardingCompleted: Boolean
) {
    composable(Route.Entry.route) {
        LaunchedEffect(onboardingCompleted) {
            navController.navigate(
                if (onboardingCompleted) Route.Today.route else Route.Onboarding.route
            ) {
                popUpTo(Route.Entry.route) { inclusive = true }
            }
        }
    }

    composable(Route.Onboarding.route) {
        OnboardingScreen {
            settingsRepository.completeOnboarding()
            navController.navigate(Route.Today.route) {
                popUpTo(Route.Onboarding.route) { inclusive = true }
            }
        }
    }
}

private fun NavGraphBuilder.staticGraph(factory: ViewModelFactory, navController: NavController) {
    composable(Route.Today.route) { TodayScreen(navController) }

    navigation(startDestination = "checkin_list", route = Route.Checkin.route) {
        composable("checkin_list") { CheckinScreen(factory, navController) }
        composable(
            route = Route.QuestionnaireDetail.route,
            arguments = listOf(navArgument("questionnaireId") { type = NavType.StringType })
        ) {
            val context = LocalContext.current
            val application = context.applicationContext as EmpiriactApplication
            val questionnaireId = it.arguments?.getString("questionnaireId")
            if (questionnaireId == "well_being") {
                val questionnaire = application.checkinRepository.getWellbeingQuestionnaire()
                QuestionnaireDetailScreen(questionnaire) { navController.popBackStack() }
            }
        }
    }

    composable(Route.Overview.route) { OverviewScreen(factory, navController) }
    composable(Route.Settings.route) { SettingsScreen() }
}

private fun NavGraphBuilder.modularGraph(factory: ViewModelFactory, navController: NavController) {
    composable(Route.Learn.route) { LearnScreen(navController) }
    composable(Route.LearnBasics.route) { LearnBasicsScreen(navController) }
    composable(Route.LearnAdvanced.route) { LearnAdvancedScreen(navController) }
    composable(Route.LearnPractice.route) { LearnPracticeScreen(navController) }
    composable(Route.LearnTest.route) { LearnTestScreen(navController) }
    composable(Route.Leo.route) { LeoScreen() }
    composable(Route.Resources.route) { ResourcesScreen(factory, navController) }
    composable(Route.ValuesCompassExercise.route) { ValuesCompassExercise(navController) }
    composable(Route.FlowChartExercise.route) { FlowChartExerciseScreen(navController) }

    composable(
        route = Route.SituationalAttentionRefocusingExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from")!!
        SituationalAttentionRefocusingExercise(navController, from)
    }
    composable(
        route = Route.FiveFourThreeTwoOneExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from")!!
        FiveFourThreeTwoOneExerciseScreen(navController, from)
    }
    composable(
        route = Route.SelectiveAttentionExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from")!!
        SelectiveAttentionExercise(navController, from)
    }
    composable(
        route = Route.AttentionSwitchingExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from")!!
        AttentionSwitchingExercise(navController, from)
    }
    composable(
        route = Route.SharedAttentionExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from")!!
        SharedAttentionExercise(navController, from)
    }
    composable(
        route = Route.DistractionSkillExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from")!!
        DistractionSkillExercise(navController, from)
    }
    composable(
        route = Route.ExerciseRating.route,
        arguments = listOf(
            navArgument("exerciseId") { type = NavType.StringType },
            navArgument("from") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val exerciseId = backStackEntry.arguments?.getString("exerciseId")
        val from = backStackEntry.arguments?.getString("from")
        if (exerciseId != null && from != null) {
            ExerciseRatingScreen(navController, exerciseId, from, factory)
        }
    }
}

private fun navigateToTopLevel(navController: NavController, route: Route) {
    navController.navigate(route.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private data class BottomNavItem(
    val route: Route,
    val label: String,
    val icon: ImageVector,
)
