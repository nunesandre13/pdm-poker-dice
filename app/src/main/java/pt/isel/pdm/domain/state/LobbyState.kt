package pt.isel.pdm.domain.state

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.lobby.Lobby
import pt.isel.pdm.domain.State

sealed interface LobbyScreenState : State{
    data object Loading : LobbyScreenState
    data object Creation : LobbyScreenState
    data class JoinedLobby(val lobby : StateFlow<Lobby>) : LobbyScreenState
    data class LobbiesList(val lobby : StateFlow<List<Lobby>>) : LobbyScreenState
}

sealed class LobbyError(override val message: String?): DomainError {
    data object LobbyFull: LobbyError(null)
    data object NoError: LobbyError(null)

    data class ApiError(override val message: String?): LobbyError(message)
    data object LobbyNotFound: DomainError, LobbyError("Lobby not found")

    data object NetWorkError: LobbyError("NetWorkError")
}