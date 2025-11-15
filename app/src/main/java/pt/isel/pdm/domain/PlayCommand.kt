package pt.isel.pdm.domain

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