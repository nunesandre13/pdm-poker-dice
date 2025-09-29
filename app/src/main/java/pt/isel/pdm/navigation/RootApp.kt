package pt.isel.pdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.actions.onAction
import pt.isel.pdm.about.AboutScreen
import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.lobby.lobbyUi.LobbyCreationView
import pt.isel.pdm.lobby.lobbyUi.LobbyListView
import pt.isel.pdm.profile.ProfileScreen
import pt.isel.pdm.ui.HandlingView

@Composable
fun RootApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.HOME_SCREEN.route
    ) {

        composable(Screens.HOME_SCREEN.route) {
            TitleScreen(
                onAboutClick = { navController.navigate(Screens.ABOUT.route) },
                onProfileClick = { navController.navigate((Screens.PROFILE.route)) },
                onStartMatchClick = { navController.navigate((Screens.START_MATCH.route)) }
            )
        }

        composable(Screens.ABOUT.route) {
            AboutScreen(
                onDetails = { action -> onAction(action) },
                onSendEmail = { action -> onAction(action) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screens.PROFILE.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screens.START_MATCH.route) {
            LobbyListView(
                lobbies = emptyList(),
                onJoinClick = { navController.navigate(Screens.START_MATCH.route) },
                onBack = { navController.popBackStack() },
                onCreateLobby = { navController.navigate(Screens.CREATE_LOBBY.route) }
            )
        }

        composable(Screens.CREATE_LOBBY.route) {
            LobbyCreationView(
                onCreateLobby = { navController.navigate(Screens.AWAIING_GAME.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screens.AWAIING_GAME.route) {
            HandlingView(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
