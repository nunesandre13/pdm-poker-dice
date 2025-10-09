package pt.isel.pdm.domain

sealed interface LobbyEvent {
    data class PlayerAdded(val player: User) : LobbyEvent
    data class PlayerRemoved(val player: User) : LobbyEvent
    data object LobbyClosed : LobbyEvent
    data object LobbyFull : LobbyEvent
    data object MatchStarted : LobbyEvent
}