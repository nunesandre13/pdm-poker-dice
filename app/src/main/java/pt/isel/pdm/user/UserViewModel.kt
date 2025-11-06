package pt.isel.pdm.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.runOperation

class UserViewModel(private val userService: UserServices) : ViewModel() {

    private val _stateUi: MutableStateFlow<UserScreenState> = MutableStateFlow(UserScreenState.Idle)
    val stateUi: StateFlow<UserScreenState> = _stateUi

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

    fun navigateTo(userState: UserScreenState) {
        _stateUi.value = userState
    }

    fun login(userLogin: UserLogin) {
        viewModelScope.launch {
            runOperation(_stateUi.value) {
                _stateUi.value = UserScreenState.Idle
                userService.login(userLogin)?.let { response ->
                        return@let UserScreenState.UserLoggIn(response)
                } ?: run {
                    emitError(UserError.ErrorLogin)
                    return@run null
                }
            }.also{
                navigateTo(it)
            }
        }
    }


    fun createUser(userCreate: UserCreate) {
        viewModelScope.launch {
            runOperation(_stateUi.value) {
                _stateUi.value = UserScreenState.Idle
                userService.createUser(userCreate)?.let { response ->
                    return@let UserScreenState.UserLoggIn(response)
                } ?: run {
                    emitError(UserError.ErrorLogin)
                    return@run null
                }
            }.also{
                navigateTo(it)
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