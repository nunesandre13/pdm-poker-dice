package pt.isel.pdm.users.reactive

import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pt.isel.pdm.domain.state.ProfileError
import pt.isel.pdm.domain.state.ProfileScreenState
import pt.isel.pdm.profile.viewmodel.ProfileViewModel
import pt.isel.pdm.user.services.UsersServiceMock


class ProfileViewModelTests {

    @Test
    fun `initial state should be OnProfileView if a user is already logged in`() = runTest {

        val fakeService = UsersServiceMock()
        val sut = ProfileViewModel.factory(fakeService).create(ProfileViewModel::class.java)

        val latch = SuspendingLatch()
        var lastState: ProfileScreenState? = null

        val job = launch {
            sut.stateUi.collect { state ->
                lastState = state
                if (state is ProfileScreenState.OnProfileView) {
                    latch.open()
                }
            }
        }

        latch.await()
        job.cancel()

        assert(lastState is ProfileScreenState.OnProfileView)
        val profile = lastState as ProfileScreenState.OnProfileView
        assert(profile.user.name.name == "guilherme")
    }

    @Test
    fun `logout should change state to LoggedOut`() = runTest {
        val fakeService = UsersServiceMock()
        val sut = ProfileViewModel.factory(fakeService).create(ProfileViewModel::class.java)

        val latch = SuspendingLatch()
        var lastState: ProfileScreenState? = null

        val job = launch {
            sut.stateUi.collect { state ->
                lastState = state
                if (state is ProfileScreenState.LoggedOut) {
                    latch.open()
                }
            }
        }

        sut.logout()

        latch.await()
        job.cancel()

        assert(lastState == ProfileScreenState.LoggedOut)
    }

    @Test
    fun `generateInviteCode updates state with a new code`() = runTest {
        val fakeService = UsersServiceMock()
        val sut = ProfileViewModel.factory(fakeService).create(ProfileViewModel::class.java)

        val latch = SuspendingLatch()
        var lastState: ProfileScreenState? = null

        val job = launch {
            sut.stateUi.collect { state ->
                lastState = state
                if (state is ProfileScreenState.OnProfileView && state.inviteCode != null) {
                    latch.open()
                }
            }
        }

        sut.generateInviteCode()

        latch.await()
        job.cancel()

        assert(lastState is ProfileScreenState.OnProfileView)
        val inviteCode = (lastState as ProfileScreenState.OnProfileView).inviteCode
        assert(!inviteCode.isNullOrEmpty())
    }
}