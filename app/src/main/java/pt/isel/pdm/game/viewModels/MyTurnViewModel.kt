package pt.isel.pdm.game.viewModels

import androidx.lifecycle.ViewModel

class MyTurnViewModel(private val gameActions: GameActions): ViewModel(), GameActions by gameActions{


}