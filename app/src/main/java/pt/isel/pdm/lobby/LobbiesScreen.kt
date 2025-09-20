package pt.isel.pdm.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LobbyListScreen(
    lobbies: List<String>,
    onJoinClick: (String) -> Unit,
    onBack: () -> Unit = {},
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = { onBack() }) {
            Icon(
                Icons.Default.ArrowBack
                ,contentDescription = "Back to Menu",
            )
        }
        lobbies.forEach { lobby ->
            Spacer(modifier = Modifier.height(32.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Red),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = lobby, style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { onJoinClick(lobby) }) {
                        Text("Join")
                    }
                }
            }
        }
        Button(onClick = {
        }) {
            Text("Create Lobby")
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    LobbyListScreen(
        lobbies = listOf("Lobby 1", "Lobby 2", "Lobby 3"),
        onJoinClick = {},
        onBack = {}
    )
}

