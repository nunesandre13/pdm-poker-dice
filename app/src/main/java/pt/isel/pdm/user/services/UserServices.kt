package pt.isel.pdm.user.services



import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin


interface UserServices {

    val currentUser: StateFlow<User?>

    suspend fun login(user: UserLogin): User?
    suspend fun logout(): Boolean
    suspend fun createUser(user: UserCreate): User?

}