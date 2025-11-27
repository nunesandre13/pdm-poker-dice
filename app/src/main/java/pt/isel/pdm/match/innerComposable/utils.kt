package pt.isel.pdm.match.innerComposable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.PlayerRoundState
@Composable
fun DrawOnPlayers(
    players: List<PlayerRoundState>,
    registry: PlayerRegistry,
    content: @Composable (playerState: PlayerRoundState, modifier: Modifier) -> Unit
) {
    players.forEach { playerState ->
        val bounds = registry[playerState.playerId]
        if (bounds != null) {
            Box {
                content(
                    playerState,
                    Modifier.applyBounds(bounds)
                )
            }
        }
    }
}
fun Modifier.applyBounds(bounds: Rect): Modifier {
    return this
        .absoluteOffset {
            IntOffset(bounds.left.toInt(), bounds.top.toInt())
        }
        .size(
            width = bounds.width.dp,
            height = bounds.height.dp
        )
}
