package pt.isel.pdm.match.ui.matchLayout

import PlayerViewStatic
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus


@Composable
fun GameTableLayoutStatic(me: PlayerRoundState, others: List<PlayerRoundState>) {
    GameTableLayout(me = me, others = others) { player, modifier ->
        PlayerViewStatic(player = player, modifier = modifier)
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun GameTableLayoutStaticPreview() {
    val me = PlayerRoundState(
        playerId = 1,
        coins = 120,
        playerStatus = PlayerStatus.StillRolling(
            hand = DicesHand(
                listOf(
                    DiceFace.ACE,
                    DiceFace.KING,
                    DiceFace.QUEEN,
                    DiceFace.JACK,
                    DiceFace.TEN
                )
            ),
            remainingRolls = 1
        )
    )
    val others = listOf(
        PlayerRoundState(2, 80, PlayerStatus.NotStarted),
        PlayerRoundState(3, 60, PlayerStatus.NotStarted),
        PlayerRoundState(4, 50, PlayerStatus.NotStarted)
    )
    GameTableLayoutStatic(me = me, others = others)
}
