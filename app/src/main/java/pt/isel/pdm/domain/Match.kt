package pt.isel.pdm.domain


data class Match(
    val id: MatchId,
    val players: List<PlayerMatchState>,
    val owner: UserId,
    val actualRound : Round,
    val initialCoins: Int,
    val remainingRounds: Int,
    val matchStatus: MatchStatus
)

enum class MatchStatus {
    FINISHED,
    ELAPSED
}