package pt.isel.pdm.match.innerComposable.screens.myturn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import pt.isel.pdm.match.innerComposable.findMe

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
            is MyTurnUiState.Idle -> MyTurnUiIdle(state, playersPosition, vm)

            is MyTurnUiState.RaisingAnte -> MyTurnUiRaisingAnte(state, playersPosition)

            is MyTurnUiState.Rolling -> MyTurnUiRolling(state, playersPosition, vm)

            is MyTurnUiState.SettingHand -> {
                DisplayOtherPlayersStatusOverlay(
                    players = state.round.players,
                    playersPosition = playersPosition
                )
            }
        }
    }
}

@Composable
private fun MyTurnUiRaisingAnte(
    state: MyTurnUiState.RaisingAnte,
    playersPosition: PlayerRegistry
) {
    DisplayOtherPlayersStatusOverlay(
        players = state.round.players,
        playersPosition = playersPosition
    )
    DrawCup()
}


@Composable
private fun MyTurnUiRolling(
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


@Composable
fun BoxScope.MyTurnUiIdle(
    state: MyTurnUiState.Idle,
    playersPosition: PlayerRegistry,
    vm: MyTurnViewModel
) {
    var ante by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    var dices by remember { mutableStateOf(emptyList<DiceFace>()) }
    DisplayOtherPlayersStatusOverlay(
        players = state.round.players,
        playersPosition = playersPosition,
        collectMyDices = { diceFace ->
            dices = if (diceFace in dices) dices - diceFace else dices + diceFace
        }
    )
    Text(
        "Select Dices to Reroll: "
                + dices.joinToString { it.name + ";" },
        modifier = Modifier
            .padding(16.dp)
            .align(Alignment.TopEnd)
    )
    Row(
        modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)
    ) {
        Button(
            onClick = { showDialog = true},
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
    if (showDialog) {
        ShowAnteDialog(
            ante = ante,
            onAnteChange = { ante = it },
            onDismiss = { showDialog = false; ante = 0},
            onConfirm = { showDialog = false; vm.raiseAnte(ante); ante = 0 }
        )
    }
    DrawCup(modifier = Modifier
        .clickable(onClick = { vm.rollDice(dices); dices })
        .align(Alignment.BottomEnd)
    )
}


@Composable
fun ShowAnteDialog(
    ante: Int,
    onAnteChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Aumentar Ante") },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { if (ante > 0) onAnteChange(ante - 1) }) { Text("-") }
                Text("  $ante  ", modifier = Modifier.padding(horizontal = 16.dp))
                Button(onClick = { onAnteChange(ante + 1) }) { Text("+") }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}








