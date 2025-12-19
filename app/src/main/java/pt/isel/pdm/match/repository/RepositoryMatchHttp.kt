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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.PlayCommand
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.domain.toDto
import pt.isel.pdm.dto.Match.MatchEvent
import pt.isel.pdm.dto.Match.MatchIn
import pt.isel.pdm.user.UserPreferences
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success
import java.util.concurrent.TimeUnit

class RepositoryMatchHttp(private val userPreferences: UserPreferences) : RepositoryMatch {
    private val baseUrl = "http://10.0.2.2:4000/api"

    private val client = HttpClient(OkHttp) {
        engine {
            config {
                pingInterval(30, TimeUnit.SECONDS)
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = Long.MAX_VALUE
            socketTimeoutMillis = Long.MAX_VALUE
            connectTimeoutMillis = 10_000
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(SSE)
    }

    private val scope = CoroutineScope(Dispatchers.IO)
    private val currentMatchId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val matchSseFlow: SharedFlow<OutCome<MatchResponse, MatchError>> = currentMatchId
        .filterNotNull()
        .flatMapLatest { matchId ->
            flow {
                logger("created SSE connection for match: $matchId")
                val clientId = userPreferences.getUserId() ?: return@flow
                client.sse("$baseUrl/sse/match/$matchId/events?playerId=$clientId") {
                    incoming.collect { event ->
                        logger("EVENT: ${event.event}")
                        logger("new event: ${event.data}")
                        if (event.event == "MATCH_EVENT") {
                            event.data?.let { data ->
                                try {
                                    val matchResponse = Json.decodeFromString<MatchEvent>(data)
                                    logger("decoded: $matchResponse")
                                    this@flow.emit(Success(matchResponse.toDomain()))
                                } catch (e: Exception) {
                                    logger("decode error: $e")
                                    emit(Failure(MatchError.SomeError))
                                }
                            }
                        }
                    }
                }
            }
        }
        .retry(2) { cause ->
            logger("SSE error: $cause")
            delay(2000)
            logger("trying to reconnect...")
            true
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

    override suspend fun play(command: PlayCommand): OutCome<Match, MatchError> {
        return try {
            val response = client.post("$baseUrl/match/${currentMatchId.value}/play") {
                contentType(ContentType.Application.Json)
                userPreferences.getToken()?.let { t ->
                    header("Authorization", "Bearer $t")
                } ?: return Failure(MatchError.SomeError)
                setBody(command.toDto())
            }
            val match = response.body<MatchIn>().toDomain()
            Success(match)
        } catch (e: Exception) {
            logger("play error: $e")
            Failure(MatchError.SomeError)
        }
    }

    override suspend fun leaveMatch(match: Match): OutCome<Match, MatchError> {
        return try {
            val response = client.post("$baseUrl/match/${match.id}/leave") {
                contentType(ContentType.Application.Json)
                userPreferences.getToken()?.let { t ->
                    header("Authorization", "Bearer $t")
                } ?: return Failure(MatchError.SomeError)
            }
            val updatedMatch = response.body<MatchIn>().toDomain()
            currentMatchId.value = null
            Success(updatedMatch)
        } catch (e: Exception) {
            logger("leaveMatch error: $e")
            Failure(MatchError.SomeError)
        }
    }

    private fun logger(str: String) {
        Log.v("HTTP_MATCH", str)
    }
}
