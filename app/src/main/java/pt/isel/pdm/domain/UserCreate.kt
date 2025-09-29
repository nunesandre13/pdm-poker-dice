package pt.isel.pdm.domain

data class UserCreate(
    val name: Name,
    val email: Email,
    val password: Password
){

    fun convertToUserLogin(user: UserCreate) = UserLogin(email = user.email, password = user.password)

}