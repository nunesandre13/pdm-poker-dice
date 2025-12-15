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
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.events.LobbyResponse
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
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
                    User("1", Name("Gui"), Email("gui@gmail"))
                ))))
                id++
            }

        }

        awaitClose {
            // do something
        }
    }.shareIn(
        scope,
        started = SharingStarted.WhileSubscribed(),
        replay = 0
    )

    override fun setClientId(id: String) {
        TODO("Not yet implemented")
    }


    override suspend fun createNewLobby(lobby: Lobby): OutCome<Lobby, LobbyError> {
        scope.launch {
            shFlow.emit(LobbyResponse.AddedLobby(lobby))
        }
        return Success(lobby)
    }

    override suspend fun joinLobby(lobby: Lobby): OutCome<Lobby, LobbyError> {
        if (lobby.players.size >= lobby.maxPlayers) {
            scope.launch {
                shFlow.emit(LobbyResponse.LobbyFull(lobby))
            }
            return Failure(LobbyError.LobbyFull)
        }

        val updatedLobby = lobby.copy(players = lobby.players + User("newUser", Name("New Player"), Email("new@player.com")))
        scope.launch {
            shFlow.emit(LobbyResponse.UpdatedLobby(updatedLobby))
        }
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
