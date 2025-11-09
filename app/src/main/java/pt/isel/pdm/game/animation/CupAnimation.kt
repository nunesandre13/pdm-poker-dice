package pt.isel.pdm.game.animation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R

@Composable
fun DiceRollAnimationOverlay(
    onAnimationFinished: () -> Unit
) {
    var moved by remember { mutableStateOf(false) }
    var cupSize by remember { mutableStateOf(IntSize.Zero) }

    val offsetX by animateFloatAsState(
        targetValue = if (moved) -cupSize.width.toFloat() * 2.2f else 0f,
        animationSpec = tween(durationMillis = 2000),
        finishedListener = { if (moved) {
                moved = !moved
                onAnimationFinished()
            }
        }
    )

    val offsetY by animateFloatAsState(
        targetValue = if (moved) -cupSize.height.toFloat() * 1.1f else 0f,
        animationSpec = tween(durationMillis = 2000)
    )

    val rotation by animateFloatAsState(
        targetValue = if (moved) -85f else 0f,
        animationSpec = tween(durationMillis = 1200,
            delayMillis = 600,
            easing = LinearOutSlowInEasing)
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Image(
            painter = painterResource(id = R.drawable.cup),
            contentDescription = "Dice cup",
            modifier = Modifier
                .padding(16.dp)
                .size(130.dp)
                .onGloballyPositioned { coordinates ->
                    cupSize = coordinates.size
                }
                .graphicsLayer(
                    translationX = offsetX,
                    translationY = offsetY,
                    rotationZ = rotation,
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                )
                .clickable { moved = !moved }
        )
    }
}


@Preview
@Composable
fun DiceRollAnimationOverlayPreview() {
    DiceRollAnimationOverlay(onAnimationFinished = {})
}