package pt.isel.pdm.navigation

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.actions.onAction
import pt.isel.pdm.about.AboutScreen
import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.lobby.LobbyViewModel
import pt.isel.pdm.lobby.LobbyViewModelFactory
import pt.isel.pdm.lobby.lobbyUi.LobbyCreationView
import pt.isel.pdm.lobby.lobbyUi.LobbyScreen
import pt.isel.pdm.lobby.services.LobbyServiceMock
import pt.isel.pdm.profile.ProfileScreen
import pt.isel.pdm.ui.HandlingView
import pt.isel.pdm.user.UserScreen
import pt.isel.pdm.user.UserViewModel
import pt.isel.pdm.user.UserViewModelFactory
import pt.isel.pdm.user.services.UsersServiceMock



@Composable
fun RootApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.HOME_SCREEN.route,
        route = Screens.ROOT.route
    ) {

        composable(Screens.HOME_SCREEN.route) {
            val userVm by with(LocalContext.current as ComponentActivity) {
                viewModels<UserViewModel>(
                    factoryProducer = { UserViewModelFactory( UsersServiceMock()) }
                )
            }
            UserScreen(
                viewModel = userVm,
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



        composable(Screens.START_MATCH.route) { backStackEntry ->
            val rootGraphEntry = remember { navController.getBackStackEntry(Screens.ROOT.route) }
            val lobbyVm: LobbyViewModel = viewModel(
                viewModelStoreOwner = rootGraphEntry,
                factory = LobbyViewModelFactory(LobbyServiceMock(), UsersServiceMock())
            )

            LobbyScreen(
                viewModel = lobbyVm,
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
