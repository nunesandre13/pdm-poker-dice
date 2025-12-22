package pt.isel.pdm.match.innerComposable.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.innerComposable.screens.otherPlayers.OtherPlayerTurnScreen
import pt.isel.pdm.match.screens.RoundRoute
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.match.viewModels.otherPlayers.OtherPlayerTurnViewModel

fun NavGraphBuilder.otherPlayerTurn(matchViewModel: MatchViewModel, playersPosition: PlayerRegistry) {
    composable<RoundRoute.OtherPlayerTurn> { backStackEntry ->
        val vmOtherPlayerTurnViewModel: OtherPlayerTurnViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = OtherPlayerTurnViewModel.factory(
                stateProvider = matchViewModel
            )
        )
        OtherPlayerTurnScreen(vm = vmOtherPlayerTurnViewModel, playersPosition = playersPosition)
    }
}