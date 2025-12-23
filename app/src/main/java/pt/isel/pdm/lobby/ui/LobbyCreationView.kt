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
import androidx.compose.ui.res.stringResource
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.topBar.TopBarConfig


data class LobbyCreation(
    val name: String,
    val description: String,
    val minPlayer: Int,
    val maxPlayer: Int,
    val numberOfRounds: Int,
    val firstAnte: Int
)

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

    LobbyCreationContent(
        lobbyName = lobbyName,
        onLobbyNameChange = { lobbyName = it },
        description = description,
        onDescriptionChange = { description = it },
        minPlayers = minPlayers,
        onMinPlayersChange = { minPlayers = it.filter(Char::isDigit) },
        maxPlayers = maxPlayers,
        onMaxPlayersChange = { maxPlayers = it.filter(Char::isDigit) },
        numberOfRounds = numberOfRounds,
        onNumberOfRoundsChange = { numberOfRounds = it.filter(Char::isDigit) },
        firstAnte = firstAnte,
        onFirstAnteChange = { firstAnte = it.filter(Char::isDigit) },
        onCreateLobby = onCreateLobby,
        onBack = onBack
    )
}

@Composable
fun LobbyCreationContent(
    lobbyName: String,
    onLobbyNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    minPlayers: String,
    onMinPlayersChange: (String) -> Unit,
    maxPlayers: String,
    onMaxPlayersChange: (String) -> Unit,
    numberOfRounds: String,
    onNumberOfRoundsChange: (String) -> Unit,
    firstAnte: String,
    onFirstAnteChange: (String) -> Unit,
    onCreateLobby: (LobbyCreation) -> Unit,
    onBack: () -> Unit,
) {
    DefaultBackGround(
        { HeaderSection() },
        { FormFieldsSection(
            lobbyName = lobbyName,
            onLobbyNameChange = onLobbyNameChange,
            description = description,
            onDescriptionChange = onDescriptionChange,
            minPlayers = minPlayers,
            onMinPlayersChange = onMinPlayersChange,
            maxPlayers = maxPlayers,
            onMaxPlayersChange = onMaxPlayersChange,
            numberOfRounds = numberOfRounds,
            onNumberOfRoundsChange = onNumberOfRoundsChange,
            firstAnte = firstAnte,
            onFirstAnteChange = onFirstAnteChange
        ) },
        { CreateButtonSection(
            lobbyName = lobbyName,
            description = description,
            minPlayers = minPlayers,
            maxPlayers = maxPlayers,
            numberOfRounds = numberOfRounds,
            firstAnte = firstAnte,
            onCreateLobby = onCreateLobby
        ) },
        topBarConfig = TopBarConfig.WithBack(
            title = stringResource(R.string.createLobby),
            onBack = onBack
        ),
        modifier = Modifier
    )
}

@Composable
private fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.createLobby),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.setLobbysDetails),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
private fun FormFieldsSection(
    lobbyName: String,
    onLobbyNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    minPlayers: String,
    onMinPlayersChange: (String) -> Unit,
    maxPlayers: String,
    onMaxPlayersChange: (String) -> Unit,
    numberOfRounds: String,
    onNumberOfRoundsChange: (String) -> Unit,
    firstAnte: String,
    onFirstAnteChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val invalidNumberError = stringResource(R.string.invalid_number)

        OutlinedTextField(
            value = lobbyName,
            onValueChange = onLobbyNameChange,
            label = { Text(stringResource(R.string.lobbyname)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text(stringResource(R.string.lobbydescription)) },
            modifier = Modifier.fillMaxWidth()
        )

        NumericTextField(
            value = minPlayers,
            onValueChange = onMinPlayersChange,
            label = stringResource(R.string.minPlayers),
            errorMessage = invalidNumberError
        )

        NumericTextField(
            value = maxPlayers,
            onValueChange = onMaxPlayersChange,
            label = stringResource(R.string.maxPlayers),
            errorMessage = invalidNumberError
        )

        NumericTextField(
            value = numberOfRounds,
            onValueChange = onNumberOfRoundsChange,
            label = stringResource(R.string.numOfRounds),
            errorMessage = invalidNumberError
        )

        NumericTextField(
            value = firstAnte,
            onValueChange = onFirstAnteChange,
            label = stringResource(R.string.firstAnte),
            errorMessage = invalidNumberError
        )
    }
}

@Composable
private fun NumericTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String
) {
    val isError = value.isNotEmpty() && value.toIntOrNull() == null
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
@Composable
private fun CreateButtonSection(
    lobbyName: String,
    description: String,
    minPlayers: String,
    maxPlayers: String,
    numberOfRounds: String,
    firstAnte: String,
    onCreateLobby: (LobbyCreation) -> Unit
) {
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
            val minPlayerInt = minPlayers.toIntOrNull()
            val maxPlayerInt = maxPlayers.toIntOrNull()
            val numberOfRoundsInt = numberOfRounds.toIntOrNull()
            val firstAnteInt = firstAnte.toIntOrNull()
            val isValid = lobbyName.trim().isNotEmpty() &&
                    minPlayerInt != null &&
                    maxPlayerInt != null &&
                    numberOfRoundsInt != null &&
                    firstAnteInt != null

            Button(
                onClick = {
                    if (isValid) {
                        onCreateLobby(
                            LobbyCreation(
                                name = lobbyName.trim(),
                                description = description.trim(),
                                minPlayer = minPlayerInt,
                                maxPlayer = maxPlayerInt,
                                numberOfRounds = numberOfRoundsInt,
                                firstAnte = firstAnteInt
                            )
                        )
                    }
                },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = stringResource(R.string.createLobby))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LobbyCreationViewPreview() {
    LobbyCreationView(onCreateLobby = {}, onBack = {})
}

