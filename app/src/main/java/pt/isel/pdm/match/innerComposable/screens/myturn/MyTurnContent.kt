package pt.isel.pdm.match.innerComposable.screens.myturn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.ui.DrawPlayers.DisplayOtherPlayersStatusOverlay
import pt.isel.pdm.match.viewModels.myTurn.MyTurnUiState
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel

@Composable
fun MyTurnContent(
    state: MyTurnUiState.ValidState,
    vm: MyTurnViewModel,
    playersPosition: PlayerRegistry
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            is MyTurnUiState.Idle -> MyTurnUiIdle(state, playersPosition, vm)

            is MyTurnUiState.RaisingAnte -> MyTurnUiRaisingAnte(state, playersPosition)

            is MyTurnUiState.Rolling -> MyTurnUiRolling(state, playersPosition, vm)

            is MyTurnUiState.SettingHand, is MyTurnUiState.PassingTurn -> {
                DisplayOtherPlayersStatusOverlay(state.round.players,playersPosition)
            }
        }
    }
}