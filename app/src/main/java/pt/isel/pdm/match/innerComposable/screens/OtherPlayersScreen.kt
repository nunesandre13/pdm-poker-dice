// kotlin
package pt.isel.pdm.match.innerComposable.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.match.innerComposable.DrawOnPlayers
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.ui.GameScreen
import pt.isel.pdm.match.ui.dices.DisplayStaticDices
import pt.isel.pdm.match.viewModels.otherPlayers.OtherPlayerTurnUiState
import pt.isel.pdm.match.viewModels.otherPlayers.OtherPlayerTurnViewModel
import pt.isel.pdm.domain.PlayerStatus

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
            if (players.isNotEmpty()) {
                DrawOnPlayers(
                    players = players,
                    registry = playersPosition
                ) { playerState, modifier ->
                    Box(modifier = modifier, contentAlignment = Alignment.Center) {
                        val hand = when (val s = playerState.playerStatus) {
                            is PlayerStatus.StillRolling -> s.hand
                            is PlayerStatus.FinalHand -> s.hand
                            PlayerStatus.NotStarted,
                            PlayerStatus.PassRound -> null
                        }

                        if (hand?.dices?.isNotEmpty() == true) {
                            DisplayStaticDices(
                                dicesHand = hand,
                                size = 100.dp
                            )
                        } else {
                            Text(
                                text = "Sem Dados",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
