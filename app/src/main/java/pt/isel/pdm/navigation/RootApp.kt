package pt.isel.pdm.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.actions.onAction
import pt.isel.pdm.about.AboutScreen
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.lobby.viewmodel.LobbyViewModel
import pt.isel.pdm.lobby.ui.LobbyCreationView
import pt.isel.pdm.lobby.ui.LobbyScreen
import pt.isel.pdm.profile.ui.ProfileScreen
import pt.isel.pdm.profile.viewmodel.ProfileViewModel
import pt.isel.pdm.ui.HandlingView
import pt.isel.pdm.user.ui.UserScreen
import pt.isel.pdm.user.viewmodel.UserViewModel



@Composable
fun RootApp(appConfiguration: DependenciesContainer) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.HOME_SCREEN.route
    ) {

        composable(Screens.HOME_SCREEN.route) { backStackEntry ->
            val userVm: UserViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = UserViewModel.factory(appConfiguration.userServices)
            )
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

        composable(Screens.ABOUT.route) {
            AboutScreen(
                onDetails = { action -> onAction(action) },
                onSendEmail = { action -> onAction(action) },
                onBack = { navController.popBackStack() }
            )
        }


        composable(Screens.START_MATCH.route) { backStackEntry ->
            val lobbyVm: LobbyViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = LobbyViewModel.getFactory(appConfiguration.lobbyServices, appConfiguration.userServices)
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

        composable(Screens.PROFILE.route) { backStackEntry ->
            val profileVm: ProfileViewModel = viewModel(
                viewModelStoreOwner = backStackEntry,
                factory = ProfileViewModel.factory(appConfiguration.userServices)
            )
            ProfileScreen(
                viewModel = profileVm,
                onLogOut = {navController.navigate(Screens.HOME_SCREEN.route)}
            )
        }
    }
}
