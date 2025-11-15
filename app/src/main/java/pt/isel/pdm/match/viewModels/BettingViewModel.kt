package pt.isel.pdm.match.viewModels

import androidx.lifecycle.ViewModel

class BettingViewModel (private val gameActions: GameActions): ViewModel(), GameActions by gameActions{


}