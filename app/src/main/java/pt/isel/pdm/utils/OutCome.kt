package pt.isel.pdm.utils

sealed interface OutCome<out S, out F>

data class Success<out S>(val value: S) : OutCome<S, Nothing>

data class Failure<out F>(val value: F) : OutCome<Nothing, F>

inline fun <S, F, R> OutCome<S, F>.onOutCome(
    onSuccess: (S) -> R,
    onFailure: (F) -> R
): R = when (this) {
    is Success -> onSuccess(value)
    is Failure -> onFailure(value)
}


fun <S> OutCome<S, *>.getOrNull(): S? =
    if (this is Success) value else null

fun <F> OutCome<*, F>.errorOrNull(): F? =
    if (this is Failure) value else null