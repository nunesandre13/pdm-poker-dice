package pt.isel.pdm.match.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player
import pt.isel.pdm.match.ui.animation.DiceRollAnimationOverlay
import pt.isel.pdm.match.ui.animation.RolledDices
import pt.isel.pdm.match.ui.matchLayout.GameTableLayoutClickable
import kotlin.time.Duration.Companion.seconds

@Composable
fun GameScreen(
    me: Player,
    others: List<Player>,
    onRollFinished: () -> Unit
) {

    var rollDices by remember { mutableStateOf(false) }

    var showRolledDices by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        GameTableLayoutClickable(
            me = me,
            others = others,
            onMyPlayerClick = {

            }
        )

        DiceRollAnimationOverlay(
            startAnimation = rollDices,
            onClick = {
                rollDices = true
            },
            onAnimationFinished = {
                onRollFinished()
            }
        )

        LaunchedEffect(rollDices) {
            if (rollDices) {
                delay(1.6.seconds)
                showRolledDices = true
                rollDices = false
                delay(2.seconds)
                showRolledDices = false
            }
        }

        if (showRolledDices) {
            RolledDices(
                dices = me.hand.dices,
                modifier = Modifier.size(width = 170.dp, height = 100.dp)
                    .offset((-50).dp, (-50).dp)
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
        onRollFinished = {}
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenRollingPreview() {
    val me = Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.KING,DiceFace.QUEEN,DiceFace.JACK,DiceFace.NINE)), id = 1)
    val others = listOf(
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 2),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 3),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 4),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 2),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 3))

    val isRolling = remember { mutableStateOf(true) }

    GameScreen(
        me = me,
        others = others,
        onRollFinished = { isRolling.value = false }
    )
}
