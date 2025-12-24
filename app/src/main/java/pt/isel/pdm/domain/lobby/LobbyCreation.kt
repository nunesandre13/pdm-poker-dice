package pt.isel.pdm.domain.lobby

import pt.isel.pdm.dto.lobby.LobbyOut

data class LobbyCreation(
    val name: String,
    val description: String,
    val maxPlayer: Int,
    val minPlayer: Int,
    val numberOfRounds: Int,
    val firstAnte: Int,
)
fun LobbyCreation.toDto(): LobbyOut = LobbyOut(name,description,maxPlayer,minPlayer,numberOfRounds,firstAnte)