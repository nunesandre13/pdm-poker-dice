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


@Composable
fun <T> ConstraintLayoutScope.LayoutFor3Players(others: List<T>,  playersComposable: PlayerComposable<T>) {
    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf(
        {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(parent.start, margin = 32.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
        },
        {
            top.linkTo(parent.top, margin = 16.dp)
            end.linkTo(parent.end, margin = 32.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
        }
    )
    PlaceOtherPlayers(players = others, constraintBlocks = constraints, playersComposable)
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor3PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        //LayoutFor3Players(others = listOf(1, 2))
    }
}
