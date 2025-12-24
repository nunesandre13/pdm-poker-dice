package pt.isel.pdm.domain.user

import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.UserId

data class User(
    val id: UserId,
    val name: Name,
    val email: Email
)
