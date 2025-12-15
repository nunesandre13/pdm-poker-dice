package pt.isel.pdm.dto.user

import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Password

data class UserInput(
    val name: String,
    val email: String,
    val password: Password,
)