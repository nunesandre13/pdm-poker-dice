package pt.isel.pdm.match.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand
import pt.isel.pdm.domain.Player
import pt.isel.pdm.match.ui.gameLayout.GameTableLayoutClickable

@Composable
fun MyTurnView(players: List<Player>) {
    if (players.isEmpty()) return
    val me = players.first()
    val others = players.drop(1)
    GameTableLayoutClickable(me = me, others = others) {

    }
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
        Player(hand = DicesHand(listOf(DiceFace.TEN,DiceFace.KING,DiceFace.JACK,DiceFace.ACE,DiceFace.NINE)), id = 1),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 2),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 3),
        Player(hand = DicesHand(listOf(DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE,DiceFace.ACE)), id = 4)

    )
    MyTurnView(players = samplePlayers)
}
