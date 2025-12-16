package pt.isel.pdm.match.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R

@Composable
fun Cup(
    offsetX: Float,
    offsetY: Float,
    rotation: Float,
    onCupSized: (IntSize) -> Unit,
    onClick: () -> Unit,
    enable: Boolean,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.cup),
        contentDescription = "Dice cup",
        modifier = modifier
            .padding(16.dp)
            .size(130.dp)
            .onGloballyPositioned { coordinates ->
                onCupSized(coordinates.size)
            }
            .graphicsLayer(
                translationX = offsetX,
                translationY = offsetY,
                rotationZ = rotation,
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            )
            .clickable(
                enabled = enable,
                onClick = onClick
            )
    )
}
