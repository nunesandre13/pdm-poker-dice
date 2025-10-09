package pt.isel.pdm.lobby

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.user.services.UserServices

class LobbyViewModel(private val lobbyService: LobbyServices, private val userService: UserServices) : ViewModel() {

    private val _stateUi : MutableStateFlow<LobbyScreenState> = MutableStateFlow(LobbyScreenState.Loading)
    val stateUi : StateFlow<LobbyScreenState> = _stateUi

    private val _errorState : MutableStateFlow<LobbyError> = MutableStateFlow(LobbyError.NoError)
    val errorState : StateFlow<LobbyError> = _errorState

    init {
        getLobbies()
    }

    fun emitError(error: LobbyError) {
        _errorState.value = error
    }

    fun navigateTo(lobbyState: LobbyScreenState) {
        _stateUi.value = lobbyState
    }

    fun joinLobby(lobby: Lobby) {
        viewModelScope.launch {
            lobbyService.selectLobby(lobby).let { response ->
                if (response) navigateTo(LobbyScreenState.JoinedLobby(lobby)) else {
                    emitError(LobbyError.LobbyNotFound)
                }
            }
        }
    }

    fun createLobby(lobby: Lobby) {
        viewModelScope.launch {
            lobbyService.createNewLobby(lobby).let { response ->
                if (response) navigateTo(LobbyScreenState.JoinedLobby(lobby)) else {
                    emitError(LobbyError.LobbyNotFound)
                }
            }
        }
    }

    fun goToLobbiesList(){
        navigateTo(LobbyScreenState.Loading).also {
            getLobbies()
        }
    }

    fun goToCreation(){
        _stateUi.value = LobbyScreenState.Creation
    }

    private fun getLobbies(): SharedFlow<List<Lobby>> {
        return lobbyService.listAvailableLobbies().also { sharedFlow ->
            navigateTo(LobbyScreenState.LobbiesList(sharedFlow))
        }
    }

    fun dismissError(){
        emitError(LobbyError.NoError)
    }


    fun leaveLobby(lobby: Lobby) {
        viewModelScope.launch {
            userService.getCurrentUser()?.let { user ->
                lobbyService.leaveLobby(lobby, user.id).let { response ->
                    if (response) navigateTo(LobbyScreenState.Loading).also {
                        getLobbies()
                    } else {
                        emitError(LobbyError.LobbyNotFound)
                    }
                }
            }
        }
    }

     companion object {

         fun getFactory(lobbyServices: LobbyServices, userServices: UserServices ) = object : ViewModelProvider.Factory {
         @Suppress("UNCHECKED_CAST")
             override fun <T : ViewModel> create(modelClass: Class<T>): T {
                 return LobbyViewModel(lobbyServices, userServices) as T
             }
         }
     }

}

sealed interface LobbyScreenState {
    data object Loading : LobbyScreenState
    data object Creation : LobbyScreenState
    data class JoinedLobby(val lobby : Lobby) : LobbyScreenState
    data class LobbiesList(val lobby : Flow<List<Lobby>>) : LobbyScreenState
}

sealed class LobbyError(override val message: String?): DomainError {
    data object NoError: LobbyError(null)
    data object LobbyNotFound: DomainError, LobbyError("Lobby not found")
}