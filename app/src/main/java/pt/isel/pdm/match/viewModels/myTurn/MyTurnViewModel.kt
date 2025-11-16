package pt.isel.pdm.match.viewModels.myTurn

import androidx.lifecycle.ViewModel
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider
import pt.isel.pdm.match.viewModels.interfaces.RollingActions

class MyTurnViewModel(
    private val stateProvider: MatchStateProvider,
    private val actions: RollingActions
): ViewModel(),
    MatchStateProvider by stateProvider,
    RollingActions by actions {

}