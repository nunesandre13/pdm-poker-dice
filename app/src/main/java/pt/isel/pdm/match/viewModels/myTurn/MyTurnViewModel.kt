package pt.isel.pdm.match.viewModels.myTurn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.domain.Round
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.domain.State
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider
import pt.isel.pdm.match.viewModels.interfaces.RollingActions
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState

sealed interface MyTurnActionState {
    object Idle : MyTurnActionState
    object Rolling : MyTurnActionState
    object SettingHand : MyTurnActionState
}

data class MyTurnDataState(
    val currentDice: List<DiceFace>,
    val rollsLeft: Int
)

sealed interface MyTurnUiState : State {
    interface ValidState: MyTurnUiState {
        val data: MyTurnDataState
        val round: Round
    }
    object InitialLoading : MyTurnUiState

    data class Idle(override val data: MyTurnDataState, override val round: Round) : ValidState

    data class Rolling(override val data: MyTurnDataState, override val round: Round ) : ValidState

    data class SettingHand(override val data: MyTurnDataState, override val round: Round ) : ValidState
}

sealed class MyTurnError(
    override val message: String?
): DomainError {
    data object SomeError: MyTurnError(null)
}

class MyTurnViewModel(
    private val baseViewModel: ViewModelState<MyTurnUiState, MyTurnError>,
    private val stateProvider: MatchStateProvider,
    private val actions: RollingActions
) : ViewModel(),
    ViewModelState<MyTurnUiState, MyTurnError> by baseViewModel {

        init {
            viewModelScope.launch {
                transformStateInUiState()
            }
        }

    private val actualRound = stateProvider.matchState.filterIsInstance<MatchState.ActualMatch>().map { it.match.actualRound }
    private val _actionState = MutableStateFlow<MyTurnActionState>(MyTurnActionState.Idle)

    private fun RoundState.Rolling.extractDataFromRoundState(): MyTurnDataState? =
        when(val myTurn = turn.playerStatus){
            is PlayerStatus.PassRound, is PlayerStatus.NotStarted  -> null
            is PlayerStatus.FinalHand, -> {
               MyTurnDataState(
                    currentDice = myTurn.hand.dices,
                    rollsLeft = 0
                )
            }
            is PlayerStatus.StillRolling -> {
                MyTurnDataState(
                    currentDice = myTurn.hand.dices,
                    rollsLeft = myTurn.remainingRolls
                )
            }
        }

    private suspend fun transformStateInUiState() {
        actualRound.mapNotNull { round ->
            (round.state as? RoundState.Rolling)
                ?.extractDataFromRoundState()?.let { data ->
                    round to data
                }
        }.combine(_actionState) { (round, data), action ->
            when (action) {
                is MyTurnActionState.Idle -> MyTurnUiState.Idle(data, round)
                is MyTurnActionState.Rolling -> MyTurnUiState.Rolling(data, round)
                is MyTurnActionState.SettingHand -> MyTurnUiState.SettingHand(data, round)
            }
        }.collect { uiState ->
            navigateTo(uiState)
        }
    }

    fun rollDice(dices: List<DiceFace>) {
        when (val state = stateUi.value) {
            MyTurnUiState.InitialLoading -> {  /* do nothing */  }
            is MyTurnUiState.ValidState -> {
                if (state.data.rollsLeft > 0 && _actionState.compareAndSet(MyTurnActionState.Idle, MyTurnActionState.Rolling)) {
                    runAndSetAction(MyTurnActionState.Idle){
                        actions.rollDice(dices)
                    }
                }
            }
        }
    }

    fun setHand() {
        when (stateUi.value) {
            MyTurnUiState.InitialLoading -> { /* do nothing */ }
            is MyTurnUiState.ValidState -> {
                if (_actionState.compareAndSet(MyTurnActionState.Idle, MyTurnActionState.SettingHand)) {
                    runAndSetAction(MyTurnActionState.Idle){
                        actions.setHand()
                    }
                }
            }
        }
    }

    private fun runAndSetAction(endAction: MyTurnActionState, code: suspend ()-> Unit){
        viewModelScope.launch {
            try {
                code()
            }finally {
                _actionState.value = endAction
            }
        }
    }

    companion object {
        fun factory(
            stateProvider: MatchStateProvider,
            actions: RollingActions,
            base: ViewModelState<MyTurnUiState, MyTurnError> =
                ViewModelBase(MyTurnUiState.InitialLoading, MyTurnError.SomeError)
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MyTurnViewModel(
                    baseViewModel = base,
                    stateProvider = stateProvider,
                    actions = actions
                ) as T
            }
        }
    }

}