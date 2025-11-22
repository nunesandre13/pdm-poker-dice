package pt.isel.pdm.match.ui.playerLayouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope

const val ADVERSARY_PLAYER_WIDTH_PERCENT = 0.22f

const val ADVERSARY_PLAYER_HEIGHT_PERCENT = 0.25f
const val PLAYER_WIDTH_1 = 0.5f
const val PLAYER_HEIGHT_1 = 0.6f
const val PLAYER_WIDTH_2 = 0.35f
const val PLAYER_HEIGHT_2 = 0.5f
const val PLAYER_WIDTH_OTHERS = 0.22f
const val PLAYER_HEIGHT_OTHERS = 0.35f

@Composable
fun <T> ConstraintLayoutScope.PlaceOtherPlayers(
    players: List<T>,
    constraintBlocks: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit>,
    playersComposable: PlayerComposable<T>
) {
    val refs = List(players.size) { createRef() }
    val (maxWidthPercent, maxHeightPercent) =
        when (players.size) {
            1 -> PLAYER_WIDTH_1 to PLAYER_HEIGHT_1
            2 -> PLAYER_WIDTH_2 to PLAYER_HEIGHT_2
            else -> PLAYER_WIDTH_OTHERS to PLAYER_HEIGHT_OTHERS
        }
    players.zip(constraintBlocks).forEachIndexed { index, (player, constraintBlock) ->
        playersComposable(
            player,
            Modifier
                .constrainAs(refs[index]) { constraintBlock(refs) }
                //.fillMaxWidth(maxWidthPercent)
                //.fillMaxHeight(maxHeightPercent)
        )
    }
}
