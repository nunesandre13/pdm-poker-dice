package pt.isel.pdm.dto.match

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.PlayerMatchState

@Serializable
data class PlayerMatchStateIn(
    val playerId: Int,
    val coins: Int
) {
    fun toDomain() = PlayerMatchState(
        playerId = PlayerId(playerId),
        coins = coins
    )
}