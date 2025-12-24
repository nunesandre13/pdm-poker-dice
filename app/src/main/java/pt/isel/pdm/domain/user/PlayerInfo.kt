package pt.isel.pdm.domain.user

import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.PlayerId

data class PlayerInfo(val id: PlayerId, val name: Name)

fun User.toPlayerInfo() = PlayerInfo(PlayerId(id.id), name)