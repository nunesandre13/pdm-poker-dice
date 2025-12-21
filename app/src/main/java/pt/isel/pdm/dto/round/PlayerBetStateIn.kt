package pt.isel.pdm.dto.round

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.BetState
import pt.isel.pdm.domain.PlayerBetState
import pt.isel.pdm.domain.PlayerId

@Serializable
data class PlayerBetStateIn(val playerId: Int, val betState: String)
{
    fun toDomain(): PlayerBetState =
        PlayerBetState(
            playerId = PlayerId(playerId),
            betState = BetState.valueOf(betState)
        )
}