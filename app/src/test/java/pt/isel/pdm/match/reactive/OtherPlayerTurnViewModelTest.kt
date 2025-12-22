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
import pt.isel.pdm.match.viewModels.interfaces.RoundStateProvider
import pt.isel.pdm.match.viewModels.otherPlayers.*
import pt.isel.pdm.utils.ViewModelBase


@OptIn(ExperimentalCoroutinesApi::class)
class OtherPlayerTurnViewModelTest {

    private val opponentId = PlayerId(20)
    private val myId = PlayerId(10)

    // Um round qualquer onde é a vez do adversário rolar
    private val sampleRound = Round(
        id = RoundId(1),
        ante = 10,
        totalBet = 30,
        players = listOf(
            PlayerRoundStateWithName(
                playerId = myId,
                name = Name("Me"),
                coins = 100,
                playerStatus = PlayerStatus.FinalHand(DicesHand(emptyList<DiceFace>().toImmutableList()))
            ),
            PlayerRoundStateWithName(
                playerId = opponentId,
                name = Name("Opponent"),
                coins = 120,
                playerStatus = PlayerStatus.StillRolling(
                    hand = DicesHand(listOf(DiceFace.KING, DiceFace.QUEEN).toImmutableList()),
                    remainingRolls = 2
                )
            )
        ).toImmutableList(),
        state = RoundState.Rolling(
            turn = PlayerRoundState(
                playerId = opponentId,
                coins = 120,
                playerStatus = PlayerStatus.StillRolling(
                    hand = DicesHand(listOf(DiceFace.KING, DiceFace.QUEEN).toImmutableList()),
                    remainingRolls = 2
                )
            )
        )
    )

    private fun createSut(roundFlow: MutableStateFlow<Round?> = MutableStateFlow(null)): OtherPlayerTurnViewModel {
        val stateProvider = object : RoundStateProvider {
            override val roundState = roundFlow
            override val player = MutableStateFlow(User(UserId(10), Name("Me"), Email("teste@email")))
        }

        return OtherPlayerTurnViewModel(
            stateProvider = stateProvider,
            baseViewModel = ViewModelBase(OtherPlayerTurnUiState.Loading, OtherPlayerTurnError.SomeError)
        )
    }

    @Test
    fun `starts in Loading state`() = runTest {
        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        assertTrue(sut.stateUi.value is OtherPlayerTurnUiState.Loading)
    }

    @Test
    fun `transitions to ShowingTurn when a round is emitted`() = runTest {
        val showingDeferred = CompletableDeferred<OtherPlayerTurnUiState.ShowingTurn>()

        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is OtherPlayerTurnUiState.ShowingTurn) {
                    showingDeferred.complete(state)
                }
            }
        }

        roundFlow.value = sampleRound
        advanceUntilIdle()

        val emittedState = showingDeferred.await()
        assertEquals(sampleRound, emittedState.round)

        job.cancel()
    }

    @Test
    fun `updates ShowingTurn when round changes`() = runTest {
        val firstDeferred = CompletableDeferred<Round>()
        val secondDeferred = CompletableDeferred<Round>()

        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is OtherPlayerTurnUiState.ShowingTurn) {
                    if (!firstDeferred.isCompleted) {
                        firstDeferred.complete(state.round)
                    } else if (!secondDeferred.isCompleted) {
                        secondDeferred.complete(state.round)
                    }
                }
            }
        }

        // Primeiro round
        roundFlow.value = sampleRound
        advanceUntilIdle()
        assertEquals(sampleRound, firstDeferred.await())

        // Segundo round (ex: adversário rolou de novo)
        val updatedRound = sampleRound.copy(
            state = RoundState.Rolling(
                turn = PlayerRoundState(
                    playerId = opponentId,
                    coins = 120,
                    playerStatus = PlayerStatus.StillRolling(
                        hand = DicesHand(listOf(DiceFace.ACE, DiceFace.ACE).toImmutableList()),
                        remainingRolls = 1
                    )
                )
            )
        )
        roundFlow.value = updatedRound
        advanceUntilIdle()

        assertEquals(updatedRound, secondDeferred.await())

        job.cancel()
    }

    @Test
    fun `remains in Loading if roundState is null`() = runTest {
        val roundFlow = MutableStateFlow<Round?>(null)
        val sut = createSut(roundFlow)

        advanceUntilIdle()

        assertTrue(sut.stateUi.value is OtherPlayerTurnUiState.Loading)

        roundFlow.value = null
        advanceUntilIdle()

        assertTrue(sut.stateUi.value is OtherPlayerTurnUiState.Loading)
    }
}