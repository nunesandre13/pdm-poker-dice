package pt.isel.pdm.dto.user


import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.Password

@Serializable
data class UserInput(
    val name: String,
    val email: String,
    val password: Password,
)