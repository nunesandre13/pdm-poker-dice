package pt.isel.pdm.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension

private const val MY_PLAYER_WIDTH_PERCENT = 0.28f
private const val ADVERSARY_PLAYER_WIDTH_PERCENT = 0.22f

@Composable
fun GameTableLayout(me: Player, others: List<Player>) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0A3D17), Color(0xFF062D12))
                )
            )
    ) {

        val tableRef = createRef()
        PokerTableSurface(
            modifier = Modifier.constrainAs(tableRef) {
                centerTo(parent)
                width = Dimension.percent(0.78f)
                height = Dimension.ratio("2:1")
            }
        )

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
private fun PokerTableSurface(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val corner = CornerRadius(size.height / 2f, size.height / 2f)

        drawRoundRect(
            color = Color(0xFF5D4037),
            size = size,
            cornerRadius = corner
        )

        val inset = size.minDimension * 0.04f
        drawRoundRect(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20)),
                center = center,
                radius = size.minDimension / 1.2f
            ),
            topLeft = Offset(inset, inset),
            size = Size(size.width - inset * 2f, size.height - inset * 2f),
            cornerRadius = CornerRadius(corner.x - inset, corner.y - inset)
        )

        val lineInset = inset * 2f
        drawRoundRect(
            color = Color.White.copy(alpha = 0.25f),
            topLeft = Offset(lineInset, lineInset),
            size = Size(size.width - lineInset * 2f, size.height - lineInset * 2f),
            cornerRadius = CornerRadius(corner.x - lineInset, corner.y - lineInset),
            style = Stroke(width = inset * 0.6f)
        )
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
