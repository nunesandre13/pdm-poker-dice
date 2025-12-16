package pt.isel.pdm.dto.Match

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.Match
import pt.isel.pdm.dto.Round.RoundIn

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
    fun toDomain(): Match = Match(
        id = id,
        players = players.map { it.toDomain() },
        owner = owner,
        actualRound = actualRound.toDomain(),
        initialCoins = initialCoins,
        remainingRounds = remainingRounds,
        matchStatus = pt.isel.pdm.domain.MatchStatus.valueOf(matchStatus)
    )
}




