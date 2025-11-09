package pt.isel.pdm.game

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
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player

private const val ADVERSARY_PLAYER_WIDTH_PERCENT = 0.22f

@Composable
private fun ConstraintLayoutScope.PlaceOtherPlayers(
    players: List<Player>,
    constraintBlocks: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit>
) {
    val refs = List(players.size) { createRef() }
    players.zip(constraintBlocks).forEachIndexed { index, (player, constraintBlock) ->
        PlayerView(
            player = player,
            modifier = Modifier.constrainAs(refs[index]) {
                constraintBlock(refs)
            }
        )
    }
}

@Composable
fun ConstraintLayoutScope.LayoutFor2Players(others: List<Player>) {
    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf(
        {
            top.linkTo(parent.top, margin = 16.dp)
            centerHorizontallyTo(parent)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        }
    )
    PlaceOtherPlayers(players = others, constraintBlocks = constraints)
}

@Composable
fun ConstraintLayoutScope.LayoutFor3Players(others: List<Player>) {
    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf(
        {
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(parent.start, margin = 32.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        },
        {
            top.linkTo(parent.top, margin = 16.dp)
            end.linkTo(parent.end, margin = 32.dp)
            width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        }
    )
    PlaceOtherPlayers(players = others, constraintBlocks = constraints)
}


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

@Composable
fun ConstraintLayoutScope.LayoutFor5Players(others: List<Player>) {
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

@Composable
fun ConstraintLayoutScope.LayoutFor6Players(others: List<Player>) {
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

private val mockPlayers = List(5) {
    Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), it)
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor2PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LayoutFor2Players(others = mockPlayers.take(1))
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor3PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LayoutFor3Players(others = mockPlayers.take(2))
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor4PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LayoutFor4Players(others = mockPlayers.take(3))
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor5PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LayoutFor5Players(others = mockPlayers.take(4))
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor6PlayersPreview() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        LayoutFor6Players(others = mockPlayers.take(5))
    }
}
