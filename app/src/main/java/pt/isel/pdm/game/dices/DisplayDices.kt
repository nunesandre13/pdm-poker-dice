package pt.isel.pdm.game.dices


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand

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
