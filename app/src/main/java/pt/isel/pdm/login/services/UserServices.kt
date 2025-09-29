package pt.isel.pdm.login.services


import kotlinx.coroutines.flow.SharedFlow
import pt.isel.pdm.domain.User

interface UserServices {

    suspend fun selectUser(user: User): Boolean

    suspend fun createNewUser(user: User): Boolean

    fun listAvailableUsers(): SharedFlow<List<User>>

    suspend fun removeUser(user: User): Boolean

}