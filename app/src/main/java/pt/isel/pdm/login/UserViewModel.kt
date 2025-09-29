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

    fun navigateTo(userState: UserScreenState) {
        _stateUi.value = userState
    }

    val stateUi: StateFlow<UserScreenState> = _stateUi

    fun login(user: User) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Loading
            userService.selectUser(user).let { response ->
                if (response) {
                     navigateTo(UserScreenState.UserLogged(user))
                } else {
                    //
                }
            }
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Loading
            userService.createNewUser(user).let { response ->
                if (response) {
                    navigateTo(UserScreenState.UserLogged(user))
                } else {
                    //
                }
            }
        }
    }

    fun logout(user: User) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Loading
            userService.removeUser(user).let { response ->
                if (response) {
                    navigateTo(UserScreenState.UserLoggedOut)
                } else {
                    //
                }
            }
        }
    }
}

sealed interface UserScreenState {
    data object Idle : UserScreenState
    data object Loading : UserScreenState
    data object UserLoggedOut : UserScreenState
    data class UserLogged(val user: User) : UserScreenState
}
