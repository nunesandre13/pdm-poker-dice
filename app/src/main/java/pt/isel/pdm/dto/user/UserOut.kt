package pt.isel.pdm.dto.user

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User

@Serializable
data class UserOut(
    val id: Int,
    val name: String,
    val email: String
)

fun UserOut.toDomain() = User(id.toString(), Name(name), Email(email))