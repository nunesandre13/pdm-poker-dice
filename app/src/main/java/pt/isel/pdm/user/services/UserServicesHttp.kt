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
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserCreateTokenOutputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.dto.user.UserOut
import pt.isel.pdm.dto.user.toDomain
import pt.isel.pdm.user.UserPreferences
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.onOutCome

class UserServicesHttp(private val userPreferences: UserPreferences) : UserServices {
    private val baseUrl = "http://10.0.2.2:4000/api"

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

    override suspend fun login(user: UserCreateTokenInputModel): OutCome<UserCreateTokenOutputModel, UserError> =
        try {
            val response = client.post("$baseUrl/users/token") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            val tokenModel = response.body<UserCreateTokenOutputModel>()
            val token = tokenModel.token

            val logged = client.get("$baseUrl/me") {
                header("Authorization", "Bearer $token")
            }.body<UserOut>()
            _currentUser.value = logged.toDomain()
            userPreferences.saveSession(tokenModel.token, logged.id.toString())
            Success(tokenModel)
        } catch (e: Exception) {
            Failure(UserError.NetworkError)
        }

    override suspend fun logout(): OutCome<Unit, UserError> {
        return try {
            client.post("$baseUrl/logout")
            _currentUser.value = null
            userPreferences.clearSession()
            Success(Unit)
        } catch (e: Exception) {
            Failure(UserError.NetworkError)
        }
    }

    suspend fun restoreSession(): Boolean {
        val savedToken = userPreferences.getToken() ?: return false
        return try {
            val logged = client.get("$baseUrl/me") {
                header("Authorization", "Bearer $savedToken")
            }.body<UserOut>()
            _currentUser.value = logged.toDomain()
            true
        } catch (e: Exception) {
            userPreferences.clearSession()
            false
        }
    }

    override suspend fun createUser(
        user: UserInput,
        inviteCode: InviteCode
    ): OutCome<User, UserError> {
        return try {
            val url = "$baseUrl/users/${inviteCode.code}"
            val response = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            if (response.status == HttpStatusCode.Created) {
                val loginInput = UserCreateTokenInputModel(user.email, user.password)
                return login(loginInput).onOutCome(
                    onSuccess = {
                        Success(currentUser.value!!)
                    },
                    onFailure = {
                        Failure(UserError.ErrorLogin)
                    }
                )
            } else {
                Failure(UserError.ErrorCreateUser)
            }
        } catch (e: Exception) {
            Failure(UserError.NetworkError)
        }
    }

    override suspend fun inviteCode(): InviteCode = try {
        val response = client.post("$baseUrl/users/invite") {
            userPreferences.getToken()?.let { token ->
                header("Authorization", "Bearer $token")
            }
        }
        val responseUrlString = response.body<String>().trim('"')
        val code = responseUrlString.substringAfterLast('/')
        InviteCode(code)
    } catch (e: Exception) {
        InviteCode("")
    }
}
