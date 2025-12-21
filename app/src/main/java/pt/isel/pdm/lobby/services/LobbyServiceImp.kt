package pt.isel.pdm.lobby.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.LobbyStatus
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.events.LobbyResponse
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.lobby.repository.RepositoryLobbies
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.onOutCome

class LobbyServiceImp(
    private val repository: RepositoryLobbies,
    private val userService: UserServices
) : LobbyServices {

    private fun user() = userService.getCurrentUser()?.id

    private fun Lobby.lobbyUpdate(): Flow<Lobby> {
       return repository.lobbySseListener.filter { response ->
            when (response) {
                is LobbyResponse.UpdatedLobby -> response.lobby.id == id
                is LobbyResponse.LobbyFull -> response.lobby.id == id
                else -> false
            }
        }.map { response ->
            when (response) {
                is LobbyResponse.UpdatedLobby -> response.lobby
                is LobbyResponse.LobbyFull -> response.lobby
                else -> error("Unexpected response type")
            }
        }.onStart { emit(this@lobbyUpdate) }
    }


    override suspend fun joinLobby(lobby: Lobby): OutCome<Flow<Lobby>, LobbyError> {
        return repository.joinLobby(lobby).onOutCome(
            onSuccess = { Success(it.lobbyUpdate()) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun createNewLobby(lobby: LobbyCreation): OutCome<Pair<Lobby,Flow<Lobby>>, LobbyError> {
        return repository.createNewLobby(lobby).onOutCome(
            onSuccess = { Success(it to it.lobbyUpdate()) },
            onFailure = { Failure(it) }
        )
    }

    override fun listAvailableLobbies(): Flow<List<Lobby>> {
        return repository.lobbySseListener.scan(emptyList()) { acc, value ->
            when (value) {
                is LobbyResponse.Lobbies -> value.lobbies
                is LobbyResponse.AddedLobby -> acc + value.lobby
                is LobbyResponse.RemovedLobby -> acc.filterNot {
                    it.id == value.lobby.id && (it.players.none { player -> player.id == user() } || it.lobbyStatus == LobbyStatus.CLOSED) }
                is LobbyResponse.UpdatedLobby -> acc.map { if (it.id == value.lobby.id) value.lobby else it }
                else -> acc
            }
        }
    }

    override suspend fun leaveLobby(lobby: Lobby, playerId: PlayerId): OutCome<Unit, LobbyError> {
        return repository.leaveLobby(lobby)
    }
}
