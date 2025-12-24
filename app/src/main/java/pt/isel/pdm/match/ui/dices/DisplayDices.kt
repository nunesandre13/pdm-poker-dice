package pt.isel.pdm.match.ui.dices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import kotlinx.collections.immutable.toImmutableList
import pt.isel.pdm.domain.match.DiceFace
import pt.isel.pdm.domain.match.DicesHand
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DisplayDices(
    dicesHand: DicesHand,
    size: Dp,
    diceContent: @Composable (face: DiceFace, size: Dp) -> Unit
) {
    val diceSize = size / 3.5f
    val spacing = diceSize / 4
    Column(
        modifier = Modifier.width(size),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            dicesHand.dices.take(2).forEach { dice ->
                diceContent(dice, diceSize)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            dicesHand.dices.drop(2).forEach { dice ->
                diceContent(dice, diceSize)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisplayDicesPreview() {
    val fakeHand = DicesHand(dices = listOf(DiceFace.ACE, DiceFace.KING, DiceFace.QUEEN, DiceFace.JACK, DiceFace.NINE).toImmutableList())
    Box(modifier = Modifier.size(300.dp)) {
        DisplayDices(
            dicesHand = fakeHand,
            size = 200.dp,
            diceContent = { face, diceSize ->
                Image(
                    painter = painterResource(id = face.resId),
                    contentDescription = face.name,
                    modifier = Modifier
                        .size(diceSize)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White)
                )
            }
        )
    }
}