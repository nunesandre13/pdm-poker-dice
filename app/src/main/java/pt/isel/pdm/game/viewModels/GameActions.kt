package pt.isel.pdm.game.viewModels

import pt.isel.pdm.domain.DiceFace


interface GameActions {
    suspend fun rollDice(dices: List<DiceFace>)
    suspend fun setHand()
    suspend fun raiseAnte(ante: Int)
    suspend fun passTurn()
    suspend fun call()
    suspend fun fold()
}
