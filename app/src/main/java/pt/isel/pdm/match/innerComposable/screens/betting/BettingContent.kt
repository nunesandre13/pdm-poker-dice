package pt.isel.pdm.match.innerComposable.screens.betting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.ui.DrawPlayers.DisplayOtherPlayersStatusOverlay
import pt.isel.pdm.match.ui.cup.DrawCup
import pt.isel.pdm.match.viewModels.betting.BettingUiState
import pt.isel.pdm.match.viewModels.betting.BettingViewModel

@Composable
fun BettingContent(
    state: BettingUiState.ValidState,
    vm: BettingViewModel,
    playersPosition: PlayerRegistry
) {
    val players = state.round.players
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DisplayOtherPlayersStatusOverlay(
            players = players,
            playersPosition = playersPosition
        )
        DrawCup(modifier = Modifier.align(Alignment.BottomEnd))
        when (state) {
            is BettingUiState.AwaitingBetting -> AwatingBetting(vm::call, vm::fold)
            is BettingUiState.Betting -> Unit
            is BettingUiState.BettingDone -> BettingDone()
        }
    }
}


