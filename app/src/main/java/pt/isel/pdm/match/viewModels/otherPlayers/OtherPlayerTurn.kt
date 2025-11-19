package pt.isel.pdm.match.viewModels.otherPlayers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.Round
import pt.isel.pdm.domain.State
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider
import pt.isel.pdm.utils.ViewModelState

sealed interface OtherPlayerTurnUiState : State{
    object Loading : OtherPlayerTurnUiState
    data class ShowingTurn(val round: Round) : OtherPlayerTurnUiState
}

sealed interface OtherPlayerTurnError: DomainError

class OtherPlayerTurnViewModel(
    private val matchStateProvider: MatchStateProvider,
    private val baseViewModel: ViewModelState<OtherPlayerTurnUiState, OtherPlayerTurnError>
) : ViewModel(), ViewModelState<OtherPlayerTurnUiState, OtherPlayerTurnError> by baseViewModel {

    init {
        viewModelScope.launch {
            matchStateProvider.matchState
                .filterIsInstance<MatchState.ActualMatch>()
                .map { it.match.actualRound }
                .collect { round ->
                    navigateTo(OtherPlayerTurnUiState.ShowingTurn(round))
                }
        }
    }
}
