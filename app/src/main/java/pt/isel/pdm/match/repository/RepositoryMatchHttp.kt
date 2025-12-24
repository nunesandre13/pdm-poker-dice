package pt.isel.pdm.match.repository

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import pt.isel.pdm.domain.match.RawMatch
import pt.isel.pdm.domain.match.PlayCommand
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.domain.lobby.toDto
import pt.isel.pdm.domain.match.toDto
import pt.isel.pdm.dto.match.MatchEvent
import pt.isel.pdm.dto.match.MatchIn
import pt.isel.pdm.dto.match.PlayCommandOut
import pt.isel.pdm.httpConfig.MethodRequest
import pt.isel.pdm.httpConfig.NetworkClient
import pt.isel.pdm.httpConfig.NetworkResult
import pt.isel.pdm.httpConfig.RequestConfigBuilder
import pt.isel.pdm.httpConfig.listen
import pt.isel.pdm.httpConfig.request
import pt.isel.pdm.user.UserPreferences
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success

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
            val config = request<Unit>("$baseUrl/sse/match/$matchId/events") {
                parameter("playerId", userId)
                numberOfTries = 3
            }
            networkClient.listen<MatchEvent>(config, eventName = "MATCH_EVENT")
                .map { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            logger("SSE Received: ${result.data}")
                            Success(result.data.toDomain())
                        }
                        is NetworkResult.ApiError -> {
                            logger("SSE API Error: ${result.code}")
                            Failure(MatchError.ApiError(result.message))
                        }
                        is NetworkResult.NetworkError -> {
                            logger("SSE Network Error: ${result.exception.message}")
                            Failure(MatchError.NetworkError)
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
        val config = request<PlayCommandOut>("$baseUrl/match/$matchId/play") {
            method = MethodRequest.POST
            body = bodyDto
            authenticated()
        }
        return when (val result = networkClient.request<MatchIn, PlayCommandOut>(config)) {
            is NetworkResult.Success -> Success(result.data.toDomain())
            is NetworkResult.ApiError -> {
                logger("Play API Error: ${result.code}")
                Failure(MatchError.ApiError(result.message))
            }
            is NetworkResult.NetworkError -> {
                logger("Play Network Error: ${result.exception.message}")
                Failure(MatchError.NetworkError)
            }
            else -> {
                logger("Play Network/Unknown Error")
                Failure(MatchError.SomeError)
            }
        }
    }

    override suspend fun leaveMatch(match: RawMatch): OutCome<RawMatch, MatchError> {
        val config = request<Unit>("$baseUrl/match/${match.id.id}/leave") {
            method = MethodRequest.POST
            authenticated()
        }
        return when (val result = networkClient.request<MatchIn, Unit>(config)) {
            is NetworkResult.Success -> {
                currentMatchId.value = null
                Success(result.data.toDomain())
            }
            is NetworkResult.ApiError -> {
                logger("Leave API Error: ${result.code}")
                Failure(MatchError.ApiError(result.message))
            }
            is NetworkResult.NetworkError -> {
                logger("Leave Network Error: ${result.exception.message}")
                Failure(MatchError.NetworkError)
            }
            else -> {
                logger("Leave Error")
                Failure(MatchError.SomeError)
            }
        }
    }

    private suspend fun <T> RequestConfigBuilder<T>.authenticated() {
        val token = userPreferences.getToken()
        if (token != null) header("Authorization", "Bearer $token")
    }

    private fun logger(str: String) {
        Log.v("HTTP_MATCH", str)
    }
}
