package pt.isel.pdm.match.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.match.innerComposable.RoundScreen
import pt.isel.pdm.match.viewModels.MatchGlobalStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel

@Composable
fun MatchScreen(
    matchViewModel: MatchViewModel,
    onMatchEnded: () -> Unit
) {
    val globalUiState by matchViewModel.stateUi.collectAsStateWithLifecycle()
    when (globalUiState) {
        is MatchGlobalStateUi.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is MatchGlobalStateUi.Finished -> {
            MatchFinishedView(
                onExit = onMatchEnded
            )
        }
        is MatchGlobalStateUi.Elapsed -> {
            MatchActiveGameTable(
                matchViewModel = matchViewModel,
                content = {
                    RoundScreen(matchViewModel = matchViewModel)
                }
            )
        }
    }
}

@Composable
private fun MatchActiveGameTable(
    matchViewModel: MatchViewModel,
    content: @Composable () -> Unit
) {
    var showMatchDetails by remember { mutableStateOf(false) }

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
                Box(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxHeight()
                        .background(Color.Cyan.copy(alpha = 0.8f))
                ) {
                    Text("Match Details Here", modifier = Modifier.align(Alignment.Center))
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
    }
}

@Composable
private fun MatchFinishedView(onExit: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "GAME OVER",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onExit) {
                Text("Back to Menu")
            }
        }
    }
}