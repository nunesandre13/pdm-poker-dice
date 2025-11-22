package pt.isel.pdm.match.innerComposable.Navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.match.innerComposable.screens.BettingScreen
import pt.isel.pdm.match.innerComposable.screens.MyTurnScreen
import pt.isel.pdm.match.innerComposable.screens.OtherPlayerTurnScreen
import pt.isel.pdm.match.screens.MatchRoute
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.match.viewModels.betting.BettingViewModel
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel
import pt.isel.pdm.match.viewModels.otherPlayers.OtherPlayerTurnViewModel

fun NavGraphBuilder.betting(matchViewModel: MatchViewModel) {
    composable<MatchRoute.Betting> { backStackEntry ->
        val vmBettingViewModel: BettingViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = BettingViewModel.factory(
                stateProvider = matchViewModel,
                actions = matchViewModel
            )
        )
        BettingScreen(vm = vmBettingViewModel)
    }
}

fun NavGraphBuilder.myTurn(matchViewModel: MatchViewModel) {
    composable<MatchRoute.MyTurn> { backStackEntry ->
        val vmMyTurnViewModel: MyTurnViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = MyTurnViewModel.factory(
                stateProvider = matchViewModel,
                actions = matchViewModel
            )
        )
        MyTurnScreen(vm = vmMyTurnViewModel)
    }
}

fun NavGraphBuilder.otherPlayerTurn(matchViewModel: MatchViewModel) {
    composable<MatchRoute.OtherPlayerTurn> { backStackEntry ->
        val vmOtherPlayerTurnViewModel: OtherPlayerTurnViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = OtherPlayerTurnViewModel.factory(
                stateProvider = matchViewModel
            )
        )
        OtherPlayerTurnScreen(vm = vmOtherPlayerTurnViewModel)
    }
}


fun NavGraphBuilder.idle() {
    composable<MatchRoute.Idle> {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Black
            )
        }
    }
}