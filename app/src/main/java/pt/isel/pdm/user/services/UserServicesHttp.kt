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
import pt.isel.pdm.httpConfig.MethodRequest
import pt.isel.pdm.httpConfig.NetworkClient
import pt.isel.pdm.httpConfig.NetworkResult
import pt.isel.pdm.httpConfig.RequestConfig
import pt.isel.pdm.httpConfig.RequestConfigBuilder
import pt.isel.pdm.httpConfig.request
import pt.isel.pdm.user.UserPreferences
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.onOutCome

class UserServicesHttp(private val networkClient: NetworkClient, private val userPreferences: UserPreferences) : UserServices {
    private val baseUrl = "http://10.0.2.2:4000/api"

    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    override fun getCurrentUser(): User? = currentUser.value

    override suspend fun login(user: UserCreateTokenInputModel): OutCome<UserCreateTokenOutputModel, UserError> {
        val tokenConfig = request("$baseUrl/users/token") {
            method = MethodRequest.POST
            body = user
        }
        val tokenResult = networkClient.request<UserCreateTokenOutputModel,UserCreateTokenInputModel>(tokenConfig)
        val tokenModel = when (tokenResult) {
            is NetworkResult.Success -> tokenResult.data
            is NetworkResult.ApiError -> return Failure(UserError.ErrorLogin)
            else -> return Failure(UserError.NetworkError)
        }
        return when (val meResult = fetchMe(tokenModel.token)) {
            is NetworkResult.Success -> {
                val userDomain = meResult.data.toDomain()
                _currentUser.value = userDomain
                userPreferences.saveSession(tokenModel.token, userDomain.id.id.toString())
                Success(tokenModel)
            }
            else -> Failure(UserError.NetworkError)
        }
    }

    private suspend fun fetchMe(token: String): NetworkResult<UserOut> {
        val config = request<Unit>("$baseUrl/me") {
            method = MethodRequest.GET
            header("Authorization", "Bearer $token")
        }
        return networkClient.request<UserOut, Unit>(config)
    }

    override suspend fun logout(): OutCome<Unit, UserError> {
        val config = request<Unit>("$baseUrl/logout") {
            method = MethodRequest.POST
            authenticated()
        }
        val result = networkClient.request<Unit, Unit>(config)
        return when(result) {
            is NetworkResult.Success -> {
                _currentUser.value = null
                userPreferences.clearSession()
                Success(Unit)
            }
            else -> Failure(UserError.NetworkError)
        }
    }

    override suspend fun restoreSession(): Boolean {
        val savedToken = userPreferences.getToken() ?: return false
        return when (val result = fetchMe(savedToken)) {
            is NetworkResult.Success -> {
                _currentUser.value = result.data.toDomain()
                true
            }
            else -> {
                userPreferences.clearSession()
                false
            }
        }
    }

    override suspend fun createUser(user: UserInput, inviteCode: InviteCode): OutCome<User, UserError> {
        val config = request<UserInput>("$baseUrl/users/${inviteCode.code}") {
            method = MethodRequest.POST
            body = user
        }
        val createResult = networkClient.request<Unit, UserInput>(config)
        if (createResult !is NetworkResult.Success) {
            return if (createResult is NetworkResult.ApiError) Failure(UserError.ErrorCreateUser)
            else Failure(UserError.NetworkError)
        }
        val loginInput = UserCreateTokenInputModel(user.email, user.password)
        return when (login(loginInput)) {
            is Success -> {
                currentUser.value?.let { Success(it) } ?: Failure(UserError.ErrorLogin)
            }
            is Failure -> Failure(UserError.ErrorLogin)
        }
    }


    override suspend fun inviteCode(): InviteCode {
        val config = request<Unit>("$baseUrl/users/invite") {
            method = MethodRequest.POST
            authenticated()
        }
        return when (val result = networkClient.request<String, Unit>(config)) {
            is NetworkResult.Success -> {
                val responseUrlString = result.data
                val code = responseUrlString.trim('"').substringAfterLast('/')
                InviteCode(code)
            }
            else -> InviteCode("")
        }
    }
    private suspend fun <T> RequestConfigBuilder<T>.authenticated() {
        val token = userPreferences.getToken()
        if (token != null) header("Authorization", "Bearer $token")
    }
}
