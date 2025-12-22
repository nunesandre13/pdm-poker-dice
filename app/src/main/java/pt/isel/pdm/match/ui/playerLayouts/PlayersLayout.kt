package pt.isel.pdm.match.ui.playerLayouts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope

const val ADVERSARY_PLAYER_WIDTH_PERCENT = 0.22f
const val ADVERSARY_PLAYER_HEIGHT_PERCENT = 0.25f

@Composable
fun <T> ConstraintLayoutScope.PlaceOtherPlayers(
    players: List<T>,
    constraintBlocks: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit>,
    playersComposable: PlayerComposable<T>
) {
    val refs = List(players.size) { createRef() }
    players.zip(constraintBlocks).forEachIndexed { index, (player, constraintBlock) ->
        playersComposable(
            player,
            Modifier
                .constrainAs(refs[index]) { constraintBlock(refs) }
        )
    }
}
