package pt.isel.pdm.dto.player

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserId

@Serializable
data class PlayerInfoIn(val playerId: Int, val name: String) {
    fun toDomain() = User(
        id = UserId(playerId),
        name = Name(name),
        email = Email("$name@gmail")
    )
}
