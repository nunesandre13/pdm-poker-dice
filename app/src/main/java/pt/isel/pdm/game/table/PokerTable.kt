package pt.isel.pdm.game.table

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.chelasmulti_playerpokerdice.R

@Composable
fun PokerTableSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val tableOuterColor = colorResource(id = R.color.table_outer)
    val tableInner1Color = colorResource(id = R.color.table_inner1)
    val tableInner2Color = colorResource(id = R.color.table_inner2)
    val tableLineColor = colorResource(id = R.color.white).copy(alpha = 0.25f)

    Box(modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val corner = CornerRadius(size.height / 2f, size.height / 2f)
            drawRoundRect(
                color = tableOuterColor,
                size = size,
                cornerRadius = corner
            )
            val inset = size.minDimension * 0.04f
            drawRoundRect(
                brush = Brush.radialGradient(
                    colors = listOf(tableInner1Color, tableInner2Color),
                    center = center,
                    radius = size.minDimension / 1.2f
                ),
                topLeft = Offset(inset, inset),
                size = Size(size.width - inset * 2f, size.height - inset * 2f),
                cornerRadius = CornerRadius(corner.x - inset, corner.y - inset)
            )

            val lineInset = inset * 2f
            drawRoundRect(
                color = tableLineColor,
                topLeft = Offset(lineInset, lineInset),
                size = Size(size.width - lineInset * 2f, size.height - lineInset * 2f),
                cornerRadius = CornerRadius(corner.x - lineInset, corner.y - lineInset),
                style = Stroke(width = inset * 0.6f)
            )
        }
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PokerTableSurfacePreview() {
    PokerTableSurface(modifier = Modifier.fillMaxSize())
}
