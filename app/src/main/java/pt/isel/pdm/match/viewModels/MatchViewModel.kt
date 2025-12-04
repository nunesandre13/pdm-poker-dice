package pt.isel.pdm.match.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.MatchStatus
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.domain.State
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.match.services.MatchServices
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider
import pt.isel.pdm.match.viewModels.interfaces.RollingActions
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState
import pt.isel.pdm.utils.errorOrNull
import pt.isel.pdm.utils.getOrNull
import kotlin.jvm.Throws
import kotlin.time.Duration.Companion.seconds

open class MatchViewModel(
    private val viewModelBase: ViewModelState<MatchStateUi, MatchError>,
    private val matchServices: MatchServices,
    userRepository: UserServices,
    matchId: Int
) : ViewModel(),
    ViewModelState<MatchStateUi, MatchError> by viewModelBase,
    MatchStateProvider , RollingActions, BettingActions {
    private val matchUpdates = matchServices.getMatchUpdate(matchId)

    override val matchState: StateFlow<MatchState> = matchUpdates.transformFlowIntoMatchStateFlow()

    open val player = userRepository.currentUser

    init {
        viewModelScope.launch { transformMatchStateIntoStateUi() }
    }

    private suspend fun executePlayerAction(action: suspend (playerId: Int, roundId: Int) -> OutCome<Unit, MatchError>) {
        val currentState = matchState.value
        val myPlayer = player.value
        if (currentState is MatchState.ActualMatch && myPlayer != null) {
            action(myPlayer.id.toInt(), currentState.match.actualRound.id)
                .errorOrNull()?.let { emitError(it) }
        } else emitError(MatchError.InvalidPlay)
    }

    override suspend fun rollDice(dices: List<DiceFace>) {
        executePlayerAction { playerId, roundId ->
            matchServices.rollDice(playerId, roundId, dices)
        }
    }

    override suspend fun setHand() {
        executePlayerAction { playerId, roundId ->
            matchServices.setHand(playerId, roundId)
        }
    }

    override suspend fun raiseAnte(ante: Int) {
        executePlayerAction { playerId, roundId ->
            matchServices.raiseAnte(playerId, roundId, ante)
        }
    }

    override suspend fun passTurn() {
        executePlayerAction { playerId, roundId ->
            matchServices.passTurn(playerId, roundId)
        }
    }

    override suspend fun call() {
        executePlayerAction { playerId, roundId ->
            matchServices.call(playerId, roundId)
        }
    }

    override suspend fun fold() {
        executePlayerAction { playerId, roundId ->
            matchServices.fold(playerId, roundId)
        }
    }

    private fun Flow<OutCome<MatchResponse, MatchError>>.transformFlowIntoMatchStateFlow(): StateFlow<MatchState> {
        val initial: MatchState = MatchState.NoMatch
        return onEach { outcome -> outcome.errorOrNull()?.let { emitError(it) } }
            .map { it.getOrNull() }
            .scan(initial) { previous, response ->
                when (response) {
                    is MatchResponse.MatchEnded -> {
                        if (previous is MatchState.ActualMatch)
                            MatchState.ActualMatch(previous.match.copy(matchStatus = MatchStatus.FINISHED))
                        else previous
                    }

                    is MatchResponse.NewMatch -> MatchState.ActualMatch(response.newMatch)
                    null -> previous
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = MatchState.NoMatch
            )
    }

    private suspend fun transformMatchStateIntoStateUi() {
        player.filterNotNull()
            .combine(matchState) { myPlayer, matchState ->
                when (matchState) {
                    is MatchState.ActualMatch -> {
                        val round = matchState.match.actualRound
                        when (val state = round.state) {
                            is RoundState.Betting -> MatchStateUi.BettingState
                            is RoundState.Rolling -> {
                                if (state.turn.playerId == myPlayer.id.toInt()) {
                                    MatchStateUi.MyTurnState
                                } else {
                                    MatchStateUi.OtherPlayerTurn
                                }
                            }
                            else -> null // Nothing
                        }
                    }
                    is MatchState.NoMatch -> MatchStateUi.Idle
                }
            }
            .filterNotNull()
            .collect { uiState ->
                navigateTo(uiState)
            }
    }

    companion object {
        fun factory(
            matchServices: MatchServices,
            userService: UserServices,
            matchId: Int,
            base: ViewModelState<MatchStateUi, MatchError> =
                ViewModelBase(MatchStateUi.Idle, MatchError.SomeError)
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MatchViewModel(
                    viewModelBase = base,
                    matchServices = matchServices,
                    userRepository = userService,
                    matchId = matchId
                ) as T
            }
        }
    }

}

sealed interface MatchStateUi : State {
    data object Idle: MatchStateUi
    data object OtherPlayerTurn: MatchStateUi
    data object BettingState: MatchStateUi
    data object MyTurnState: MatchStateUi
}

sealed interface MatchState{
    data object NoMatch: MatchState
    data class ActualMatch(val match: Match): MatchState
}