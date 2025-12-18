package pt.isel.pdm.navigation.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.match.screens.MatchScreen
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens

fun NavGraphBuilder.match(
    appConfiguration: DependenciesContainer,
    onNavigation: (NavigationEvent) -> Unit
) {
    composable<Screens.Match> { backStackEntry ->
        val matchId = backStackEntry.toRoute<Screens.Match>().matchId
        val matchVm: MatchViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = MatchViewModel.factory(
                matchServices = appConfiguration.matchService,
                userService = appConfiguration.userServices,
                matchId = matchId,
            )
        )
        MatchScreen(matchVm, {onNavigation(NavigationEvent.Navigate(Screens.StartMatch))} )
    }
}
