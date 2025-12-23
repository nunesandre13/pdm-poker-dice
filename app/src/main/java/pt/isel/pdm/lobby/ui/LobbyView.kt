package pt.isel.pdm.lobby.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyId
import pt.isel.pdm.domain.LobbyStatus
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.domain.toPlayerInfo
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.ui.author.PlayerItem

@Composable
fun LobbyView(
    lobby: Lobby?,
    onLeave: () -> Unit,
    modifier: Modifier = Modifier
) {
    DefaultBackGround(
        {
            if (lobby == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@DefaultBackGround
            }

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
                                Text(
                                    text = lobby.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = stringResource(R.string.lobby_players, lobby.maxPlayer, lobby.players.size),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(onClick = onLeave) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                    contentDescription = stringResource(R.string.leave)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 0.dp, max = 300.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(lobby.players) { player ->
                                PlayerItem(playerInfo = player)
                            }
                        }

                    }
                }
            }
        },
        topBarConfig = TopBarConfig.WithBack(
            title = stringResource(R.string.lobby),
            onBack = onLeave
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LobbyViewPreview() {
    val fakePlayers = listOf(
        User(UserId(1), Name("Alice"), Email("alice@gmail.com")).toPlayerInfo(),
        User(UserId(2), Name("Bob"), Email("bob@gmail.com")).toPlayerInfo()
    )
    val fakeLobby = Lobby(
        id = LobbyId(1),
        name = "Poker Night",
        description = "Casual game with friends",
        players = fakePlayers,
        owner =UserId(1),
        maxPlayer = 4,
        minPlayer = 2,
        numberOdRounds = 5,
        firstAnte = 10,
        matchId = null,
        lobbyStatus = LobbyStatus.OPEN
    )

    MaterialTheme {
        LobbyView(
            lobby = fakeLobby,
            onLeave = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}