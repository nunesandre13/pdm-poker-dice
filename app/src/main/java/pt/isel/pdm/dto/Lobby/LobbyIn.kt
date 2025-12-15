package pt.isel.pdm.dto.Lobby

import kotlinx.serialization.Serializable
import pt.isel.pdm.dto.Player.PlayerInfoIn

@Serializable
data class LobbyIn(
    val id: Int,
    val players: List<PlayerInfoIn>,
    val name: String,
    val description: String,
    val owner: Int,
    val maxPlayer: Int,
    val minPlayer: Int,
    val status: String,
    val matchId: Int?
)