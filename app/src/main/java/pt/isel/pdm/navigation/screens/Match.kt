package pt.isel.pdm.navigation.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens

fun NavGraphBuilder.match(
    onNavigation: (NavigationEvent) -> Unit
) {
    composable<Screens.Match> { backStackEntry ->
        val matchId = backStackEntry.toRoute<Screens.Match>().matchId

    }
}
