package pt.isel.pdm.navigation.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navDeepLink
import pt.isel.pdm.DeepLinks
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.lobby.ui.LobbyScreen
import pt.isel.pdm.lobby.viewmodel.LobbyViewModel
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens
import pt.isel.pdm.navigation.composableWithOrientation

fun NavGraphBuilder.lobby(
    appConfiguration: DependenciesContainer,
    onNavigation: (NavigationEvent) -> Unit
) {
    composableWithOrientation<Screens.Lobby>(getDeepLink()) { backStackEntry ->
        val lobbyVm: LobbyViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = LobbyViewModel.getFactory(
                appConfiguration.lobbyServices,
                appConfiguration.userServices
            )
        )
        LobbyScreen(
            viewModel = lobbyVm,
            goBack = {onNavigation(NavigationEvent.Back)},
            onUp = {onNavigation(NavigationEvent.Navigate(Screens.Match(it.toInt())))}
        )
    }
}

private fun getDeepLink() = listOf(
    navDeepLink<Screens.Match>(
        basePath = DeepLinks.LOBBY_BASE
    )
)