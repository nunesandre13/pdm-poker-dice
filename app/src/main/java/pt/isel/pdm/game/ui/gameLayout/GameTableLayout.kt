package pt.isel.pdm.game.ui.gameLayout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.domain.Player
import pt.isel.pdm.game.ui.playerLayouts.LayoutFor2Players
import pt.isel.pdm.game.ui.playerLayouts.LayoutFor3Players
import pt.isel.pdm.game.ui.playerLayouts.LayoutFor4Players
import pt.isel.pdm.game.ui.playerLayouts.LayoutFor5Players
import pt.isel.pdm.game.ui.playerLayouts.LayoutFor6Players
import pt.isel.pdm.game.ui.table.PokerTableSurface
import pt.isel.pdm.game.ui.table.TableTitle


private const val MY_PLAYER_WIDTH_PERCENT = 0.28f



@Composable
fun GameTableLayout(
    me: Player,
    others: List<Player>,
    myPlayerContent: @Composable (player: Player, modifier: Modifier) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.table_bg_light),
                        colorResource(R.color.table_bg_dark)
                    )
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
        ){ TableTitle() }
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