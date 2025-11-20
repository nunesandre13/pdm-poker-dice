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
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus

@Composable
fun ConstraintLayoutScope.LayoutFor2Players(others: List<PlayerRoundState>) {
    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf {
            top.linkTo(parent.top, margin = 16.dp)
            centerHorizontallyTo(parent)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        }
    PlaceOtherPlayers(players = others, constraintBlocks = constraints)
}


@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor2PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val other = PlayerRoundState(
            playerId = 2,
            coins = 0,
            playerStatus = PlayerStatus.NotStarted
        )
        LayoutFor2Players(others = listOf(other))
    }
}