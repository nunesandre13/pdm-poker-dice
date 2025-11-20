package pt.isel.pdm.match.viewModels.otherPlayers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.Round
import pt.isel.pdm.domain.State
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState

sealed interface OtherPlayerTurnUiState : State{
    object Loading : OtherPlayerTurnUiState
    data class ShowingTurn(val round: Round) : OtherPlayerTurnUiState
}

sealed class OtherPlayerTurnError(
    override val message: String?
): DomainError {
    data object SomeError: OtherPlayerTurnError(null)
}

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

    companion object {
        fun factory(
            stateProvider: MatchStateProvider,
            base: ViewModelState<OtherPlayerTurnUiState, OtherPlayerTurnError> =
                ViewModelBase(OtherPlayerTurnUiState.Loading, OtherPlayerTurnError.SomeError)
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return OtherPlayerTurnViewModel(
                    matchStateProvider = stateProvider,
                    baseViewModel = base
                ) as T
            }
        }
    }
}
