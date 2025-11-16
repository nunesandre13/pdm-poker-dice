package pt.isel.pdm.navigation.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.about.AboutScreen
import pt.isel.pdm.actions.onAction
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens

fun NavGraphBuilder.about(
    onNavigation: (NavigationEvent) -> Unit,
) {
    composable<Screens.About> {
        AboutScreen(
            onDetails = { action -> onAction(action) },
            onSendEmail = { action -> onAction(action) },
            onBack = {onNavigation(NavigationEvent.Back)}
        )
    }
}
