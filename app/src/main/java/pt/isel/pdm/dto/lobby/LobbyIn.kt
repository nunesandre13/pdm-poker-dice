package pt.isel.pdm.dto.lobby

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyId
import pt.isel.pdm.domain.LobbyStatus
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.dto.player.PlayerInfoIn

@Serializable
data class LobbyIn(
    val id: Int,
    val players: List<PlayerInfoIn>,
    val name: String,
    val description: String,
    val owner: Int,
    val maxPlayer: Int,
    val minPlayer: Int,
    val status: String,
    val matchId: Int?
)
fun LobbyIn.toDomain(): Lobby =
    Lobby(
        id = LobbyId(id),
        name = name,
        description = description,
        owner = UserId(owner),
        maxPlayer = maxPlayer,
        minPlayer = minPlayer,
        numberOdRounds = 0,
        firstAnte = 0,
        matchId = matchId?.toString(),
        lobbyStatus = LobbyStatus.valueOf(status),
        players = players.map { User(UserId(it.playerId), Name(it.name), Email(it.name + "@gmail")) }
    )
