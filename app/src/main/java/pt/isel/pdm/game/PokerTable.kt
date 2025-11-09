package pt.isel.pdm.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PokerTableSurface(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val corner = CornerRadius(size.height / 2f, size.height / 2f)
        drawRoundRect(
            color = Color(0xFF5D4037),
            size = size,
            cornerRadius = corner
        )
        val inset = size.minDimension * 0.04f
        drawRoundRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20)),
                center = center,
                radius = size.minDimension / 1.2f
            ),
            topLeft = Offset(inset, inset),
            size = Size(size.width - inset * 2f, size.height - inset * 2f),
            cornerRadius = CornerRadius(corner.x - inset, corner.y - inset)
        )

        val lineInset = inset * 2f
        drawRoundRect(
            color = Color.White.copy(alpha = 0.25f),
            topLeft = Offset(lineInset, lineInset),
            size = Size(size.width - lineInset * 2f, size.height - lineInset * 2f),
            cornerRadius = CornerRadius(corner.x - lineInset, corner.y - lineInset),
            style = Stroke(width = inset * 0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PokerTableSurfacePreview() {
    PokerTableSurface(modifier = Modifier.fillMaxSize())
}

