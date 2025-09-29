package pt.isel.pdm.domain

data class UserLogin(
    val email: Email,
    val password: Password
)