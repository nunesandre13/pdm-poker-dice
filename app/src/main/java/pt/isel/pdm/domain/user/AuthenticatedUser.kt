package pt.isel.pdm.domain.user

class AuthenticatedUser(
    val user: User,
    val token: String,
)