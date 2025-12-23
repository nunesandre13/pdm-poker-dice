package pt.isel.pdm.match.innerComposable.screens.myturn

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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

            is MyTurnUiState.SettingHand, is MyTurnUiState.PassingTurn -> {
                DisplayOtherPlayersStatusOverlay(state.round.players,playersPosition)
            }
        }
    }
}

@Composable
private fun BoxScope.MyTurnUiRaisingAnte(
    state: MyTurnUiState.RaisingAnte,
    playersPosition: PlayerRegistry
) {
    DisplayOtherPlayersStatusOverlay(
        players = state.round.players,
        playersPosition = playersPosition
    )
    DrawCup(modifier = Modifier.align(Alignment.BottomEnd))
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
fun MyTurnButtons(
    onRaiseAnteClick: () -> Unit,
    onSetHandClick: () -> Unit,
    onPassTurn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.38f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = onRaiseAnteClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Raise Ante")
            }
            Button(
                onClick = onSetHandClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
            ) {
                Text("Set Ante ")
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth(0.62f))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onPassTurn,
                colors = IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Pass Turn",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Dark Mode", widthDp = 412, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyTurnButtonsDarkPreview() {
    MaterialTheme {
        Surface {
            MyTurnButtons(
                onRaiseAnteClick = {},
                onSetHandClick = {},
                onPassTurn = {}
            )
        }
    }
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
    SelectedDiceOverlay(
        dices = dices,
        modifier = Modifier
            .padding(16.dp)
            .align(Alignment.TopEnd)
    )
    MyTurnButtons(
        onRaiseAnteClick = { showDialog = true },
        onSetHandClick = { vm.setHand() },
        onPassTurn =  {vm.passTurn()},
        modifier = Modifier
            .align(Alignment.BottomStart)
    )
    if (showDialog) {
        ShowAnteDialog(
            ante = ante,
            onAnteChange = { ante = it },
            onDismiss = { showDialog = false; ante = 0},
            onConfirm = { showDialog = false; vm.raiseAnte(ante); ante = 0 }
        )
    }
    DrawCup(modifier = Modifier
        .clickable(onClick = { vm.rollDice(dices); dices = emptyList() })
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
        title = { Text("Raise Ante") },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { if (ante > 0) onAnteChange(ante - 1) }) { Text("-") }
                Text("  $ante  ", modifier = Modifier.padding(horizontal = 16.dp))
                Button(onClick = { onAnteChange(ante + 1) }) { Text("+") }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun SelectedDiceOverlay(
    dices: List<DiceFace>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = "Select Dices to Reroll:",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (dices.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                dices.forEach { dice ->
                    Image(
                        painter = painterResource(id = dice.resId),
                        contentDescription = dice.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        } else {
            Text(
                text = "(None selected)",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}







