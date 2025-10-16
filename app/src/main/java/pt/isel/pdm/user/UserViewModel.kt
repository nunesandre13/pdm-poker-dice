package pt.isel.pdm.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.lobby.LobbyError
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.services.UserServices

class UserViewModel(private val userService: UserServices) : ViewModel() {

    private val _stateUi: MutableStateFlow<UserScreenState> = MutableStateFlow(UserScreenState.Idle)
    val stateUi: StateFlow<UserScreenState> = _stateUi

    private val _email: MutableStateFlow<EmailInput?> = MutableStateFlow(null)
    val email: StateFlow<EmailInput?> = _email

    private val _password: MutableStateFlow<PasswordInput?> = MutableStateFlow(null)
    val password: StateFlow<PasswordInput?> = _password

    private val _name: MutableStateFlow<NameInput?> = MutableStateFlow(null)
    val name: StateFlow<NameInput?> = _name

    private val _showPassword: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword

    private val _errorState : MutableStateFlow<UserError> = MutableStateFlow(UserError.NoError)

    val errorState : StateFlow<UserError> = _errorState

    fun emitError(error: UserError) {
        _errorState.value = error
    }
    fun dismissError(){
        emitError(UserError.NoError)
    }

    init {
        viewModelScope.launch {
            userService.getCurrentUser()?.let { user ->
                navigateTo(UserScreenState.UserLoggIn(user))
            } ?: run {
                navigateTo(UserScreenState.UserLoggedOut)
            }
        }
    }

    fun onEmailChange(emailInput: EmailInput) {
        _email.value = emailInput
    }

    fun onPasswordChange(passwordInput: PasswordInput) {
        _password.value = passwordInput
    }

    fun onNameChange(nameInput: NameInput) {
        _name.value = nameInput
    }

    fun onShowPassword(){
        _showPassword.value = !_showPassword.value
    }


    fun navigateTo(userState: UserScreenState) {
        _stateUi.value = userState
    }

    fun login() {
        viewModelScope.launch {
            runCatching {
                UserLogin(
                    email = _email.value?.toEmail() ?: throw IllegalArgumentException("Email inválido"),
                    password = _password.value?.toPassword() ?: throw IllegalArgumentException("Password inválida")
                )
            }.onSuccess { userLogin ->
                val lastState = _stateUi.value
                _stateUi.value = UserScreenState.Idle

                userService.login(userLogin)?.let { response ->
                    navigateTo(UserScreenState.UserLoggIn(response))
                } ?: run {
                    emitError(UserError.ErrorLogin)
                    _stateUi.value = lastState
                }
            }.onFailure { error ->

            }
        }
    }

    fun createUser() {
        viewModelScope.launch {
            runCatching {
                UserCreate(
                    name = _name.value?.toName() ?: throw IllegalArgumentException("Name inválido"),
                    email = _email.value?.toEmail() ?: throw IllegalArgumentException("Email inválido"),
                    password = _password.value?.toPassword() ?: throw IllegalArgumentException("Password inválida")
                )
            }.onSuccess { userCreate ->
                val lastState = _stateUi.value
                _stateUi.value = UserScreenState.Idle

                userService.createUser(userCreate)?.let { response ->
                    navigateTo(UserScreenState.UserLoggIn(response))
                } ?: run {
                    emitError(UserError.ErrorLogin)
                    _stateUi.value = lastState
                }
            }.onFailure { error ->

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
                    emitError(UserError.NoError)
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


sealed class UserError(override val message: String?): DomainError {
    data object NoError: UserError(null)
    data object UserNotFound: DomainError, UserError("Users not found")
    data object ErrorLogin: DomainError, UserError("Login not possible")
    data object ErrorCreateUser: DomainError, UserError("Create user not possible")
}