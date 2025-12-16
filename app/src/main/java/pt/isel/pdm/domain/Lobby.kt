package pt.isel.pdm.domain

data class Lobby(
    val id: String,
    val name: String,
    val description: String,
    val players: List<User>,
    val owner: String,
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