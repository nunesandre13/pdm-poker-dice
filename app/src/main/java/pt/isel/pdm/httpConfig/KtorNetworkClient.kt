package pt.isel.pdm.httpConfig

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.IOException
import kotlinx.serialization.SerializationException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

class KtorNetworkClient(val delay: Duration) : NetworkClient {
    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }

    private val client = HttpClient(OkHttp) {
        engine {
            config {
                pingInterval(3, TimeUnit.SECONDS)
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

    override suspend fun <T, V> execute(
        config: RequestConfig<V>,
        responseSerializer: KSerializer<T>
    ): NetworkResult<T> {
        repeat(config.numberOfTries) { attempt ->
            try {
                val response = when (config.method) {
                    HttpMethod.GET -> client.get(config.url){ buildRequest(config) }
                    HttpMethod.POST -> client.post(config.url){ buildRequest(config) }
                    HttpMethod.PUT -> client.put(config.url){ buildRequest(config) }
                    HttpMethod.DELETE -> client.delete(config.url){ buildRequest(config) }
                }
                if (response.status.isSuccess()) {
                    val bodyText = response.bodyAsText()
                    val data = jsonConfig.decodeFromString(responseSerializer, bodyText)
                    return NetworkResult.Success(data)
                } else {
                    return NetworkResult.ApiError(response.status.value, response.status.description)
                }

            } catch (e: SerializationException) {
                return NetworkResult.SerializationError(e)
            } catch (e: IOException) {
                if (attempt < config.numberOfTries - 1) {
                    delay(delay)
                } else {
                    return NetworkResult.NetworkError(e)
                }
            } catch (e: Exception) {
                return NetworkResult.UnknownError(e)
            }
        }
        return NetworkResult.UnknownError(Exception("Should not reach here"))
    }


    override fun <T> listen(
        config: RequestConfig<Unit>,
        eventName: String?,
        responseSerializer: KSerializer<T>
    ): Flow<NetworkResult<T>> {
        return flow {
            client.sse(
                urlString = config.url,
                request = {
                    url {
                        config.queryParams.forEach { (key, value) ->
                            parameters.append(key, value)
                        }
                    }
                    config.headers.forEach { (key, value) ->
                        header(key, value)
                    }
                }
            ) {
                incoming.collect { event ->
                    val data = event.data
                    if (!data.isNullOrBlank() && (eventName == null || event.event == eventName)) {
                        try {
                            val parsedData = jsonConfig.decodeFromString(responseSerializer, data)
                            emit(NetworkResult.Success(parsedData))
                        } catch (e: Exception) {
                            if (e is IOException) throw e
                            if (e is CancellationException) throw e
                            if (e is SerializationException) {
                                emit(NetworkResult.SerializationError(e))
                            } else {
                                emit(NetworkResult.UnknownError(e))
                            }
                        }
                    }
                }
            }
        }.retry(retries = config.numberOfTries.toLong()) { cause ->
            if (cause is IOException) {
                delay(delay)
                return@retry true
            }
            return@retry false
        }.catch { cause ->
            when (cause) {
                is IOException -> emit(NetworkResult.NetworkError(cause))
                else -> emit(NetworkResult.UnknownError(cause))
            }
        }
    }

    private fun <V> HttpRequestBuilder.buildRequest(config: RequestConfig<V>) {
        contentType(ContentType.Application.Json)
        url {
            config.queryParams.forEach { (key, value) ->
                parameters.append(key, value)
            }
        }
        config.headers.forEach { (key, value) ->
            header(key, value)
        }
        if (config.body != null) {
            setBody(config.body)
        }
    }
}

