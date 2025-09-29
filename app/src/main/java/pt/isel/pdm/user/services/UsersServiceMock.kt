package pt.isel.pdm.user.services

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin


class UsersServiceMock : UserServices {
    private val _currentUser = MutableStateFlow<User?>(
        User(
            id = "1",
            name = "Mock User"
        ))

    override val currentUser: StateFlow<User?> = _currentUser

    override suspend fun login(user: UserLogin): User? {
        return _currentUser.value
    }

    override suspend fun logout(): Boolean {
        _currentUser.value = null
        return true
    }

    override suspend fun createUser(user: UserCreate): User? {
        return _currentUser.value

    }

}
