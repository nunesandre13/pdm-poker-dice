package pt.isel.pdm.lobby.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig

@Composable
fun LobbyCreationView(
    onCreateLobby: (Lobby) -> Unit,
    onBack: () -> Unit = {}
) {
    var lobbyName by remember { mutableStateOf("") }
    var numOfPlayers by remember { mutableIntStateOf(0) }

    DefaultBackGround({
        OutlinedTextField(
            value = numOfPlayers.toString(),
            onValueChange = { numOfPlayers = it.toInt() },
            label = { Text("Number of Players:") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = lobbyName,
            onValueChange = { lobbyName = it },
            label = { Text("Name of lobby") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onCreateLobby(Lobby(
                lobbyName,numOfPlayers,emptyList()
            )) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Lobby")
        }
    },
    topBarConfig = TopBarConfig.WithBack(
        title = "Create Lobby",
        onBack = onBack
    ),
        modifier = Modifier
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LobbyCreationViewPreview() {
    LobbyCreationView(onCreateLobby = {})
}
