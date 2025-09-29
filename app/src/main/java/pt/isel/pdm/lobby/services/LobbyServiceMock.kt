package pt.isel.pdm.lobby.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.User
import kotlin.collections.plus
import kotlin.time.Duration.Companion.seconds


class LobbyServiceMock : LobbyServices {
    private val lobbiesFlow = MutableStateFlow<List<Lobby>>(emptyList())

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            lobbiesFlow.emit(
                listOf(
                    Lobby("Lobby 1", 4, listOf(User("1234", "user1"))),
                    Lobby("Lobby 2", 6, listOf(User("123445", "user2"), User("1235677", "user3")))
                )
            )
        }

        scope.launch {
            while (true) {
                delay(5.seconds)
                lobbiesFlow.value.plus(
                    Lobby("Lobby 4", 4, listOf(User("1234", "user1"))),
                ).also { lobbies ->
                    lobbiesFlow.value = lobbies
                }
                delay(5.seconds)
                lobbiesFlow.value.dropLast(1).also { lobbies ->
                    lobbiesFlow.value = lobbies
                }
            }
        }
    }

    override suspend fun selectLobby(lobby: Lobby): Boolean {
        return true
    }

    override suspend fun createNewLobby(lobby: Lobby): Boolean {
        // Adiciona o novo lobby à lista e emite
        val current = lobbiesFlow.replayCache.firstOrNull() ?: emptyList()
        lobbiesFlow.emit(current + lobby)
        return true
    }

    override fun listAvailableLobbies(): SharedFlow<List<Lobby>> {
        return lobbiesFlow
    }

    override suspend fun leaveLobby(lobby: Lobby, playerId: String): Boolean {
        val current = lobbiesFlow.replayCache.firstOrNull() ?: emptyList()
        val updated = current.map {
            if (it.name == lobby.name) it.copy(players = it.players.filter { user -> user.id != playerId })
            else it
        }
        lobbiesFlow.emit(updated)
        return true
    }
}