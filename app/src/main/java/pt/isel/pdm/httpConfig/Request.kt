package pt.isel.pdm.httpConfig


enum class HttpMethod { GET, POST, PUT, DELETE }
data class RequestConfig<T>(
    val url: String,
    val method: HttpMethod = HttpMethod.GET,
    val numberOfTries: Int = 3,
    val queryParams: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap(),
    val body: T? = null
)