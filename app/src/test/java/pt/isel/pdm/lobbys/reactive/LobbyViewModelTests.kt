package pt.isel.pdm.lobbys.reactive

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pt.isel.pdm.SuspendingLatch
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.state.LobbyScreenState
import pt.isel.pdm.lobby.repository.RepositoryLobbiesMock
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.lobby.viewmodel.LobbyViewModel
import pt.isel.pdm.user.services.UsersServiceMock

@OptIn(ExperimentalCoroutinesApi::class)
class LobbyViewModelTests {

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
}