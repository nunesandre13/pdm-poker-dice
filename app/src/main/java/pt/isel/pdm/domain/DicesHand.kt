package pt.isel.pdm.domain

data class DicesHand(
    val diceOne: DiceState,
    val diceTwo: DiceState,
    val diceThree: DiceState,
    val diceFour: DiceState,
    val diceFive: DiceState,
)