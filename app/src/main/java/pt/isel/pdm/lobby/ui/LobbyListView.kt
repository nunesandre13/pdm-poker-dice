package pt.isel.pdm.lobby.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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

@Composable
fun LobbyListView(
    lobbies: List<Lobby>,
    onJoinClick: (Lobby) -> Unit,
    onBack: () -> Unit = {},
    onCreateLobby: () -> Unit = {}
) {
    DefaultBackGround(
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.lobbies),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.Desclobbies),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        },
        {
            Box(modifier = Modifier.fillMaxWidth()) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lobbies) { lobby ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = lobby.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stringResource(R.string.lobby_players_count, lobby.players.size, lobby.maxPlayer),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Button(
                                    onClick = { onJoinClick(lobby) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(stringResource(R.string.join))
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        },
        {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onCreateLobby,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(stringResource(R.string.createLobby))
                    }
                }
            }
        },
        topBarConfig = TopBarConfig.WithBack(
            title = stringResource(R.string.lobbies),
            onBack = onBack
        ),
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LobbyListPreview() {
    val players = listOf(
        User(UserId(1), Name("Player 1"), Email("teste@email")).toPlayerInfo(),
        User(UserId(2), Name("Player 2"), Email("teste2@email")).toPlayerInfo(),
        User(UserId(3), Name("Player 3"), Email("teste3@email")).toPlayerInfo()
    )

    val lobbyList = listOf(
        Lobby(LobbyId(1), "Lobby Beginner", "Ideal for new players", players, UserId(1), 4, 2, 3, 10, null, LobbyStatus.OPEN),
        Lobby(LobbyId(2), "High Stakes", "Only for pros", players.take(2), UserId(2), 8, 2, 5, 50, null, LobbyStatus.OPEN),
        Lobby(LobbyId(3), "Final Table", "Tournament final", players, UserId(3), 3, 2, 10, 100, "match_123", LobbyStatus.IN_GAME)
    )
    MaterialTheme {
        LobbyListView(
            lobbies = lobbyList,
            onJoinClick = { },
            onBack = { },
            onCreateLobby = { }
        )
    }
}