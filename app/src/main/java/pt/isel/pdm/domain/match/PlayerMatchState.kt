package pt.isel.pdm.domain.match

import pt.isel.pdm.domain.PlayerId

data class PlayerMatchState(
    val playerId: PlayerId,
    val coins: Int
)
