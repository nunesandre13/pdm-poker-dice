package pt.isel.pdm.game.ui.playerLayouts

import PlayerViewStatic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player

const val ADVERSARY_PLAYER_WIDTH_PERCENT = 0.22f

val mockPlayers = List(5) {
    Player(
        hand = DicesHand(
            listOf(
                DiceFace.ACE,
                DiceFace.ACE,
                DiceFace.ACE,
                DiceFace.ACE,
                DiceFace.ACE)
        ),
        it
    )
}
@Composable
fun ConstraintLayoutScope.PlaceOtherPlayers(
    players: List<Player>,
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