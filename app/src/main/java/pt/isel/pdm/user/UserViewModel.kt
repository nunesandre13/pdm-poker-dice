package pt.isel.pdm.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.services.UserServices

class UserViewModel(private val userService: UserServices) : ViewModel() {

    private val _stateUi: MutableStateFlow<UserScreenState> = MutableStateFlow(UserScreenState.Idle)
    val stateUi: StateFlow<UserScreenState> = _stateUi

    private val _email: MutableStateFlow<Email?> = MutableStateFlow(null)
    val email: StateFlow<Email?> = _email

    private val _password: MutableStateFlow<Password?> = MutableStateFlow(null)
    val password: StateFlow<Password?> = _password

    private val _name: MutableStateFlow<Name?> = MutableStateFlow(null)
    val name: StateFlow<Name?> = _name

    private val _showPassword: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword

    init {
        viewModelScope.launch {
            userService.getCurrentUser()?.let { user ->
                navigateTo(UserScreenState.UserLoggIn(user))
            } ?: run {
                navigateTo(UserScreenState.UserLoggedOut)
            }
        }
    }

    fun onEmailChange(email: Email) {
        _email.value = email
    }

    fun onPasswordChange(password: Password) {
        _password.value = password
    }

    fun onNameChange(name: Name) {
        _name.value = name
    }

    fun onShowPassword(){
        _showPassword.value = !_showPassword.value
    }


    fun navigateTo(userState: UserScreenState) {
        _stateUi.value = userState
    }

    fun login(user: UserLogin) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Idle
            userService.login(user)?.let { response ->
                navigateTo(UserScreenState.UserLoggIn(response))
            }
        }
    }

    fun createUser(user: UserCreate) {
        viewModelScope.launch {
            _stateUi.value = UserScreenState.Idle
            userService.createUser(user)?.let { response ->
                navigateTo(UserScreenState.UserLoggIn(response))
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
    companion object {
        fun factory(userService: UserServices)  =object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(userService) as T
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
