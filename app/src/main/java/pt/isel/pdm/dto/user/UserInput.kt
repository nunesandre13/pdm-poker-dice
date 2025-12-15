package pt.isel.pdm.dto.user

import kotlinx.serialization.Serializable
@Serializable
data class UserInput(
    val name: String,
    val email: String,
    val password: String,
)