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
fun ConstraintLayoutScope.LayoutFor5Players(others: List<PlayerRoundState>) {
    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf(
        {
            centerVerticallyTo(parent)
            start.linkTo(parent.start, margin = 16.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(parent.start, margin = 64.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top, margin = 16.dp)
            end.linkTo(parent.end, margin = 64.dp)
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
        val other1 = PlayerRoundState(
            playerId = 2,
            coins = 0,
            playerStatus = PlayerStatus.NotStarted
        )
        val other2 = PlayerRoundState(
            playerId = 3,
            coins = 0,
            playerStatus = PlayerStatus.NotStarted
        )
        val other3 = PlayerRoundState(
            playerId = 6,
            coins = 0,
            playerStatus = PlayerStatus.NotStarted
        )
        val other4 = PlayerRoundState(
            playerId = 7,
            coins = 0,
            playerStatus = PlayerStatus.NotStarted
        )

        LayoutFor5Players(others = listOf(other1, other2, other3, other4))
    }
}