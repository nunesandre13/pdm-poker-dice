package pt.isel.pdm.navigation.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.lobby.ui.LobbyCreationView
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens

fun NavGraphBuilder.createLobby(
    onNavigation: (NavigationEvent) -> Unit
) {
    composable<Screens.CreateLobby> {
        LobbyCreationView(
            onCreateLobby = {  },
            onBack = {onNavigation(NavigationEvent.Back)}
        )
    }
}
