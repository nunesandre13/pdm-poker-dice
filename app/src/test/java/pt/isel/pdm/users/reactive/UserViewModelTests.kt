package pt.isel.pdm.users.reactive

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Test
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.domain.state.UserScreenState
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserCreateTokenOutputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.user.viewmodel.UserViewModel
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.ViewModelBase

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTests {
    private val testUser1 = User("1", Name("Guilherme"), Email("gui@email"))
    private val testUser2 = User("2", Name("Andre"), Email("andre@email"))

    private fun createSut(config: UserServiceConfig = UserServiceConfig()): UserViewModel {
        return UserViewModel(
            userService = getStubService(config),
            viewModelBase = ViewModelBase(UserScreenState.Idle, UserError.NoError)
        )
    }

    @Test
    fun `init should navigate to LoggedIn when user exists`() = runTest {
        val config = UserServiceConfig(currentUserValue = testUser1, restoreSessionResult = true)
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.stateUi.collect {
                if (it is UserScreenState.UserLoggIn) deferred.complete(Unit)
            }
        }

        sut.init()
        withTimeout(2000) { deferred.await() }
        job.cancel()

        assert(sut.stateUi.value is UserScreenState.UserLoggIn)
        assert((sut.stateUi.value as UserScreenState.UserLoggIn).user.name.name == "Guilherme")
    }

    @Test
    fun `login should update state to LoggedIn on success`() = runTest {
        val config = UserServiceConfig(
            loginResult = Success(UserCreateTokenOutputModel("token-123")),
            currentUserValue = testUser2
        )
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.stateUi.collect {
                if (it is UserScreenState.UserLoggIn) deferred.complete(Unit)
            }
        }

        sut.login(UserCreateTokenInputModel("andre@email", "pass123"))
        withTimeout(2000) { deferred.await() }
        job.cancel()

        val finalState = sut.stateUi.value as UserScreenState.UserLoggIn
        assert(finalState.user.email.email == "andre@email")
    }

    @Test
    fun `login should emit ErrorLogin when service fails`() = runTest {
        val config = UserServiceConfig(
            loginResult = Failure(UserError.ErrorLogin)
        )
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.errorState.collect {
                if (it is UserError.ErrorLogin) deferred.complete(Unit)
            }
        }

        sut.login(UserCreateTokenInputModel("fail@test.com", "wrong"))
        withTimeout(2000) { deferred.await() }
        job.cancel()

        assert(sut.errorState.value is UserError.ErrorLogin)
    }


    @Test
    fun `createUser should navigate to LoggedIn on success`() = runTest {
        val config = UserServiceConfig(
            createUserResult = Success(testUser1)
        )
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.stateUi.collect {
                if (it is UserScreenState.UserLoggIn) deferred.complete(Unit)
            }
        }

        sut.createUser(
            UserInput("Guilherme", "gui@email", "password"),
            InviteCode("INVITE123")
        )

        withTimeout(2000) { deferred.await() }
        job.cancel()

        assert(sut.stateUi.value is UserScreenState.UserLoggIn)
    }

    private fun getStubService(config: UserServiceConfig): UserServices {
        return object : UserServices {
            override val currentUser = MutableStateFlow(config.currentUserValue)
            override fun getCurrentUser(): User? = currentUser.value
            override suspend fun restoreSession(): Boolean = config.restoreSessionResult
            override suspend fun login(user: UserCreateTokenInputModel) = config.loginResult
            override suspend fun logout() = Success(Unit)
            override suspend fun createUser(user: UserInput, inviteCode: InviteCode) = config.createUserResult
            override suspend fun inviteCode() = InviteCode("ABCDE")
        }
    }

    data class UserServiceConfig(
        val currentUserValue: User? = null,
        val restoreSessionResult: Boolean = false,
        val loginResult: OutCome<UserCreateTokenOutputModel, UserError> = Failure(UserError.NoError),
        val createUserResult: OutCome<User, UserError> = Failure(UserError.NoError)
    )
}