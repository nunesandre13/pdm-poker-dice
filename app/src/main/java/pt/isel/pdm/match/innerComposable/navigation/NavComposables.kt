package pt.isel.pdm.match.innerComposable.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.innerComposable.screens.betting.BettingScreen
import pt.isel.pdm.match.innerComposable.screens.myturn.MyTurnScreen
import pt.isel.pdm.match.innerComposable.screens.otherPlayers.OtherPlayerTurnScreen
import pt.isel.pdm.match.screens.RoundRoute
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.match.viewModels.betting.BettingViewModel
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel
import pt.isel.pdm.match.viewModels.otherPlayers.OtherPlayerTurnViewModel

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

fun NavGraphBuilder.idle() {
    composable<RoundRoute.Idle> {
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

fun NavGraphBuilder.finished() {
    composable<RoundRoute.Finished> {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color.Yellow
            )
        }
    }
}

