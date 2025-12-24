package pt.isel.pdm.match.innerComposable.screens.finished

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.match.PlayerStatus
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.domain.match.RoundState
import pt.isel.pdm.domain.state.PlayerRoundStateWithName
import pt.isel.pdm.domain.state.Round
import pt.isel.pdm.match.viewModels.interfaces.RoundStateProvider
import pt.isel.pdm.domain.user.PlayerInfo

@Composable
fun FinishedScreen(roundStateProvider: RoundStateProvider) {
    val round = roundStateProvider.roundState.collectAsStateWithLifecycle()
    val winnerId = when (val rs = round.value?.state) {
        is RoundState.Finished -> rs.winner
        else -> null
    }
    val winner = round.value?.players?.find { it.playerId == winnerId }?.name?.name
    val infiniteTransition = rememberInfiniteTransition(label = "blink_animation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blink_alpha"
    )
    val displayText = if (winner != null) {
        "WINNER:\n$winner"
    } else {
        "DRAW"
    }
    val textColor = if (winner != null) Color.Yellow else Color.White
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = textColor,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(alpha)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FinishedScreenPreview() {
    val player1 = PlayerRoundStateWithName(playerId = PlayerId(1), name = Name("Alice"), coins = 100, playerStatus = PlayerStatus.NotStarted)
    val player2 = PlayerRoundStateWithName(playerId = PlayerId(2), name = Name("Bob"), coins = 50, playerStatus = PlayerStatus.NotStarted)
    val mockRound = Round(id = RoundId(1), state = RoundState.Finished(winner = PlayerId(1)), players = listOf(player1, player2))
    val mockProvider = object : RoundStateProvider {
        override val roundState: StateFlow<Round?> = MutableStateFlow(mockRound)
        override val player: StateFlow<PlayerInfo?> = MutableStateFlow(null)
    }
    MaterialTheme {
        FinishedScreen(roundStateProvider = mockProvider)
    }
}