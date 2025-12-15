package pt.isel.pdm.lobby.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.utils.OutCome

interface LobbyServices {

    fun setClientId(id: String)
    suspend fun joinLobby(lobby: Lobby) : OutCome<StateFlow<Lobby>, LobbyError>

    suspend fun createNewLobby(lobby: Lobby) : OutCome<StateFlow<Lobby>, LobbyError>

    fun listAvailableLobbies() : Flow<List<Lobby>>

    suspend fun leaveLobby(lobby: Lobby, playerId: String) : OutCome<Unit, LobbyError>

}