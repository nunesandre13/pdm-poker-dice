package pt.isel.pdm.match.viewModels.betting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.match.BetState
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.match.RoundState
import pt.isel.pdm.domain.State
import pt.isel.pdm.domain.state.Round
import pt.isel.pdm.match.viewModels.betting.BettingUiState.*
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.match.viewModels.interfaces.RoundStateProvider
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState
import pt.isel.pdm.utils.updateIf

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

sealed class BettingError(
    override val message: String?
): DomainError {
    data object SomeError: BettingError(null)
}


class BettingViewModel(
    private val baseViewModel: ViewModelState<BettingUiState, BettingError>,
    private val stateProvider: RoundStateProvider,
    private val actions: BettingActions
) : ViewModel(),
    ViewModelState<BettingUiState, BettingError> by baseViewModel {

    private val actualRound = stateProvider.roundState.filterNotNull()

    private val _actionState = MutableStateFlow<BettingActionState>(BettingActionState.Idle)

    private suspend fun transformStateInUiState() {
        actualRound
            .filter { it.state is RoundState.Betting }
            .combine(_actionState) { round, action ->
                val state = round.state as RoundState.Betting
                if (state.hasPlayerBet()) {
                    BettingDone(round)
                } else {
                    action.toUiState(round)
                }
            }
            .collectLatest { navigateTo(it) }
    }


    fun call() {
        when (stateUi.value) {
            is InitialLoading -> { /* Não fazer nada */ }
            is ValidState -> {
                if (_actionState.updateIf(BettingActionState.Idle, BettingActionState.PlacingBet)) {
                    runAndSetAction(BettingActionState.BettingDone) {
                        actions.call()
                    }
                }
            }
        }
    }

    fun fold() {
        when (stateUi.value) {
            is InitialLoading -> { /* Não fazer nada */ }
            is ValidState -> {
                if (_actionState.updateIf(BettingActionState.Idle, BettingActionState.PlacingBet)) {
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

    private fun RoundState.Betting.hasPlayerBet() =
        stateProvider.player.value?.id.let {  playerId ->
            playersBets.firstOrNull{ it.playerId.id == playerId?.id }?.betState != BetState.PENDING
        }

    private fun BettingActionState.toUiState(round: Round) = when (this) {
        is BettingActionState.Idle -> AwaitingBetting(round)
        is BettingActionState.PlacingBet -> Betting(round)
        is BettingActionState.BettingDone -> BettingDone(round)
    }

    fun init() {
        viewModelScope.launch {
            transformStateInUiState()
        }
    }

    companion object {
        fun factory(
            stateProvider: RoundStateProvider,
            actions: BettingActions,
            base: ViewModelState<BettingUiState, BettingError> =
                ViewModelBase(InitialLoading, BettingError.SomeError)
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return BettingViewModel(
                    baseViewModel = base,
                    stateProvider = stateProvider,
                    actions = actions
                ).also { it.init() } as T
            }
        }
    }
}
