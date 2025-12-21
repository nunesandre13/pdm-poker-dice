package pt.isel.pdm.dto.user

import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserId

@Serializable
data class UserOut(
    val id: Int,
    val name: String,
    val email: String
)

fun UserOut.toDomain() = User(UserId(id), Name(name), Email(email))