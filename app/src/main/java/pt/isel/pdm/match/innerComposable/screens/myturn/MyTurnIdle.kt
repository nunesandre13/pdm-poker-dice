package pt.isel.pdm.match.innerComposable.screens.myturn

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.match.DiceFace
import pt.isel.pdm.domain.state.PlayerRoundStateWithName
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.innerComposable.PlayerRegistryManager
import pt.isel.pdm.match.ui.DrawPlayers.DisplayOtherPlayersStatusOverlay
import pt.isel.pdm.match.ui.cup.DrawCup
import pt.isel.pdm.match.viewModels.myTurn.MyTurnUiState
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel
import kotlin.collections.forEach

@Composable
fun MyTurnUiIdleContent(
    players: List<PlayerRoundStateWithName>,
    playersPosition: PlayerRegistry,
    selectedDices: List<DiceFace>,
    anteValue: Int,
    isAnteDialogVisible: Boolean,
    onToggleDice: (DiceFace) -> Unit,
    onRaiseAnteClick: () -> Unit,
    onSetHandClick: () -> Unit,
    onPassTurnClick: () -> Unit,
    onAnteChange: (Int) -> Unit,
    onDismissAnteDialog: () -> Unit,
    onConfirmAnte: () -> Unit,
    onRollDiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        DisplayOtherPlayersStatusOverlay(
            players = players,
            playersPosition = playersPosition,
            collectMyDices = onToggleDice
        )
        SelectedDiceOverlay(
            dices = selectedDices,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        )
        MyTurnButtons(
            onRaiseAnteClick = onRaiseAnteClick,
            onSetHandClick = onSetHandClick,
            onPassTurn = onPassTurnClick,
            modifier = Modifier
                .align(Alignment.BottomStart)
        )
        DrawCup(
            modifier = Modifier
                .clickable(onClick = onRollDiceClick)
                .align(Alignment.BottomEnd)
        )
        if (isAnteDialogVisible) {
            ShowAnteDialog(
                ante = anteValue,
                onAnteChange = onAnteChange,
                onDismiss = onDismissAnteDialog,
                onConfirm = onConfirmAnte
            )
        }
    }
}


@Composable
fun MyTurnUiIdle(
    state: MyTurnUiState.Idle,
    playersPosition: PlayerRegistry,
    vm: MyTurnViewModel
) {
    var ante by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedDices by remember { mutableStateOf(emptyList<DiceFace>()) }
    val onToggleDiceSelection: (DiceFace) -> Unit = { diceFace ->
        selectedDices = if (diceFace in selectedDices) {
            selectedDices - diceFace
        } else {
            selectedDices + diceFace
        }
    }

    val onConfirmAnte: () -> Unit = {
        showDialog = false
        vm.raiseAnte(ante)
        ante = 0
    }
    val onRollDice: () -> Unit = {
        vm.rollDice(selectedDices)
        selectedDices = emptyList()
    }
    MyTurnUiIdleContent(
        players = state.round.players,
        playersPosition = playersPosition,
        selectedDices = selectedDices,
        anteValue = ante,
        isAnteDialogVisible = showDialog,
        onToggleDice = onToggleDiceSelection,
        onRaiseAnteClick = { showDialog = true },
        onSetHandClick = { vm.setHand() },
        onPassTurnClick = { vm.passTurn() },
        onAnteChange = { ante = it },
        onDismissAnteDialog = { showDialog = false; ante = 0 },
        onConfirmAnte = onConfirmAnte,
        onRollDiceClick = onRollDice,
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
            AnteSelector(
                ante = ante,
                onIncrement = { onAnteChange(ante + 1) },
                onDecrement = { if (ante > 0) onAnteChange(ante - 1) }
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
@Composable
private fun AnteSelector(
    ante: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(onClick = onDecrement) { Text("-") }
        Text(
            text = "$ante",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = onIncrement) { Text("+") }
    }
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
            DiceListRow(dices)
        } else {
            Text(
                text = "(None selected)",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun DiceListRow(dices: List<DiceFace>) {
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
                Text("Set Hand")
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



@Preview(showBackground = true, widthDp = 412, heightDp = 732, name = "1. Estado Normal")
@Composable
fun PreviewMyTurnUiIdle_Default() {
    val mockPlayers = emptyList<PlayerRoundStateWithName>()
    val mockRegistry = PlayerRegistryManager().build()

    MaterialTheme {
        MyTurnUiIdleContent(
            players = mockPlayers,
            playersPosition = mockRegistry,
            selectedDices = emptyList(),
            anteValue = 0,
            isAnteDialogVisible = false,
            onToggleDice = {},
            onRaiseAnteClick = {},
            onSetHandClick = {},
            onPassTurnClick = {},
            onAnteChange = {},
            onDismissAnteDialog = {},
            onConfirmAnte = {},
            onRollDiceClick = {}
        )
    }
}

@Preview(showBackground = true, name = "2. Com Dados Selecionados")
@Composable
fun PreviewMyTurnUiIdle_SelectedDice() {
    val mockDices = listOf<DiceFace>()
    MyTurnUiIdleContent(
        players = emptyList(),
        playersPosition = PlayerRegistryManager().build(),
        selectedDices = mockDices,
        anteValue = 50,
        isAnteDialogVisible = false,
        onToggleDice = {},
        onRaiseAnteClick = {},
        onSetHandClick = {},
        onPassTurnClick = {},
        onAnteChange = {},
        onDismissAnteDialog = {},
        onConfirmAnte = {},
        onRollDiceClick = {}
    )
}

@Preview(showBackground = true, name = "3. Dialogo Ante Aberto")
@Composable
fun PreviewMyTurnUiIdle_DialogVisible() {
    MyTurnUiIdleContent(
        players = emptyList(),
        playersPosition = PlayerRegistryManager().build(),
        selectedDices = emptyList(),
        anteValue = 100,
        isAnteDialogVisible = true,
        onToggleDice = {},
        onRaiseAnteClick = {},
        onSetHandClick = {},
        onPassTurnClick = {},
        onAnteChange = {},
        onDismissAnteDialog = {},
        onConfirmAnte = {},
        onRollDiceClick = {}
    )
}