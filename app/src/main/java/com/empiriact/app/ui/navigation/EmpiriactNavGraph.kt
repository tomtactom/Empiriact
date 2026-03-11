package com.empiriact.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoStories
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
import com.empiriact.app.BuildConfig
import com.empiriact.app.data.SettingsRepository
import com.empiriact.app.ui.common.ViewModelFactory
import androidx.compose.runtime.collectAsState
import com.empiriact.app.ui.screens.onboarding.OnboardingScreen
import com.empiriact.app.ui.screens.overview.OverviewScreen
import com.empiriact.app.ui.screens.rating.ExerciseRatingScreen
import com.empiriact.app.ui.screens.resources.ResourcesScreen
import com.empiriact.app.ui.screens.resources.methods.AttentionSwitchingExercise
import com.empiriact.app.ui.screens.resources.methods.DistractionSkillExercise
import com.empiriact.app.ui.screens.resources.methods.FiveFourThreeTwoOneExerciseScreen
import com.empiriact.app.ui.screens.resources.methods.PsychoeducationActivationFoundationsScreen
import com.empiriact.app.ui.screens.resources.methods.SelectiveAttentionExercise
import com.empiriact.app.ui.screens.resources.methods.SharedAttentionExercise
import com.empiriact.app.ui.screens.resources.methods.SituationalAttentionRefocusingExercise
import com.empiriact.app.ui.screens.resources.methods.ValuesCompassExercise
import com.empiriact.app.ui.screens.skills.RuminationExerciseScreen
import com.empiriact.app.ui.screens.settings.SettingsScreen
import com.empiriact.app.ui.screens.today.FlowChartExerciseScreen
import com.empiriact.app.ui.screens.today.TodayScreen
import com.empiriact.app.ui.screens.modules.GruebelnModuleScreen
import com.empiriact.app.ui.screens.modules.DenkstileModuleScreen
import com.empiriact.app.ui.screens.modules.RNDModuleScreen
import com.empiriact.app.ui.screens.modules.RuminationModuleScreen
import com.empiriact.app.ui.screens.modules.HabitModuleScreen
import com.empiriact.app.ui.screens.modules.ContentModuleScreen
import com.empiriact.app.ui.screens.modules.ThreadModuleScreen
import com.empiriact.app.ui.screens.modules.ThumbRuleModuleScreen
import com.empiriact.app.ui.screens.resources.PsychoeducationScreen
import com.empiriact.app.ui.screens.resources.InteractiveExercisesScreen
import com.empiriact.app.ui.screens.resources.ResourceBrowserScreen
import com.empiriact.app.ui.screens.resources.LearningPathScreen

@Composable
fun EmpiriactNavGraph(factory: ViewModelFactory, settingsRepository: SettingsRepository) {
    val navController = rememberNavController()
    val shouldShowOnboarding by settingsRepository
        .shouldShowOnboarding(BuildConfig.VERSION_CODE)
        .collectAsState(initial = true)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(Route.Today, "Heute", Icons.Default.Today),
        BottomNavItem(Route.Overview, "Übersicht", Icons.Default.Assessment),
        BottomNavItem(Route.PsychoeducationScreen, "Module", Icons.Default.School),
        BottomNavItem(Route.Resources, "Inhalte", Icons.Default.AutoStories),
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
                                if (isSelected) {
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
            introGraph(navController, settingsRepository, shouldShowOnboarding)
            staticGraph(factory, navController)
            modularGraph(factory, navController)
        }
    }
}

private fun NavGraphBuilder.introGraph(
    navController: NavController,
    settingsRepository: SettingsRepository,
    shouldShowOnboarding: Boolean
) {
    composable(Route.Entry.route) {
        LaunchedEffect(shouldShowOnboarding) {
            navController.navigate(
                if (shouldShowOnboarding) Route.Onboarding.route else Route.Today.route
            ) {
                popUpTo(Route.Entry.route) { inclusive = true }
            }
        }
    }

    composable(Route.Onboarding.route) {
        OnboardingScreen {
            settingsRepository.completeOnboarding(BuildConfig.VERSION_CODE)
            navController.navigate(Route.Today.route) {
                popUpTo(Route.Onboarding.route) { inclusive = true }
            }
        }
    }
}

