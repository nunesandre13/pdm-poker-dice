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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp


@Composable
fun <T> ConstraintLayoutScope.LayoutFor2Players(others: List<T>, playersComposable: PlayerComposable<T>) {
    val constraints: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit> = listOf {
        top.linkTo(parent.top, margin = 16.dp)
        centerHorizontallyTo(parent)
        width = Dimension.percent(ADVERSARY_PLAYER_WIDTH_PERCENT)
        height = Dimension.percent(ADVERSARY_PLAYER_HEIGHT_PERCENT)
        }
    PlaceOtherPlayers(players = others, constraintBlocks = constraints, playersComposable)
}


@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
private fun LayoutFor2PlayersPreview() {
    val fakeOpponents = listOf("Adversário 1")
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B5E20))
    ) {
        LayoutFor2Players(
            others = fakeOpponents,
            playersComposable = { name, modifier ->
                Box(
                    modifier = modifier
                        .border(2.dp, Color.White)
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        )
    }
}