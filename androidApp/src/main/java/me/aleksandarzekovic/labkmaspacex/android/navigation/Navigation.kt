package me.aleksandarzekovic.labkmaspacex.android.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import me.aleksandarzekovic.labkmaspacex.android.ui.characters.ShipsScreen
import me.aleksandarzekovic.labkmaspacex.android.ui.shipdetails.ShipDetailsScreen

internal const val idArg = "id"

@Composable
internal fun SpaceXNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destination.Ships.route,
        enterTransition = { fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
        exitTransition = { fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start) },
        popEnterTransition = { fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End) },
        popExitTransition = { fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End) },
    ) {
        shipsScreen(
            onShipClicked = {
                navController.navigate(Destination.ShipDetails.route + "/${it}")
            }
        )

        shipDetailsScreen(
            popBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.shipsScreen(onShipClicked: (String) -> Unit) {
    composable(
        route = Destination.Ships.route
    ) { ShipsScreen(onShipClicked) }
}

fun NavGraphBuilder.shipDetailsScreen(popBack: () -> Unit) {
    composable(
        route = Destination.ShipDetails.route + "/{$idArg}",
        arguments = listOf(
            navArgument(idArg) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        ShipDetailsScreen(
            shipId = backStackEntry.arguments?.getString(idArg) as String,
            popBack = popBack
        )
    }
}
