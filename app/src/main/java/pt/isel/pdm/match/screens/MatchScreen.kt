package pt.isel.pdm.match.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.match.innerComposable.RoundScreen
import pt.isel.pdm.match.viewModels.MatchGlobalStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel

@Composable
fun MatchScreen(
    matchViewModel: MatchViewModel,
    onMatchEnded: () -> Unit
) {
    val globalUiState by matchViewModel.stateUi.collectAsStateWithLifecycle()
    when (globalUiState) {
        is MatchGlobalStateUi.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is MatchGlobalStateUi.Finished -> {
            MatchFinishedView(
                onExit = onMatchEnded
            )
        }
        is MatchGlobalStateUi.Elapsed -> {
            MatchActiveGameTable(
                matchViewModel = matchViewModel,
                content = {
                    RoundScreen(matchViewModel = matchViewModel)
                }
            )
        }
    }
}

