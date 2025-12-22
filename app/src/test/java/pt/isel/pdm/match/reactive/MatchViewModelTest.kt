package pt.isel.pdm.match.reactive

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pt.isel.pdm.SuspendingLatch
import pt.isel.pdm.domain.BetState
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.RawMatch
import pt.isel.pdm.domain.MatchId
import pt.isel.pdm.domain.MatchStatus
import pt.isel.pdm.domain.PlayerBetState
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.PlayerMatchState
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.domain.RawRound
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.match.repository.RepositoryMatchMock
import pt.isel.pdm.match.services.MatchServiceImp
import pt.isel.pdm.match.viewModels.InnerRoute
import pt.isel.pdm.match.viewModels.MatchGlobalStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.user.services.UsersServiceMock

class MatchViewModelTest {
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

    @Test
    fun `MatchViewModel updates state to Finished when SSE receives MatchEnded`() = runTest {
        val matchRepoMock = RepositoryMatchMock()
        val matchService = MatchServiceImp(matchRepoMock)
        val userMock = UsersServiceMock()

        val sut = MatchViewModel.factory(matchService, userMock, matchId = 123)
            .create(MatchViewModel::class.java)

        val latchFinished = SuspendingLatch()

        //para simular a UI a ouvir o estado global
        val job = launch {
            sut.stateUi.collect { state ->
                if (state is MatchGlobalStateUi.Finished)
                    latchFinished.open()
            }
        }

        // Simular evento de fim de jogo vindo do Mock
        matchRepoMock.emitMatchEvent(MatchResponse.MatchEnded)

        // O teste fica parado aqui até o latch.open() ser chamado ou o tempo expirar
        latchFinished.await()
        job.cancel()

        // Verifica se o estado final guardado no ViewModel é de facto Finished
        assert(sut.stateUi.value is MatchGlobalStateUi.Finished)
    }


    //Garante que o ViewModel deteta que é a nossa vez de apostar.
    @Test
    fun `MatchViewModel transitions to MyTurnState when round state is Betting and is my turn`() = runTest {

        val matchRepoMock = RepositoryMatchMock()
        val matchService = MatchServiceImp(matchRepoMock)
        val userMock = UsersServiceMock()

        val sut = MatchViewModel.factory(matchService, userMock, matchId = 1)
            .create(MatchViewModel::class.java)

        val latchMyTurn = SuspendingLatch()

        // Observamos a 'innerNavigation' que controla que sub-ecrã mostrar dentro do jogo
        val job = launch {
            sut.innerNavigation.collect { route ->
                if (route is InnerRoute.BettingState) {
                    latchMyTurn.open()
                }
            }
        }
        // Simulamos a receção de um Match NOVO (NewMatch) com os dados do fakeMatch
        // O fakeMatch está em estado RoundState.Betting e o turno é do ID 10
        matchRepoMock.emitMatchEvent(MatchResponse.NewMatch(fakeMatch))
        latchMyTurn.await()
        // Confirma que a navegação interna está no estado em betting
        assert(sut.innerNavigation.value is InnerRoute.BettingState)
    }

    @Test
    fun `MatchViewModel transitions to BettingState when round is in betting phase`() = runTest {
        val matchRepoMock = RepositoryMatchMock()
        val matchService = MatchServiceImp(matchRepoMock)
        val userMock = UsersServiceMock()

        val sut = MatchViewModel.factory(matchService, userMock, matchId = 1)
            .create(MatchViewModel::class.java)

        val latch = SuspendingLatch()
        var currentRoute: InnerRoute? = null

        val job = launch {
            sut.innerNavigation.collect { route ->
                currentRoute = route
                if (route is InnerRoute.BettingState) latch.open()
            }
        }

        matchRepoMock.emitMatchEvent(MatchResponse.NewMatch(fakeMatch))

        latch.await()
        job.cancel()

        assert(currentRoute is InnerRoute.BettingState)
    }

    @Test
    fun `innerNavigation stays Idle until both Player and Match are available`() = runTest {
        val matchRepoMock = RepositoryMatchMock()
        val matchService = MatchServiceImp(matchRepoMock)
        val userMock = UsersServiceMock()

        val sut = MatchViewModel.factory(matchService, userMock, matchId = 1)
            .create(MatchViewModel::class.java)

        // Inicialmente deve ser Idle
        assert(sut.innerNavigation.value is InnerRoute.Idle)

        // Emitimos o Match, mas se o User ainda for null no combine, deve continuar Idle
        matchRepoMock.emitMatchEvent(MatchResponse.NewMatch(fakeMatch))

        assert(sut.innerNavigation.value is InnerRoute.Idle)
    }

}