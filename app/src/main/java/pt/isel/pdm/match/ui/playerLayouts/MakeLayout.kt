package pt.isel.pdm.match.ui.playerLayouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import pt.isel.pdm.match.ui.playerView.BasePlayerView

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


@Preview(showBackground = true, widthDp = 800, heightDp = 500)
@Composable
fun MakeLayoutPreview() {
    val me = "Eu"
    val opponents = listOf("Adversário 1", "Adversário 2", "Adversário 3")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B5E20))
    ) {
        MakeLayout(
            me = me,
            others = opponents,
            myPlayerComposable = { name, modifier ->
                BasePlayerView(modifier = modifier) {
                    Text(text = name, color = Color.White)
                }
            },
            otherPlayersComposable = { name, modifier ->
                BasePlayerView(modifier = modifier) {
                    Text(text = name, color = Color.White)
                }
            }
        )
    }
}