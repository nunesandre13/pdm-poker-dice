package pt.isel.pdm.game.animation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R

@Composable
fun DiceRollAnimationOverlay(
    onAnimationFinished: () -> Unit
) {
    var hasStarted by remember { mutableStateOf(false) }

    val targetOffsetX = (-250).dp
    val targetOffsetY = (-180).dp

    val offsetX by animateDpAsState(
        targetValue = if (hasStarted) targetOffsetX else 0.dp,
        label = "offsetXAnimation",
        animationSpec = tween(durationMillis = 1500)
    )

    val offsetY by animateDpAsState(
        targetValue = if (hasStarted) targetOffsetY else 0.dp,
        label = "offsetYAnimation",
        animationSpec = tween(durationMillis = 2000)
    )

    val rotation by animateFloatAsState(
        targetValue = if (hasStarted) -15f else 0f,
        animationSpec = tween(durationMillis = 2000),
        label = "rotationAnimation"
    )

    LaunchedEffect(offsetX, offsetY) {
        if (offsetX == targetOffsetX && offsetY == targetOffsetY) {
            onAnimationFinished()
        }
    }

    LaunchedEffect(key1 = true) {
        hasStarted = true
    }

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
                .graphicsLayer(
                    rotationZ = rotation,
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                )
                .offset(offsetX, offsetY)
        )
    }
}

@Preview
@Composable
fun DiceRollAnimationOverlayPreview() {
    DiceRollAnimationOverlay(onAnimationFinished = {})
}