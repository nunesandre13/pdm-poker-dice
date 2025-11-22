package pt.isel.pdm.user.services

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.Success

class UsersServiceMock : UserServices {

    init {
        Log.d("UsersServiceMock", "init")
    }

    private val _currentUser = MutableStateFlow<User?>(User("1","guilherme", Email("andre@gmail")))

    override val currentUser: StateFlow<User?> = _currentUser

    override fun getCurrentUser(): User? = _currentUser.value

    override suspend fun login(user: UserLogin): OutCome<User, UserError> {
        return Failure(UserError.ErrorLogin)
    }

    override suspend fun logout(): OutCome<Unit, UserError> {
        _currentUser.value = null
        return Success(Unit)
    }

    override suspend fun createUser(user: UserCreate): OutCome<User, UserError> {
        val newUser = User("122434566", user.name.name, user.email)
        _currentUser.value = newUser
        return Success(newUser)
    }
}
