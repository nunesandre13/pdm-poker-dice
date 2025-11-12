package pt.isel.pdm.game.ui.playerView

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player
import pt.isel.pdm.game.ui.dices.DisplayClickableDices

@Composable
fun PlayerViewClickable(
    player: Player,
    onDiceClick: (DiceFace) -> Unit,
    modifier: Modifier = Modifier
) {
    BasePlayerView(player = player, modifier = modifier) { containerWidth ->
        DisplayClickableDices(
            dicesHand = player.hand,
            onClick = onDiceClick,
            size = containerWidth
        )
    }
}


@Preview(showBackground = true, widthDp = 250)
@Composable
fun PlayerViewClickablePreview() {
    val samplePlayer = Player(hand =
        DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)),
        5)
    Surface {
        PlayerViewClickable(player = samplePlayer, onDiceClick = {})
    }
}
