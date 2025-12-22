package pt.isel.pdm.match.reactive

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Test
import pt.isel.pdm.domain.*
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.domain.state.Round
import pt.isel.pdm.match.viewModels.*
import pt.isel.pdm.match.services.MatchServices
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.*

@OptIn(ExperimentalCoroutinesApi::class)
class MatchViewModelTests {

    private val myUserId = UserId(10)
    private val myPlayerId = PlayerId(10)
    private val opponentPlayerId = PlayerId(20)
    private val matchId = MatchId(1)
    val myDices = listOf(
        DiceFace.ACE,
        DiceFace.ACE,
        DiceFace.KING,
        DiceFace.KING,
        DiceFace.KING
    ).toImmutableList()

    val fakeRound = RawRound(
        id = RoundId(1),
        players = listOf(
            PlayerRoundState(
                myPlayerId,
                90,
                PlayerStatus.StillRolling(DicesHand(myDices), 0)
            ),
            PlayerRoundState(
                opponentPlayerId,
                120,
                PlayerStatus.FinalHand(DicesHand(emptyList<DiceFace>().toImmutableList()))
            )
        ),
        ante = 10,
        totalBet = 20,
        state = RoundState.Betting(
            turn = PlayerRoundState(myPlayerId, 90),
            amount = 10,
            playersBets = listOf(
                PlayerBetState(myPlayerId, BetState.PENDING),
                PlayerBetState(opponentPlayerId, BetState.CALL)
            )
        )
    )
    private val fakeMatch = RawMatch(id = matchId, players = listOf(
            PlayerMatchState(myPlayerId, 100),
            PlayerMatchState(opponentPlayerId, 120)
        ),
        owner = myUserId,
        actualRound = fakeRound,
        initialCoins = 100,
        remainingRounds = 3,
        matchStatus = MatchStatus.ELAPSED
    )


    private val myUser = User(myUserId, Name("Me"), Email("me@test.com"))

    private val playersNameCache = PlayersNameCache().apply {
        cachePlayers(
            listOf(
                PlayerInfo(PlayerId(10), Name("Me")),
                PlayerInfo(PlayerId(20), Name("Opponent"))
            )
        )
    }
    private fun createSut(config: MatchServiceConfig= MatchServiceConfig(), user: User? = myUser): MatchViewModel {
        return MatchViewModel(
            viewModelBase = ViewModelBase(MatchGlobalStateUi.Loading, MatchError.SomeError),
            matchServices = getStubMatchService(config),
            userRepository = getStubUserService(user),
            playersNameCache = playersNameCache,
            matchId = 1
        )
    }

    @Test
    fun `initial state is NoMatch and Loading global state`() = runTest {
        val sut = createSut()
        assertTrue(sut.matchState.value is MatchState.NoMatch)
        assertEquals(MatchGlobalStateUi.Loading, sut.stateUi.value)
    }

    @Test
    fun `when NewMatch arrives, matchState becomes ActualMatch with resolved names`() =
        runTest {

            val matchLoaded = CompletableDeferred<MatchState.ActualMatch>()

            val updatesFlow = MutableStateFlow<OutCome<MatchResponse, MatchError>>(
                Success(MatchResponse.NewMatch(fakeMatch))
            )
            val config = MatchServiceConfig(updatesFlow)
            val sut = createSut(config)

            val job = launch {
                sut.matchState.collect { state ->
                    if (state is MatchState.ActualMatch) {
                        matchLoaded.complete(state)
                    }
                }
            }

            val actualState = withTimeout(5000) {
                matchLoaded.await()
            }

            assertEquals(matchId, actualState.match.id)
            assertEquals(2, actualState.match.players.size)

            val me = actualState.match.players.find { it.playerId.id == 10 }
            val opponent = actualState.match.players.find { it.playerId.id == 20 }

            assertEquals("Me", me?.name?.name)
            assertEquals("Opponent", opponent?.name?.name)

            assertEquals(MatchGlobalStateUi.Elapsed, sut.stateUi.value)
            job.cancel()
        }

    @Test
    fun `when MatchEnded arrives, stateUi becomes Finished after 4s delay`() = runTest {
        val becameElapsed = CompletableDeferred<Unit>()
        val becameFinished = CompletableDeferred<Unit>()

        val updatesFlow = MutableStateFlow<OutCome<MatchResponse, MatchError>>(
            Success(MatchResponse.NewMatch(fakeMatch))
        )
        val sut = createSut(MatchServiceConfig(updatesFlow))

        val job = launch {
            sut.stateUi.collect { state ->
                when (state) {
                    MatchGlobalStateUi.Elapsed -> becameElapsed.complete(Unit)
                    MatchGlobalStateUi.Finished -> becameFinished.complete(Unit)
                    else -> {}
                }
            }
        }

        becameElapsed.await() // Espera ficar Elapsed

        updatesFlow.value = Success(MatchResponse.MatchEnded)

        advanceTimeBy(5000) // Avança > 4s para trigger do delay

        becameFinished.await() // Agora deve estar Finished

        job.cancel()
    }

