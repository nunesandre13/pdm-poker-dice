package pt.isel.pdm.game.playerLayouts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import pt.isel.pdm.domain.Player

@Composable
fun ConstraintLayoutScope.LayoutFor2Players(others: List<Player>) {
    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf {
            top.linkTo(parent.top, margin = 16.dp)
            centerHorizontallyTo(parent)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        }
    PlaceOtherPlayers(players = others, constraintBlocks = constraints)
}


@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor3PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LayoutFor3Players(others = mockPlayers.take(1))
    }
}
