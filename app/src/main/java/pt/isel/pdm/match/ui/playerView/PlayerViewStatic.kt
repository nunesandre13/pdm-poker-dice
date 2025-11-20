import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.match.ui.dices.DisplayStaticDices
import pt.isel.pdm.match.ui.playerView.BasePlayerView

@Composable
fun PlayerViewStatic(
    player: PlayerRoundState,
    modifier: Modifier = Modifier
) {
    val hand: DicesHand? = when (val status = player.playerStatus) {
        is PlayerStatus.StillRolling -> status.hand
        is PlayerStatus.FinalHand -> status.hand
        PlayerStatus.NotStarted,
        PlayerStatus.PassRound -> null
    }
    BasePlayerView(player = player, modifier = modifier) { containerWidth ->
        hand?.let {
            DisplayStaticDices(
                dicesHand = it,
                size = containerWidth
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 250)
@Composable
private fun PlayerViewStaticPreview() {
    val samplePlayer = PlayerRoundState(
        playerId = 5,
        coins = 0,
        playerStatus = PlayerStatus.StillRolling(
            hand = DicesHand(
                listOf(
                    DiceFace.ACE,
                    DiceFace.ACE,
                    DiceFace.ACE,
                    DiceFace.ACE,
                    DiceFace.ACE
                )
            ),
            remainingRolls = 2
        )
    )
    Surface {
        PlayerViewStatic(player = samplePlayer)
    }
}
