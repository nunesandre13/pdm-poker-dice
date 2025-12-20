package pt.isel.pdm.navigation.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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
    composableWithOrientation<Screens.StartMatch> { backStackEntry ->
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
