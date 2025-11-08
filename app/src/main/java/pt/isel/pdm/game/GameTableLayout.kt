package pt.isel.pdm.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension

private const val MY_PLAYER_WIDTH_PERCENT = 0.28f
private const val ADVERSARY_PLAYER_WIDTH_PERCENT = 0.22f

@Composable
fun GameTableLayout(me: Player, others: List<Player>) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val myPlayerRef = createRef()
        PlayerView(
            player = me,
            modifier = Modifier
                .constrainAs(myPlayerRef) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.percent(MY_PLAYER_WIDTH_PERCENT)
                }
        )
        when (others.size) {
            1 -> LayoutFor2Players(others)
            2 -> LayoutFor3Players(others)
            3 -> LayoutFor4Players(others)
            4 -> LayoutFor5Players(others)
            5 -> LayoutFor6Players(others)
        }
    }
}
@Composable
private fun ConstraintLayoutScope.LayoutFor2Players(others: List<Player>) {
    val other1 = createRef()
    PlayerView(
        player = others[0],
        Modifier
            .constrainAs(other1) {
                top.linkTo(parent.top, margin = 16.dp)
                centerHorizontallyTo(parent)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
}

@Composable
private fun ConstraintLayoutScope.LayoutFor3Players(others: List<Player>) {
    val (other1, other2) = createRefs()
    PlayerView(
        player = others[0],
        Modifier
            .constrainAs(other1) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 32.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[1],
        Modifier
            .constrainAs(other2) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 32.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
}

@Composable
private fun ConstraintLayoutScope.LayoutFor4Players(others: List<Player>) {
    val (other1, other2, other3) = createRefs()
    PlayerView(
        player = others[0],
        Modifier
            .constrainAs(other1) {
                centerVerticallyTo(parent)
                start.linkTo(parent.start, margin = 16.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[1],
        Modifier
            .constrainAs(other2) {
                top.linkTo(parent.top, margin = 16.dp)
                centerHorizontallyTo(parent)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[2],
        Modifier
            .constrainAs(other3) {
                centerVerticallyTo(parent)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
}

@Composable
private fun ConstraintLayoutScope.LayoutFor5Players(others: List<Player>) {
    val (other1, other2, other3, other4) = createRefs()
    PlayerView(
        player = others[0],
        Modifier
            .constrainAs(other1) {
                centerVerticallyTo(parent)
                start.linkTo(parent.start, margin = 16.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[1],
        Modifier
            .constrainAs(other2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 64.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[2],
        Modifier
            .constrainAs(other3) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 64.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[3],
        Modifier
            .constrainAs(other4) {
                centerVerticallyTo(parent)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
}

@Composable
private fun ConstraintLayoutScope.LayoutFor6Players(others: List<Player>) {
    val startGuideline = createGuidelineFromStart(fraction = 0.1f)
    val endGuideline = createGuidelineFromEnd(fraction = 0.1f)
    val (other1, other2, other3, other4, other5) = createRefs()
    PlayerView(
        player = others[0],
        Modifier
            .constrainAs(other1) {
                centerVerticallyTo(parent)
                start.linkTo(parent.start, margin = 16.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[1],
        Modifier
            .constrainAs(other2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(startGuideline)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[2],
        Modifier
            .constrainAs(other3) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(other2.end)
                end.linkTo(other4.start)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[3],
        Modifier
            .constrainAs(other4) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(endGuideline)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
    PlayerView(
        player = others[4],
        Modifier
            .constrainAs(other5) {
                centerVerticallyTo(parent)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
            }
    )
}
