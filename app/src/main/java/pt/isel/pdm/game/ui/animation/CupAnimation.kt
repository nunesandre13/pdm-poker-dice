package pt.isel.pdm.game.ui.animation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import pt.isel.pdm.game.ui.Cup

@Composable
fun DiceRollAnimationOverlay(
    startAnimation: Boolean,
    onClick: () -> Unit,
    onAnimationFinished: () -> Unit
) {
    var cupSize by remember { mutableStateOf(IntSize.Zero) }

    val offsetX by animateFloatAsState(
        targetValue = if (startAnimation) -cupSize.width.toFloat() * 2.2f else 0f,
        animationSpec = tween(durationMillis = 2000),
        finishedListener = {
            if (startAnimation) {
                onAnimationFinished()
            }
        }
    )

    val offsetY by animateFloatAsState(
        targetValue = if (startAnimation) -cupSize.height.toFloat() * 1.1f else 0f,
        animationSpec = tween(durationMillis = 2000)
    )

    val rotation by animateFloatAsState(
        targetValue = if (startAnimation) -85f else 0f,
        animationSpec = tween(
            durationMillis = 1200,
            delayMillis = 600,
            easing = LinearOutSlowInEasing
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Cup(
            offsetX = offsetX,
            offsetY = offsetY,
            rotation = rotation,
            onCupSized = { cupSize = it },
            onClick = onClick
        )
    }
}


@Preview
@Composable
fun DiceRollAnimationOverlayPreview() {
    var start by remember { mutableStateOf(false) }
    DiceRollAnimationOverlay(
        startAnimation = start,
        onClick = { start = !start },
        onAnimationFinished = { start = false }
    )
}
