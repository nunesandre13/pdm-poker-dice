package pt.isel.pdm.lobby

import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.domain.Lobby

interface LobbyServices {

    suspend fun selectLobby(lobby: Lobby) : Boolean

    suspend fun createNewLobby(lobby: Lobby) : Boolean

    suspend fun listAvailableLobbies() : SharedFlow<List<Lobby>>

    suspend fun leaveLobby(lobby: Lobby, playerId: String) : Boolean

}