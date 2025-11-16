package pt.isel.pdm.match.viewModels

import pt.isel.pdm.domain.DiceFace


interface MatchActions {
    suspend fun rollDice(dices: List<DiceFace>)
    suspend fun setHand()
    suspend fun raiseAnte(ante: Int)
    suspend fun passTurn()
    suspend fun call()
    suspend fun fold()
}
