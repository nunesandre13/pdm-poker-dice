package pt.isel.pdm.lobby.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.domain.state.LobbyScreenState
import pt.isel.pdm.lobby.repository.RepositoryLobbiesMock
import pt.isel.pdm.lobby.services.LobbyServiceImp
import pt.isel.pdm.lobby.viewmodel.LobbyViewModel
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.utils.ViewModelBase

@Composable
fun LobbyScreen(viewModel: LobbyViewModel, goBack: () -> Unit, onUp: () -> Unit) {
    LobbyScreenContent(viewModel, goBack,onUp)
    LobbyScreenError(viewModel)
}

@Composable
private fun LobbyScreenContent(viewModel: LobbyViewModel, goBack: () -> Unit, onUp: ()-> Unit) {
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
                if (lobby.maxPlayer == lobby.players.size) {
                    onUp()
                }
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
        is LobbyError.LobbyNotFound, is LobbyError.LobbyFull  -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
        LobbyError.NetWorkError -> TODO()
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun PreviewLobbyScreen() {
    val viewModel = LobbyViewModel(LobbyServiceImp(RepositoryLobbiesMock()), UsersServiceMock(),
        ViewModelBase(LobbyScreenState.Loading,LobbyError.NoError) )
    LobbyScreen(viewModel = viewModel, goBack = {}, onUp = {})
}