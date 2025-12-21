package pt.isel.pdm.domain

data class User(
    val id: UserId,
    val name: Name,
    val email: Email
)
