package pt.isel.pdm.lobby.lobbyUi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.lobby.LobbyError
import pt.isel.pdm.lobby.LobbyScreenState
import pt.isel.pdm.lobby.LobbyViewModel
import pt.isel.pdm.lobby.services.LobbyServiceMock
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp
import pt.isel.pdm.user.services.UsersServiceMock

@Composable
fun LobbyScreen(viewModel: LobbyViewModel, goBack: () -> Unit) {
    LobbyScreenContent(viewModel, goBack)
    LobbyScreenError(viewModel)
}


@Composable
private fun LobbyScreenContent(viewModel: LobbyViewModel, goBack: () -> Unit) {
    when (val stateUi = viewModel.stateUi.collectAsState().value) {

        is LobbyScreenState.Loading -> {}

        is LobbyScreenState.Creation ->
            LobbyCreationView(
                onCreateLobby = { viewModel.createLobby(it) },
                onBack = {viewModel.goToLobbiesList()}
            )

        is LobbyScreenState.JoinedLobby ->
            LobbyView(
                lobby = stateUi.lobby,
                onLeave = {viewModel.leaveLobby(stateUi.lobby)}
            )

        is LobbyScreenState.LobbiesList -> {
            val lobbyList = stateUi.lobby.collectAsState(emptyList()).value
            LobbyListView(
                lobbies = lobbyList,
                onJoinClick = {viewModel.joinLobby(it)},
                onBack = {goBack()},
                onCreateLobby = {viewModel.goToCreation()})
        }
    }
}

@Composable
private fun LobbyScreenError(viewModel: LobbyViewModel) {
    when (val stateError = viewModel.errorState.collectAsState().value) {
        is LobbyError.NoError -> {}
        is LobbyError.LobbyNotFound -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLobbyScreen() {
    val viewModel = LobbyViewModel(LobbyServiceMock(), UsersServiceMock())
    LobbyScreen(viewModel = viewModel, goBack = {})
}