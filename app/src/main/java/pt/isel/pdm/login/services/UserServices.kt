package pt.isel.pdm.login.services


import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.User

interface UserServices {

    val currentUser: StateFlow<User?>

    suspend fun login(user: User): Boolean
    suspend fun logout(): Boolean
    suspend fun createUser(user: User): Boolean

}