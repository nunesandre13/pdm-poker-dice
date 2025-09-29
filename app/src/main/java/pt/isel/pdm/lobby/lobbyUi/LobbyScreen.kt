package pt.isel.pdm.lobby.lobbyUi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.lobby.LobbyScreenState
import pt.isel.pdm.lobby.LobbyViewModel
import pt.isel.pdm.lobby.services.LobbyServiceMock

@Composable
fun LobbyScreen(viewModel: LobbyViewModel, goBack: () -> Unit) {
    val stateUi =viewModel.stateUi.collectAsState().value
    when (stateUi) {

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

@Preview(showBackground = true)
@Composable
fun PreviewLobbyScreen() {
    val viewModel = LobbyViewModel(LobbyServiceMock())
    LobbyScreen(viewModel = viewModel, goBack = {})
}