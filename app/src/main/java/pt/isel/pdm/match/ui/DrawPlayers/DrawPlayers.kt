package pt.isel.pdm.match.ui.DrawPlayers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chelasmulti_playerpokerdice.R
import kotlinx.collections.immutable.toImmutableList
import pt.isel.pdm.domain.match.DiceFace
import pt.isel.pdm.domain.match.DicesHand
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.match.PlayerStatus
import pt.isel.pdm.domain.state.PlayerRoundStateWithName
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.innerComposable.applyBounds
import pt.isel.pdm.match.ui.dices.DisplayClickableDices
import pt.isel.pdm.match.ui.dices.DisplayStaticDices
import kotlin.collections.forEach

@Composable
fun DrawOnPlayers(
    players: List<PlayerRoundStateWithName>,
    registry: PlayerRegistry,
    content: @Composable (playerState: PlayerRoundStateWithName, modifier: Modifier) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        players.forEach { playerState ->
            val bounds = registry[playerState.playerId.id]
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
fun DisplayOtherPlayersStatusOverlay(
    players: List<PlayerRoundStateWithName>,
    playersPosition: PlayerRegistry,
    collectMyDices: ((DiceFace) -> Unit)? = null
) {
    DrawOnPlayers(
        players = players,
        registry = playersPosition
    ) { playerState, modifier ->
        BoxWithConstraints(
            modifier = modifier
        ) {
            val diceSize = (maxWidth * 0.4f)
                .coerceIn(50.dp, 96.dp)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DrawNameAndCoins(playerState)
                val hand = when (val s = playerState.playerStatus) {
                    is PlayerStatus.StillRolling -> s.hand
                    is PlayerStatus.FinalHand -> s.hand
                    PlayerStatus.NotStarted,
                    PlayerStatus.PassRound -> null
                }
                if (hand?.dices?.isNotEmpty() == true) {
                    collectMyDices?.let {
                        DisplayClickableDices(
                            dicesHand = hand,
                            onClick = it,
                            size = diceSize
                        )
                    } ?: DisplayStaticDices(
                        dicesHand = hand,
                        size = diceSize
                    )
                }
            }
        }
    }
}

@Composable
fun DrawNameAndCoins(playerState: PlayerRoundStateWithName) {
    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = playerState.name.name,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = playerState.coins.toString(),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(4.dp))
        Image(
            painter = painterResource(id = R.drawable.ficha),
            contentDescription = "Poker chip",
            modifier = Modifier.size(16.dp)
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF2E7D32)
@Composable
fun DrawNameAndCoinsPreview() {
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
        androidx.compose.material3.Surface(
            color = Color(0xFF2E7D32),
            modifier = Modifier.padding(16.dp)
        ) {
            DrawNameAndCoins(playerState = mockPlayer)
        }
    }
}
