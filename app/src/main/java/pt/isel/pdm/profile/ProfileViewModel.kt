package pt.isel.pdm.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.user.UserScreenState
import pt.isel.pdm.user.services.UserServices

class ProfileViewModel(private val userService: UserServices) : ViewModel(){

    private val _stateUi: MutableStateFlow<ProfileScreenState> = MutableStateFlow(ProfileScreenState.Idle)
    val stateUi: StateFlow<ProfileScreenState> = _stateUi

    private val _errorState : MutableStateFlow<ProfileError> = MutableStateFlow(ProfileError.NoError)

    val errorState : StateFlow<ProfileError> = _errorState


    fun emitError(error: ProfileError) {
        _errorState.value = error
    }

    fun dismissError(){
        emitError(ProfileError.NoError)
    }

    fun logout() {
        viewModelScope.launch {
            _stateUi.value = ProfileScreenState.Idle
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

    fun navigateTo( profileState: ProfileScreenState) {
        _stateUi.value = profileState
    }


    companion object {
        fun factory(userService: UserServices)  =object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProfileViewModel(userService) as T
            }
        }
    }
}

sealed class ProfileScreenState{
    data object Idle: ProfileScreenState()
    data object OnProfileView: ProfileScreenState()
    data object LoggedOut: ProfileScreenState()
}
sealed class ProfileError(override val message: String?): DomainError {
    data object NoError: ProfileError(null)
    data object LogoutError: ProfileError("Error doing the logout")
}