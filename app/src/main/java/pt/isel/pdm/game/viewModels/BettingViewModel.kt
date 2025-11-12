package pt.isel.pdm.game.viewModels

import androidx.lifecycle.ViewModel

class BettingViewModel (private val gameActions: GameActions): ViewModel(), GameActions by gameActions{


}