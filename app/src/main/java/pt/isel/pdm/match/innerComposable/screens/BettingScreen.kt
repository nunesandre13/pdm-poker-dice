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
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.match.ui.GameScreen
import pt.isel.pdm.match.viewModels.betting.BettingUiState
import pt.isel.pdm.match.viewModels.betting.BettingViewModel

@Composable
fun BettingScreen(vm: BettingViewModel) {
    val uiState by vm.stateUi.collectAsStateWithLifecycle()

    when (val state = uiState) {
        BettingUiState.InitialLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is BettingUiState.ValidState -> {
            val round = state.round


            val me = round.players.first()
            val others = round.players.drop(1)
            val bettingState = round.state as? RoundState.Betting

            Box(modifier = Modifier.fillMaxSize()) {

                GameScreen(
                    me = me,
                    others = others,
                    onRollFinished = {}
                )

                if (bettingState != null && bettingState.turn.playerId == me.playerId) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                            .fillMaxWidth(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "É a sua vez de apostar. Aposta atual: ${bettingState.amount} moedas.",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Botão FOLD
                            Button(onClick = { vm.fold() }) {
                                Text("FOLD")
                            }
                            // Botão CALL
                            Button(onClick = { /* vm.call() */ }) {
                                Text("CALL (${bettingState.amount} moedas)")
                            }
                            // Botão RAISE
                            Button(onClick = { /* vm.raise() */ }) {
                                Text("RAISE")
                            }
                        }
                    }
                } else if (bettingState != null) {
                    Text(
                        text = "Aguardando aposta do Jogador ${bettingState.turn.playerId}...",
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                    )
                }
            }
        }
    }
}