package pt.isel.pdm.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.isel.pdm.lobby.LobbyViewModel
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.user.services.UserServices

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(
    val userServices: UserServices
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(userServices) as T
    }
}