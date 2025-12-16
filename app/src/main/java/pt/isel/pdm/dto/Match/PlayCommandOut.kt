package pt.isel.pdm.dto.Match

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import pt.isel.pdm.domain.DiceFace

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed interface PlayCommandOut {
    val playerId: Int
    val roundId: Int

    @Serializable
    @SerialName("RollDice")
    data class RollDice(override val playerId: Int, override val roundId: Int, val dices: List<DiceFace>) : PlayCommandOut

    @Serializable
    @SerialName("SetHand")
    data class SetHand(override val playerId: Int, override val roundId: Int) : PlayCommandOut

    @Serializable
    @SerialName("RaiseAnte")
    data class RaiseAnte(override val playerId: Int, override val roundId: Int, val ante: Int) : PlayCommandOut

    @Serializable
    @SerialName("PassTurn")
    data class PassTurn(override val playerId: Int, override val roundId: Int) : PlayCommandOut

    @Serializable
    @SerialName("Call")
    data class Call(override val playerId: Int, override val roundId: Int) : PlayCommandOut

    @Serializable
    @SerialName("Fold")
    data class Fold(override val playerId: Int, override val roundId: Int) : PlayCommandOut
}
