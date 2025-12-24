package pt.isel.pdm.match.innerComposable.screens.myturn

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.ui.DrawPlayers.DisplayOtherPlayersStatusOverlay
import pt.isel.pdm.match.ui.cup.DrawCup
import pt.isel.pdm.match.viewModels.myTurn.MyTurnUiState

@Composable
fun BoxScope.MyTurnUiRaisingAnte(
    state: MyTurnUiState.RaisingAnte,
    playersPosition: PlayerRegistry
) {
    DisplayOtherPlayersStatusOverlay(
        players = state.round.players,
        playersPosition = playersPosition
    )
    DrawCup(modifier = Modifier.align(Alignment.BottomEnd))
}
