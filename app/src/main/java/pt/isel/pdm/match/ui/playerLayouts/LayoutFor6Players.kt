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
fun ConstraintLayoutScope.LayoutFor6Players(others: List<PlayerRoundState>) {
    val startGuideline = createGuidelineFromStart(fraction = 0.1f)
    val endGuideline = createGuidelineFromEnd(fraction = 0.1f)

    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf(
        {
            centerVerticallyTo(parent)
            start.linkTo(parent.start, margin = 16.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(startGuideline)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        { refs ->
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(refs[1].end)
            end.linkTo(refs[3].start)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top, margin = 16.dp)
            end.linkTo(endGuideline)
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
private fun LayoutFor6PlayersPreview() {
    val mockOthers = listOf(
        PlayerRoundState(2,0,PlayerStatus.NotStarted),
        PlayerRoundState(3,0,PlayerStatus.NotStarted),
        PlayerRoundState(4,0,PlayerStatus.NotStarted),
        PlayerRoundState(5,0,PlayerStatus.NotStarted),
        PlayerRoundState(6,0,PlayerStatus.NotStarted)
    )
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LayoutFor6Players(others = mockOthers)
    }
}