package pt.isel.pdm.game

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand


@Composable
fun PlayerView(player: Player, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color.Gray, CircleShape)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Player ${player.id}", textAlign = TextAlign.Center)
            if (player.hand.dices.isNotEmpty()) {
                // Pode substituir este Text por imagens dos dados
                Text(
                    text = player.hand.dices.joinToString(" "),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Organiza os jogadores na mesa, posicionando o jogador principal em baixo
 * e os outros em redor, com base no seu número.
 */
@Composable
private fun GameTableLayout(me: Player, others: List<Player>) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        // O meu jogador (em baixo, maior)
        val myPlayerRef = createRef()
        PlayerView(
            player = me,
            modifier = Modifier
                .constrainAs(myPlayerRef) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(width = 120.dp, height = 80.dp)
        )

        when (others.size) {
            1 -> { // Total: 2 jogadores
                val other1 = createRef()
                PlayerView(player = others[0], Modifier.constrainAs(other1) {
                    top.linkTo(parent.top, margin = 16.dp)
                    centerHorizontallyTo(parent)
                })
            }

            2 -> { // Total: 3 jogadores
                val (other1, other2) = createRefs()
                PlayerView(player = others[0], Modifier.constrainAs(other1) {
                    top.linkTo(parent.top, margin = 16.dp); start.linkTo(
                    parent.start,
                    margin = 32.dp
                )
                })
                PlayerView(player = others[1], Modifier.constrainAs(other2) {
                    top.linkTo(parent.top, margin = 16.dp); end.linkTo(parent.end, margin = 32.dp)
                })
            }

            3 -> { // Total: 4 jogadores
                val (other1, other2, other3) = createRefs()
                PlayerView(player = others[0], Modifier.constrainAs(other1) {
                    centerVerticallyTo(parent); start.linkTo(parent.start, margin = 16.dp)
                })
                PlayerView(player = others[1], Modifier.constrainAs(other2) {
                    top.linkTo(parent.top, margin = 16.dp); centerHorizontallyTo(parent)
                })
                PlayerView(player = others[2], Modifier.constrainAs(other3) {
                    centerVerticallyTo(parent); end.linkTo(parent.end, margin = 16.dp)
                })
            }

            4 -> { // Total: 5 jogadores
                val (other1, other2, other3, other4) = createRefs()
                PlayerView(player = others[0], Modifier.constrainAs(other1) {
                    centerVerticallyTo(parent); start.linkTo(parent.start, margin = 16.dp)
                })
                PlayerView(player = others[1], Modifier.constrainAs(other2) {
                    top.linkTo(parent.top, margin = 16.dp); start.linkTo(
                    parent.start,
                    margin = 64.dp
                )
                })
                PlayerView(player = others[2], Modifier.constrainAs(other3) {
                    top.linkTo(parent.top, margin = 16.dp); end.linkTo(parent.end, margin = 64.dp)
                })
                PlayerView(player = others[3], Modifier.constrainAs(other4) {
                    centerVerticallyTo(parent); end.linkTo(parent.end, margin = 16.dp)
                })
            }

            5 -> { // Total: 6 jogadores
                val startGuideline = createGuidelineFromStart(fraction = 0.1f)
                val endGuideline = createGuidelineFromEnd(fraction = 0.1f)

                val (other1, other2, other3, other4, other5) = createRefs()
                PlayerView(player = others[0], Modifier.constrainAs(other1) {
                    centerVerticallyTo(parent) // top.linkTo(parent.top) e bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, margin = 16.dp)
                })
                PlayerView(player = others[1], Modifier.constrainAs(other2) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(startGuideline)
                })
                PlayerView(player = others[2], Modifier.constrainAs(other3) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(other2.end)
                    end.linkTo(other4.start)
                })
                PlayerView(player = others[3], Modifier.constrainAs(other4) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(endGuideline)
                })
                PlayerView(player = others[4], Modifier.constrainAs(other5) {
                    centerVerticallyTo(parent); end.linkTo(parent.end, margin = 16.dp)
                })
            }
        }
    }
}

/**
 * A vista principal do ecrã de jogo, que mostra todos os jogadores.
 */
@Composable
fun MyTurnView(players: List<Player>) {
    if (players.isEmpty()) return

    val me = players.first()
    val others = players.drop(1)

    GameTableLayout(me = me, others = others)
}

@Preview(
    showBackground = true,
    widthDp = 480,
    heightDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun MyTurnPreview() {
    // Exemplo com 4 jogadores para a preview
    val samplePlayers = listOf(
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 1), // Eu
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 2),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 3), // Mão vazia
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 4),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 5),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 5)

    )
    MyTurnView(players = samplePlayers)
}

data class Player(
    val hand: DicesHand,
    val id: Int
)