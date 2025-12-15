package pt.isel.pdm.dto.Lobby

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.events.LobbyResponse

private const val LOBBY_LIST = "LOBBY_LIST"
private const val LOBBY_REMOVED = "LOBBY_REMOVED"
private const val LOBBY_UPDATE = "LOBBY_UPDATE"
private const val LOBBY_ADDED = "LOBBY_ADDED"
private const val LOBBY_IN_GAME = "LOBBY_IN_GAME"

@Serializable
sealed interface LobbyEvent {
    @Serializable
    @SerialName(LOBBY_ADDED)
    class AddedLobbyResponse(val lobby: LobbyIn) : LobbyEvent

    @Serializable
    @SerialName(LOBBY_REMOVED)
    class RemovedLobbyResponse(val lobby: LobbyIn) : LobbyEvent

    @Serializable
    @SerialName(LOBBY_LIST)
    class LobbyResponseList(val listLobbies: List<LobbyIn>) : LobbyEvent

    @Serializable
    @SerialName(LOBBY_IN_GAME)
    class LobbyIsInGame(val lobby: LobbyIn) : LobbyEvent

    @Serializable
    @SerialName(LOBBY_UPDATE)
    class UpdatedLobbyResponse(val lobby: LobbyIn) : LobbyEvent
}

fun LobbyEvent.toDomain(): LobbyResponse = when(this){
    is LobbyEvent.AddedLobbyResponse -> LobbyResponse.AddedLobby(lobby.toDomain())
    is LobbyEvent.LobbyIsInGame -> LobbyResponse.LobbyFull(lobby.toDomain())
    is LobbyEvent.LobbyResponseList -> LobbyResponse.Lobbies(listLobbies.map { it.toDomain() })
    is LobbyEvent.RemovedLobbyResponse -> LobbyResponse.RemovedLobby(lobby.toDomain())
    is LobbyEvent.UpdatedLobbyResponse -> LobbyResponse.UpdatedLobby(lobby.toDomain())
}
