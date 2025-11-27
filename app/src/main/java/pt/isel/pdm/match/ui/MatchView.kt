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
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.match.ui.animation.DiceRollAnimationOverlay
import pt.isel.pdm.match.ui.animation.RolledDices
import kotlin.time.Duration.Companion.seconds

@Composable
fun GameScreen(
    me: PlayerRoundState,
    others: List<PlayerRoundState>,
    onRollFinished: () -> Unit
) {

    var rollDices by remember { mutableStateOf(false) }

    var showRolledDices by remember { mutableStateOf(false) }

    val dices: List<DiceFace> = when (val status = me.playerStatus) {
        is PlayerStatus.StillRolling -> status.hand.dices
        is PlayerStatus.FinalHand -> status.hand.dices
        PlayerStatus.NotStarted,
        PlayerStatus.PassRound -> emptyList()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        GameTableLayoutClickable(
//            me = me,
//            others = others,
//            onMyPlayerClick = {
//
//            }
//        )

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
                dices = dices,
                modifier = Modifier.size(width = 170.dp, height = 100.dp).offset((-50).dp, (-50).dp)
            )
        }
    }
}


@Preview(showBackground = true, name = "GameScreen Preview")
@Composable
fun GameScreenPreview() {

    val sampleHandRolling = DicesHand(
        dices = kotlinx.collections.immutable.persistentListOf(
            DiceFace.ACE,
            DiceFace.NINE,
            DiceFace.JACK,
            DiceFace.KING,
            DiceFace.ACE
        )
    )
    val sampleHandFinal = DicesHand(
        dices = kotlinx.collections.immutable.persistentListOf(
            DiceFace.ACE,
            DiceFace.NINE,
            DiceFace.JACK,
            DiceFace.KING,
            DiceFace.ACE
        )
    )

    val me = PlayerRoundState(
        playerId = 1,
        coins = 12,
        playerStatus = PlayerStatus.StillRolling(sampleHandRolling)
    )

    val otherFinal = PlayerRoundState(
        playerId = 2,
        coins = 9,
        playerStatus = PlayerStatus.FinalHand(sampleHandFinal)

    )

    val otherWaiting = PlayerRoundState(
        playerId = 3,
        coins = 15,
        playerStatus = PlayerStatus.NotStarted
    )

    GameScreen(
        me = me,
        others = listOf(otherFinal, otherWaiting),
        onRollFinished = {}
    )
}
