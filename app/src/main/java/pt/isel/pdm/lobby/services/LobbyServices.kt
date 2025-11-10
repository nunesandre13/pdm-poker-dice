package pt.isel.pdm.lobby.services

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.Lobby

interface LobbyServices {

    suspend fun joinLobby(lobby: Lobby) : StateFlow<Lobby>

    suspend fun createNewLobby(lobby: Lobby) : Boolean

    fun listAvailableLobbies() : StateFlow<List<Lobby>>

    suspend fun leaveLobby(lobby: Lobby, playerId: String) : Boolean


}