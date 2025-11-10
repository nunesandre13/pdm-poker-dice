package pt.isel.pdm.lobby.ui

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.User
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.ui.author.PlayerItem

@Composable
fun LobbyView(
    lobby: Lobby?,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier
) {
    DefaultBackGround({
        if (lobby == null) TODO()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(lobby.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text("Máx.Players: ${lobby.maxPlayers}", style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = onLeave) {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Leave")
                            }
                        }
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(lobby.players) { user ->
                                PlayerItem(user)
                            }
                        }
                    }
                }
            }
        }
        ,
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
        lobby = fakeLobby,
        onLeave = {},
        modifier = Modifier
    )
}
