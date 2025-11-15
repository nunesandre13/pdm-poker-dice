package pt.isel.pdm.match.viewModels

import androidx.lifecycle.ViewModel

class OtherPlayerTurn(private val gameActions: GameActions): ViewModel(), GameActions by gameActions {


}