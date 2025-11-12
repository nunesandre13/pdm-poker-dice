package pt.isel.pdm.game.viewModels

import androidx.lifecycle.ViewModel

class OtherPlayerTurn(private val gameActions: GameActions): ViewModel(), GameActions by gameActions {


}