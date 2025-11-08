package pt.isel.pdm.game

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand


@Composable
fun PlayerView(player: Player, modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .border(1.dp, Color.Gray, CircleShape)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        val containerWidth = (maxWidth.value * 0.6).dp
        val fontSize = (containerWidth.value / 7).sp

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "PLayer: ${player.id}",
                textAlign = TextAlign.Center,
                fontSize = fontSize
            )
            DisplayAdversaryDices(
                dicesHand = player.hand,
                size = containerWidth
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerViewPreview() {
    PlayerView(
        player = Player(
            id = 1,
            hand = DicesHand(
                dices = listOf(
                    DiceFace.ACE,
                    DiceFace.KING,
                    DiceFace.QUEEN,
                    DiceFace.JACK,
                    DiceFace.NINE
                )
            )
        )
    )
}
