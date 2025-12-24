package pt.isel.pdm.domain.match

import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.dto.match.PlayCommandOut

sealed interface PlayCommand {
    val playerId: PlayerId
    val roundId: RoundId

    data class RollDice(override val playerId: PlayerId, override val roundId: RoundId, val dices: List<DiceFace>) : PlayCommand

    data class SetHand(override val playerId: PlayerId, override val roundId: RoundId) : PlayCommand

    data class RaiseAnte(override val playerId: PlayerId, override val roundId: RoundId, val ante: Int) : PlayCommand

    data class PassTurn(override val playerId: PlayerId, override val roundId: RoundId) : PlayCommand

    data class Call(override val playerId: PlayerId, override val roundId: RoundId) : PlayCommand

    data class Fold(override val playerId: PlayerId, override val roundId: RoundId) : PlayCommand
}

fun PlayCommand.toDto(): PlayCommandOut = when (this) {
    is PlayCommand.RollDice -> PlayCommandOut.RollDice(
        playerId = playerId.id,
        roundId = roundId.id,
        dices = dices
    )
    is PlayCommand.SetHand -> PlayCommandOut.SetHand(
        playerId = playerId.id,
        roundId = roundId.id
    )
    is PlayCommand.RaiseAnte -> PlayCommandOut.RaiseAnte(
        playerId = playerId.id,
        roundId = roundId.id,
        ante = ante
    )
    is PlayCommand.PassTurn -> PlayCommandOut.PassTurn(
        playerId = playerId.id,
        roundId = roundId.id
    )
    is PlayCommand.Call -> PlayCommandOut.Call(
        playerId = playerId.id,
        roundId = roundId.id
    )
    is PlayCommand.Fold -> PlayCommandOut.Fold(
        playerId = playerId.id,
        roundId = roundId.id
    )
}
