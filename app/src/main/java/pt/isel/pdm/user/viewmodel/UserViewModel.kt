package pt.isel.pdm.user.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.domain.state.UserScreenState
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState
import pt.isel.pdm.utils.runOperation

class UserViewModel(
    private val userService: UserServices,
    private val viewModelBase: ViewModelState<UserScreenState, UserError>) :
        ViewModelState<UserScreenState, UserError> by viewModelBase, ViewModel() {

    init {
        viewModelScope.launch {
            userService.getCurrentUser()?.let { user ->
                navigateTo(UserScreenState.UserLoggIn(user))
            } ?: run {
                navigateTo(UserScreenState.UserLoggedOut)
            }
        }
    }

    fun login(userLogin: UserLogin) {
        viewModelScope.launch {
            runOperation(stateUi.value) {
                navigateTo(UserScreenState.Idle)
                userService.login(userLogin)?.let { response ->
                    return@let UserScreenState.UserLoggIn(response)
                } ?: run {
                    emitError(UserError.ErrorLogin)
                    return@run null
                }
            }.also {
                navigateTo(it)
            }
        }
    }


    fun createUser(userCreate: UserCreate) {
        viewModelScope.launch {
            runOperation(stateUi.value) {
                navigateTo(UserScreenState.Idle)
                userService.createUser(userCreate)?.let { response ->
                    return@let UserScreenState.UserLoggIn(response)
                } ?: run {
                    emitError(UserError.ErrorCreateUser)
                    return@run null
                }
            }.also {
                navigateTo(it)
            }
        }
    }

    companion object {
        fun factory(userService: UserServices) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UserViewModel(userService, ViewModelBase(UserScreenState.Idle, UserError.NoError)) as T
            }
        }
    }
}

