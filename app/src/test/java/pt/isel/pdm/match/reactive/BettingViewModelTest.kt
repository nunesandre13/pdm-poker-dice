package pt.isel.pdm.match.reactive

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pt.isel.pdm.domain.*
import pt.isel.pdm.domain.state.PlayerRoundStateWithName
import pt.isel.pdm.domain.state.Round
import pt.isel.pdm.match.viewModels.betting.*
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.match.viewModels.interfaces.RoundStateProvider
import pt.isel.pdm.utils.ViewModelBase


@OptIn(ExperimentalCoroutinesApi::class)
class BettingViewModelTest {

    private val myPlayerId = PlayerId(10)
    private val opponentId = PlayerId(20)

    // Round em fase de Betting onde eu ainda não apostei (PENDING)
    private val bettingRoundPending = Round(
        id = RoundId(1),
        ante = 10,
        totalBet = 20,
        players = listOf(
            PlayerRoundStateWithName(
                playerId = myPlayerId,
                name = Name("Me"),
                coins = 100,
                playerStatus = PlayerStatus.FinalHand(DicesHand(emptyList<DiceFace>().toImmutableList())) // irrelevante aqui
            ),
            PlayerRoundStateWithName(
                playerId = opponentId,
                name = Name("Opponent"),
                coins = 120,
                playerStatus = PlayerStatus.FinalHand(DicesHand(emptyList<DiceFace>().toImmutableList()))
            )
        ).toImmutableList(),
        state = RoundState.Betting(
            turn = PlayerRoundState(myPlayerId, 100),
            amount = 10,
            playersBets = listOf(
                PlayerBetState(myPlayerId, BetState.PENDING),
                PlayerBetState(opponentId, BetState.CALL)
            ).toImmutableList()
        )
    )

    // Round em fase de Betting onde eu já apostei (CALL)
    private val bettingRoundAlreadyBet = bettingRoundPending.copy(
        state = RoundState.Betting(
            turn = PlayerRoundState(myPlayerId, 100),
            amount = 10,
            playersBets = listOf(
                PlayerBetState(myPlayerId, BetState.CALL),
                PlayerBetState(opponentId, BetState.CALL)
            ).toImmutableList()
        )
    )

    private fun createSut(roundFlow: MutableStateFlow<Round?> = MutableStateFlow(null)): BettingViewModel {
        val stateProvider = object : RoundStateProvider {
            override val roundState = roundFlow
            override val player = MutableStateFlow(User(UserId(10), Name("Me"), Email("me@test.com")))
        }

        val actions = object : BettingActions {
            override suspend fun raiseAnte(ante: Int): Boolean {
                return true
            }

            override suspend fun passTurn(): Boolean {
                return true
            }
            override suspend fun call(): Boolean = true
            override suspend fun fold(): Boolean = true
        }

        return BettingViewModel(
            baseViewModel = ViewModelBase(BettingUiState.InitialLoading, BettingError.SomeError),
            stateProvider = stateProvider,
            actions = actions
        )
    }

    @Test
    fun `starts in InitialLoading`() = runTest {
        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        assertTrue(sut.stateUi.value is BettingUiState.InitialLoading)
    }

    @Test
    fun `goes to AwaitingBetting when round is Betting and player has not bet yet`() = runTest {
        val awaitingDeferred = CompletableDeferred<BettingUiState.AwaitingBetting>()

        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is BettingUiState.AwaitingBetting) {
                    awaitingDeferred.complete(state)
                }
            }
        }

        roundFlow.value = bettingRoundPending
        advanceUntilIdle()

        val state = awaitingDeferred.await()
        assertEquals(bettingRoundPending, state.round)

        job.cancel()
    }

    @Test
    fun `goes directly to BettingDone when player has already bet`() = runTest {
        val doneDeferred = CompletableDeferred<BettingUiState.BettingDone>()

        val roundFlow = MutableStateFlow<Round?>(bettingRoundAlreadyBet)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is BettingUiState.BettingDone) {
                    doneDeferred.complete(state)
                }
            }
        }

        advanceUntilIdle()

        val state = doneDeferred.await()
        assertEquals(bettingRoundAlreadyBet, state.round)

        job.cancel()
    }

    @Test
    fun `call transitions from AwaitingBetting to Betting then BettingDone on success`() = runTest {
        val bettingDeferred = CompletableDeferred<Unit>()
        val doneDeferred = CompletableDeferred<Unit>()

        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is BettingUiState.Betting) bettingDeferred.complete(Unit)
                if (state is BettingUiState.BettingDone && bettingDeferred.isCompleted) doneDeferred.complete(Unit)
            }
        }

        roundFlow.value = bettingRoundPending
        advanceUntilIdle() // Vai para AwaitingBetting

        sut.call()

        bettingDeferred.await() // PlacingBet → Betting
        doneDeferred.await()    // finally → BettingDone

        job.cancel()
    }


    @Test
    fun `second call or fold is ignored due to compareAndSet protection`() = runTest {
        val roundFlow = MutableStateFlow<Round?>(bettingRoundPending)
        val sut = createSut(roundFlow)
        advanceUntilIdle() // AwaitingBetting

        sut.call() // primeira vez → OK (PlacingBet → BettingDone)
        advanceUntilIdle()

        // Segunda chamada deve ser ignorada
        val currentState = sut.stateUi.value
        sut.call()
        advanceUntilIdle()

        assertEquals(currentState, sut.stateUi.value) // estado não mudou
    }

    @Test
    fun `call and fold do nothing in InitialLoading`() = runTest {
        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        sut.call()
        sut.fold()
        advanceUntilIdle()

        assertTrue(sut.stateUi.value is BettingUiState.InitialLoading)
    }
}