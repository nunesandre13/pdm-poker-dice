package pt.isel.pdm.lobby.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.saveable.rememberSaveable
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig

@Composable
fun LobbyCreationView(
    onCreateLobby: (LobbyCreation) -> Unit,
    onBack: () -> Unit = {},
) {
    var lobbyName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var minPlayers by rememberSaveable { mutableStateOf("") }
    var maxPlayers by rememberSaveable { mutableStateOf("") }
    var numberOfRounds by rememberSaveable { mutableStateOf("") }
    var firstAnte by rememberSaveable { mutableStateOf("") }


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
                    text = "Create Lobby",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Set lobby details",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        },
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = lobbyName,
                    onValueChange = { lobbyName = it },
                    label = { Text("Lobby name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = minPlayers,
                    onValueChange = { minPlayers = it.filter(Char::isDigit) },
                    label = { Text("Minimum players") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = maxPlayers,
                    onValueChange = { maxPlayers = it.filter(Char::isDigit) },
                    label = { Text("Maximum players") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = numberOfRounds,
                    onValueChange = { numberOfRounds = it.filter(Char::isDigit) },
                    label = { Text("Number of rounds") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = firstAnte,
                    onValueChange = { firstAnte = it.filter(Char::isDigit) },
                    label = { Text("First ante") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = MaterialTheme.shapes.medium,
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
                        onClick = {
                            onCreateLobby(
                                LobbyCreation(
                                    name = lobbyName.trim(),
                                    description = description.trim(),
                                    minPlayer = minPlayers.toIntOrNull()!!,
                                    maxPlayer = maxPlayers.toIntOrNull()!!,
                                    numberOfRounds = numberOfRounds.toIntOrNull()!!,
                                    firstAnte = firstAnte.toIntOrNull()!!
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = "Create Lobby")
                    }
                }
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
    LobbyCreationView(onCreateLobby = {}, onBack = {})
}
