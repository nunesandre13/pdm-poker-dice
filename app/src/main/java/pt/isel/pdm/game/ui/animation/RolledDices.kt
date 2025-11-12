package pt.isel.pdm.game.ui.animation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.game.ui.dices.StaticDice
import java.lang.Float.min
import kotlin.random.Random


@Composable
fun RolledDices(
    dices: List<DiceFace>,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier, contentAlignment = Alignment.Center) {

        val diceSize = min(maxWidth.value, maxHeight.value).dp * 0.30f

        val maxX = (maxWidth - diceSize).value.coerceAtLeast(0f)
        val maxY = (maxHeight - diceSize).value.coerceAtLeast(0f)

        val dicePositions = remember(dices, maxWidth, maxHeight) {
            dices.map {
                Triple(
                    Random.nextDouble(0.0, maxX.toDouble()).dp,
                    Random.nextDouble(0.0, maxY.toDouble()).dp,
                    Random.nextFloat() * 360f
                )
            }
        }

        dices.forEachIndexed { index, diceFace ->
            val (offsetX, offsetY, rotation) = dicePositions[index]
            StaticDice(
                face = diceFace,
                size = diceSize,
                modifier = Modifier
                    .offset(x = offsetX, y = offsetY)
                    .rotate(rotation)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RolledDicesPreview() {
    val sampleDices = listOf(
        DiceFace.ACE,
        DiceFace.KING,
        DiceFace.QUEEN,
        DiceFace.JACK,
        DiceFace.NINE
    )

    RolledDices(
        dices = sampleDices,
        modifier = Modifier
            .size(width = 350.dp, height = 250.dp)
            .background(Color.LightGray)
    )
}