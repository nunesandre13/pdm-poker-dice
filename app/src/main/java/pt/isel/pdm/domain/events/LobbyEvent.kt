package pt.isel.pdm.domain.events

import pt.isel.pdm.domain.Lobby


sealed class LobbyResponse {
    data class Lobbies(val lobbies: List<Lobby>) : LobbyResponse()
    data class AddedLobby(val lobby: Lobby) : LobbyResponse()
    data class RemovedLobby(val lobby: Lobby) : LobbyResponse()
    data class UpdatedLobby(val lobby: Lobby) : LobbyResponse()
    data class LobbyFull(val lobby: Lobby) : LobbyResponse()
}