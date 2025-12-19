package pt.isel.pdm.match.innerComposable.screens.betting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.match.innerComposable.DisplayOtherPlayersStatusOverlay
import pt.isel.pdm.match.innerComposable.DrawCup
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.viewModels.betting.BettingUiState
import pt.isel.pdm.match.viewModels.betting.BettingViewModel

@Composable
fun BettingScreen(vm: BettingViewModel, playersPosition:PlayerRegistry) {
    val uiState by vm.stateUi.collectAsStateWithLifecycle()
    when (val state = uiState) {
        BettingUiState.InitialLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
        is BettingUiState.ValidState -> {
            BettingContent(state = state, vm = vm, playersPosition = playersPosition)
        }
    }
}

@Composable
fun BettingContent(
    state: BettingUiState.ValidState,
    vm: BettingViewModel,
    playersPosition: PlayerRegistry
) {
    val players = state.round.players
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        DisplayOtherPlayersStatusOverlay(
            players = players,
            playersPosition = playersPosition
        )
        DrawCup(modifier = Modifier.align(Alignment.BottomEnd))
        when (state) {
            is BettingUiState.AwaitingBetting -> AwatingBetting(vm)
            is BettingUiState.Betting -> Unit
            is BettingUiState.BettingDone -> BettingDone()
        }
    }
}

@Composable
fun AwatingBetting(vm: BettingViewModel){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { vm.call() }) {
                Text("Call")
            }
            Button(onClick = { vm.fold() }) {
                Text("Fold")
            }
        }
    }
}

@Composable
fun BettingDone(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Betting action completed.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}