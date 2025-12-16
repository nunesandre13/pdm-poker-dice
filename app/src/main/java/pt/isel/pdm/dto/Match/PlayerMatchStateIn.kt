package pt.isel.pdm.dto.Match

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.PlayerMatchState

@Serializable
data class PlayerMatchStateIn(
    val playerId: Int,
    val coins: Int
) {
    fun toDomain() = PlayerMatchState(
        playerId = playerId,
        coins = coins
    )
}