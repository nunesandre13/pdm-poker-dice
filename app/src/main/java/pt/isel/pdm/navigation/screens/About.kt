package pt.isel.pdm.navigation.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.about.AboutScreen
import pt.isel.pdm.actions.onAction
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens
import pt.isel.pdm.navigation.composableWithOrientation

fun NavGraphBuilder.about(
    onNavigation: (NavigationEvent) -> Unit,
) {
    composableWithOrientation<Screens.About> {
        AboutScreen(
            onDetails = { action -> onAction(action) },
            onSendEmail = { action -> onAction(action) },
            onBack = {onNavigation(NavigationEvent.Back)}
        )
    }
}
