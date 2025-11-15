import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player
import pt.isel.pdm.match.ui.dices.DisplayStaticDices
import pt.isel.pdm.match.ui.playerView.BasePlayerView

@Composable
fun PlayerViewStatic(
    player: Player,
    modifier: Modifier = Modifier
) {
    BasePlayerView(player = player, modifier = modifier) { containerWidth ->
        DisplayStaticDices(
            dicesHand = player.hand,
            size = containerWidth
        )
    }
}

@Preview(showBackground = true, widthDp = 250)
@Composable
fun PlayerViewStaticPreview() {
    val samplePlayer = Player(hand =
        DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)),
        5)
    Surface {
        PlayerViewStatic(player = samplePlayer)
    }
}


