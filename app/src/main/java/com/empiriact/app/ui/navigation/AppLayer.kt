package com.empiriact.app.ui.navigation

/**
 * Definiert die Ebenen der App-Architektur für Navigation und Feature-Gruppierung.
 */
enum class AppLayer {
    INTRO,
    NAVIGATION,
    STATIC_CONTENT,
    MODULAR_CONTENT,
    SUPPORT
}

data class AppDestinationMeta(
    val route: Route,
    val layer: AppLayer
)

object AppStructure {
    val intro = listOf(
        AppDestinationMeta(Route.Onboarding, AppLayer.INTRO)
    )

    val staticPages = listOf(
        AppDestinationMeta(Route.Today, AppLayer.STATIC_CONTENT),
        AppDestinationMeta(Route.Checkin, AppLayer.STATIC_CONTENT),
        AppDestinationMeta(Route.Overview, AppLayer.STATIC_CONTENT),
        AppDestinationMeta(Route.Settings, AppLayer.STATIC_CONTENT)
    )

    val modularPages = listOf(
        AppDestinationMeta(Route.Learn, AppLayer.MODULAR_CONTENT),
        AppDestinationMeta(Route.Resources, AppLayer.MODULAR_CONTENT),
        AppDestinationMeta(Route.Evaluations, AppLayer.MODULAR_CONTENT)
    )
}
