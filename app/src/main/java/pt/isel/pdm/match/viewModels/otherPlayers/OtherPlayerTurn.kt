package pt.isel.pdm.match.viewModels.otherPlayers

import androidx.lifecycle.ViewModel
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider

class OtherPlayerTurn(private val matchStateProvider: MatchStateProvider): ViewModel(), MatchStateProvider by matchStateProvider {


}