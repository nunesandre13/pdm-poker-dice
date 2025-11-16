package pt.isel.pdm.match.viewModels.betting

import androidx.lifecycle.ViewModel
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider

class BettingViewModel(
    private val stateProvider: MatchStateProvider,
    private val actions: BettingActions
) : ViewModel(),
    MatchStateProvider by stateProvider,
    BettingActions by actions
{

}