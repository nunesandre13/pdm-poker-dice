package pt.isel.pdm.match.reactive

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pt.isel.pdm.domain.*
import pt.isel.pdm.domain.state.PlayerRoundStateWithName
import pt.isel.pdm.domain.state.Round
import pt.isel.pdm.match.viewModels.interfaces.RollingActions
import pt.isel.pdm.match.viewModels.interfaces.RoundStateProvider
import pt.isel.pdm.match.viewModels.myTurn.*
import pt.isel.pdm.utils.ViewModelBase

@OptIn(ExperimentalCoroutinesApi::class)
class MyTurnViewModelTest {

    private val myPlayerId = PlayerId(10)
    private val opponentId = PlayerId(20)

    // Round inicial
    private val rollingRoundStart = Round(
        id = RoundId(1),
        ante = 10,
        totalBet = 20,
        players = listOf(
            PlayerRoundStateWithName(
                playerId = myPlayerId,
                name = Name("Me"),
                coins = 100,
                playerStatus = PlayerStatus.NotStarted
            ),
            PlayerRoundStateWithName(
                playerId = opponentId,
                name = Name("Opponent"),
                coins = 120,
                playerStatus = PlayerStatus.FinalHand(DicesHand(emptyList<DiceFace>().toImmutableList()))
            )
        ).toImmutableList(),
        state = RoundState.Rolling(
            turn = PlayerRoundState(
                playerId = myPlayerId,
                coins = 100,
                playerStatus = PlayerStatus.NotStarted
            )
        )
    )

    // Round a meio: já rolei, tenho dados e só me resta 1 roll
    private val rollingRoundMid = rollingRoundStart.copy(
        players = listOf(
            PlayerRoundStateWithName(
                playerId = myPlayerId,
                name = Name("Me"),
                coins = 100,
                playerStatus =PlayerStatus.StillRolling(
                    hand = DicesHand(
                        dices = listOf(DiceFace.ACE, DiceFace.ACE, DiceFace.KING, DiceFace.KING, DiceFace.QUEEN).toImmutableList()
                    ),
                    remainingRolls = 1
                )
            ),
            PlayerRoundStateWithName(
                playerId = opponentId,
                name = Name("Opponent"),
                coins = 120,
                playerStatus = PlayerStatus.FinalHand(DicesHand(emptyList<DiceFace>().toImmutableList()))
            )
        ).toImmutableList(),
        state = RoundState.Rolling(
            turn = PlayerRoundState(
                playerId = myPlayerId,
                coins = 100,
                playerStatus = PlayerStatus.StillRolling(
                    hand = DicesHand(
                        dices = listOf(DiceFace.ACE, DiceFace.ACE, DiceFace.KING, DiceFace.KING, DiceFace.QUEEN).toImmutableList()
                    ),
                    remainingRolls = 1
                )
            )
        )
    )

    // Round finalizado: já deu setHand a mão
    private val rollingRoundFinishedHand = rollingRoundStart.copy(
        players = listOf(
            PlayerRoundStateWithName(
                playerId = myPlayerId,
                name = Name("Me"),
                coins = 100,
                playerStatus = PlayerStatus.FinalHand(
                    hand = DicesHand(
                        dices = listOf(DiceFace.ACE, DiceFace.ACE, DiceFace.ACE, DiceFace.KING, DiceFace.KING).toImmutableList()
                    )
                )
            ),
            PlayerRoundStateWithName(
                playerId = opponentId,
                name = Name("Opponent"),
                coins = 120,
                playerStatus = PlayerStatus.FinalHand(DicesHand(emptyList<DiceFace>().toImmutableList()))
            )
        ).toImmutableList(),
        state = RoundState.Rolling(
            turn = PlayerRoundState(
                playerId = myPlayerId,
                coins = 100,
                playerStatus = PlayerStatus.FinalHand(
                    hand = DicesHand(
                        dices = listOf(DiceFace.ACE, DiceFace.ACE, DiceFace.ACE, DiceFace.KING, DiceFace.KING).toImmutableList()
                    )
                )
            )
        )
    )

    private fun createSut(roundFlow: MutableStateFlow<Round?> = MutableStateFlow(null)): MyTurnViewModel {
        val stateProvider = object : RoundStateProvider {
            override val roundState = roundFlow
            override val player = MutableStateFlow(User(UserId(10), Name("Me"), Email("me@test.com")))
        }

        val actions = object : RollingActions {
            override suspend fun rollDice(dices: List<DiceFace>): Boolean {
                delay(10)
                return true
            }
            override suspend fun setHand(): Boolean {
                delay(10)
                return true
            }
            override suspend fun raiseAnte(ante: Int): Boolean {
                delay(10)
                return true
            }
        }

        return MyTurnViewModel(
            baseViewModel = ViewModelBase(MyTurnUiState.InitialLoading, MyTurnError.SomeError),
            stateProvider = stateProvider,
            actions = actions
        )
    }

