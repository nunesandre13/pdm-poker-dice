package pt.isel.pdm.match.repository

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import pt.isel.pdm.domain.BetState
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.MatchStatus
import pt.isel.pdm.domain.PlayCommand
import pt.isel.pdm.domain.PlayerBetState
import pt.isel.pdm.domain.PlayerMatchState
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.domain.Round
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.domain.DiceFace
import kotlin.time.Duration.Companion.seconds

class RepositoryMatchMock : RepositoryMatch {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val shFlow: MutableSharedFlow<OutCome<MatchResponse, MatchError>> = MutableSharedFlow()

    init {
        scope.launch {

            val dummyHand = DicesHand(
                dices = persistentListOf(
                    DiceFace.ACE,
                    DiceFace.KING,
                    DiceFace.ACE,
                    DiceFace.TEN,
                    DiceFace.NINE
                )
            )

            val p1RoundState = PlayerRoundState(
                playerId = 101,
                coins = 90,
                playerStatus = PlayerStatus.StillRolling(
                    hand = dummyHand,
                    remainingRolls = 1
                )
            )
            val p2RoundState = PlayerRoundState(102, 90, PlayerStatus.NotStarted)
            val p3RoundState = PlayerRoundState(103, 90, PlayerStatus.NotStarted)
            val p4RoundState = PlayerRoundState(104, 90, PlayerStatus.NotStarted)

            val roundPlayers = listOf(p1RoundState, p2RoundState, p3RoundState, p4RoundState)
            val currentRound = Round(
                id = 1,
                players = roundPlayers,
                ante = 10,
                totalBet = 40,
                state = RoundState.Rolling(turn = p2RoundState)
            )

            val matchPlayers = listOf(
                PlayerMatchState(101, 90),
                PlayerMatchState(102, 90),
                PlayerMatchState(103, 90),
                PlayerMatchState(104, 90)
            )

            val dummyMatch = Match(
                id = 500,
                players = matchPlayers,
                owner = 101,
                actualRound = currentRound,
                initialCoins = 100,
                remainingRounds = 5,
                matchStatus = MatchStatus.ELAPSED
            )

            while (true){
                shFlow.emit(Success(MatchResponse.NewMatch(dummyMatch)))
                delay(5.seconds)
            }
        }
    }
    private val actualMatchId: MutableStateFlow<Int?> = MutableStateFlow(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    private val sseListener: SharedFlow<OutCome<MatchResponse, MatchError>> =
        actualMatchId.filterNotNull().flatMapLatest { id ->
            createSseFlow(id)
        }.shareIn(
            scope,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
            replay = 0
        )

    private val matchFlow: Flow<OutCome<MatchResponse, MatchError>> = flow {
        shFlow.collect {
            emit(it)
        }
    }


    private fun createSseFlow(matchId: Int): SharedFlow<OutCome<MatchResponse, MatchError>> {
        return callbackFlow {
            launch {
                matchFlow.collect {
                    send(it)
                }
            }
            awaitClose {
                actualMatchId.compareAndSet(matchId,null)
            }
        }.shareIn(
            scope,
            started = SharingStarted.WhileSubscribed(),
            replay = 0
        )
    }

    override fun matchSseListener(matchId: Int): SharedFlow<OutCome<MatchResponse, MatchError>> {
        actualMatchId.value = matchId
        return sseListener
    }

    override suspend fun play(command: PlayCommand): OutCome<Match, MatchError> {
        val fakeMatch = Match(
            id = 1,
            players = listOf(
                PlayerMatchState(playerId = 10, coins = 100),
                PlayerMatchState(playerId = 20, coins = 120)
            ),
            owner = 10,
            actualRound = Round(
                id = 1,
                players = listOf(
                    PlayerRoundState(
                        playerId = 10,
                        coins = 100,
                        playerStatus = PlayerStatus.StillRolling(
                            hand = DicesHand(
                                listOf(
                                    DiceFace.NINE,
                                    DiceFace.TEN,
                                    DiceFace.JACK,
                                    DiceFace.QUEEN,
                                    DiceFace.KING
                                ).toImmutableList()
                            ),
                            remainingRolls = 1
                        )
                    ),
                    PlayerRoundState(
                        playerId = 20,
                        coins = 120,
                        playerStatus = PlayerStatus.FinalHand(
                            hand = DicesHand(
                                listOf(
                                    DiceFace.NINE,
                                    DiceFace.TEN,
                                    DiceFace.JACK,
                                    DiceFace.QUEEN,
                                    DiceFace.KING
                                ).toImmutableList()
                            )
                        )
                    )
                ),
                ante = 0,
                totalBet = 10,
                state = RoundState.Betting(
                    turn = PlayerRoundState(
                        playerId = 10,
                        coins = 100,
                        playerStatus = PlayerStatus.StillRolling(
                            hand =DicesHand(
                                listOf(
                                    DiceFace.NINE,
                                    DiceFace.TEN,
                                    DiceFace.JACK,
                                    DiceFace.QUEEN,
                                    DiceFace.KING
                                ).toImmutableList()
                            ),
                            remainingRolls = 1
                        )
                    ),
                    amount = 10,
                    playersBets = listOf(
                        PlayerBetState(playerId = 10, betState = BetState.PENDING),
                        PlayerBetState(playerId = 20, betState = BetState.CALL)
                    )
                )
            ),
            initialCoins = 100,
            remainingRounds = 3,
            matchStatus = MatchStatus.ELAPSED
        )
        return Success(fakeMatch)
    }

    override suspend fun leaveMatch(match: Match): OutCome<Match, MatchError> {
        scope.launch {
            val updatedPlayers = match.players.drop(1)
            if (updatedPlayers.isEmpty()) {
                shFlow.emit(Success(MatchResponse.MatchEnded))
            }
        }
        return Success(match)
    }

}