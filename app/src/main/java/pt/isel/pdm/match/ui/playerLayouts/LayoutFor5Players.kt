package pt.isel.pdm.match.ui.playerLayouts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus

@Composable
fun <T>ConstraintLayoutScope.LayoutFor5Players(others: List<T>, playersComposable: PlayerComposable<T>) {
    val guideLineFromStart = createGuidelineFromStart(0.15f)
    val guideLineFromEnd = createGuidelineFromEnd(0.15f)

    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf(
        {
            centerVerticallyTo(parent)
            start.linkTo(parent.start)
            end.linkTo(parent.start)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top)
            bottom.linkTo(parent.top)
            start.linkTo(guideLineFromStart)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top)
            bottom.linkTo(parent.top)
            end.linkTo(guideLineFromEnd)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            centerVerticallyTo(parent)
            start.linkTo(parent.end)
            end.linkTo(parent.end)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        }
    )
    PlaceOtherPlayers(players = others, constraintBlocks = constraints,playersComposable)
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

        //LayoutFor5Players(others = listOf(other1, other2, other3, other4))
    }
}