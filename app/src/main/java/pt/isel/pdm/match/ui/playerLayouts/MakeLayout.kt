package pt.isel.pdm.match.ui.playerLayouts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

private const val MY_PLAYER_WIDTH_PERCENT = 0.30f
private const val MY_PLAYER_HEIGHT_PERCENT = 0.35f


@Composable
fun <T> MakeLayout(me: T,others: List<T>, myPlayerComposable: PlayerComposable<T>, otherPlayersComposable: PlayerComposable<T> ){
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val myPlayerRef = createRef()
        myPlayerComposable(
            me,
            Modifier.constrainAs(myPlayerRef) {
                bottom.linkTo(parent.bottom)
                top.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.percent(MY_PLAYER_WIDTH_PERCENT)
                height = Dimension.percent(MY_PLAYER_HEIGHT_PERCENT)
            }
        )
        when (others.size) {
            1 -> LayoutFor2Players(others,otherPlayersComposable)
            2 -> LayoutFor3Players(others, otherPlayersComposable)
            3 -> LayoutFor4Players(others,otherPlayersComposable)
            4 -> LayoutFor5Players(others,otherPlayersComposable)
            5 -> LayoutFor6Players(others,otherPlayersComposable)
        }
    }
}
