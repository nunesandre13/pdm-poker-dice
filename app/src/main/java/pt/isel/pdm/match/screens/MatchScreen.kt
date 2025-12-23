package pt.isel.pdm.match.screens

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.match.innerComposable.RoundScreen
import pt.isel.pdm.match.viewModels.MatchGlobalStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.ui.CircularBox
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp

@Composable
fun MatchScreen(matchViewModel: MatchViewModel, onMatchEnded: () -> Unit) {
    MatchScreenContent(matchViewModel, onMatchEnded)
    MatchScreenError(matchViewModel)
}

@Composable
fun MatchScreenContent(
    matchViewModel: MatchViewModel,
    onMatchEnded: () -> Unit
) {
    val globalUiState by matchViewModel.stateUi.collectAsStateWithLifecycle()
    when (globalUiState) {
        is MatchGlobalStateUi.Loading -> CircularBox()
        is MatchGlobalStateUi.Finished -> {
            MatchFinishedView(
                matchStateProvider = matchViewModel,
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

@Composable
fun MatchScreenError(viewModel: MatchViewModel) {
    when (val stateError = viewModel.errorState.collectAsState().value) {
        is MatchError.InvalidPlay, MatchError.SomeError, is MatchError.ApiError, is MatchError.NetworkError -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
        is MatchError.NoError -> Unit
    }
}


