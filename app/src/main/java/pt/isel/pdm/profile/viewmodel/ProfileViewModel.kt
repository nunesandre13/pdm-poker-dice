package pt.isel.pdm.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.state.ProfileError
import pt.isel.pdm.domain.state.ProfileScreenState
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState
import pt.isel.pdm.utils.onOutCome
import pt.isel.pdm.utils.runOperation

class ProfileViewModel(
    private val userService: UserServices ,
    viewModelState: ViewModelState<ProfileScreenState,ProfileError>)
        : ViewModel(), ViewModelState<ProfileScreenState,ProfileError> by viewModelState {

    init {
        viewModelScope.launch {
            userService.getCurrentUser()?.let { user ->
                navigateTo(ProfileScreenState.OnProfileView(user))
            } ?: run {
                navigateTo(ProfileScreenState.LoggedOut)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            runOperation(stateUi.value) {
                navigateTo(ProfileScreenState.Idle)
                userService.logout().onOutCome(
                    onSuccess = {
                        ProfileScreenState.LoggedOut
                    },
                    onFailure = {
                        emitError(ProfileError.NoError)
                        null
                    }
                )
            }.let { navigateTo(it) }
        }
    }

    companion object {
        fun factory(userService: UserServices)  =object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(userService, ViewModelBase(ProfileScreenState.Idle,ProfileError.NoError)) as T
            }
        }
    }
}

