package pt.isel.pdm.httpConfig

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.sse.ServerSentEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.ClassDiscriminatorMode
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

class KtorNetworkClient(val delay: Duration) : NetworkClient {
    @OptIn(ExperimentalSerializationApi::class)
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        classDiscriminatorMode = ClassDiscriminatorMode.ALL_JSON_OBJECTS
    }
    private val client = HttpClient(OkHttp) {
        engine {
            config {
                pingInterval(30, TimeUnit.SECONDS)
            }
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = Long.MAX_VALUE
            requestTimeoutMillis = Long.MAX_VALUE
        }
        install(ContentNegotiation) {
            json(jsonConfig)
        }
        install(SSE)
    }

    private fun <T> KSerializer<T>.isUnit(): Boolean =
        this.descriptor == Unit.serializer().descriptor

    override suspend fun <T, V> execute(
        config: RequestConfig<V>,
        responseSerializer: KSerializer<T>
    ): NetworkResult<T> {
        repeat(config.numberOfTries) { attempt ->
            try {
                val response = performHttpRequest(config)
                return processResponse(response, responseSerializer)
            } catch (e: Exception) {
                val errorResult = handleException(e)
                if (shouldRetry(e, attempt, config.numberOfTries)) {
                    delay(delay)
                } else {
                    return errorResult
                }
            }
        }
        return NetworkResult.UnknownError(Exception("Retry limit reached"))
    }

    override fun <T> listen(
        config: RequestConfig<Unit>,
        eventName: String?,
        responseSerializer: KSerializer<T>
    ): Flow<NetworkResult<T>> {
        return flow {
            client.sse(
                urlString = config.url,
                request = { setupBuilder(config) }
            ) {
                incoming.collect { event ->
                    processSseEvent(event, eventName, responseSerializer)?.let { emit(it) }
                }
            }
        }.retry(retries = config.numberOfTries.toLong()) { cause ->
            if (cause is IOException) {
                delay(delay)
                return@retry true
            }
            return@retry false
        }.catch { cause ->
            emit(mapToNetworkError(cause))
        }
    }

    private suspend fun <V> performHttpRequest(config: RequestConfig<V>): HttpResponse {
        return when (config.method) {
            MethodRequest.GET -> client.get(config.url) { setupBuilder(config) }
            MethodRequest.POST -> client.post(config.url) { setupBuilder(config) }
            MethodRequest.PUT -> client.put(config.url) { setupBuilder(config) }
            MethodRequest.DELETE -> client.delete(config.url) { setupBuilder(config) }
        }
    }

    private fun <V> HttpRequestBuilder.setupBuilder(config: RequestConfig<V>) {
        url {
            config.queryParams.forEach { (key, value) -> parameters.append(key, value) }
        }
        config.headers.forEach { (key, value) -> header(key, value) }

        if (config.body != null) {
            contentType(ContentType.Application.Json)
            setBody(config.body)
        }
    }

    private suspend fun <T> processResponse(
        response: HttpResponse,
        serializer: KSerializer<T>
    ): NetworkResult<T> {
        return if (response.status.isSuccess()) {
            try {
                val bodyText = response.bodyAsText()
                if (bodyText.isBlank() && serializer.isUnit()) {
                    @Suppress("UNCHECKED_CAST")
                    return NetworkResult.Success(Unit as T)
                }
                val data = jsonConfig.decodeFromString(serializer, bodyText)
                NetworkResult.Success(data)
            } catch (e: SerializationException) {
                NetworkResult.SerializationError(e)
            }
        } else {
            NetworkResult.ApiError(response.status.value, response.status.description)
        }
    }

    private fun <T> processSseEvent(
        event: ServerSentEvent,
        targetEventName: String?,
        serializer: KSerializer<T>
    ): NetworkResult<T>? {
        val data = event.data
        if (!data.isNullOrBlank() && (targetEventName == null || event.event == targetEventName)) {
            return try {
                val parsedData = jsonConfig.decodeFromString(serializer, data)
                NetworkResult.Success(parsedData)
            } catch (e: Exception) {
                if (e is IOException || e is CancellationException) throw e
                mapToNetworkError(e)
            }
        }
        return null
    }

    private fun shouldRetry(e: Exception, currentAttempt: Int, maxTries: Int): Boolean {
        return (e is IOException) && (currentAttempt < maxTries - 1)
    }

    private fun handleException(e: Exception): NetworkResult<Nothing> {
        return when (e) {
            is SerializationException -> NetworkResult.SerializationError(e)
            is IOException -> NetworkResult.NetworkError(e)
            is CancellationException -> throw e
            else -> NetworkResult.UnknownError(e)
        }
    }

    private fun mapToNetworkError(t: Throwable): NetworkResult<Nothing> {
        return when (t) {
            is SerializationException -> NetworkResult.SerializationError(t)
            is IOException -> NetworkResult.NetworkError(t)
            else -> NetworkResult.UnknownError(t)
        }
    }
}

