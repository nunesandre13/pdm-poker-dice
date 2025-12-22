package pt.isel.pdm.domain

data class PlayerInfo(val id: PlayerId, val name: Name)

fun User.toPlayerInfo() = PlayerInfo(PlayerId(id.id), name)