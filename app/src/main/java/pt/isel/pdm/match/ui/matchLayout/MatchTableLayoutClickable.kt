package pt.isel.pdm.match.ui.matchLayout

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.match.ui.playerView.PlayerViewClickable


@Composable
fun GameTableLayoutClickable(
    me: PlayerRoundState,
    others: List<PlayerRoundState>,
    onMyPlayerClick: (DiceFace) -> Unit
) {
    GameTableLayout(me = me, others = others) { player, modifier ->
        PlayerViewClickable(
            player = player,
            onDiceClick = onMyPlayerClick,
            modifier = modifier
        )
    }
}


@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
private fun GameTableLayoutClickablePreview() {
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
            remainingRolls = 2
        )
    )
    val others = listOf(
        PlayerRoundState(2, 0, PlayerStatus.NotStarted),
        PlayerRoundState(3, 0, PlayerStatus.NotStarted),
        PlayerRoundState(4, 0, PlayerStatus.NotStarted),
        PlayerRoundState(5, 0, PlayerStatus.NotStarted)
    )
    GameTableLayoutClickable(
        me = me,
        others = others,
        onMyPlayerClick = {}
    )
}

