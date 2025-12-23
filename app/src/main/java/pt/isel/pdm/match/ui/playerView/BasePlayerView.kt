package pt.isel.pdm.match.ui.playerView

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BasePlayerView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val shape = RoundedCornerShape(18.dp)
    val glowColor = colorResource(id = R.color.playerview_circle)
    val backgroundGradient1 = colorResource(id = R.color.pv_backgroundBox1)
    val backgroundGradient2 = colorResource(id = R.color.pv_backgroundBox2)
    val borderGradient1 = colorResource(id = R.color.pv_backgroundBorder1)
    val borderGradient2 = colorResource(id = R.color.pv_backgroundBorder2)
    val innerBorderColor = colorResource(id = R.color.pv_backgroundBorder2)
    Box(
        modifier = modifier
            .drawBehind {
                val glow = size.minDimension * 0.6f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(glowColor, Color.Transparent)
                    ),
                    radius = glow,
                    center = Offset(size.width / 2f, size.height)
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
                        colors = listOf(backgroundGradient1, backgroundGradient2)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(borderGradient1, borderGradient2)
                    ),
                    shape = shape
                )
                .drawBehind {
                    drawRoundRect(
                        color = innerBorderColor,
                        style = Stroke(width = 2.dp.toPx()),
                        cornerRadius = CornerRadius(16.dp.toPx()),
                        topLeft = Offset(6.dp.toPx(), 6.dp.toPx()),
                        size = Size(
                            width = size.width - 12.dp.toPx(),
                            height = size.height - 12.dp.toPx()
                        )
                    )
                }
                .padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1B5E20)
@Composable
fun BasePlayerViewPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(32.dp)) {
            Text("Estado Vazio:", color = Color.White)
            BasePlayerView(modifier = Modifier.width(120.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Text("Com Conteúdo:", color = Color.White)
            BasePlayerView(modifier = Modifier.width(150.dp)) {
                Column(horizontalAlignment =Alignment.CenterHorizontally) {
                    Text("Player One", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    Text("1000 🪙", color = Color.Yellow, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}