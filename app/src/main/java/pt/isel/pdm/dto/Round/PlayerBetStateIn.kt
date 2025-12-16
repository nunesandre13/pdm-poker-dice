package pt.isel.pdm.dto.Round

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.BetState
import pt.isel.pdm.domain.PlayerBetState

@Serializable
data class PlayerBetStateIn(val playerId: Int, val betState: String)
{
    fun toDomain(): PlayerBetState =
        PlayerBetState(
            playerId = playerId,
            betState = BetState.valueOf(betState)
        )
}