private fun NavGraphBuilder.staticGraph(factory: ViewModelFactory, navController: NavController) {
    composable(Route.Today.route) { TodayScreen(navController) }

    composable(Route.Overview.route) { OverviewScreen(factory, navController) }
    composable(Route.Settings.route) { SettingsScreen() }
}

private fun NavGraphBuilder.modularGraph(factory: ViewModelFactory, navController: NavController) {
    addModuleRoutes(navController)
    addExerciseRoutes(factory, navController)
}

private fun NavGraphBuilder.addModuleRoutes(navController: NavController) {
    composable(Route.PsychoeducationScreen.route) { PsychoeducationScreen(onBack = { navController.popBackStack() }) }
    composable(Route.InteractiveExercisesScreen.route) { InteractiveExercisesScreen(onBack = { navController.popBackStack() }) }
    composable(Route.ResourceBrowserScreen.route) { ResourceBrowserScreen(onBack = { navController.popBackStack() }) }
    composable(Route.LearningPathScreen.route) { LearningPathScreen(onBack = { navController.popBackStack() }) }
    composable(Route.GruebelnModule.route) { GruebelnModuleScreen(onBack = { navController.popBackStack() }) }
    composable(Route.DenkstileModule.route) { DenkstileModuleScreen(onBack = { navController.popBackStack() }) }
    composable(Route.RNDModule.route) { RNDModuleScreen(onBack = { navController.popBackStack() }) }
    composable(Route.RuminationModule.route) { RuminationModuleScreen(onBack = { navController.popBackStack() }) }
    composable(Route.HabitModule.route) { HabitModuleScreen(onBack = { navController.popBackStack() }) }
    composable(Route.ContentModule.route) { ContentModuleScreen(onBack = { navController.popBackStack() }) }
    composable(Route.ThreadModule.route) { ThreadModuleScreen(onBack = { navController.popBackStack() }) }
    composable(Route.ThumbRuleModule.route) { ThumbRuleModuleScreen(onBack = { navController.popBackStack() }) }
    composable(Route.Resources.route) { ResourcesScreen(navController) }
}

private fun NavGraphBuilder.addExerciseRoutes(factory: ViewModelFactory, navController: NavController) {
    composable(Route.RuminationExercise.route) {
        RuminationExerciseScreen(onFinish = { navController.popBackStack() })
    }
    composable(Route.ValuesCompassExercise.route) { ValuesCompassExercise(navController) }
    composable(Route.FlowChartExercise.route) { FlowChartExerciseScreen(navController) }
    composable(Route.PsychoeducationActivationFoundations.route) {
        PsychoeducationActivationFoundationsScreen(navController)
    }

    composable(
        route = Route.SituationalAttentionRefocusingExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from") ?: "resources"
        SituationalAttentionRefocusingExercise(navController, from)
    }
    composable(
        route = Route.FiveFourThreeTwoOneExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from") ?: "resources"
        FiveFourThreeTwoOneExerciseScreen(navController, from)
    }
    composable(
        route = Route.SelectiveAttentionExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from") ?: "resources"
        SelectiveAttentionExercise(navController, from)
    }
    composable(
        route = Route.AttentionSwitchingExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from") ?: "resources"
        AttentionSwitchingExercise(navController, from)
    }
    composable(
        route = Route.SharedAttentionExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from") ?: "resources"
        SharedAttentionExercise(navController, from)
    }
    composable(
        route = Route.DistractionSkillExercise.route,
        arguments = listOf(navArgument("from") { type = NavType.StringType })
    ) { backStackEntry ->
        val from = backStackEntry.arguments?.getString("from") ?: "resources"
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
        if (exerciseId == null || from == null) return@composable

        ExerciseRatingScreen(navController, exerciseId, from, factory)
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
