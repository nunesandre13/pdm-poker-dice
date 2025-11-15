package pt.isel.pdm.match.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
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

class RepositoryMatchMock : RepositoryMatch {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val shFlow: MutableSharedFlow<MatchResponse> = MutableSharedFlow()

    private val matchFlow: Flow<MatchResponse> = flow {
        shFlow.collect {
            emit(it)
        }
    }

    override val matchSseListener: SharedFlow<MatchResponse> = callbackFlow {
        launch {
            matchFlow.collect {
                send(it)
            }
        }
        awaitClose {
            // do something
        }
    }.shareIn(
        scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 0
    )



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
                                )
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
                                )
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
                                )
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
                shFlow.emit(MatchResponse.MatchEnded)
            }
        }
        return Success(match)
    }


}