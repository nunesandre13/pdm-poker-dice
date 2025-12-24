package pt.isel.pdm.domain.user

import pt.isel.pdm.domain.Name

data class UserCreate(
    val name: Name,
    val email: Email,
    val password: Password
){

    fun convertToUserLogin(user: UserCreate) = UserLogin(email = user.email, password = user.password)

}