    @Test
    fun `starts in InitialLoading and goes to Idle with 3 rolls left when round starts`() = runTest {
        val idleDeferred = CompletableDeferred<MyTurnUiState.Idle>()

        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is MyTurnUiState.Idle) {
                    idleDeferred.complete(state)
                }
            }
        }

        roundFlow.value = rollingRoundStart

        val state = idleDeferred.await()
        assertEquals(3, state.data.rollsLeft)
        assertEquals(rollingRoundStart, state.round)

        job.cancel()
    }

    @Test
    fun `shows correct dice and remaining rolls when in mid-roll`() = runTest {
        val idleDeferred = CompletableDeferred<MyTurnUiState.Idle>()

        val roundFlow = MutableStateFlow<Round?>(rollingRoundMid)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is MyTurnUiState.Idle) {
                    idleDeferred.complete(state)
                }
            }
        }

        val state = idleDeferred.await()
        assertEquals(1, state.data.rollsLeft)
        assertEquals(
            listOf(DiceFace.ACE, DiceFace.ACE, DiceFace.KING, DiceFace.KING, DiceFace.QUEEN),
            state.data.currentDice
        )

        job.cancel()
    }

    @Test
    fun `shows 0 rolls left when hand is finalized`() = runTest {
        val idleDeferred = CompletableDeferred<MyTurnUiState.Idle>()

        val roundFlow = MutableStateFlow<Round?>(rollingRoundFinishedHand)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is MyTurnUiState.Idle) {
                    idleDeferred.complete(state)
                }
            }
        }

        val state = idleDeferred.await()
        assertEquals(0, state.data.rollsLeft)
        assertEquals(
            listOf(DiceFace.ACE, DiceFace.ACE, DiceFace.ACE, DiceFace.KING, DiceFace.KING),
            state.data.currentDice
        )

        job.cancel()
    }
    @Test
    fun `setHand changes to SettingHand then back to Idle on success`() = runTest {
        val settingDeferred = CompletableDeferred<Unit>()
        val idleAfterDeferred = CompletableDeferred<Unit>()

        // Começa sem round → InitialLoading
        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is MyTurnUiState.SettingHand) {
                    settingDeferred.complete(Unit)
                }
                if (state is MyTurnUiState.Idle && settingDeferred.isCompleted) {
                    idleAfterDeferred.complete(Unit)
                }
            }
        }

        // Ativa o round → vai para Idle (primeiro)
        roundFlow.value = rollingRoundMid
        advanceUntilIdle() // Garante que o primeiro Idle foi emitido e processado

        // Agora chama a ação
        sut.setHand()

        settingDeferred.await()  // Deve entrar em SettingHand
        idleAfterDeferred.await() // Deve voltar a Idle após o finally

        job.cancel()
    }

    @Test
    fun `raiseAnte changes to RaisingAnte then back to Idle on success`() = runTest {
        val raisingDeferred = CompletableDeferred<Unit>()
        val idleAfterDeferred = CompletableDeferred<Unit>()

        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is MyTurnUiState.RaisingAnte) {
                    raisingDeferred.complete(Unit)
                }
                if (state is MyTurnUiState.Idle && raisingDeferred.isCompleted) {
                    idleAfterDeferred.complete(Unit)
                }
            }
        }

        roundFlow.value = rollingRoundMid
        advanceUntilIdle() //anda todo o tempo virtual do teste necessário para processar o Idle inicial

        sut.raiseAnte(30)

        raisingDeferred.await()
        idleAfterDeferred.await()

        job.cancel()
    }


    @Test
    fun `rollDice does nothing if no rolls left`() = runTest {
        val roundFlow = MutableStateFlow<Round?>(rollingRoundFinishedHand) // rollsLeft = 0
        val sut = createSut(roundFlow)

        advanceUntilIdle() // Processa o Idle inicial

        val initialState = sut.stateUi.value as MyTurnUiState.Idle
        assertFalse(sut.starRollingAnimation)

        val dices = listOf(DiceFace.ACE, DiceFace.ACE, DiceFace.ACE, DiceFace.ACE, DiceFace.ACE)
        sut.rollDice(dices)

        // Nada deve mudar
        assertFalse(sut.starRollingAnimation)
        assertTrue(sut.stateUi.value is MyTurnUiState.Idle)
        assertEquals(initialState.data.rollsLeft, (sut.stateUi.value as MyTurnUiState.Idle).data.rollsLeft)
    }
}