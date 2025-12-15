package pt.isel.pdm.dto.Lobby

import kotlinx.serialization.Serializable

@Serializable
data class LobbyOut(
    val name: String,
    val description: String,
    val maxPlayer: Int,
    val minPlayer: Int,
    val numberOfRounds: Int,
    val firstAnte: Int,
)