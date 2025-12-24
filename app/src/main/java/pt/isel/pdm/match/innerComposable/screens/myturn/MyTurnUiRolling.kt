package pt.isel.pdm.match.innerComposable.screens.myturn

import androidx.compose.runtime.Composable
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.ui.DrawPlayers.DisplayOtherPlayersStatusOverlay
import pt.isel.pdm.match.ui.cup.DrawCup
import pt.isel.pdm.match.viewModels.myTurn.MyTurnUiState
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel
import pt.isel.pdm.utils.findMe

@Composable
fun MyTurnUiRolling(
    state: MyTurnUiState.Rolling,
    playersPosition: PlayerRegistry,
    vm: MyTurnViewModel
) {
    DisplayOtherPlayersStatusOverlay(
        players = state.round.players,
        playersPosition = playersPosition
    )
    DrawCup(state.round.players.findMe(vm.player?.id), vm.starRollingAnimation, {}) { vm.stopRollingAnimation() }
}