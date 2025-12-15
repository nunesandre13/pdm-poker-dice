package pt.isel.pdm.dto.user

data class UserCreateTokenInputModel(
    val email: String,
    val password: String,
)