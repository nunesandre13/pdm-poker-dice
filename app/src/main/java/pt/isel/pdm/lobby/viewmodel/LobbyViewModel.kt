package pt.isel.pdm.lobby.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.domain.state.LobbyScreenState
import pt.isel.pdm.lobby.services.LobbyServices
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.ViewModelBase
import pt.isel.pdm.utils.ViewModelState
import pt.isel.pdm.utils.onOutCome
import pt.isel.pdm.utils.runOperation

class LobbyViewModel(
    private val lobbyService: LobbyServices,
    private val userService: UserServices,
    viewModelState: ViewModelState<LobbyScreenState, LobbyError>
) : ViewModel(), ViewModelState<LobbyScreenState, LobbyError> by viewModelState {

    private val lobbiesListStateFlow : StateFlow<List<Lobby>> = lobbyService.listAvailableLobbies().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    init {
        goToLobbiesList()
    }

    fun joinLobby(lobby: Lobby) {
        viewModelScope.launch {
            runOperation(stateUi.value) {
                navigateTo(LobbyScreenState.Loading)
                lobbyService.joinLobby(lobby).onOutCome(
                    onSuccess = { updatedLobby ->
                        LobbyScreenState.JoinedLobby(updatedLobby.toState(lobby))
                    },
                    onFailure = { error ->
                        emitError(error)
                        null
                    }
                )
            }.let { navigateTo(it) }
        }
    }

    fun createLobby(lobby: LobbyCreation) {
        viewModelScope.launch {
            runOperation(stateUi.value) {
                navigateTo(LobbyScreenState.Loading)
                lobbyService.createNewLobby(lobby)
                    .onOutCome(
                        onSuccess = { createdLobby ->
                            LobbyScreenState.JoinedLobby(createdLobby.second.toState(createdLobby.first))
                                    },
                        onFailure = { error ->
                            emitError(error)
                            null
                        }
                    )
            }.let { navigateTo(it) }
        }
    }

    fun goToLobbiesList() {
        navigateTo(LobbyScreenState.Loading).also {
            navigateTo(LobbyScreenState.LobbiesList(lobbiesListStateFlow))
        }
    }

    fun goToCreation() {
        navigateTo(LobbyScreenState.Creation)
    }

    fun leaveLobby(lobby: Lobby) {
        viewModelScope.launch {
            runOperation(stateUi.value) {
                navigateTo(LobbyScreenState.Loading)
                userService.getCurrentUser()?.let { user ->
                    lobbyService.leaveLobby(lobby, user.id).onOutCome(
                        onSuccess = {
                            LobbyScreenState.LobbiesList(lobbiesListStateFlow)
                        },
                        onFailure = { error ->
                            emitError(error)
                            null
                        }
                    )
                } ?: run {
                    emitError(LobbyError.LobbyNotFound)
                    null
                }
            }.let { navigateTo(it) }
        }
    }

    fun Flow<Lobby>.toState(lobby: Lobby) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = lobby
    )

    override fun onCleared() {
        super.onCleared()
        Log.v("HTTP_LOBBIES_onCleared", "ViewModel destruído - Cancelando scope")
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
