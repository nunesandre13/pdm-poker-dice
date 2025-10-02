package pt.isel.pdm.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.user.services.UserServices

@Suppress("UNCHECKED_CAST")
class LobbyViewModelFactory(private val lobbyServices: LobbyServices, val userServices: UserServices ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LobbyViewModel(lobbyServices, userServices) as T
    }
}