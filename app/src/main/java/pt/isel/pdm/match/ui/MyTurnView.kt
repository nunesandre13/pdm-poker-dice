//package pt.isel.pdm.match.ui
//
//import android.content.res.Configuration
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
//import pt.isel.pdm.domain.DiceFace
//import pt.isel.pdm.domain.DicesHand
//import pt.isel.pdm.domain.PlayerRoundState
//import pt.isel.pdm.domain.PlayerStatus
//import pt.isel.pdm.match.ui.matchLayout.GameTableLayoutClickable
//
//@Composable
//fun MyTurnView(players: List<PlayerRoundState>) {
//    if (players.isEmpty()) return
//    val me = players.first()
//    val others = players.drop(1)
//    GameTableLayoutClickable(me = me, others = others) {}
//}
//
//@Preview(
//    showBackground = true,
//    widthDp = 480,
//    heightDp = 320,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun MyTurnPreview() {
//    val samplePlayers = listOf(
//        PlayerRoundState(
//            playerId = 1,
//            coins = 50,
//            playerStatus = PlayerStatus.StillRolling(
//                hand = DicesHand(
//                    listOf(
//                        DiceFace.TEN,
//                        DiceFace.KING,
//                        DiceFace.JACK,
//                        DiceFace.ACE,
//                        DiceFace.NINE
//                    )
//                ),
//                remainingRolls = 1
//            )
//        ),
//        PlayerRoundState(playerId = 2, coins = 0, playerStatus = PlayerStatus.NotStarted),
//        PlayerRoundState(playerId = 3, coins = 0, playerStatus = PlayerStatus.NotStarted),
//        PlayerRoundState(playerId = 4, coins = 0, playerStatus = PlayerStatus.NotStarted)
//    )
//    MyTurnView(players = samplePlayers)
//}