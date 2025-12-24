package pt.isel.pdm.domain.user

data class UserLogin(
    val email: Email,
    val password: Password
)