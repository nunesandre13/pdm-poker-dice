package pt.isel.pdm.match.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.domain.MatchStatus
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.match.viewModels.interfaces.MatchStateProvider

@Composable
fun MatchFinishedView(
    matchStateProvider: MatchStateProvider,
    onExit: () -> Unit
) {
    val matchState by matchStateProvider.matchState.collectAsStateWithLifecycle()

    val winner = if (matchState is MatchState.ActualMatch &&
        (matchState as MatchState.ActualMatch).match.matchStatus == MatchStatus.FINISHED) {
        (matchState as MatchState.ActualMatch).match.players.maxByOrNull { it.coins }
    } else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "GAME OVER",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (winner != null) {
                Text(
                    text = "Winner:",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Text(
                    text = winner.name.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Green
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${winner.coins} coins",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Yellow
                )
            } else {
                Text(
                    text = "No winner",
                    color = Color.White,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = onExit) {
                Text("Back to Menu")
            }
        }
    }
}