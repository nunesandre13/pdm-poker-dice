package pt.isel.pdm.match.ui.cup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import pt.isel.pdm.domain.match.DiceFace
import pt.isel.pdm.domain.match.DicesHand
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.match.PlayerStatus
import pt.isel.pdm.domain.state.PlayerRoundStateWithName
import pt.isel.pdm.match.ui.animation.DiceRollAnimationOverlay
import pt.isel.pdm.match.ui.animation.RolledDices
import kotlin.time.Duration.Companion.seconds

@Composable
fun Cup(
    offsetX: Float,
    offsetY: Float,
    rotation: Float,
    onCupSized: (IntSize) -> Unit,
    onClick: () -> Unit,
    enable: Boolean,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.cup),
        contentDescription = "Dice cup",
        modifier = modifier
            .padding(16.dp)
            .size(130.dp)
            .onGloballyPositioned { coordinates ->
                onCupSized(coordinates.size)
            }
            .graphicsLayer(
                translationX = offsetX,
                translationY = offsetY,
                rotationZ = rotation,
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            )
            .clickable(
                enabled = enable,
                onClick = onClick
            )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CupPreview() {

    var rotation by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Cup(
            offsetX = 0f,
            offsetY = 0f,
            rotation = rotation,
            onCupSized = { },
            onClick = {
                rotation += 45f
            },
            enable = true
        )
    }
}

@Composable
fun DrawCup(
    me: PlayerRoundStateWithName?,
    startAnimmation: Boolean,
    onClick: () -> Unit,
    onRollFinished: () -> Unit
) {
    val dices = when(val status = me?.playerStatus){
        is PlayerStatus.FinalHand -> status.hand
        PlayerStatus.NotStarted -> null
        PlayerStatus.PassRound -> null
        is PlayerStatus.StillRolling -> status.hand
        else -> null
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
    Image(
        painter = painterResource(id = R.drawable.cup),
        contentDescription = "Dice cup",
        modifier = modifier
            .padding(16.dp)
            .size(130.dp)
    )
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

@Preview(showBackground = true, name = "Cup with Rolled Dices")
@Composable
fun DrawCupAnimationStatePreview() {
    val mockPlayer = PlayerRoundStateWithName(
        playerId = PlayerId(1),
        name = pt.isel.pdm.domain.Name("Jogador 1"),
        coins = 500,
        playerStatus = PlayerStatus.StillRolling(
            hand = DicesHand(
                listOf(DiceFace.ACE, DiceFace.KING, DiceFace.QUEEN).toImmutableList()
            )
        )
    )
    MaterialTheme {
        // Simulando o estado onde os dados aparecem após o lançamento
        DrawCup(
            me = mockPlayer,
            startAnimmation = false,
            onClick = {},
            onRollFinished = {}
        )
    }
}
