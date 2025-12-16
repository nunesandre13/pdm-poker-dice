package pt.isel.pdm.domain

import pt.isel.pdm.dto.Match.PlayCommandOut

sealed interface PlayCommand {
    val playerId: Int
    val roundId: Int

    data class RollDice(override val playerId: Int, override val roundId: Int, val dices: List<DiceFace>) : PlayCommand

    data class SetHand(override val playerId: Int, override val roundId: Int) : PlayCommand

    data class RaiseAnte(override val playerId: Int, override val roundId: Int, val ante: Int) : PlayCommand

    data class PassTurn(override val playerId: Int, override val roundId: Int) : PlayCommand

    data class Call(override val playerId: Int, override val roundId: Int) : PlayCommand

    data class Fold(override val playerId: Int, override val roundId: Int) : PlayCommand
}

fun PlayCommand.toDto(): PlayCommandOut = when (this) {
    is PlayCommand.RollDice -> PlayCommandOut.RollDice(
        playerId = playerId,
        roundId = roundId,
        dices = dices
    )
    is PlayCommand.SetHand -> PlayCommandOut.SetHand(
        playerId = playerId,
        roundId = roundId
    )
    is PlayCommand.RaiseAnte -> PlayCommandOut.RaiseAnte(
        playerId = playerId,
        roundId = roundId,
        ante = ante
    )
    is PlayCommand.PassTurn -> PlayCommandOut.PassTurn(
        playerId = playerId,
        roundId = roundId
    )
    is PlayCommand.Call -> PlayCommandOut.Call(
        playerId = playerId,
        roundId = roundId
    )
    is PlayCommand.Fold -> PlayCommandOut.Fold(
        playerId = playerId,
        roundId = roundId
    )
}