    @Test
    fun `innerNavigation changes correctly based on round state`() = runTest {
        val bettingDeferred = CompletableDeferred<Unit>()
        val myTurnDeferred = CompletableDeferred<Unit>()
        val opponentTurnDeferred = CompletableDeferred<Unit>()
        val finishedDeferred = CompletableDeferred<Unit>()

        val updatesFlow = MutableStateFlow<OutCome<MatchResponse, MatchError>>(
            Success(MatchResponse.NewMatch(fakeMatch.copy(
                actualRound = fakeRound.copy(state = RoundState.Betting(
                    turn = PlayerRoundState(myPlayerId, 90),
                    amount = 10,
                    playersBets = listOf(
                        PlayerBetState(myPlayerId, BetState.PENDING),
                        PlayerBetState(opponentPlayerId, BetState.CALL)
                    )
                ))
            )))
        )
        val sut = createSut(MatchServiceConfig(updatesFlow))

        val job = launch {
            sut.innerNavigation.collect { route ->
                when (route) {
                    InnerRoute.BettingState -> bettingDeferred.complete(Unit)
                    InnerRoute.MyTurnState -> myTurnDeferred.complete(Unit)
                    InnerRoute.OtherPlayerTurn -> opponentTurnDeferred.complete(Unit)
                    InnerRoute.Finished -> finishedDeferred.complete(Unit)
                    else -> {}
                }
            }
        }

        bettingDeferred.await()

        // Muda para Rolling - minha vez
        updatesFlow.value = Success(MatchResponse.NewMatch(fakeMatch.copy(
            actualRound = fakeRound.copy(state = RoundState.Rolling(turn = PlayerRoundState(myPlayerId, 90)))
        )))
        myTurnDeferred.await()

        // Muda para Rolling - vez do adversário
        updatesFlow.value = Success(MatchResponse.NewMatch(fakeMatch.copy(
            actualRound = fakeRound.copy(state = RoundState.Rolling(turn = PlayerRoundState(opponentPlayerId, 120)))
        )))
        opponentTurnDeferred.await()

        // Round termina
        updatesFlow.value = Success(MatchResponse.NewMatch(fakeMatch.copy(
            actualRound = fakeRound.copy(state = RoundState.Finished(winner = myPlayerId.id))
        )))
        finishedDeferred.await()

        job.cancel()
    }

    @Test
    fun `call returns false and emits error when no match`() = runTest {
        val sut = createSut()
        val result = sut.call()
        assertFalse(result)
        assertTrue(sut.errorState.value is MatchError.InvalidPlay)
    }

    @Test
    fun `roundState reflects the current round after match loads`() = runTest {
        val roundDeferred = CompletableDeferred<Round>()

        val updatesFlow = MutableStateFlow<OutCome<MatchResponse, MatchError>>(
            Success(MatchResponse.NewMatch(fakeMatch))
        )
        val sut = createSut(MatchServiceConfig(updatesFlow))

        val job = launch {
            sut.roundState.collect { round ->
                if (round != null) roundDeferred.complete(round)
            }
        }

        val round = roundDeferred.await()
        assertEquals(RoundId(1), round.id)
        assertTrue(round.state is RoundState.Betting)

        job.cancel()
    }




    class MatchServiceConfig(
        val matchUpdatesFlow: MutableStateFlow<OutCome<MatchResponse, MatchError>> =
            MutableStateFlow(Failure(MatchError.SomeError))
    )

    private fun getStubMatchService(config: MatchServiceConfig) = object : MatchServices {
        override val matchIdState = MutableStateFlow(1)
        override fun getMatchUpdate(matchId: MatchId) = config.matchUpdatesFlow
        override suspend fun rollDice(p: PlayerId, r: RoundId, d: List<DiceFace>) = Success(Unit)
        override suspend fun setHand(p: PlayerId, r: RoundId) = Success(Unit)
        override suspend fun raiseAnte(p: PlayerId, r: RoundId, a: Int) = Success(Unit)
        override suspend fun passTurn(p: PlayerId, r: RoundId) = Success(Unit)
        override suspend fun call(p: PlayerId, r: RoundId) = Success(Unit)
        override suspend fun fold(p: PlayerId, r: RoundId) = Success(Unit)
        override suspend fun leaveMatch(m: RawMatch) = Success(Unit)
    }

    private fun getStubUserService(user: User?) = object : UserServices {
        override val currentUser = MutableStateFlow(user)
        override fun getCurrentUser() = user
        override suspend fun restoreSession() = true
        override suspend fun login(u: pt.isel.pdm.dto.user.UserCreateTokenInputModel) = Failure(pt.isel.pdm.domain.state.UserError.NoError)
        override suspend fun logout() = Success(Unit)
        override suspend fun createUser(u: pt.isel.pdm.dto.user.UserInput, c: InviteCode) = Failure(pt.isel.pdm.domain.state.UserError.NoError)
        override suspend fun inviteCode() = InviteCode("TEST")
    }
}