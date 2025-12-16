package pt.isel.pdm.match.innerComposable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R
import kotlinx.coroutines.delay
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.PlayerRoundState
import pt.isel.pdm.domain.PlayerStatus
import pt.isel.pdm.match.ui.animation.DiceRollAnimationOverlay
import pt.isel.pdm.match.ui.animation.RolledDices
import pt.isel.pdm.match.ui.dices.DisplayStaticDices
import pt.isel.pdm.match.viewModels.myTurn.MyTurnUiState
import kotlin.time.Duration.Companion.seconds

@Composable
fun DrawOnPlayers(
    players: List<PlayerRoundState>,
    registry: PlayerRegistry,
    content: @Composable (playerState: PlayerRoundState, modifier: Modifier) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        players.forEach { playerState ->
            val bounds = registry[playerState.playerId]
            if (bounds != null) {
                content(
                    playerState,
                    Modifier.applyBounds(bounds)
                )
            }
        }
    }
}

@Composable
fun Modifier.applyBounds(bounds: Rect): Modifier {
    val density = LocalDensity.current
    return this
        .absoluteOffset {
            IntOffset(bounds.left.toInt(), bounds.top.toInt())
        }
        .size(
            width = with(density) { bounds.width.toDp() },
            height = with(density) { bounds.height.toDp() }
        )
}


@Composable
fun DisplayOtherPlayersStatusOverlay(
    players: List<PlayerRoundState>,
    playersPosition: PlayerRegistry
) {
    DrawOnPlayers(
        players = players,
        registry = playersPosition
    ) { playerState, modifier ->
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Player ${playerState.playerId}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )

                val hand = when (val s = playerState.playerStatus) {
                    is PlayerStatus.StillRolling -> s.hand
                    is PlayerStatus.FinalHand -> s.hand
                    PlayerStatus.NotStarted,
                    PlayerStatus.PassRound -> null
                }

                if (hand?.dices?.isNotEmpty() == true) {
                    DisplayStaticDices(
                        dicesHand = hand,
                        size = 80.dp
                    )
                }
            }
        }
    }
}

@Composable
fun DrawCup(
    me: PlayerRoundState,
    startAnimmation: Boolean,
    onClick: ()-> Unit,
    onRollFinished: () -> Unit
) {
    val dices = when(val status = me.playerStatus){
        is PlayerStatus.FinalHand -> status.hand
        PlayerStatus.NotStarted -> null
        PlayerStatus.PassRound -> null
        is PlayerStatus.StillRolling -> status.hand
    }

    var showRolledDices by remember { mutableStateOf(false) }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        DiceRollAnimationOverlay(
            startAnimation = startAnimmation,
            onAnimationFinished = {
                onRollFinished()
            },
            onClick = onClick
        )

        LaunchedEffect(startAnimmation) {
            if (startAnimmation) {
                delay(1.6.seconds)
                showRolledDices = true
                delay(2.seconds)
                showRolledDices = false
            }
        }

        if (showRolledDices) {
            RolledDices(
                dices = dices?.dices ?: emptyList(),
                modifier = Modifier.size(width = 170.dp, height = 100.dp).offset((-50).dp, (-50).dp)
            )
        }
    }
}

@Composable
fun DrawCup(
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Image(
            painter = painterResource(id = R.drawable.cup),
            contentDescription = "Dice cup",
            modifier = modifier
                .padding(16.dp)
                .size(130.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DrawCupPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        DrawCup(
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}