package pt.isel.pdm.match.innerComposable.screens.finished

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.match.viewModels.interfaces.RoundStateProvider

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