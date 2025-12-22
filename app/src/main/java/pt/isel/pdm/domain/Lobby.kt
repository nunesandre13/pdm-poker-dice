package pt.isel.pdm.domain

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