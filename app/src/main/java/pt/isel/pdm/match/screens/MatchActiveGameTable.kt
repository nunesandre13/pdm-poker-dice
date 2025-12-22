package pt.isel.pdm.match.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.domain.PlayerMatchStateWithName
import pt.isel.pdm.match.ui.PlayerDetailsDialog
import pt.isel.pdm.match.ui.PlayerListItem
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.MatchViewModel

@Composable
fun MatchActiveGameTable(
    matchViewModel: MatchViewModel,
    content: @Composable () -> Unit
) {
    var showMatchDetails by remember { mutableStateOf(false) }

    var selectedPlayer by remember { mutableStateOf<PlayerMatchStateWithName?>(null) }

    val pokerTableContent = remember(matchViewModel) {
        movableContentOf {
            content()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.table_bg_light),
                        colorResource(R.color.table_bg_dark)
                    )
                )
            )
    ) {
        if (showMatchDetails) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                        .padding(top = 50.dp)
                ) {
                    val matchState by matchViewModel.matchState.collectAsStateWithLifecycle()
                    Text(
                        text = "PLAYERS",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    if (matchState is MatchState.ActualMatch) {
                        val match = (matchState as MatchState.ActualMatch).match
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(match.players) { player ->
                                PlayerListItem(
                                    player = player,
                                    onClick = { selectedPlayer = player }
                                )
                            }
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
                Box(modifier = Modifier.weight(0.8f).fillMaxHeight()) {
                    pokerTableContent()
                }
            }
        } else {
            pokerTableContent()
        }
        Button(
            onClick = { showMatchDetails = !showMatchDetails },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text(
                text = if (showMatchDetails) "Close" else "Info",
                style = MaterialTheme.typography.labelSmall
            )
        }
        selectedPlayer?.let { player ->
            PlayerDetailsDialog(
                player = player,
                onDismiss = { selectedPlayer = null }
            )
        }
    }
}
