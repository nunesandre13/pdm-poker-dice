package pt.isel.pdm.match.innerComposable.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.match.innerComposable.DrawOnPlayers
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.ui.GameScreen
import pt.isel.pdm.match.ui.dices.DisplayStaticDices
import pt.isel.pdm.match.viewModels.myTurn.MyTurnUiState
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel

@Composable
fun MyTurnScreen(vm: MyTurnViewModel, playersPosition: PlayerRegistry) {

    val uiState by vm.stateUi.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MyTurnUiState.InitialLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
        is MyTurnUiState.ValidState -> {
            val players = state.round.players

            if (players.isNotEmpty()) {

                val me = players.first()
                val others = players.drop(1)
                GameScreen(
                    me = me,
                    others = others,
                    onRollFinished = {
                        vm.setHand()
                    }
                )
                DrawOnPlayers(
                    players = others,
                    registry = playersPosition
                ) { playerState, modifier ->
                    Box(modifier = modifier, contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Player ${playerState.playerId}",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                            val hand = when (val s = playerState.playerStatus) {
                                is PlayerStatus.StillRolling -> s.hand
                                is PlayerStatus.FinalHand -> s.hand
                                PlayerStatus.NotStarted,
                                PlayerStatus.PassRound -> null
                            }
                            if (hand?.dices?.isNotEmpty() == true) {
                                DisplayStaticDices(
                                    dicesHand = hand,
                                    size = 80.dp
                                )
                            } else {
                                Text(
                                    text = "No dices",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}