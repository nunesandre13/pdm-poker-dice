package pt.isel.pdm.lobby.repository

import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.domain.lobby.Lobby
import pt.isel.pdm.domain.lobby.LobbyCreation
import pt.isel.pdm.domain.events.LobbyResponse
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.utils.OutCome


interface RepositoryLobbies {

    val lobbySseListener: SharedFlow<LobbyResponse>

    suspend fun createNewLobby(lobby: LobbyCreation): OutCome<Lobby, LobbyError>

    suspend fun joinLobby(lobby: Lobby): OutCome<Lobby, LobbyError>

    suspend fun leaveLobby(lobby: Lobby): OutCome<Unit, LobbyError>

}
