package pt.isel.pdm.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand

@Composable
fun DisplayAdversaryDices(dicesHand: DicesHand, size: Dp){
    val diceSize = size / 3.5f
    val spacing = diceSize / 4
    Column(
        modifier = Modifier.width(size),
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            dicesHand.dices.take(2).forEach { dice ->
                StopedDice(
                    face = dice,
                    onClick = { /* Os dados do adversário não são clicáveis */ },
                    size = diceSize
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            dicesHand.dices.drop(2).forEach { dice ->
                StopedDice(
                    face = dice,
                    onClick = { /* Os dados do adversário não são clicáveis */ },
                    size = diceSize
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDisplayAdversaryDices() {
    DisplayAdversaryDices(
        dicesHand = DicesHand(
            dices = listOf(
                DiceFace.ACE,
                DiceFace.KING,
                DiceFace.QUEEN,
                DiceFace.JACK,
                DiceFace.NINE
            )
        ),
        size = 200.dp
    )
}
