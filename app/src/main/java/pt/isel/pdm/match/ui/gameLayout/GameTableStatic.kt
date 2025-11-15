package pt.isel.pdm.match.ui.gameLayout

import PlayerViewStatic
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player


@Composable
fun GameTableLayoutStatic(me: Player, others: List<Player>) {
    GameTableLayout(me = me, others = others) { player, modifier ->
        PlayerViewStatic(player = player, modifier = modifier)
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun GameTableLayoutStaticPreview() {
    val me = Player(DicesHand(emptyList()), 1)
    val others = emptyList<Player>()
    GameTableLayoutStatic(me = me, others = others)
}
