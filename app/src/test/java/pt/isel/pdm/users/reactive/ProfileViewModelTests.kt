package pt.isel.pdm.users.reactive

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Test
import pt.isel.pdm.domain.user.Email
import pt.isel.pdm.domain.user.InviteCode
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.user.User
import pt.isel.pdm.domain.UserId
import pt.isel.pdm.domain.state.ProfileError
import pt.isel.pdm.domain.state.ProfileScreenState
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.profile.viewmodel.ProfileViewModel
import pt.isel.pdm.user.services.UserServices
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.ViewModelBase

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTests {
    private val testUser = User(UserId(1), Name("Guilherme"), Email("gui@test.com"))
    private fun createSut(config: ProfileServiceConfig = ProfileServiceConfig()): ProfileViewModel {
        return ProfileViewModel(
            userService = getStubService(config),
            viewModelState = ViewModelBase(ProfileScreenState.Idle, ProfileError.NoError)
        )
    }

    @Test
    fun `initial state should be OnProfileView if user is logged in`() = runTest {
        val config = ProfileServiceConfig(currentUserValue = testUser)
        val deferred = CompletableDeferred<Unit>()
        val sut = createSut(config)

        val job = launch {
            sut.stateUi.collect {
                if (it is ProfileScreenState.OnProfileView) deferred.complete(Unit)
            }
        }

        withTimeout(2000) { deferred.await() }
        job.cancel()

        val state = sut.stateUi.value as ProfileScreenState.OnProfileView
        assert(state.user.name.name == "Guilherme")
    }

    @Test
    fun `logout should change state to LoggedOut`() = runTest {
        val config = ProfileServiceConfig(currentUserValue = testUser, logoutResult = Success(Unit))
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.stateUi.collect {
                if (it is ProfileScreenState.LoggedOut) deferred.complete(Unit)
            }
        }

        sut.logout()
        withTimeout(2000) { deferred.await() }
        job.cancel()

        assert(sut.stateUi.value is ProfileScreenState.LoggedOut)
    }

    @Test
    fun `generateInviteCode updates state with a new code`() = runTest {
        val config = ProfileServiceConfig(
            currentUserValue = testUser,
            inviteCodeResult = InviteCode("NEW123")
        )
        val sut = createSut(config)
        val deferred = CompletableDeferred<Unit>()

        val job = launch {
            sut.stateUi.collect {
                if (it is ProfileScreenState.OnProfileView && it.inviteCode == "NEW123") {
                    deferred.complete(Unit)
                }
            }
        }

        sut.generateInviteCode()
        withTimeout(2000) { deferred.await() }
        job.cancel()

        val state = sut.stateUi.value as ProfileScreenState.OnProfileView
        assert(state.inviteCode == "NEW123")
    }

    private fun getStubService(config: ProfileServiceConfig): UserServices {
        return object : UserServices {
            override val currentUser = MutableStateFlow(config.currentUserValue)
            override fun getCurrentUser(): User? = currentUser.value
            override suspend fun logout(): OutCome<Unit, UserError> = config.logoutResult
            override suspend fun inviteCode(): InviteCode = config.inviteCodeResult

            override suspend fun restoreSession() = true
            override suspend fun login(user: UserCreateTokenInputModel) = Failure(UserError.NoError)
            override suspend fun createUser(user: UserInput, inviteCode: InviteCode) = Failure(UserError.NoError)
        }
    }
    data class ProfileServiceConfig(
        val currentUserValue: User? = null,
        val logoutResult: OutCome<Unit, UserError> = Success(Unit),
        val inviteCodeResult: InviteCode = InviteCode("")
    )
}