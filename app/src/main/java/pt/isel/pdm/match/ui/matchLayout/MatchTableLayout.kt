package pt.isel.pdm.match.ui.matchLayout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.match.ui.playerLayouts.LayoutFor2Players
import pt.isel.pdm.match.ui.playerLayouts.LayoutFor3Players
import pt.isel.pdm.match.ui.playerLayouts.LayoutFor4Players
import pt.isel.pdm.match.ui.playerLayouts.LayoutFor5Players
import pt.isel.pdm.match.ui.playerLayouts.LayoutFor6Players



private const val MY_PLAYER_WIDTH_PERCENT = 0.28f

@Composable
fun GameTableLayout(
    me: PlayerRoundState,
    others: List<PlayerRoundState>,
    myPlayerContent: @Composable (player: PlayerRoundState, modifier: Modifier) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val myPlayerRef = createRef()
        myPlayerContent(
            me,
            Modifier.constrainAs(myPlayerRef) {
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