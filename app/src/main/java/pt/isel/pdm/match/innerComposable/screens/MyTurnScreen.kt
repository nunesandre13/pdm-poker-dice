package pt.isel.pdm.match.innerComposable.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.match.innerComposable.DisplayOtherPlayersStatusOverlay
import pt.isel.pdm.match.innerComposable.DrawCup
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.viewModels.myTurn.MyTurnUiState
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

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
            MyTurnContent(state = state, vm = vm, playersPosition = playersPosition)
        }
    }
}

@Composable
fun MyTurnContent(
    state: MyTurnUiState.ValidState,
    vm: MyTurnViewModel,
    playersPosition: PlayerRegistry
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            is MyTurnUiState.Idle -> {
                var dices by remember { mutableStateOf(emptyList<DiceFace>()) }
                DisplayOtherPlayersStatusOverlay(
                    players = state.round.players,
                    playersPosition = playersPosition,
                    collectMyDices = {diceFace -> dices =
                        if (diceFace in dices) dices - diceFace else dices + diceFace }
                )
                Text(
                    "Select Dices to Reroll: "
                            + dices.joinToString { it.name + ";" },
                    modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)
                )
                Row(modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)) {
                    Button(
                        onClick = { TODO()},
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text("Raise Ante")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { vm.setHand() },
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text("Set Hand")
                    }
                }

                DrawCup(modifier = Modifier.clickable(onClick = { vm.rollDice(dices); dices}).align(Alignment.BottomEnd))
            }
            is MyTurnUiState.RaisingAnte -> {
                DisplayOtherPlayersStatusOverlay(
                    players = state.round.players,
                    playersPosition = playersPosition
                )
                DrawCup()
            }
            is MyTurnUiState.Rolling -> {
                DisplayOtherPlayersStatusOverlay(
                    players = state.round.players,
                    playersPosition = playersPosition
                )
                DrawCup(state.round.players.first(), vm.starRollingAnimation,{}){ vm.stopRollingAnimation() }
            }
            is MyTurnUiState.SettingHand -> {
                DisplayOtherPlayersStatusOverlay(
                    players = state.round.players,
                    playersPosition = playersPosition
                )
            }
        }
    }
}








