package pt.isel.pdm.match.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.PlayerMatchStateWithName
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.PlayerId

@Composable
fun PlayerListItem(
    player: PlayerMatchStateWithName,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = player.name.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PlayerDetailsDialog(
    player: PlayerMatchStateWithName,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = player.name.name, style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Column {
                Text("ID: ${player.playerId.id}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Saldo atual: ${player.coins} moedas")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PlayerListItemPreview() {
    val fakePlayer = PlayerMatchStateWithName(
        playerId = PlayerId(123),
        name = Name("Gabriel Silva"),
        coins = 1500
    )

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            PlayerListItem(
                player = fakePlayer,
                onClick = {  }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerDetailsDialogPreview() {
    val fakePlayer = PlayerMatchStateWithName(
        playerId = PlayerId(456),
        name = Name("Alice Santos"),
        coins = 2500
    )

    var showDialog by remember { mutableStateOf(true) }
    MaterialTheme {
        if (showDialog) {
            PlayerDetailsDialog(
                player = fakePlayer,
                onDismiss = { showDialog = false }
            )
        }
    }
}