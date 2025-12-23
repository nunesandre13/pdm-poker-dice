package pt.isel.pdm.match.repository

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json
import pt.isel.pdm.domain.RawMatch
import pt.isel.pdm.domain.PlayCommand
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.domain.toDto
import pt.isel.pdm.dto.match.MatchEvent
import pt.isel.pdm.dto.match.MatchIn
import pt.isel.pdm.dto.match.PlayCommandOut
import pt.isel.pdm.httpConfig.MethodRequest
import pt.isel.pdm.httpConfig.NetworkClient
import pt.isel.pdm.httpConfig.NetworkResult
import pt.isel.pdm.httpConfig.RequestConfig
import pt.isel.pdm.httpConfig.listen
import pt.isel.pdm.httpConfig.request
import pt.isel.pdm.user.UserPreferences
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import java.util.concurrent.TimeUnit

class RepositoryMatchHttp(
    private val networkClient: NetworkClient,
    private val userPreferences: UserPreferences
) : RepositoryMatch {
    private val baseUrl = "http://10.0.2.2:4000/api"
    private val scope = CoroutineScope(Dispatchers.IO)
    private val currentMatchId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val matchSseFlow: SharedFlow<OutCome<MatchResponse, MatchError>> = currentMatchId
        .filterNotNull()
        .flatMapLatest { matchId ->
            val userId = userPreferences.getUserId()
            if (userId == null) {
                logger("User ID not found, skipping SSE")
                return@flatMapLatest emptyFlow()
            }
            logger("Starting SSE for match: $matchId, player: $userId")
            val config = RequestConfig<Unit>(
                url = "$baseUrl/sse/match/$matchId/events",
                queryParams = mapOf("playerId" to userId),
                numberOfTries = 3
            )
            networkClient.listen<MatchEvent>(config, eventName = "MATCH_EVENT")
                .map { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            logger("SSE Received: ${result.data}")
                            Success(result.data.toDomain())
                        }
                        is NetworkResult.NetworkError -> {
                            logger("SSE Network Error: ${result.exception.message}")
                            Failure(MatchError.SomeError)
                        }
                        else -> {
                            logger("SSE Error: $result")
                            Failure(MatchError.SomeError)
                        }
                    }
                }
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 0
        )

    override fun matchSseListener(matchId: Int): SharedFlow<OutCome<MatchResponse, MatchError>> {
        currentMatchId.value = matchId
        return matchSseFlow
    }

    override suspend fun play(command: PlayCommand): OutCome<RawMatch, MatchError> {
        val matchId = currentMatchId.value ?: return Failure(MatchError.SomeError)
        val bodyDto: PlayCommandOut = command.toDto()
        val config = RequestConfig<PlayCommandOut>(
            url = "$baseUrl/match/$matchId/play",
            method = MethodRequest.POST,
            body = bodyDto,
            headers = getAuthHeader()
        )
        return when (val result = networkClient.request<MatchIn, PlayCommandOut>(config)) {
            is NetworkResult.Success -> Success(result.data.toDomain())
            is NetworkResult.ApiError -> {
                logger("Play API Error: ${result.code}")
                Failure(MatchError.SomeError)
            }
            else -> {
                logger("Play Network/Unknown Error")
                Failure(MatchError.SomeError)
            }
        }
    }

    override suspend fun leaveMatch(match: RawMatch): OutCome<RawMatch, MatchError> {
        val config = RequestConfig<Unit>(
            url = "$baseUrl/match/${match.id.id}/leave",
            method = MethodRequest.POST,
            headers = getAuthHeader()
        )

        return when (val result = networkClient.request<MatchIn, Unit>(config)) {
            is NetworkResult.Success -> {
                currentMatchId.value = null
                Success(result.data.toDomain())
            }
            else -> {
                logger("Leave Error")
                Failure(MatchError.SomeError)
            }
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
        Log.v("HTTP_MATCH", str)
    }
}
