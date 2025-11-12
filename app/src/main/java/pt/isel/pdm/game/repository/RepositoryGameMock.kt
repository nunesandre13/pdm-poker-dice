package pt.isel.pdm.game.repository

import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.events.LobbyResponse

class RepositoryGameMock : RepositoryGame{
    override val matchSseListener: SharedFlow<LobbyResponse>
        get() = TODO("Not yet implemented")

    override suspend fun createNewLobby(lobby: Lobby): Lobby {
        TODO("Not yet implemented")
    }

    override suspend fun joinLobby(lobby: Lobby): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun leaveLobby(lobby: Lobby): Boolean {
        TODO("Not yet implemented")
    }

}