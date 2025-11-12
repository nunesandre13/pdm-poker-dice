package pt.isel.pdm.game.ui.dices

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand


@Composable
fun DisplayStaticDices(dicesHand: DicesHand, size: Dp){
    DisplayDices(dicesHand = dicesHand, size = size) { face, diceSize ->
        StaticDice(
            face = face,
            size = diceSize
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDisplayPlayerDices() {
    DisplayStaticDices(
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
