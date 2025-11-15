package pt.isel.pdm.match.ui.playerLayouts

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
fun ConstraintLayoutScope.LayoutFor4Players(others: List<Player>) {
    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf(
        {
            centerVerticallyTo(parent)
            start.linkTo(parent.start, margin = 16.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top, margin = 16.dp)
            centerHorizontallyTo(parent)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            centerVerticallyTo(parent)
            end.linkTo(parent.end, margin = 16.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        }
    )
    PlaceOtherPlayers(players = others, constraintBlocks = constraints)
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor5PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LayoutFor5Players(others = mockPlayers.take(3))
    }
}
