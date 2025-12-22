package pt.isel.pdm.dto.round

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.RoundState


@Serializable
sealed class RoundStateIn {
    @Serializable
    @SerialName("ROLLING")
    data class Rolling(val turn: PlayerRoundStateIn) : RoundStateIn()

    @Serializable
    @SerialName("BETTING")
    data class Betting(
        val turn: PlayerRoundStateIn,
        val amount: Int,
        val playersBets: List<PlayerBetStateIn>
    ) : RoundStateIn()

    @Serializable
    @SerialName("FINISHED")
    data class Finished(val winner: Int?) : RoundStateIn()

    fun toDomain(): RoundState = when (this) {
        is Rolling -> RoundState.Rolling(turn.toDomain())
        is Betting -> RoundState.Betting(
            turn = turn.toDomain(),
            amount = amount,
            playersBets = playersBets.map { it.toDomain() }
        )
        is Finished -> RoundState.Finished(winner?.let { PlayerId(it) })
    }
}