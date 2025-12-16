package pt.isel.pdm.dto.Match

import pt.isel.pdm.domain.INITIAL_ANTE
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.NUMBER_OF_ROLLS

data class MatchIn(
    val id: Int,
    val players: List<PlayerMatchStateOut>,
    val owner: Int,
    val actualRound : RoundOut,
    val initialCoins: Int,
    val remainingRounds: Int,
    val matchStatus: MatchStatusOut
)

enum class MatchStatusOut {
    FINISHED,
    ELAPSED;
}

data class PlayerRoundStateOut(
    val playerId: Int,
    val coins: Int,
    val playerStatus: PlayerStatusIn = PlayerStatusIn.NotStarted
)

sealed class PlayerStatusIn {
    data object NotStarted : PlayerStatusIn()

    data object PassRound : PlayerStatusIn()

    data class FinalHand(val hand: HandOut) : PlayerStatusIn()

    data class StillRolling(val hand: HandOut, val remainingRolls: Int = NUMBER_OF_ROLLS) : PlayerStatusIn()
}
data class PlayerMatchStateOut(
    val playerId: Int,
    val coins: Int
)
data class RoundOut(
    val id: Int,
    val players: List<PlayerRoundStateOut>,
    val ante: Int = INITIAL_ANTE,
    val totalBet: Int = INITIAL_ANTE,
    val state: RoundStateOut
)

sealed class RoundStateOut {
    data class Rolling(val turn: PlayerRoundStateOut) : RoundStateOut()

    data class Betting(val turn: PlayerRoundStateOut, val amount: Int, val playersBets: List<PlayerBetStateOut>) :
        RoundStateOut()

    data class Finished(val winner: Int?) : RoundStateOut()
}

data class HandOut(
    val dices: List<DicesOut>,
)

class DiceValueOut(val rank: Int)


enum class DicesOut(val value: DiceValueOut) {
    ACE(DiceValueOut(14)),
    KING(DiceValueOut(13)),
    QUEEN(DiceValueOut(12)),
    JACK(DiceValueOut(11)),
    TEN(DiceValueOut(10)),
    NINE(DiceValueOut(9));
}

data class PlayerBetStateOut(val playerId: Int, val betState: BetStateOut)

enum class BetStateOut {
    FOLD,
    CALL,
    PENDING
}

fun MatchIn.toDomain(): Match = TODO()