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
            navigateTo(ProfileScreenState.Idle)
            userService.logout().let { response ->
                if (response) {
                    navigateTo(ProfileScreenState.LoggedOut)
                }
                else {
                    emitError(ProfileError.NoError)
                }
            }
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

