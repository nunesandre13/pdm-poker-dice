package pt.isel.pdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.actions.onAction
import pt.isel.pdm.about.AboutScreen
import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.lobby.LobbyViewModel
import pt.isel.pdm.lobby.lobbyUi.LobbyCreationView
import pt.isel.pdm.lobby.lobbyUi.LobbyListView
import pt.isel.pdm.lobby.lobbyUi.LobbyScreen
import pt.isel.pdm.lobby.services.LobbyServiceMock
import pt.isel.pdm.profile.ProfileScreen
import pt.isel.pdm.ui.HandlingView
import pt.isel.pdm.user.UserScreen
import pt.isel.pdm.user.UserViewModel
import pt.isel.pdm.user.services.UsersServiceMock

@Composable
fun RootApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.HOME_SCREEN.route
    ) {

        val viewModelUser= UserViewModel(UsersServiceMock())
        composable(Screens.HOME_SCREEN.route) {
            UserScreen(
                viewModel = viewModelUser,
                onTitleScreen = { navController.navigate(Screens.TITLE_SCREEN.route) }
            )
        }

        composable(Screens.TITLE_SCREEN.route) {
            TitleScreen(
                onAboutClick = { navController.navigate(Screens.ABOUT.route) },
                onProfileClick = { navController.navigate(Screens.PROFILE.route) },
                onStartMatchClick = { navController.navigate(Screens.START_MATCH.route) }
            )
        }

        val user = UsersServiceMock().getCurrentUser()
        composable(Screens.PROFILE.route) {
            ProfileScreen(
                user = user!!,
                onBack = { navController.popBackStack() }

            )
        }

        composable(Screens.ABOUT.route) {
            AboutScreen(
                onDetails = { action -> onAction(action) },
                onSendEmail = { action -> onAction(action) },
                onBack = { navController.popBackStack() }
            )
        }


        val viewModel= LobbyViewModel(LobbyServiceMock())
        composable(Screens.START_MATCH.route) {
            LobbyScreen(
                viewModel = viewModel,
                goBack = { navController.popBackStack() }
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
