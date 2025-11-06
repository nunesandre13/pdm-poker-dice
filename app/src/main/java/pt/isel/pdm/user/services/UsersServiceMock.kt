package pt.isel.pdm.user.services

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin


class UsersServiceMock : UserServices {

    init {
        Log.d("UsersServiceMock", "init")
    }

    private val _currentUser = MutableStateFlow<User?>(null)

    override val currentUser: StateFlow<User?> = _currentUser

    override fun getCurrentUser(): User? = _currentUser.value

    override suspend fun login(user: UserLogin): User? {
        return null
    }

    override suspend fun logout(): Boolean {
        _currentUser.value = null
        return true
    }

    override suspend fun createUser(user: UserCreate): User? {
        _currentUser.value =
            User("122434566",user.name.name,user.email)
        return _currentUser.value
    }

}
