package pt.isel.pdm.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.User
import pt.isel.pdm.lobby.LobbyScreenState
import pt.isel.pdm.login.services.UserServices

class UserViewModel(private val userService: UserServices) : ViewModel() {

    private val _stateUi: MutableStateFlow<UserScreenState> = MutableStateFlow(UserScreenState.Idle)
    val stateUi: StateFlow<UserScreenState> = _stateUi

    val user = User(
        id = "1",
        name = "andrezinhoooo"
    )

    fun navigateTo(userState: UserScreenState) {
        _stateUi.value = userState
    }

    fun login(user: User) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Idle
            userService.login(user).let { response ->
                if (response) {
                    navigateTo(UserScreenState.UserLoggIn(user))
                } else {
                    // Tratar erro de login
                }
            }
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Idle
            userService.createUser(user).let { response ->
                if (response) {
                    navigateTo(UserScreenState.UserLoggIn(user))
                } else {
                    // Tratar erro de criação
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Idle
            userService.logout().let { response ->
                if (response) {
                    navigateTo(UserScreenState.UserLoggedOut)
                } else {
                    // Tratar erro de logout
                }
            }
        }
    }
}

sealed interface UserScreenState {
    data object Idle : UserScreenState
    data object UserLoggedOut : UserScreenState
    data class UserLoggIn(val user: User) : UserScreenState

    data object CreatingUser : UserScreenState
}
