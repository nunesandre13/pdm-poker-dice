package pt.isel.pdm.match.innerComposable.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.innerComposable.screens.myturn.MyTurnScreen
import pt.isel.pdm.match.screens.RoundRoute
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel

fun NavGraphBuilder.myTurn(matchViewModel: MatchViewModel,playersPosition:PlayerRegistry) {
    composable<RoundRoute.MyTurn> { backStackEntry ->
        val vmMyTurnViewModel: MyTurnViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = MyTurnViewModel.factory(
                stateProvider = matchViewModel,
                actions = matchViewModel
            )
        )
        MyTurnScreen(vm = vmMyTurnViewModel, playersPosition = playersPosition)
    }
}