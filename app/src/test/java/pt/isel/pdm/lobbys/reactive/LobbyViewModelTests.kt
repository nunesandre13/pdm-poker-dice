package pt.isel.pdm.lobbys.reactive

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Test
import pt.isel.pdm.SuspendingLatch
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.LobbyStatus
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.domain.state.LobbyScreenState
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.lobby.repository.RepositoryLobbiesMock
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.lobby.viewmodel.LobbyViewModel
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.users.reactive.UserViewModelTests
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.ViewModelBase

@OptIn(ExperimentalCoroutinesApi::class)
class LobbyViewModelTests {

    val players = listOf(
        User("1", Name("Player 1"), Email("teste@email")),
        User("2", Name("Player 2"), Email("teste2@email")),
        User("3", Name("Player 3"), Email("teste3@email"))
    )
    val lobbyList = listOf(
        Lobby("1", "Lobby Beginner", "Ideal for new players", players, "1", 4, 2, 3, 10, null, LobbyStatus.OPEN),
        Lobby("2", "High Stakes", "Only for pros", players.take(2), "2", 8, 2, 5, 50, null, LobbyStatus.OPEN),
        Lobby("3", "Final Table", "Tournament final", players, "3", 3, 2, 10, 100, "match_123", LobbyStatus.IN_GAME)
    )

    private fun createSut(lobbyConfig: LobbyServiceConfig = LobbyServiceConfig(), currentUser: User? = players[0]): LobbyViewModel {
        return LobbyViewModel(
            lobbyService = getStubService(lobbyConfig),
            userService = getStubUserService(currentUser),
            viewModelState = ViewModelBase(LobbyScreenState.Loading, LobbyError.NoError)
        )
    }

    @Test
    fun `leaveLobby success should navigate back to LobbiesList`() = runTest {
        val config = LobbyServiceConfig(leaveLobbyResult = Success(Unit))
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.stateUi.collect {
                // Verifica se o estado mudou para LobbiesList (resultado do leaveLobby com sucesso)
                if (it is LobbyScreenState.LobbiesList) deferred.complete(Unit)
            }
        }

        sut.leaveLobby(lobbyList[0])
        withTimeout(2000) { deferred.await() }
        job.cancel()

        assert(sut.stateUi.value is LobbyScreenState.LobbiesList)
    }

    @Test
    fun `joinLobby success should navigate to JoinedLobby`() = runTest {
        val targetLobby = lobbyList[0]
        val config = LobbyServiceConfig(
            joinLobbyResult = Success(flowOf(targetLobby))
        )
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.stateUi.collect {
                if (it is LobbyScreenState.JoinedLobby) deferred.complete(Unit)
            }
        }

        sut.joinLobby(targetLobby)

        withTimeout(2000) { deferred.await() }
        job.cancel()

        assert(sut.stateUi.value is LobbyScreenState.JoinedLobby)
    }

    @Test
    fun `joinLobby failure should emit LobbyNotFound error`() = runTest {
        val targetLobby = lobbyList[0]
        val config = LobbyServiceConfig(
            joinLobbyResult = Failure(LobbyError.LobbyNotFound)
        )
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.errorState.collect {
                if (it == LobbyError.LobbyNotFound) deferred.complete(Unit)
            }
        }

        sut.joinLobby(targetLobby)

        withTimeout(2000) { deferred.await() }
        job.cancel()

        assert(sut.errorState.value == LobbyError.LobbyNotFound)
    }

    @Test
    fun `createLobby success should update state to JoinedLobby`() = runTest {
        val newLobby = lobbyList[0]
        val config = LobbyServiceConfig()
        config.createNewLobbyResult = {
            Success(Pair(newLobby, flowOf(newLobby)))
        }
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.stateUi.collect {
                if (it is LobbyScreenState.JoinedLobby) deferred.complete(Unit)
            }
        }

        sut.createLobby(LobbyCreation("Test", "Desc", 4, 2, 5, 10))

        deferred.await()
        job.cancel()

        assert(sut.stateUi.value is LobbyScreenState.JoinedLobby)
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

    private fun getStubUserService(user: User?) = object : UserServices {
        override val currentUser = MutableStateFlow(user)
        override fun getCurrentUser(): User? = user
        override suspend fun restoreSession() = true
        override suspend fun login(user: UserCreateTokenInputModel) = Failure(UserError.NoError)
        override suspend fun logout() = Success(Unit)
        override suspend fun createUser(user: UserInput, inviteCode: InviteCode) = Failure(UserError.NoError)
        override suspend fun inviteCode() = InviteCode("ABCDE")
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