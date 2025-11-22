package pt.isel.pdm.match.innerComposable.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.match.viewModels.betting.BettingUiState
import pt.isel.pdm.match.viewModels.betting.BettingViewModel

@Composable
fun BettingScreen(vm: BettingViewModel) {
    val uiState by vm.stateUi.collectAsStateWithLifecycle()
    when (val state = uiState) {
        BettingUiState.InitialLoading -> TODO()
        is BettingUiState.ValidState -> TODO()
    }
}
