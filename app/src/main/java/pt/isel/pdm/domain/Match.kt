package pt.isel.pdm.domain


data class Match(
    val id: Int,
    val players: List<PlayerMatchState>,
    val owner: Int,
    val actualRound : Round,
    val initialCoins: Int,
    val remainingRounds: Int,
    val matchStatus: MatchStatus
)

enum class MatchStatus {
    FINISHED,
    ELAPSED
}