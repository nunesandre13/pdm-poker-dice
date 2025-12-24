package pt.isel.pdm.dto.round

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.match.PlayerRoundState
import pt.isel.pdm.dto.player.PlayerStatusIn

@Serializable
data class PlayerRoundStateIn(
    val playerId: Int,
    val coins: Int,
    val playerStatus: PlayerStatusIn = PlayerStatusIn.NotStarted
){
    fun toDomain() = PlayerRoundState(
        playerId = PlayerId(playerId),
        coins = coins,
        playerStatus = playerStatus.toDomain()
    )
}