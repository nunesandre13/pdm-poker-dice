package pt.isel.pdm.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.domain.state.UserScreenState
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState
import pt.isel.pdm.utils.onOutCome
import pt.isel.pdm.utils.runOperation

class UserViewModel(
    private val userService: UserServices,
    private val viewModelBase: ViewModelState<UserScreenState, UserError>
) :
    ViewModelState<UserScreenState, UserError> by viewModelBase, ViewModel() {

    init {
        viewModelScope.launch {
            userService.restoreSession()
            userService.getCurrentUser()?.let { user ->
                navigateTo(UserScreenState.UserLoggIn(user))
            } ?: run {
                navigateTo(UserScreenState.UserLoggedOut)
            }
        }
    }

    fun login(userLogin: UserCreateTokenInputModel) {
        viewModelScope.launch {
            runOperation(stateUi.value) {
                navigateTo(UserScreenState.Idle)
                userService.login(userLogin).onOutCome(
                    onSuccess = {
                        userService.getCurrentUser()?.let { loggedUser ->
                            UserScreenState.UserLoggIn(loggedUser)
                        }
                    },
                    onFailure = { error ->
                        emitError(error)
                        null
                    }
                )
            }.let { navigateTo(it) }
        }
    }


    fun createUser(userCreate: UserInput,inviteCode: InviteCode) {
        viewModelScope.launch {
            runOperation(stateUi.value) {
                navigateTo(UserScreenState.Idle)
                userService.createUser(userCreate,inviteCode).onOutCome(
                    onSuccess = { user ->
                        UserScreenState.UserLoggIn(user)
                    },
                    onFailure = { error ->
                        emitError(error)
                        null
                    }
                )
            }.let {  navigateTo(it) }
        }
    }

    companion object {
        fun factory(userService: UserServices) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(
                    userService,
                    ViewModelBase(UserScreenState.Idle, UserError.NoError)
                ) as T
            }
        }
    }
}
