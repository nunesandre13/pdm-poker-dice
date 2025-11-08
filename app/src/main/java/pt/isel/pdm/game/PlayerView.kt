package pt.isel.pdm.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PlayerView(player: Player, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .drawBehind {
                val glow = size.minDimension * 0.6f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x552E7D32), Color.Transparent)
                    ),
                    radius = glow,
                    center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height)
                )
            }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 84.dp)
                .shadow(12.dp, shape, clip = false)
                .clip(shape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E3A2A), Color(0xFF0D2017))
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0x66FFFFFF), Color(0x22FFFFFF))
                    ),
                    shape = shape
                )
                .drawBehind {
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.10f),
                        style = Stroke(width = 2.dp.toPx()),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx()),
                        topLeft = androidx.compose.ui.geometry.Offset(6.dp.toPx(), 6.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(
                            width = size.width - 12.dp.toPx(),
                            height = size.height - 12.dp.toPx()
                        )
                    )
                }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            val containerWidth = maxWidth * 0.6f
            val fontSize = (containerWidth.value / 7f).sp
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Player: ${player.id}",
                    textAlign = TextAlign.Center,
                    fontSize = fontSize,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFE8F5E9)
                )
                DisplayAdversaryDices(
                    dicesHand = player.hand,
                    size = containerWidth
                )
            }
        }
    }
}
