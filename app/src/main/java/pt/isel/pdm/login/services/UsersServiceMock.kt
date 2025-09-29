package pt.isel.pdm.login.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import pt.isel.pdm.domain.User
import kotlin.time.Duration.Companion.seconds


class UsersServiceMock : UserServices {
    private val usersFlow = MutableStateFlow<List<User>>(emptyList())

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            usersFlow.emit(
                listOf(
                    User("1", "Andre"),
                    User("2", "Gui"),
                    User("3", "paulo")
                )
            )
        }


        scope.launch {
            while (true) {
                delay(5.seconds)
                usersFlow.value.plus(User("4", "pedro")).also { users ->
                    usersFlow.value = users
                }
                delay(5.seconds)
                usersFlow.value.dropLast(1).also { users ->
                    usersFlow.value = users
                }
            }
        }
    }

    override suspend fun selectUser(user: User): Boolean {
        return true
    }

    override suspend fun createNewUser(user: User): Boolean {
        val current = usersFlow.replayCache.firstOrNull() ?: emptyList()
        usersFlow.emit(current + user)
        return true
    }

    override fun listAvailableUsers(): SharedFlow<List<User>> {
        return usersFlow
    }

    override suspend fun removeUser(user: User): Boolean {
        val current = usersFlow.replayCache.firstOrNull() ?: emptyList()
        val updated = current.filter { it.id != user.id }
        usersFlow.emit(updated)
        return true
    }


}
