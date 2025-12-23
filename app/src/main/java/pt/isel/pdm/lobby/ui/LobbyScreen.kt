package pt.isel.pdm.lobby.ui

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.domain.state.LobbyScreenState
import pt.isel.pdm.lobby.repository.RepositoryLobbiesMock
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.lobby.viewmodel.LobbyViewModel
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.utils.PlayersNameCache
import pt.isel.pdm.utils.ViewModelBase

@Composable
fun LobbyScreen(viewModel: LobbyViewModel, goBack: () -> Unit, onUp: (matchId: String) -> Unit) {
    LobbyScreenContent(viewModel, goBack,onUp)
    LobbyScreenError(viewModel)
}

@Composable
private fun LobbyScreenContent(viewModel: LobbyViewModel, goBack: () -> Unit, onUp: (matchId: String)-> Unit) {
    when (val stateUi = viewModel.stateUi.collectAsState().value) {

        is LobbyScreenState.Loading -> {

        }

        is LobbyScreenState.Creation ->
            LobbyCreationView(
                onCreateLobby = { viewModel.createLobby(it) },
                onBack = {viewModel.goToLobbiesList()}
            )

        is LobbyScreenState.JoinedLobby -> {
            val lobby = stateUi.lobby.collectAsState().value
            LaunchedEffect(lobby) {
                val matchId = lobby.matchId
                if ( matchId != null) { onUp(matchId)}
            }
            LobbyView(
                lobby = lobby,
                onLeave = { viewModel.leaveLobby(lobby) }
            )

        }
        is LobbyScreenState.LobbiesList -> {
            val lobbyList = stateUi.lobby.collectAsState().value
            LobbyListView(
                lobbies = lobbyList,
                onJoinClick = {viewModel.joinLobby(it)},
                onBack = {goBack()},
                onCreateLobby = {viewModel.goToCreation()}
            )
        }
    }
}

@Composable
private fun LobbyScreenError(viewModel: LobbyViewModel) {
    when (val stateError = viewModel.errorState.collectAsState().value) {
        is LobbyError.NoError -> {}
        is LobbyError.LobbyNotFound,is LobbyError.NetWorkError, is LobbyError.LobbyFull  -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLobbyScreen() {
    val lobbyService = LobbyServiceImp(
        repository = RepositoryLobbiesMock(shouldFail = false),
        playersNameCache = PlayersNameCache(),
        userService = UsersServiceMock()
    )
    val mockLobbiesFlow = MutableStateFlow(emptyList<Lobby>())
    val initialState = LobbyScreenState.LobbiesList(mockLobbiesFlow)

    val viewModel = LobbyViewModel(
        lobbyService = lobbyService,
        userService = UsersServiceMock(),
        viewModelState = ViewModelBase(initialState, LobbyError.NoError)
    )

    MaterialTheme {
        LobbyScreen(
            viewModel = viewModel,
            goBack = { },
            onUp = { }
        )
    }
}