package pt.isel.pdm.match.innerComposable.screens.otherPlayers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.viewModels.otherPlayers.OtherPlayerTurnUiState
import pt.isel.pdm.match.viewModels.otherPlayers.OtherPlayerTurnViewModel
import pt.isel.pdm.match.ui.DrawPlayers.DisplayOtherPlayersStatusOverlay

@Composable
fun OtherPlayerTurnScreen(vm: OtherPlayerTurnViewModel, playersPosition: PlayerRegistry) {
    val uiState by vm.stateUi.collectAsStateWithLifecycle()
    when (val state = uiState) {
        is OtherPlayerTurnUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
        is OtherPlayerTurnUiState.ShowingTurn -> {
            val players = state.round.players
            DisplayOtherPlayersStatusOverlay(
                players = players,
                playersPosition = playersPosition
            )
        }
    }
}