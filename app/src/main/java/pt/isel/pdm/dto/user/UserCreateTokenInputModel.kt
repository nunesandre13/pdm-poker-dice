package pt.isel.pdm.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UserCreateTokenInputModel(
    val email: String,
    val password: String,
)