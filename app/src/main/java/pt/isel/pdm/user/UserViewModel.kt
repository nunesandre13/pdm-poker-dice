package pt.isel.pdm.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.user.services.UserServices

class UserViewModel(private val userService: UserServices) : ViewModel() {

    private val _stateUi: MutableStateFlow<UserScreenState> = MutableStateFlow(UserScreenState.Idle)
    val stateUi: StateFlow<UserScreenState> = _stateUi

    fun navigateTo(userState: UserScreenState) {
        _stateUi.value = userState
    }

    fun login(user: UserLogin) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Idle
            userService.login(user)?.let { response ->
                navigateTo(UserScreenState.UserLoggIn(user))
            }
        }
    }

    fun createUser(user: UserCreate) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Idle
            userService.createUser(user)?.let { response ->
                navigateTo(UserScreenState.UserLoggIn(user.convertToUserLogin(user)))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Idle
            userService.logout().let { response ->
                if (response) {
                    navigateTo(UserScreenState.UserLoggedOut)
                }
                else {
                    ///
                }
            }
        }
    }
}

sealed interface UserScreenState {
    data object Idle : UserScreenState
    data object UserLoggedOut : UserScreenState
    data class UserLoggIn(val user: UserLogin) : UserScreenState

    data object CreatingUser : UserScreenState
}
