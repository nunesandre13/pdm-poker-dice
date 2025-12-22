package pt.isel.pdm.httpConfig

import kotlinx.coroutines.flow.Flow
import kotlinx.io.IOException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer


interface NetworkClient {
    suspend fun <T, V> execute(
        config: RequestConfig<V>,
        responseSerializer: KSerializer<T>
    ): NetworkResult<T>

    fun <T> listen(
        config: RequestConfig<Unit>,
        eventName: String?,
        responseSerializer: KSerializer<T>
    ): Flow<NetworkResult<T>>
}

suspend inline fun <reified T, V> NetworkClient.request(
    config: RequestConfig<V>
): NetworkResult<T> {
    return execute(config, serializer<T>())
}

inline fun <reified T> NetworkClient.listen(
    config: RequestConfig<Unit>,
    eventName: String?
): Flow<NetworkResult<T>> {
    return listen(config, eventName, serializer<T>())
}

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class NetworkError(val exception: IOException) : NetworkResult<Nothing>()
    data class ApiError(val code: Int, val message: String? = null) : NetworkResult<Nothing>()
    data class SerializationError(val exception: Throwable) : NetworkResult<Nothing>()
    data class UnknownError(val exception: Throwable) : NetworkResult<Nothing>()
}
