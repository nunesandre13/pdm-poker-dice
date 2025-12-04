package pt.isel.pdm.user.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success

class UserServicesHttp : UserServices {
    private val baseUrl = "https://api/"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override fun getCurrentUser(): User? = currentUser.value

    override suspend fun login(user: UserLogin): OutCome<User, UserError> {
        return try {
            val response = client.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            val loggedUser = response.body<User>()
            _currentUser.value = loggedUser
            Success(loggedUser)
        } catch (e: Exception) {
            Failure(UserError.NetworkError)
        }
    }

    override suspend fun logout(): OutCome<Unit, UserError> {
        return try {
            client.post("$baseUrl/auth/logout")
            _currentUser.value = null
            Success(Unit)
        } catch (e: Exception) {
            Failure(UserError.NetworkError)
        }
    }

    override suspend fun createUser(user: UserCreate): OutCome<User, UserError> {
        return try {
            val response = client.post("$baseUrl/users") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            val createdUser = response.body<User>()
            Success(createdUser)
        } catch (e: Exception) {
            Failure(UserError.NetworkError)
        }
    }
}
