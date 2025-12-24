package pt.isel.pdm.user.services

import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.user.Email
import pt.isel.pdm.domain.user.InviteCode
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.user.User
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.Success
import pt.isel.pdm.dto.user.UserCreateTokenOutputModel

class UsersServiceMock(
    private val shouldFail: Boolean = false
): UserServices {

    init {
        Log.d("UsersServiceMock", "init")
    }

    private val _currentUser = MutableStateFlow<User?>(User(UserId(1),Name("guilherme"), Email("andre@gmail")))

    override val currentUser: StateFlow<User?> = _currentUser

    override fun getCurrentUser(): User? = _currentUser.value

    override suspend fun restoreSession(): Boolean {
        return false
    }

    override suspend fun login(user: UserCreateTokenInputModel): OutCome<UserCreateTokenOutputModel, UserError> {
        delay(50)
        return if (shouldFail) {
            Failure(UserError.ErrorLogin)
        } else {
            val newUser = User(UserId(123), Name("Guilherme"), Email(user.email))
            _currentUser.value = newUser
            Success(UserCreateTokenOutputModel("fake-token"))
        }
    }

    override suspend fun logout(): OutCome<Unit, UserError> {
        _currentUser.value = null
        return Success(Unit)
    }

    override suspend fun createUser(
        user: UserInput,
        inviteCode: InviteCode
    ): OutCome<User, UserError> {
        return if (shouldFail) {
            Failure(UserError.ErrorCreateUser)
        } else {
            val newUser = User(UserId(123), Name(user.name), Email(user.email))
            _currentUser.value = newUser
            Success(newUser)
        }
    }

    override suspend fun inviteCode(): InviteCode {
        return if (shouldFail) {
            InviteCode("")
        } else {
            InviteCode("Invite Code")
        }
    }

}
