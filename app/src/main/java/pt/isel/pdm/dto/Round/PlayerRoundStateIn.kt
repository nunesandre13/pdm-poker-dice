package pt.isel.pdm.dto.Round

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.dto.Player.PlayerStatusIn

@Serializable
data class PlayerRoundStateIn(
    val playerId: Int,
    val coins: Int,
    val playerStatus: PlayerStatusIn = PlayerStatusIn.NotStarted
){
    fun toDomain() = PlayerRoundState(
        playerId = playerId,
        coins = coins,
        playerStatus = playerStatus.toDomain()
    )
}