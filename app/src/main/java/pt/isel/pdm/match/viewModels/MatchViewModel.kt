package pt.isel.pdm.match.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.RawMatch
import pt.isel.pdm.domain.MatchId
import pt.isel.pdm.domain.MatchStatus
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.PlayerInfo
import pt.isel.pdm.domain.RawRound
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.domain.State
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.domain.state.Round
import pt.isel.pdm.domain.state.mapping
import pt.isel.pdm.domain.state.toRound
import pt.isel.pdm.domain.toMatch
import pt.isel.pdm.domain.toPlayerInfo
import pt.isel.pdm.match.services.MatchServices
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider
import pt.isel.pdm.match.viewModels.interfaces.RollingActions
import pt.isel.pdm.match.viewModels.interfaces.RoundStateProvider
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.PlayerNameResolver
import pt.isel.pdm.utils.PlayersNameCache
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState
import pt.isel.pdm.utils.errorOrNull
import pt.isel.pdm.utils.getOrNull
import pt.isel.pdm.utils.mapping
import kotlin.time.Duration.Companion.seconds

class MatchViewModel(
    private val viewModelBase: ViewModelState<MatchGlobalStateUi, MatchError>,
    private val matchServices: MatchServices,
    private val playersNameCache: PlayersNameCache,
    userRepository: UserServices,
    matchId: Int
) : ViewModel(),
    ViewModelState<MatchGlobalStateUi, MatchError> by viewModelBase,
    MatchStateProvider , RoundStateProvider, RollingActions, BettingActions {
    private val matchUpdates = matchServices.getMatchUpdate(MatchId(matchId))

    override val player = userRepository.currentUser.map{ it?.toPlayerInfo()}.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )
    override val matchState: StateFlow<MatchState> = matchUpdates.transformFlowIntoMatchStateFlow().rawToRich()

    override val roundState: StateFlow<Round?> = matchState.filterIsInstance<MatchState.ActualMatch>()
        .map { it.match.actualRound }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    val innerNavigation: StateFlow<InnerRoute> = combine(matchState, player) { state, myPlayer ->
        calculateInnerRoute(state, myPlayer)
    }.distinctUntilChanged()
        .scan<InnerRoute, Pair<InnerRoute?, InnerRoute>>(null to InnerRoute.Idle) { (previous, current), new ->
            current to new
        }
        .transform { (previous, current) ->
            if (
                previous == InnerRoute.MyTurnState &&
                current == InnerRoute.OtherPlayerTurn
            ) {
                delay(2.seconds)
            }
            emit(current)
            if (current == InnerRoute.Finished) {
                delay(4.seconds)
            }
        }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = InnerRoute.Idle
    )

    val tableSetup: StateFlow<TableSetup?> = makeTableSetUp()

    private suspend fun executePlayerAction(action: suspend (playerId: Int, roundId: Int) -> OutCome<Unit, MatchError>): Boolean {
        val currentState = matchState.value
        val myPlayer = player.value
        if (currentState is MatchState.ActualMatch && myPlayer != null) {
            action(myPlayer.id.id, currentState.match.actualRound.id.id)
                .errorOrNull()?.let { emitError(it); return false }
            return true
        } else emitError(MatchError.InvalidPlay); return false
    }

    override suspend fun rollDice(dices: List<DiceFace>) : Boolean{
        return executePlayerAction { playerId, roundId ->
            matchServices.rollDice(PlayerId(playerId), RoundId(roundId), dices)
        }
    }

    override suspend fun setHand(): Boolean {
        return executePlayerAction { playerId, roundId ->
            matchServices.setHand(PlayerId(playerId), RoundId(roundId))
        }
    }

    override suspend fun raiseAnte(ante: Int): Boolean {
        return executePlayerAction { playerId, roundId ->
            matchServices.raiseAnte(PlayerId(playerId), RoundId(roundId), ante)
        }
    }

    override suspend fun passTurn(): Boolean {
        return executePlayerAction { playerId, roundId ->
            matchServices.passTurn(PlayerId(playerId), RoundId(roundId))
        }
    }

    override suspend fun call(): Boolean {
        return executePlayerAction { playerId, roundId ->
            matchServices.call(PlayerId(playerId), RoundId(roundId))
        }
    }

    override suspend fun fold(): Boolean {
        return executePlayerAction { playerId, roundId ->
            matchServices.fold(PlayerId(playerId),RoundId(roundId))
        }
    }

    private fun calculateInnerRoute(state: MatchState, myPlayer: PlayerInfo?): InnerRoute {
        if (state !is MatchState.ActualMatch || myPlayer == null) {
            return InnerRoute.Idle
        }
        if (state.match.matchStatus != MatchStatus.ELAPSED) {
            return InnerRoute.Idle
        }
        val round = state.match.actualRound
        return when (round.state) {
            is RoundState.Finished -> InnerRoute.Finished
            is RoundState.Betting -> InnerRoute.BettingState
            is RoundState.Rolling -> {
                if (round.state.turn.playerId.id == myPlayer.id.id) {
                    InnerRoute.MyTurnState
                } else {
                    InnerRoute.OtherPlayerTurn
                }
            }
        }
    }

    private fun makeTableSetUp(): StateFlow<TableSetup?> = combine(player, matchState) { myPlayer, state ->
        val actualMatch = (state as? MatchState.ActualMatch)?.match
        if (myPlayer != null && actualMatch != null) {
            val myId = myPlayer.id.id
            val others = actualMatch.players
                .map { it.playerId.id }
                .filter { it != myId }
            TableSetup(myId, others)
        } else {
            null
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    private fun Flow<OutCome<MatchResponse, MatchError>>.transformFlowIntoMatchStateFlow(): StateFlow<RawMatchState> {
        val initial: RawMatchState = RawMatchState.NoMatch
        return onEach { outcome -> outcome.errorOrNull()?.let { emitError(it) } }
            .map { it.getOrNull() }
            .scan(initial) { previous, response ->
                when (response) {
                    is MatchResponse.MatchEnded -> {
                        if (previous is RawMatchState.Actual)
                            RawMatchState.Actual(previous.match.copy(matchStatus = MatchStatus.FINISHED))
                        else previous
                    }
                    is MatchResponse.NewMatch -> RawMatchState.Actual(response.newMatch)
                    null -> previous
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = RawMatchState.NoMatch
            )
    }

    private suspend fun observeGlobalState() {
        matchState.collect { state ->
            val globalState = when (state) {
                is MatchState.NoMatch -> MatchGlobalStateUi.Loading
                is MatchState.ActualMatch -> {
                    if (state.match.matchStatus == MatchStatus.FINISHED) {
                        delay(4.seconds) // congelar a maquina de estados
                        MatchGlobalStateUi.Finished
                    } else {
                        MatchGlobalStateUi.Elapsed
                    }
                }
            }
            navigateTo(globalState)
        }
    }

    private fun StateFlow<RawMatchState>.rawToRich(): StateFlow<MatchState> =
        combine(playersNameCache.playersCache){ rawState, namesCache ->
        val resolver = PlayerNameResolver(namesCache)
            when (rawState) {
                is RawMatchState.NoMatch -> MatchState.NoMatch
                is RawMatchState.Actual -> rawState.match.run {
                    MatchState.ActualMatch(
                        toMatch(players.mapping(resolver),
                            actualRound.toRound(
                                actualRound.players.mapping(resolver)
                            )
                        )
                    )
                }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MatchState.NoMatch
    )

    fun init() = viewModelScope.launch { observeGlobalState() }

    companion object {
        fun factory(
            matchServices: MatchServices,
            userService: UserServices,
            playersNameCache: PlayersNameCache,
            matchId: Int,
            base: ViewModelState<MatchGlobalStateUi, MatchError> =
                ViewModelBase(MatchGlobalStateUi.Loading, MatchError.NoError)
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MatchViewModel(
                    viewModelBase = base,
                    matchServices = matchServices,
                    userRepository = userService,
                    playersNameCache = playersNameCache,
                    matchId = matchId
                ).also { it.init() } as T
            }
        }
    }
}

sealed interface InnerRoute : State {
    data object Idle: InnerRoute
    data object Finished: InnerRoute

    data object OtherPlayerTurn: InnerRoute
    data object BettingState: InnerRoute
    data object MyTurnState: InnerRoute
}

sealed interface MatchGlobalStateUi: State {

    data object Loading : MatchGlobalStateUi
    data object Elapsed : MatchGlobalStateUi
    data object Finished : MatchGlobalStateUi
}

sealed interface MatchState{
    data object NoMatch: MatchState
    data class ActualMatch(val match: Match): MatchState
}

private sealed interface RawMatchState {
    data object NoMatch : RawMatchState
    data class Actual(val match: RawMatch) : RawMatchState
}

data class TableSetup(val myId: Int, val opponentsIds: List<Int>)