package pt.isel.pdm.users.reactive

import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import pt.isel.pdm.SuspendingLatch
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.domain.state.UserScreenState
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.user.viewmodel.UserViewModel

class UserViewModelTests {
    @Test
    fun `once login is called, state changes to UserLoggIn when successful`() = runTest {
        val fakeService = UsersServiceMock() // Usa o Mock
        val sut =
            UserViewModel.factory(fakeService).create(UserViewModel::class.java) // usa o factory do ViewModel pois tem dependências

        val latch = SuspendingLatch() // Sincroniza o teste com a Coroutine assíncrona
        var lastState: UserScreenState? = null

        val collectorJob = launch {
            sut.stateUi.collect { state ->
                lastState = state
                if (state is UserScreenState.UserLoggIn) {
                    latch.open() // Quando o login termina, liberta o latch.await()
                }
            }
        }

        sut.login(UserCreateTokenInputModel("email@test.com", "pass123"))

        latch.await()
        collectorJob.cancel() // Importante para não deixar coroutines ativas no teste

        assert(lastState is UserScreenState.UserLoggIn)
        val loggedUser = (lastState as UserScreenState.UserLoggIn).user
        assert(loggedUser.email.email == "email@test.com")
    }

    @Test
    fun `state changes to Error when login fails`() = runTest {
        // Forçamos o Mock a devolver um Failure
        val fakeService = UsersServiceMock(shouldFail = true)
        val sut = UserViewModel.factory(fakeService).create(UserViewModel::class.java)

        val latch = SuspendingLatch()
        var errorOccurred: UserError? = null

        val collectorJob = launch {
            sut.errorState.collect { error ->
                if (error is UserError.ErrorLogin) {
                    errorOccurred = error
                    latch.open()
                }
            }
        }

        sut.login(UserCreateTokenInputModel("fail@test.com", "wrong"))

        latch.await()
        collectorJob.cancel()

        assert(errorOccurred is UserError.ErrorLogin) // Garante que o erro certo foi emitido
    }

    @Test
    fun `createUser changes state to UserLoggIn when successful`() = runTest {
        val fakeService = UsersServiceMock(shouldFail = false)
        val sut = UserViewModel.factory(fakeService).create(UserViewModel::class.java)
        val latch = SuspendingLatch()
        var lastState: UserScreenState? = null

        val collectorJob = launch {
            sut.stateUi.collect { state ->
                lastState = state
                if (state is UserScreenState.UserLoggIn) {
                    latch.open()
                }
            }
        }

        sut.createUser(
            UserInput("Guilherme", "gui@test.com", "pass123"),
            InviteCode("ABCDE")
        )

        latch.await()
        collectorJob.cancel()

        assert(lastState is UserScreenState.UserLoggIn)
        val user = (lastState as UserScreenState.UserLoggIn).user
        assert(user.name.name == "Guilherme")
    }
}