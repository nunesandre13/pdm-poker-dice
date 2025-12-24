package pt.isel.pdm.match.innerComposable.screens.betting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

