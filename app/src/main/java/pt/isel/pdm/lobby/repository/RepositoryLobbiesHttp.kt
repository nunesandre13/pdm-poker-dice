package pt.isel.pdm.lobby.repository

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.events.LobbyResponse
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.dto.lobby.LobbyEvent
import pt.isel.pdm.dto.lobby.toDomain
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.header
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import pt.isel.pdm.domain.LobbyCreation
import pt.isel.pdm.domain.toDto
import pt.isel.pdm.dto.lobby.LobbyIn
import pt.isel.pdm.dto.lobby.LobbyOut
import pt.isel.pdm.httpConfig.MethodRequest
import pt.isel.pdm.httpConfig.NetworkClient
import pt.isel.pdm.httpConfig.NetworkResult
import pt.isel.pdm.httpConfig.RequestConfig
import pt.isel.pdm.httpConfig.listen
import pt.isel.pdm.httpConfig.request
import pt.isel.pdm.user.UserPreferences
import java.util.concurrent.TimeUnit

class RepositoryLobbiesHttp(
    private val userPreferences: UserPreferences,
    private val networkClient: NetworkClient,
) : RepositoryLobbies {
    private val baseUrl = "http://10.0.2.2:4000/api"

    private val scope = CoroutineScope(Dispatchers.IO)
    val currentClientId = userPreferences.userId.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val lobbySseListener: SharedFlow<LobbyResponse> = currentClientId
        .filterNotNull()
        .flatMapLatest { clientId ->
            logger("Starting SSE for client: $clientId")

            val config = RequestConfig<Unit>(
                url = "$baseUrl/lobbies/events",
                queryParams = mapOf("clientId" to clientId),
                numberOfTries = 3
            )
            networkClient.listen<LobbyEvent>(config, eventName = "LOBBY_EVENT")
                .mapNotNull { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            logger("SSE Received: ${result.data}")
                            result.data.toDomain()
                        }
                        is NetworkResult.NetworkError -> {
                            logger("SSE Network Error: ${result.exception.message}")
                            null
                        }
                        is NetworkResult.SerializationError -> {
                            logger("SSE Parsing Error: ${result.exception.message}")
                            null
                        }
                        else -> {
                            logger("SSE Unknown Error: $result")
                            null
                        }
                    }
                }
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            replay = 0
        )

    override suspend fun createNewLobby(lobby: LobbyCreation): OutCome<Lobby, LobbyError> {
        val config = RequestConfig(
            url = "$baseUrl/lobbies",
            method = MethodRequest.POST,
            body = lobby.toDto(),
            headers = getAuthHeader()
        )
        return when (val result = networkClient.request<LobbyIn, LobbyOut>(config)) {
            is NetworkResult.Success -> Success(result.data.toDomain())
            // falta mapear erros aquiii
            is NetworkResult.ApiError -> Failure(LobbyError.NetWorkError)
            else -> Failure(LobbyError.NetWorkError)
        }
    }

    override suspend fun joinLobby(lobby: Lobby): OutCome<Lobby, LobbyError> {
        val config = RequestConfig<Unit>(
            url = "$baseUrl/lobbies/join/${lobby.id.id}",
            method = MethodRequest.POST,
            headers = getAuthHeader()
        )
        return when (val result = networkClient.request<LobbyIn, Unit>(config)) {
            is NetworkResult.Success -> Success(result.data.toDomain())
            else -> Failure(LobbyError.NetWorkError)
        }
    }

    override suspend fun leaveLobby(lobby: Lobby): OutCome<Unit, LobbyError> {
        val config = RequestConfig<Unit>(
            url = "$baseUrl/lobbies/leave/${lobby.id.id}",
            method = MethodRequest.POST,
            headers = getAuthHeader()
        )
        val result = networkClient.request<Unit, Unit>(config)
        return when (result) {
            is NetworkResult.Success -> Success(Unit)
            else -> Failure(LobbyError.NetWorkError)
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

    private fun logger(str: String) {
        Log.v("HTTP_LOBBIES", str)
    }

}
