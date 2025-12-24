package pt.isel.pdm.match.viewModels.interfaces

import pt.isel.pdm.domain.match.DiceFace

interface RollingActions {
    suspend fun rollDice(dices: List<DiceFace>): Boolean
    suspend fun setHand(): Boolean
    suspend fun raiseAnte(ante: Int): Boolean
    suspend fun passTurn(): Boolean
}