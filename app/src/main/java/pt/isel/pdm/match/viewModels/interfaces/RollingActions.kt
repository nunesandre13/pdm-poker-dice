package pt.isel.pdm.match.viewModels.interfaces

import pt.isel.pdm.domain.DiceFace

interface RollingActions {
    suspend fun rollDice(dices: List<DiceFace>)
    suspend fun setHand()
}