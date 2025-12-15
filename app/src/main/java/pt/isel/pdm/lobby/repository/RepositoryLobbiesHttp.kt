package pt.isel.pdm.lobby.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json
import pt.isel.pdm.domain.Lobby
import pt.isel.pdm.domain.events.LobbyResponse
import pt.isel.pdm.domain.state.LobbyError
import pt.isel.pdm.dto.Lobby.LobbyEvent
import pt.isel.pdm.dto.Lobby.toDomain
import pt.isel.pdm.utils.Failure
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.Success

class RepositoryLobbiesHttp : RepositoryLobbies {
    private val baseUrl = "https://localhost:4000/api"

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(SSE)
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override val lobbySseListener: SharedFlow<LobbyResponse> = flow {
        client.sse("$baseUrl/lobbies/events?clientId=?") {
            incoming.collect { event ->
                if (event.event == "LOBBY_EVENT") {
                    event.data?.let { data ->
                        try {
                            val lobbyResponse = Json.decodeFromString<LobbyEvent>(data)
                            emit(lobbyResponse.toDomain())
                        } catch (e: Exception) {

                        }
                    }
                }
            }
        }
    }.retry(2){ cause ->
            delay(2000)
            true
        }
        .flowOn(Dispatchers.IO)
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            replay = 0
        )

    override suspend fun createNewLobby(lobby: Lobby): OutCome<Lobby, LobbyError> {
        return try {
            val response = client.post("$baseUrl/lobbies") {
                contentType(ContentType.Application.Json)
                setBody(lobby)
            }
            val createdLobby = response.body<Lobby>()
            Success(createdLobby)
        } catch (e: Exception) {
            Failure(LobbyError.NetWorkError)
        }
    }

    override suspend fun joinLobby(lobby: Lobby): OutCome<Lobby, LobbyError> {
        return try {
            val response = client.post("$baseUrl/lobbies/join/${lobby.id}") {
                contentType(ContentType.Application.Json)
            }
            val joinedLobby = response.body<Lobby>()
            Success(joinedLobby)
        } catch (e: Exception) {
            Failure(LobbyError.NetWorkError)
        }
    }

    override suspend fun leaveLobby(lobby: Lobby): OutCome<Unit, LobbyError> {
        return try {
            client.post("$baseUrl/lobbies/leave/${lobby.id}") {
                contentType(ContentType.Application.Json)
            }
            Success(Unit)
        } catch (e: Exception) {
            Failure(LobbyError.NetWorkError)
        }
    }
}
