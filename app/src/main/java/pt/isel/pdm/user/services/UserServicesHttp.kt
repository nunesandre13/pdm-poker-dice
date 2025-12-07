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
import kotlinx.serialization.json.JsonPrimitive
import pt.isel.pdm.domain.AuthenticatedUser
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import kotlin.collections.get

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
            val response = client.post("$baseUrl/users/token") {
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
            client.post("$baseUrl/logout")
            _currentUser.value = null
            Success(Unit)
        } catch (e: Exception) {
            Failure(UserError.NetworkError)
        }
    }

    override suspend fun createUser(user: UserCreate, inviteCode: InviteCode): OutCome<User, UserError> {
        return try {
            val response = client.post("$baseUrl/users/$inviteCode") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            val createdUser = response.body<User>()
            Success(createdUser)
        } catch (e: Exception) {
            Failure(UserError.NetworkError)
        }
    }

    override suspend fun inviteCode(user: AuthenticatedUser): InviteCode {
        return try {
            val response = client.post("$baseUrl/users/invite") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            val bodyText: String = response.body()

            val path = try {
                val jsonElem = Json.parseToJsonElement(bodyText)
                val str = if (jsonElem is JsonPrimitive && jsonElem.isString) jsonElem.content else ""
                str.substringAfterLast('/')
            } catch (e: Exception) {
                ""
            }

            val code = path.ifEmpty { "" }
            InviteCode(code)
        } catch (e: Exception) {
            InviteCode("")
        }
    }
}
