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
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.sp
import pt.isel.pdm.domain.Player

private const val MY_PLAYER_WIDTH_PERCENT = 0.28f

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

        val tableTitleRef = createRef()
        Text(
            text = "POKER DICE GAME",
            fontSize = 38.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFFD54F),
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.6f),
                    offset = Offset(3f, 3f),
                    blurRadius = 10f
                )
            ),
            modifier = Modifier.constrainAs(tableTitleRef) {
                centerTo(tableRef)
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

