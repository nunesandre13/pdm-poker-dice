package pt.isel.pdm.lobby.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
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
import kotlin.toString

class LobbyViewModel(
    private val lobbyService: LobbyServices,
    private val userService: UserServices,
    viewModelState: ViewModelState<LobbyScreenState, LobbyError>
) : ViewModel(), ViewModelState<LobbyScreenState, LobbyError> by viewModelState {

    private val lobbiesListStateFlow : StateFlow<List<Lobby>> = lobbyService.listAvailableLobbies().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
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
                        LobbyScreenState.JoinedLobby(updatedLobby)
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
                            LobbyScreenState.JoinedLobby(createdLobby)
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
