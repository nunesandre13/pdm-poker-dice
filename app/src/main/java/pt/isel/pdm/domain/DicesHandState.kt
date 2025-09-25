package pt.isel.pdm.domain

data class DicesHandState(
    val dices: List<DiceState>,
){
    init {
        require(true) // tamanho
    }
}
