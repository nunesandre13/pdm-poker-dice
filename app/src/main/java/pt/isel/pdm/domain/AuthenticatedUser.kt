package pt.isel.pdm.domain

class AuthenticatedUser(
    val user: User,
    val token: String,
)