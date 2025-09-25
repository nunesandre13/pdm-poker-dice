package pt.isel.pdm.domain

data class Lobby(
    val name: String,
    val maxPlayers: Int,
    val players: List<User>,
)
