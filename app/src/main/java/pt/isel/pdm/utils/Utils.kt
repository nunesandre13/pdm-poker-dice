package pt.isel.pdm.utils

suspend fun <T> runOperation(defaultValue: T, operation: suspend () -> T?) : T {
    return operation() ?: defaultValue
}

inline fun presentError(onError: () -> Nothing): Nothing {
    onError()
}

