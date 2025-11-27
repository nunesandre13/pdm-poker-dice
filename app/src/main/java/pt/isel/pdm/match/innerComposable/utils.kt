package pt.isel.pdm.match.innerComposable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.geometry.Rect
import pt.isel.pdm.domain.PlayerRoundState
@Composable
fun DrawOnPlayers(
    players: List<PlayerRoundState>,
    registry: PlayerRegistry,
    content: @Composable (playerState: PlayerRoundState, modifier: Modifier) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        players.forEach { playerState ->
            val bounds = registry[playerState.playerId]
            if (bounds != null) {
                content(
                    playerState,
                    Modifier.applyBounds(bounds)
                )
            }
        }
    }
}

@Composable
fun Modifier.applyBounds(bounds: Rect): Modifier {
    val density = LocalDensity.current
    return this
        .absoluteOffset {
            IntOffset(bounds.left.toInt(), bounds.top.toInt())
        }
        .size(
            width = with(density) { bounds.width.toDp() },
            height = with(density) { bounds.height.toDp() }
        )
}