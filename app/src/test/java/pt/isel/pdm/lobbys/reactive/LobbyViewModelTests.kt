package pt.isel.pdm.lobbys.reactive

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pt.isel.pdm.SuspendingLatch
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.domain.state.LobbyScreenState
import pt.isel.pdm.lobby.repository.RepositoryLobbiesMock
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.lobby.viewmodel.LobbyViewModel
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success

@OptIn(ExperimentalCoroutinesApi::class)
class LobbyViewModelTests {

    val lobbyList = listOf<Lobby>()

    @Test
    fun `JoinedLobby flow updates automatically when a new player joins via SSE`() = runTest {
        val lobbyRepoMock = RepositoryLobbiesMock()
        val userMock = UsersServiceMock()
        val lobbyService = LobbyServiceImp(lobbyRepoMock, userMock)
        val sut = LobbyViewModel.getFactory(lobbyService, userMock).create(LobbyViewModel::class.java)

        val latchJoined = SuspendingLatch()
        val latchPlayerAdded = SuspendingLatch()
        var playersCount = 0

        val collectorJob = launch {
            sut.stateUi.collect { state ->
                if (state is LobbyScreenState.JoinedLobby) {
                    latchJoined.open()
                    // Observar o StateFlow INTERNO do lobby
                    state.lobby.collect { lobbyData ->
                        playersCount = lobbyData.players.size
                        if (playersCount == 2) latchPlayerAdded.open()
                    }
                }
            }
        }

        sut.createLobby(LobbyCreation("Live", "Desc", 2, 4, 3, 10))
        latchJoined.await()

        // Simular evento vindo do "servidor" (Mock)
        val currentLobby = (sut.stateUi.value as LobbyScreenState.JoinedLobby).lobby.value
        lobbyRepoMock.joinLobby(currentLobby)

        latchPlayerAdded.await()
        collectorJob.cancel()
        assert(playersCount == 2)
    }

    @Test
    fun `createLobby changes state to JoinedLobby and contains the new lobby`() = runTest {
        val userMock = UsersServiceMock()
        val lobbyRepoMock = RepositoryLobbiesMock()
        val lobbyService = LobbyServiceImp(lobbyRepoMock, userMock)

        val sut = LobbyViewModel.getFactory(lobbyService, userMock)
            .create(LobbyViewModel::class.java)

        val latch = SuspendingLatch()
        var finalState: LobbyScreenState? = null

        //courotina que fica a observar o stateUi
        val collectorJob = launch {
            sut.stateUi.collect { state ->
                finalState = state
                if (state is LobbyScreenState.JoinedLobby) {
                    latch.open()
                }
            }
        }


        val lobbyCreation = LobbyCreation(
            name = "Test Lobby",
            description = "Desc",
            minPlayer = 2,
            maxPlayer = 4,
            numberOfRounds = 3,
            firstAnte = 10
        )

        sut.createLobby(lobbyCreation)

        //teste fica parado no await() até que o coletor encontre o estado JoinedLobby e chame latch.open().
        latch.await()
        collectorJob.cancel()


        assert(finalState is LobbyScreenState.JoinedLobby)
        val joinedState = finalState as LobbyScreenState.JoinedLobby
        val lobbyData = joinedState.lobby.value // Aceder ao StateFlow interno
        assert(lobbyData.name == "Test Lobby")
    }

    @Test
    fun `goToCreation changes state to Creation`() = runTest {
        val userMock = UsersServiceMock()
        val lobbyService = LobbyServiceImp(RepositoryLobbiesMock(), userMock)
        val sut = LobbyViewModel.Companion.getFactory(lobbyService, userMock)
            .create(LobbyViewModel::class.java)

        val latch = SuspendingLatch()

        val job = launch {
            sut.stateUi.collect {
                if (it is LobbyScreenState.Creation) latch.open()
            }
        }

        sut.goToCreation()
        latch.await()
        job.cancel()

        assert(sut.stateUi.value is LobbyScreenState.Creation)
    }


    @Test
    fun `leaveLobby changes state back to LobbiesList`() = runTest {
        val lobbyRepoMock = RepositoryLobbiesMock()
        val userMock = UsersServiceMock()
        val lobbyService = LobbyServiceImp(lobbyRepoMock, userMock)
        val sut = LobbyViewModel.getFactory(lobbyService, userMock).create(LobbyViewModel::class.java)

        val latchJoined = SuspendingLatch()
        val latchLeft = SuspendingLatch()
        var joinedLobbyData: Lobby? = null

        val job = launch {
            sut.stateUi.collect { state ->
                if (state is LobbyScreenState.JoinedLobby) {
                    joinedLobbyData = state.lobby.value
                    latchJoined.open() // Notifica que já entramos
                }
                if (state is LobbyScreenState.LobbiesList) {
                    latchLeft.open() // Notifica que já saímos
                }
            }
        }


        sut.createLobby(
            LobbyCreation(
                "ToLeave",
                "Desc",
                2,
                4,
                3,
                10)
        )

        latchJoined.await()

        joinedLobbyData?.let {
            sut.leaveLobby(it)
        }

        latchLeft.await()
        job.cancel()

        assert(sut.stateUi.value is LobbyScreenState.LobbiesList)
    }

    fun getStubService(config: LobbyServiceConfig): LobbyServices {
        return object : LobbyServices {
            override fun listAvailableLobbies(): Flow<List<Lobby>> {
                return config.listAvailableLobbiesFlow
            }
            override suspend fun createNewLobby(lobby: LobbyCreation): OutCome<Pair<Lobby, Flow<Lobby>>, LobbyError> {
                return config.createNewLobbyResult(lobby)
            }

            override suspend fun joinLobby(lobby: Lobby): OutCome<Flow<Lobby>, LobbyError> {
                return config.joinLobbyResult
            }

            override suspend fun leaveLobby(lobby: Lobby, playerId: String): OutCome<Unit, LobbyError> {
                return config.leaveLobbyResult
            }
        }
    }

    data class LobbyServiceConfig(
        val listAvailableLobbiesFlow: MutableStateFlow<List<Lobby>> =  MutableStateFlow(emptyList()),

        var createNewLobbyResult: (LobbyCreation) -> OutCome<Pair<Lobby, Flow<Lobby>>, LobbyError> = { _ ->
            Failure(LobbyError.NoError)
        },

        var joinLobbyResult: OutCome<Flow<Lobby>, LobbyError> = Failure(LobbyError.LobbyNotFound),

        var leaveLobbyResult: OutCome<Unit, LobbyError> = Success(Unit)
    )
}