package pt.isel.pdm.dto.player

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import pt.isel.pdm.domain.NUMBER_OF_ROLLS
import pt.isel.pdm.dto.round.HandIn
import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.PlayerStatus

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("status")
sealed class PlayerStatusIn {
    @Serializable
    @SerialName("NOT_STARTED")
    data object NotStarted : PlayerStatusIn()

    @Serializable
    @SerialName("PASS_ROUND")
    data object PassRound : PlayerStatusIn()

    @Serializable
    @SerialName("FINAL_HAND")
    data class FinalHand(val hand: HandIn) : PlayerStatusIn()

    @Serializable
    @SerialName("STILL_ROLLING")
    data class StillRolling(
        val hand: HandIn,
        val remainingRolls: Int = NUMBER_OF_ROLLS
    ) : PlayerStatusIn()

    fun toDomain(): PlayerStatus = when (this) {
        is NotStarted -> PlayerStatus.NotStarted
        is PassRound -> PlayerStatus.PassRound
        is FinalHand -> PlayerStatus.FinalHand(hand.toDomain())
        is StillRolling -> PlayerStatus.StillRolling(hand = hand.toDomain(), remainingRolls = remainingRolls)
    }
}