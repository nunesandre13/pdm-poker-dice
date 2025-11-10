package pt.isel.pdm.lobby.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyResponse
import pt.isel.pdm.domain.User
import kotlin.time.Duration.Companion.seconds

class RepositoryLobbiesMock: RepositoryLobbies {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val shFlow: MutableSharedFlow<LobbyResponse> = MutableSharedFlow()

    private val lobbyFlow : Flow<LobbyResponse> = flow{
        shFlow.collect {
            emit(it)
        }
    }

    override val lobbySseListener: SharedFlow<LobbyResponse> = callbackFlow {
        launch {
            lobbyFlow.collect {
                send(it)
            }
        }

        launch {
            var id = 0
            while (true){
                delay(3.seconds)
                send(LobbyResponse.AddedLobby(Lobby("Lobby_number $id",id,2,listOf(
                    User("1","Gui", Email("gui@gmail"))
                ))))
                id++
            }

        }

        awaitClose {
            // do something
        }
    }.shareIn(
        scope,
        SharingStarted.WhileSubscribed(1.seconds.inWholeMilliseconds), 0
    )


    override suspend fun createNewLobby(lobby: Lobby): Lobby {
        scope.launch {
            shFlow.emit(LobbyResponse.AddedLobby(lobby))
        }
        return lobby
    }

    override suspend fun joinLobby(lobby: Lobby): Boolean {
        scope.launch {
            if (lobby.players.size >= lobby.maxPlayers) {
                shFlow.emit(LobbyResponse.LobbyFull(lobby))
            } else {
                val updatedLobby = lobby.copy(players = lobby.players + User("newUser", "New Player", Email("new@player.com")))
                shFlow.emit(LobbyResponse.UpdatedLobby(updatedLobby))
            }
        }
        return lobby.players.size < lobby.maxPlayers
    }

    override suspend fun leaveLobby(lobby: Lobby): Boolean {
        scope.launch {
            val updatedPlayers = lobby.players.drop(1)
            if (updatedPlayers.isEmpty()) {
                shFlow.emit(LobbyResponse.RemovedLobby(lobby))
            } else {
                val updatedLobby = lobby.copy(players = updatedPlayers)
                shFlow.emit(LobbyResponse.UpdatedLobby(updatedLobby))
            }
        }
        return true
    }
}