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