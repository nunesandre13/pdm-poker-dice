package pt.isel.pdm.match.innerComposable.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.match.innerComposable.DisplayOtherPlayersStatusOverlay
import pt.isel.pdm.match.innerComposable.DrawCup
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.ui.dices.DisplayClickableDices
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
    val players = state.round.players
    val me = players.first()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            is MyTurnUiState.Idle -> {
                DisplayOtherPlayersStatusOverlay(
                    players = players,
                    playersPosition = playersPosition
                )
                Box(modifier = Modifier.clickable(onClick = { vm.rollDice(emptyList())}).fillMaxSize()){
                    DrawCup()
                }
                Button(
                    onClick = { TODO()},
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text("Raise Ante")
                }
                Button(
                    onClick = { vm.setHand() },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text("Set Hand")
                }
            }
            is MyTurnUiState.RaisingAnte -> {
                DisplayOtherPlayersStatusOverlay(
                    players = players,
                    playersPosition = playersPosition
                )
                DrawCup()
            }
            is MyTurnUiState.Rolling -> {
                DisplayOtherPlayersStatusOverlay(
                    players = players,
                    playersPosition = playersPosition
                )
                DrawCup(me, vm.starRollingAnimation,{}){ vm.stopRollingAnimation() }
            }
            is MyTurnUiState.SettingHand -> {
                DisplayOtherPlayersStatusOverlay(
                    players = players,
                    playersPosition = playersPosition
                )
            }
        }
    }
}








