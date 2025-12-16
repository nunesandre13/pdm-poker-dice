package pt.isel.pdm.dto.Round

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.INITIAL_ANTE
import pt.isel.pdm.domain.Round

@Serializable
data class RoundIn(
    val id: Int,
    val players: List<PlayerRoundStateIn>,
    val ante: Int = INITIAL_ANTE,
    val totalBet: Int = INITIAL_ANTE,
    val state: RoundStateIn
) {
    fun toDomain(): Round = Round(
        id = id,
        players = players.map { it.toDomain() },
        ante = ante,
        totalBet = totalBet,
        state = state.toDomain()
    )
}