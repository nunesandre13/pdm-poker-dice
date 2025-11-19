package pt.isel.pdm.match.viewModels.betting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.Round
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.domain.State
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.betting.BettingUiState.*
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider
import pt.isel.pdm.utils.ViewModelState

sealed interface BettingActionState {
    object Idle : BettingActionState
    object PlacingBet : BettingActionState

    object BettingDone: BettingActionState
}

sealed interface BettingUiState : State {
    interface ValidState: BettingUiState {
        val round: Round
    }
    object InitialLoading : BettingUiState
    data class AwaitingBetting(override val round: Round) : ValidState
    data class Betting(override val round: Round) : ValidState

    data class BettingDone(override val round: Round) : ValidState
}

sealed interface BettingError: DomainError {

}


class BettingViewModel(
    private val baseViewModel: ViewModelState<BettingUiState, BettingError>,
    private val stateProvider: MatchStateProvider,
    private val actions: BettingActions
) : ViewModel(),
    ViewModelState<BettingUiState, BettingError> by baseViewModel {

    init {
        viewModelScope.launch {
            transformStateInUiState()
        }
    }

    private val actualRound = stateProvider.matchState
        .filterIsInstance<MatchState.ActualMatch>()
        .map { it.match.actualRound }

    private val _actionState = MutableStateFlow<BettingActionState>(BettingActionState.Idle)

    private suspend fun transformStateInUiState() {
        actualRound.filter {
            round -> round.state is RoundState.Betting
        }.combine(_actionState) { round, action ->
            when (action) {
                is BettingActionState.Idle -> AwaitingBetting(round)
                is BettingActionState.PlacingBet -> Betting(round)
                is BettingActionState.BettingDone -> BettingDone(round)
            }
        }.collect { uiState ->
            navigateTo(uiState)
        }
    }

    fun call() {
        when (stateUi.value) {
            is BettingUiState.InitialLoading -> { /* Não fazer nada */ }
            is BettingUiState.ValidState -> {
                if (_actionState.compareAndSet(BettingActionState.Idle, BettingActionState.PlacingBet)) {
                    runAndSetAction(BettingActionState.BettingDone) {
                        actions.call()
                    }
                }
            }
        }
    }

    fun fold() {
        when (stateUi.value) {
            is BettingUiState.InitialLoading -> { /* Não fazer nada */ }
            is BettingUiState.ValidState -> {
                if (_actionState.compareAndSet(BettingActionState.Idle, BettingActionState.PlacingBet)) {
                    runAndSetAction(BettingActionState.BettingDone) {
                        actions.fold()
                    }
                }else {
                    // emit some error
                }
            }
        }
    }

    private fun runAndSetAction(endAction: BettingActionState, code: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                code()
            } finally {
                _actionState.value = endAction
            }
        }
    }
}
