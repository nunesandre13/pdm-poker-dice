package pt.isel.pdm.user.services

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.AuthenticatedUser
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.Success
import pt.isel.pdm.dto.user.UserCreateTokenOutputModel

class UsersServiceMock : UserServices {

    init {
        Log.d("UsersServiceMock", "init")
    }

    private val _currentUser = MutableStateFlow<User?>(User("1",Name("guilherme"), Email("andre@gmail")))

    override val currentUser: StateFlow<User?> = _currentUser

    override fun getCurrentUser(): User? = _currentUser.value

    override suspend fun restoreSession(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun login(user: UserCreateTokenInputModel): OutCome<UserCreateTokenOutputModel, UserError> {
        return Failure(UserError.ErrorLogin)
    }

    override suspend fun logout(): OutCome<Unit, UserError> {
        _currentUser.value = null
        return Success(Unit)
    }

    override suspend fun createUser(
        user: UserInput,
        inviteCode: InviteCode
    ): OutCome<User, UserError> {
        val newUser = User("122434566", Name(user.name), Email(user.email))
        _currentUser.value = newUser
        return Success(newUser)
    }

    override suspend fun inviteCode(): InviteCode {
        TODO("Not yet implemented")
    }
}
