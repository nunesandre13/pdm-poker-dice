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
        val tokenConfig = RequestConfig(
            url = "$baseUrl/users/token",
            method = MethodRequest.POST,
            body = user
        )
        val tokenResult = networkClient.request<UserCreateTokenOutputModel, UserCreateTokenInputModel>(tokenConfig)
        return when (tokenResult) {
            is NetworkResult.Success -> {
                val tokenModel = tokenResult.data
                when (val meResult = fetchMe(tokenModel.token)) {
                    is NetworkResult.Success -> {
                        val userDomain = meResult.data.toDomain()
                        _currentUser.value = userDomain
                        userPreferences.saveSession(tokenModel.token, userDomain.id.id.toString())
                        Success(tokenModel)
                    }
                    else -> Failure(UserError.NetworkError)
                }
            }
            is NetworkResult.ApiError -> Failure(UserError.ErrorLogin)
            else -> Failure(UserError.NetworkError)
        }
    }

    private suspend fun fetchMe(token: String): NetworkResult<UserOut> {
        val config = RequestConfig<Unit>(
            url = "$baseUrl/me",
            method = MethodRequest.GET,
            headers = mapOf("Authorization" to "Bearer $token")
        )
        return networkClient.request<UserOut, Unit>(config)
    }

    override suspend fun logout(): OutCome<Unit, UserError> {
        val config = RequestConfig<Unit>(
            url = "$baseUrl/logout",
            method = MethodRequest.POST,
            headers = getAuthHeader()
        )
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

    override suspend fun createUser(
        user: UserInput,
        inviteCode: InviteCode
    ): OutCome<User, UserError> {
        val config = RequestConfig(
            url = "$baseUrl/users/${inviteCode.code}",
            method = MethodRequest.POST,
            body = user
        )
        val createResult = networkClient.request<Unit, UserInput>(config)
        return when (createResult) {
            is NetworkResult.Success -> {
                val loginInput = UserCreateTokenInputModel(user.email, user.password)
                login(loginInput).onOutCome(
                    onSuccess = {
                        Success(currentUser.value!!)
                    },
                    onFailure = {
                        Failure(UserError.ErrorLogin)
                    }
                )
            }
            is NetworkResult.ApiError -> Failure(UserError.ErrorCreateUser)
            else -> Failure(UserError.NetworkError)
        }
    }


    override suspend fun inviteCode(): InviteCode {
        val config = RequestConfig<Unit>(
            url = "$baseUrl/users/invite",
            method = MethodRequest.POST,
            headers = getAuthHeader()
        )
        return when (val result = networkClient.request<String, Unit>(config)) {
            is NetworkResult.Success -> {
                val responseUrlString = result.data
                val code = responseUrlString.trim('"').substringAfterLast('/')
                InviteCode(code)
            }
            else -> InviteCode("")
        }
    }

    private suspend fun getAuthHeader(): Map<String, String> {
        val token = userPreferences.getToken()
        return if (token != null) {
            mapOf("Authorization" to "Bearer $token")
        } else {
            emptyMap()
        }
    }

}
