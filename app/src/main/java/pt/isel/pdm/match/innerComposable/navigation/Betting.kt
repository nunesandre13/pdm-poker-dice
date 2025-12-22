package pt.isel.pdm.match.innerComposable.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.innerComposable.screens.betting.BettingScreen
import pt.isel.pdm.match.screens.RoundRoute
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.match.viewModels.betting.BettingViewModel

fun NavGraphBuilder.betting(matchViewModel: MatchViewModel, playersPosition:PlayerRegistry) {
    composable<RoundRoute.Betting> { backStackEntry ->
        val vmBettingViewModel: BettingViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = BettingViewModel.factory(
                stateProvider = matchViewModel,
                actions = matchViewModel
            )
        )
        BettingScreen(vm = vmBettingViewModel, playersPosition = playersPosition)
    }
}