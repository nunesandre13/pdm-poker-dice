package pt.isel.pdm.lobby.repository

import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyResponse

interface RepositoryLobbies {

    val lobbySseListener : SharedFlow<LobbyResponse>

    suspend fun createNewLobby(lobby: Lobby) : Lobby

    suspend fun joinLobby(lobby: Lobby): Boolean

    suspend fun leaveLobby(lobby: Lobby): Boolean

}