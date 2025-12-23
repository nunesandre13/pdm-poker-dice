package pt.isel.pdm.match.ui.playerLayouts

import androidx.compose.runtime.Composable
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension


@Composable
fun <T>ConstraintLayoutScope.LayoutFor6Players(others: List<T>, playersComposable: PlayerComposable<T>) {
    val startGuideline = createGuidelineFromStart(fraction = 0.1f)
    val endGuideline = createGuidelineFromEnd(fraction = 0.1f)

    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf(
        {
            centerVerticallyTo(parent)
            start.linkTo(parent.start)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top)
            start.linkTo(startGuideline)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        { refs ->
            top.linkTo(parent.top)
            start.linkTo(refs[1].end)
            end.linkTo(refs[3].start)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top)
            end.linkTo(endGuideline)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            centerVerticallyTo(parent)
            end.linkTo(parent.end)
            height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        }
    )
    PlaceOtherPlayers(players = others, constraintBlocks = constraints, playersComposable)
}


