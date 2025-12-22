package pt.isel.pdm.domain


data class RawMatch(
    val id: MatchId,
    val players: List<PlayerMatchState>,
    val owner: UserId,
    val actualRound : RawRound,
    val initialCoins: Int,
    val remainingRounds: Int,
    val matchStatus: MatchStatus
)

enum class MatchStatus {
    FINISHED,
    ELAPSED
}