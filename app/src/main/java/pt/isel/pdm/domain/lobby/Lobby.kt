package pt.isel.pdm.domain.lobby

import pt.isel.pdm.domain.LobbyId
import pt.isel.pdm.domain.user.PlayerInfo
import pt.isel.pdm.domain.UserId

data class Lobby(
    val id: LobbyId,
    val name: String,
    val description: String,
    val players: List<PlayerInfo>,
    val owner: UserId,
    val maxPlayer: Int,
    val minPlayer: Int,
    val numberOdRounds: Int,
    val firstAnte: Int,
    val matchId: String?,
    val lobbyStatus: LobbyStatus
)
enum class LobbyStatus {
    OPEN,
    IN_GAME,
    CLOSED
}