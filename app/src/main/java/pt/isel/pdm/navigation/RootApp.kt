package pt.isel.pdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.Screens
import pt.isel.pdm.actions.onAction
import pt.isel.pdm.about.AboutScreen
import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.lobby.LobbyCreationView
import pt.isel.pdm.lobby.LobbyListScreen
import pt.isel.pdm.login.LoginScreen
import pt.isel.pdm.profile.ProfileScreen

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
                onBack = { navController.popBackStack() })
        }

        composable(Screens.PROFILE.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screens.START_MATCH.route) {
            LobbyListScreen(
                lobbies = listOf("Lobby 1", "Lobby 2", "Lobby 3", "Lobby 4"),
                onJoinClick = { lobby -> navController.navigate(Screens.START_MATCH) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
