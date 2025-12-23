package pt.isel.pdm.httpConfig


enum class MethodRequest { GET, POST, PUT, DELETE }
data class RequestConfig<T>(
    val url: String,
    val method: MethodRequest = MethodRequest.GET,
    val numberOfTries: Int = 3,
    val queryParams: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap(),
    val body: T? = null
)

class RequestConfigBuilder<T>(private val url: String) {
    var method: MethodRequest = MethodRequest.GET
    var numberOfTries: Int = 3
    var body: T? = null
    private val headers = mutableMapOf<String, String>()
    private val queryParams = mutableMapOf<String, String>()

    fun header(key: String, value: String) {
        headers[key] = value
    }

    fun parameter(key: String, value: String) {
        queryParams[key] = value
    }
    fun build(): RequestConfig<T> = RequestConfig(
        url = url,
        method = method,
        numberOfTries = numberOfTries,
        headers = headers,
        queryParams = queryParams,
        body = body
    )
}

suspend fun <T> request(url: String, block: suspend RequestConfigBuilder<T>.() -> Unit = {}): RequestConfig<T> {
    val builder = RequestConfigBuilder<T>(url)
    builder.block()
    return builder.build()
}