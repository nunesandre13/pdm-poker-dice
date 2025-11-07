package pt.isel.pdm.lobby.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.User
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig

@Composable
fun LobbyView(
    lobby: Lobby?,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier
) {
    DefaultBackGround(
        {
            lobby?.let {
                Column {
                    Text("Nome: ${it.name}")
                    Text("Máx. Jogadores: ${it.maxPlayers}")
                    Text("Jogadores: ${it.players.joinToString { user -> user.name }}")
                    Button(onClick = onLeave) {
                        Text("Sair do Lobby")
                    }
                }
            } ?: Text("A carregar lobby...")
        },
        topBarConfig = TopBarConfig.WithBack(
            title = "Lobby",
            onBack = onLeave
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun LobbyScreenPreview() {
    val fakeLobby = Lobby(
        name = "Exemplo",
        maxPlayers = 4,
        players = listOf(
            User("1233","Alice", Email("Alice@gamail.com")),
            User("1234444","Bob",Email("Bob@gmail.com"))
        )
    )
    LobbyView(
        fakeLobby,
        onLeave = {},
        modifier = Modifier
    )
}