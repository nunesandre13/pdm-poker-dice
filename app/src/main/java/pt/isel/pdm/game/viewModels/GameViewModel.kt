package pt.isel.pdm.game.viewModels

import androidx.lifecycle.ViewModel
import pt.isel.pdm.domain.DiceFace

class GameViewModel() : ViewModel(), GameActions {

    override suspend fun rollDice(dices: List<DiceFace>) {
        TODO("Not yet implemented")
    }

    override suspend fun setHand() {
        TODO("Not yet implemented")
    }

    override suspend fun raiseAnte(ante: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun passTurn() {
        TODO("Not yet implemented")
    }

    override suspend fun call() {
        TODO("Not yet implemented")
    }

    override suspend fun fold() {
        TODO("Not yet implemented")
    }

}