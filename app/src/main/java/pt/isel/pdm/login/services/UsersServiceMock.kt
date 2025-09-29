package pt.isel.pdm.login.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.User
import kotlin.time.Duration.Companion.seconds


class UsersServiceMock : UserServices {

    private val _currentUser = MutableStateFlow<User?>(null)

    override val currentUser: StateFlow<User?> = _currentUser

    override suspend fun login(user: User): Boolean {
        _currentUser.value = user
        return true
    }

    override suspend fun logout(): Boolean {
        _currentUser.value = null
        return true
    }

    override suspend fun createUser(user: User): Boolean {
        return true
    }

}
