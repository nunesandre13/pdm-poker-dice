package pt.isel.pdm.domain

const val INITIAL_ANTE = 0

data class RawRound(
    val id: RoundId,
    val players: List<PlayerRoundState>,
    val ante: Int = INITIAL_ANTE,
    val totalBet: Int =(INITIAL_ANTE),
    val state: RoundState
)

sealed class RoundState {

    data class Rolling(val turn: PlayerRoundState) : RoundState()
    data class Betting(val turn: PlayerRoundState, val amount: Int, val playersBets: List<PlayerBetState>) : RoundState()
    data class Finished(val winner: PlayerId?) : RoundState()
}

data class PlayerBetState(val playerId: PlayerId, val betState: BetState)

enum class BetState {
    FOLD,
    CALL,
    PENDING
}