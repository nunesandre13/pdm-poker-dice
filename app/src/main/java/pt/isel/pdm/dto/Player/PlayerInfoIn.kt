package pt.isel.pdm.dto.Player

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User

@Serializable
data class PlayerInfoIn(val playerId: Int, val name: String) {
    fun toDomain() = User(
        id = playerId.toString(),
        name = Name(name),
        email = Email("$name@gmail")
    )
}
