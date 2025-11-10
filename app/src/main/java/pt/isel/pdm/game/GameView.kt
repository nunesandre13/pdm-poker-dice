
package pt.isel.pdm.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player
import pt.isel.pdm.game.animation.DiceRollAnimationOverlay
import pt.isel.pdm.game.gameLayout.GameTableLayoutClickable

@Composable
fun GameScreen(
    me: Player,
    others: List<Player>,
    isRolling: Boolean,
    onRollAnimationFinished: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        GameTableLayoutClickable(
            me = me,
            others = others,
            onMyPlayerClick = {}
        )

        if (isRolling) {
            DiceRollAnimationOverlay(
                onAnimationFinished = onRollAnimationFinished
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    val me = Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 1)
    val others = listOf(
    Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 2),
    Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 3),
    Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 4))
    GameScreen(
        me = me,
        others = others,
        isRolling = false,
        onRollAnimationFinished = {}
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenRollingPreview() {
    val me = Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 1)
    val others = listOf(
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 2),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 3),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 4),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 2),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 3))
    GameScreen(
        me = me,
        others = others,
        isRolling = true,
        onRollAnimationFinished = {}
    )
}
