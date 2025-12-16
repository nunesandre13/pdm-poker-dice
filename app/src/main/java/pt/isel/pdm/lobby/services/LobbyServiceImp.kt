package pt.isel.pdm.lobby.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.LobbyStatus
import pt.isel.pdm.domain.events.LobbyResponse
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.lobby.repository.RepositoryLobbies
import pt.isel.pdm.user.UserPreferences
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.onOutCome

class LobbyServiceImp(private val repository: RepositoryLobbies, private val userPreferences: UserPreferences) : LobbyServices {
    private val scope = CoroutineScope(Dispatchers.Default)

    private suspend fun user() = userPreferences.getUserId()

    private fun Lobby.lobbyUpdate(): StateFlow<Lobby> {
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
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = this
        )
    }


    override suspend fun joinLobby(lobby: Lobby): OutCome<StateFlow<Lobby>, LobbyError> {
        return repository.joinLobby(lobby).onOutCome(
            onSuccess = { Success(it.lobbyUpdate()) },
            onFailure = { Failure(it) }
        )
    }

    override suspend fun createNewLobby(lobby: LobbyCreation): OutCome<StateFlow<Lobby>, LobbyError> {
        return repository.createNewLobby(lobby).onOutCome(
            onSuccess = { Success(it.lobbyUpdate()) },
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

    override suspend fun leaveLobby(lobby: Lobby, playerId: String): OutCome<Unit, LobbyError> {
        return repository.leaveLobby(lobby)
    }
}
