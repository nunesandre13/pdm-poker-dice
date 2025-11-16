package pt.isel.pdm.match.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.Match
import pt.isel.pdm.match.services.MatchServices
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider
import pt.isel.pdm.match.viewModels.interfaces.RollingActions

class MatchViewModel(
    private val matchServices: MatchServices,
    private val matchId: Int
) : ViewModel(), MatchStateProvider, RollingActions, BettingActions {


    override val matchState: StateFlow<Match>
        get() = TODO("Not yet implemented")

    override suspend fun rollDice(dices: List<DiceFace>) {

    }

    override suspend fun setHand() {

    }

    override suspend fun raiseAnte(ante: Int) {

    }

    override suspend fun passTurn() {

    }

    override suspend fun call() {

    }

    override suspend fun fold() {

    }

}

sealed interface MatchStateUi {
    data object OtherPlayerTurn
    data object BettingState
    data object MyTurnState
}