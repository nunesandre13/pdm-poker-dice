package pt.isel.pdm.game

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand

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
    val samplePlayers = listOf(
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 1),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 2),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 3),
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