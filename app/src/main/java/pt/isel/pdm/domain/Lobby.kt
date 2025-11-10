package pt.isel.pdm.domain

data class Lobby(
    val name: String,
    val id: Int,
    val maxPlayers: Int,
    val players: List<User>,
)
