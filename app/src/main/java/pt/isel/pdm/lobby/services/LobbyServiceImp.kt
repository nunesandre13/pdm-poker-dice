package pt.isel.pdm.lobby.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.events.LobbyResponse
import pt.isel.pdm.lobby.repository.RepositoryLobbies
import kotlin.time.Duration.Companion.seconds


class LobbyServiceImp(private val repository: RepositoryLobbies) : LobbyServices {

    private val scope = CoroutineScope(Dispatchers.Default)

    override suspend fun joinLobby(lobby: Lobby): StateFlow<Lobby> {
        repository.joinLobby(lobby)
        return repository.lobbySseListener.filter { response ->
            when (response) {
                is LobbyResponse.UpdatedLobby -> response.lobby.id == lobby.id
                is LobbyResponse.LobbyFull -> response.lobby.id == lobby.id
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
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = lobby
        )
    }

    override suspend fun createNewLobby(lobby: Lobby): Boolean {
        repository.createNewLobby(lobby)
        return true
    }

    override fun listAvailableLobbies(): StateFlow<List<Lobby>> {
        return repository.lobbySseListener.scan(emptyList<Lobby>()) { acc, value ->
            when (value) {
                is LobbyResponse.Lobbies -> value.lobbies
                is LobbyResponse.AddedLobby -> acc + value.lobby
                is LobbyResponse.RemovedLobby -> acc.filterNot { it.id == value.lobby.id }
                is LobbyResponse.UpdatedLobby -> acc.map { if (it.id == value.lobby.id) value.lobby else it }
                else -> acc
            }
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(1.seconds.inWholeMilliseconds),
            initialValue = emptyList()
        )
    }

    override suspend fun leaveLobby(lobby: Lobby, playerId: String): Boolean {
        return repository.leaveLobby(lobby)
    }
}
