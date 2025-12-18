package pt.isel.pdm.lobby.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.utils.OutCome

interface LobbyServices {

    suspend fun joinLobby(lobby: Lobby) : OutCome<Flow<Lobby>, LobbyError>

    suspend fun createNewLobby(lobby: LobbyCreation) : OutCome<Pair<Lobby,Flow<Lobby>>, LobbyError>

    fun listAvailableLobbies() : Flow<List<Lobby>>

    suspend fun leaveLobby(lobby: Lobby, playerId: String) : OutCome<Unit, LobbyError>

}