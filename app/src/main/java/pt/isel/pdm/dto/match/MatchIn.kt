package pt.isel.pdm.dto.match

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.RawMatch
import pt.isel.pdm.domain.MatchId
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.dto.round.RoundIn

@Serializable
data class MatchIn(
    val id: Int,
    val players: List<PlayerMatchStateIn>,
    val owner: Int,
    val actualRound : RoundIn,
    val initialCoins: Int,
    val remainingRounds: Int,
    val matchStatus: String
){
    fun toDomain(): RawMatch = RawMatch(
        id = MatchId(id),
        players = players.map { it.toDomain() },
        owner = UserId(owner),
        actualRound = actualRound.toDomain(),
        initialCoins = initialCoins,
        remainingRounds = remainingRounds,
        matchStatus = pt.isel.pdm.domain.MatchStatus.valueOf(matchStatus)
    )
}




