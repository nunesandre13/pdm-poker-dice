package pt.isel.pdm.game.repository

import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.events.LobbyResponse

interface RepositoryGame {
    val matchSseListener : SharedFlow<LobbyResponse>

    suspend fun createNewLobby(lobby: Lobby) : Lobby

    suspend fun joinLobby(lobby: Lobby): Boolean

    suspend fun leaveLobby(lobby: Lobby): Boolean
}