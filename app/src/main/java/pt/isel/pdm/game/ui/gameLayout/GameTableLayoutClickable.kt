package pt.isel.pdm.game.ui.gameLayout

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player
import pt.isel.pdm.game.ui.playerView.PlayerViewClickable


@Composable
fun GameTableLayoutClickable(
    me: Player,
    others: List<Player>,
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
fun GameTableLayoutClickablePreview() {
    val me = Player(DicesHand(emptyList()), 1)
    val others = emptyList<Player>()
    GameTableLayoutStatic(me = me, others = others)
}


