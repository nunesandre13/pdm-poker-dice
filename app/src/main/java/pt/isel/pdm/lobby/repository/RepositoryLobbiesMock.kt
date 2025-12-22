package pt.isel.pdm.lobby.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.LobbyId
import pt.isel.pdm.domain.LobbyStatus
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.PlayerInfo
import pt.isel.pdm.domain.events.LobbyResponse
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success

class RepositoryLobbiesMock(
    private val shouldFail: Boolean = false
): RepositoryLobbies {
    private val scope = CoroutineScope(Dispatchers.Default)


    private val shFlow = MutableSharedFlow<LobbyResponse>(replay = 1)

    // Mudança 2: Expõe diretamente o fluxo sem callbackFlow ou filtros extras
    override val lobbySseListener: SharedFlow<LobbyResponse> = shFlow


    override suspend fun createNewLobby(lobby: LobbyCreation): OutCome<Lobby, LobbyError> {
        if (shouldFail) return Failure(LobbyError.NetWorkError)
        val newLobby = Lobby(
            id = LobbyId(123),
            name = lobby.name,
            maxPlayer = lobby.maxPlayer,
            minPlayer = lobby.minPlayer,
            description = "Lobby mock",
            owner = UserId(1),
            numberOdRounds = 3,
            firstAnte = 10,
            matchId = "match-123",
            players = listOf(PlayerInfo(PlayerId(1), Name("Host"))),
            lobbyStatus = LobbyStatus.OPEN
        )
        return Success(newLobby)
    }

    override suspend fun joinLobby(lobby: Lobby): OutCome<Lobby, LobbyError> {
        val updatedLobby = lobby.copy(players = lobby.players + PlayerInfo(PlayerId(2), Name("Guest")))
        shFlow.emit(LobbyResponse.UpdatedLobby(updatedLobby))
        return Success(updatedLobby)
    }

    override suspend fun leaveLobby(lobby: Lobby): OutCome<Unit, LobbyError> {
        scope.launch {
            val updatedPlayers = lobby.players.drop(1)
            if (updatedPlayers.isEmpty()) {
                shFlow.emit(LobbyResponse.RemovedLobby(lobby))
            } else {
                val updatedLobby = lobby.copy(players = updatedPlayers)
                shFlow.emit(LobbyResponse.UpdatedLobby(updatedLobby))
            }
        }
        return Success(Unit)
    }
}
