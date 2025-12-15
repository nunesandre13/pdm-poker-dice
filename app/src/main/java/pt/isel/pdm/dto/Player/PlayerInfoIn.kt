package pt.isel.pdm.dto.Player

import kotlinx.serialization.Serializable

@Serializable
data class PlayerInfoIn(val playerId: Int, val name: String)
