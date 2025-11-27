package pt.isel.pdm.match.innerComposable.screens

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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (state) {
            is BettingUiState.AwaitingBetting -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text("É a sua vez de apostar.", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
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
            is BettingUiState.Betting -> {
                CircularProgressIndicator()
                Text(text = "A processar a sua aposta...")
            }
            is BettingUiState.BettingDone -> {
                Text(text = "Aposta submetida. A aguardar pelos outros jogadores.")
            }
        }
    }
}

