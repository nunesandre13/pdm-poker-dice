package pt.isel.pdm.lobby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.domain.state.LobbyScreenState
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState

class LobbyViewModel(
    private val lobbyService: LobbyServices,
    private val userService: UserServices,
    viewModelState: ViewModelState<LobbyScreenState, LobbyError>
) : ViewModel(), ViewModelState<LobbyScreenState, LobbyError> by viewModelState {

    init {
        getLobbies()
    }

    fun joinLobby(lobby: Lobby) {
        viewModelScope.launch {
            navigateTo(LobbyScreenState.JoinedLobby(lobby))
            lobbyService.joinLobby(lobby).collect { updatedLobby ->
                navigateTo(LobbyScreenState.JoinedLobby(updatedLobby))
            }
        }
    }

    fun createLobby(lobby: Lobby) {
        viewModelScope.launch {
            userService.getCurrentUser()?.let { user ->
                val lobbyWithCreator = lobby.copy(players = listOf(user))
                if (lobbyService.createNewLobby(lobbyWithCreator)) {
                    joinLobby(lobbyWithCreator)
                } else {
                    emitError(LobbyError.LobbyNotFound)
                }
            } ?: emitError(LobbyError.LobbyNotFound)
        }
    }

    fun goToLobbiesList() {
        navigateTo(LobbyScreenState.Loading).also {
            getLobbies()
        }
    }

    fun goToCreation() {
        navigateTo(LobbyScreenState.Creation)
    }

    private fun getLobbies(): StateFlow<List<Lobby>> {
        return lobbyService.listAvailableLobbies().also { stateFlow ->
            navigateTo(LobbyScreenState.LobbiesList(stateFlow))
        }
    }

    fun leaveLobby(lobby: Lobby) {
        viewModelScope.launch {
            userService.getCurrentUser()?.let { user ->
                if (lobbyService.leaveLobby(lobby, user.id)) {
                    navigateTo(LobbyScreenState.Loading).also {
                        getLobbies()
                    }
                } else {
                    emitError(LobbyError.LobbyNotFound)
                }
            }
        }
    }

    companion object {
        fun getFactory(lobbyServices: LobbyServices, userServices: UserServices) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LobbyViewModel(
                        lobbyServices,
                        userServices,
                        ViewModelBase(LobbyScreenState.Loading, LobbyError.NoError)
                    ) as T
                }
            }
    }
}
