package pt.isel.pdm.lobby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyEvent
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.domain.state.LobbyScreenState
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState

class LobbyViewModel(
    private val lobbyService: LobbyServices,
    private val userService: UserServices,
    viewModelState: ViewModelState<LobbyScreenState,LobbyError>) : ViewModel(), ViewModelState<LobbyScreenState,LobbyError> by viewModelState  {

    init {
        getLobbies()
    }

    fun joinLobby(lobby: Lobby) {
        viewModelScope.launch {
            lobbyService.selectLobby(lobby).let { response ->
                if (response) {
                    viewModelScope.launch {
                        lobbyService.getLobbyEvent(lobby).let { event ->
                            event.collect { newEvent ->
                                if (stateUi.value is LobbyScreenState.JoinedLobby) {
                                    navigateTo(LobbyScreenState.JoinedLobby(handleNewState(newEvent)))
                                }
                            }
                        }
                    }
                    navigateTo(LobbyScreenState.JoinedLobby(lobby))
                } else {
                    emitError(LobbyError.LobbyNotFound)
                }
            }
        }
    }

    private fun handleNewState(newState: LobbyEvent): Lobby {
        when(newState) {
            is LobbyEvent.LobbyClosed -> TODO()
            is LobbyEvent.LobbyFull -> TODO()
            is LobbyEvent.MatchStarted -> TODO()
            is LobbyEvent.PlayerAdded -> TODO()
            is LobbyEvent.PlayerRemoved -> TODO()
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
        navigateTo(LobbyScreenState.Creation)
    }

    private fun getLobbies(): SharedFlow<List<Lobby>> {
        return lobbyService.listAvailableLobbies().also { sharedFlow ->
            navigateTo(LobbyScreenState.LobbiesList(sharedFlow))
        }
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
                 return LobbyViewModel(lobbyServices, userServices, ViewModelBase(LobbyScreenState.Loading,LobbyError.NoError) ) as T
             }
         }
     }

}

