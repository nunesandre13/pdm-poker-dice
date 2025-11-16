package pt.isel.pdm.match.viewModels

import androidx.lifecycle.ViewModel

class MyTurnViewModel(private val matchActions: MatchActions): ViewModel(), MatchActions by matchActions{


}