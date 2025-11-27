package pt.isel.pdm.match.ui.dices

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.toImmutableList
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand

@Composable
fun DisplayClickableDices(
    dicesHand: DicesHand,
    onClick: (DiceFace)-> Unit,
    size: Dp,
    modifier: Modifier = Modifier
){
    DisplayDices(dicesHand = dicesHand, size = size) { face, diceSize ->
        ClickableDice(
            face = face,
            onClick = { onClick(face) },
            size = diceSize
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDisplayClickableDices() {
    DisplayClickableDices(
        dicesHand = DicesHand(
            dices = listOf(
                DiceFace.ACE,
                DiceFace.KING,
                DiceFace.QUEEN,
                DiceFace.JACK,
                DiceFace.NINE
            ).toImmutableList()
        ),
        onClick = {},
        size = 200.dp,
    )
}
