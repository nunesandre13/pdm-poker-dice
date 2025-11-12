package pt.isel.pdm.domain
const val NUMBER_OF_ROLLS = 2
sealed class PlayerStatus {
    abstract val type: StatusType

    data object NotStarted : PlayerStatus() {
        override val type = StatusType.NOT_STARTED
    }

    data object PassRound : PlayerStatus() {
        override val type = StatusType.PASS_ROUND
    }

    data class FinalHand(val hand: DicesHand) : PlayerStatus() {
        override val type = StatusType.FINAL_HAND
    }

    data class StillRolling(val hand: DicesHand, val remainingRolls: Int = NUMBER_OF_ROLLS) : PlayerStatus() {
        override val type = StatusType.STILL_ROLLING
    }
}

data class PlayerRoundState(
    val playerId: Int,
    val coins: Int,
    val playerStatus: PlayerStatus = PlayerStatus.NotStarted
)

enum class StatusType {
    NOT_STARTED,
    PASS_ROUND,
    FINAL_HAND,
    STILL_ROLLING
}
