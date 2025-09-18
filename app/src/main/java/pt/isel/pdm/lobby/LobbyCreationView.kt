package pt.isel.pdm.lobby


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LobbyCreationView(onCreateLobby: (String) -> Unit) {
    var lobbyName by remember { mutableStateOf("") } //simples estado

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Create Lobby",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lobbyName,
            onValueChange = { lobbyName = it },
            label = { Text("Name do Lobbyyyyyyyyyyyy") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onCreateLobby(lobbyName) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Lobby")
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LobbyCreationViewPreview() {
    LobbyCreationView(onCreateLobby = {})
}
