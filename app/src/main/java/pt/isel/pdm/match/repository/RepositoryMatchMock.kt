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
import pt.isel.pdm.domain.match.BetState
import pt.isel.pdm.domain.match.DicesHand
import pt.isel.pdm.domain.match.RawMatch
import pt.isel.pdm.domain.match.MatchStatus
import pt.isel.pdm.domain.match.PlayCommand
import pt.isel.pdm.domain.match.PlayerBetState
import pt.isel.pdm.domain.match.PlayerMatchState
import pt.isel.pdm.domain.match.PlayerRoundState
import pt.isel.pdm.domain.match.PlayerStatus
import pt.isel.pdm.domain.match.RawRound
import pt.isel.pdm.domain.match.RoundState
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.domain.match.DiceFace
import pt.isel.pdm.domain.MatchId
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.domain.UserId
import kotlin.time.Duration.Companion.seconds

class RepositoryMatchMock : RepositoryMatch {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val shFlow = MutableSharedFlow<OutCome<MatchResponse, MatchError>>(replay = 1)


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

            val dummyHand2 = DicesHand(
                dices = persistentListOf(
                    DiceFace.ACE,
                    DiceFace.KING,
                    DiceFace.ACE,
                    DiceFace.TEN,
                    DiceFace.NINE
                )
            )

            val p1RoundState = PlayerRoundState(
                playerId = PlayerId(101),
                coins = 90,
                playerStatus = PlayerStatus.StillRolling(
                    hand = dummyHand,
                    remainingRolls = 1
                )
            )
            val p2RoundState = PlayerRoundState(PlayerId(102), 90,playerStatus = PlayerStatus.StillRolling(
                hand = dummyHand2,
                remainingRolls = 1
            ))

            val p3RoundState = PlayerRoundState(PlayerId(103), 90, PlayerStatus.NotStarted)
            val p4RoundState = PlayerRoundState(PlayerId(104), 90, PlayerStatus.NotStarted)

            val roundPlayers = listOf(p1RoundState, p2RoundState, p3RoundState, p4RoundState)
            val currentRound = RawRound(
                id = RoundId(1),
                players = roundPlayers,
                ante = 10,
                totalBet = 40,
                state = RoundState.Rolling(turn = p2RoundState)
            )

            val matchPlayers = listOf(
                PlayerMatchState(PlayerId(101), 90),
                PlayerMatchState(PlayerId(102), 90),
                PlayerMatchState(PlayerId(103), 90),
                PlayerMatchState(PlayerId(104), 90)
            )

            val dummyMatch = RawMatch(
                id = MatchId(500),
                players = matchPlayers,
                owner = UserId(101),
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
        return shFlow
    }

    override suspend fun play(command: PlayCommand): OutCome<RawMatch, MatchError> {
        val fakeMatch = RawMatch(
            id = MatchId(1),
            players = listOf(
                PlayerMatchState(playerId = PlayerId(10), coins = 100),
                PlayerMatchState(playerId = PlayerId(20), coins = 120)
            ),
            owner = UserId(10),
            actualRound = RawRound(
                id = RoundId(1),
                players = listOf(
                    PlayerRoundState(
                        playerId = PlayerId(10),
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
                        playerId = PlayerId(20),
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
                        playerId = PlayerId(10),
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
                        PlayerBetState(playerId = PlayerId(10), betState = BetState.PENDING),
                        PlayerBetState(playerId = PlayerId(20), betState = BetState.CALL)
                    )
                )
            ),
            initialCoins = 100,
            remainingRounds = 3,
            matchStatus = MatchStatus.ELAPSED
        )
        return Success(fakeMatch)
    }

    override suspend fun leaveMatch(match: RawMatch): OutCome<RawMatch, MatchError> {
        scope.launch {
            val updatedPlayers = match.players.drop(1)
            if (updatedPlayers.isEmpty()) {
                shFlow.emit(Success(MatchResponse.MatchEnded))
            }
        }
        return Success(match)
    }

    suspend fun emitMatchEvent(event: MatchResponse) {
        shFlow.emit(Success(event))
    }

}