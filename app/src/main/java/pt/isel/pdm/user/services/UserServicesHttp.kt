package pt.isel.pdm.user.services

import android.util.Log
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
import pt.isel.pdm.domain.AuthenticatedUser
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.dto.user.InvitationUrlModel
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserCreateTokenOutputModel
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.dto.user.UserOut
import pt.isel.pdm.dto.user.toDomain
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.onOutCome

class UserServicesHttp : UserServices {
    private val baseUrl = "http://10.0.2.2:4000/api"
    private var token: String? = null

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
            logger("entering on the login")
            val response = client.post("$baseUrl/users/token") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            val tokenModel = response.body<UserCreateTokenOutputModel>()
            token = tokenModel.token

            val logged = client.get("$baseUrl/me") {
                token?.let { token ->
                    this.header("Authorization", "Bearer $token")
                }

            }.body<UserOut>()

            _currentUser.value = logged.toDomain()
            Success(tokenModel)
        } catch (e: Exception) {
            logger(e.message ?: "SOME ERROR")
            Failure(UserError.NetworkError)
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

    override suspend fun createUser(
        user: UserInput,
        inviteCode: InviteCode
    ): OutCome<User, UserError> {
        return try {
            val response = client.post("$baseUrl/users/${inviteCode.code}") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }

            if (response.status == HttpStatusCode.Created) {
                val loginInput = UserCreateTokenInputModel(user.email, user.password.password)
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
            logger(e.message ?: "SOME ERROR")
            Failure(UserError.NetworkError)
        }
    }

    override suspend fun inviteCode(user: AuthenticatedUser): InviteCode =
        try {
            val response = client.post("$baseUrl/users/invite")
            val responseUrl = response.body<InvitationUrlModel>()
            val code = responseUrl.url.substringAfterLast('/')
            InviteCode(code)
        } catch (e: Exception) {
            InviteCode("")
        }

    private fun logger(str: String) {
        Log.v("HTTP_USERS", str)
    }
}
