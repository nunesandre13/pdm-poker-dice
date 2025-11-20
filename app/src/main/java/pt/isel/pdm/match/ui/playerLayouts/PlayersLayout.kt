package pt.isel.pdm.match.ui.playerLayouts

import PlayerViewStatic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import pt.isel.pdm.domain.PlayerRoundState

const val ADVERSARY_PLAYER_WIDTH_PERCENT = 0.22f

@Composable
fun ConstraintLayoutScope.PlaceOtherPlayers(
    players: List<PlayerRoundState>,
    constraintBlocks: List<ConstrainScope.(List<ConstrainedLayoutReference>) -> Unit>
) {
    val refs = List(players.size) { createRef() }
    players.zip(constraintBlocks).forEachIndexed { index, (player, constraintBlock) ->
        PlayerViewStatic(
            player = player,
            modifier = Modifier.constrainAs(refs[index]) {
                constraintBlock(refs)
            }
        )
    }
}