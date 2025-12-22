package pt.isel.pdm.utils

import pt.isel.pdm.domain.PlayerMatchState
import pt.isel.pdm.domain.PlayerMatchStateWithName
import pt.isel.pdm.domain.withName

suspend fun <T> runOperation(defaultValue: T, operation: suspend () -> T?) : T {
    return operation() ?: defaultValue
}

inline fun presentError(onError: () -> Nothing): Nothing {
    onError()
}


fun List<PlayerMatchState>.mapping(resolver: PlayerNameResolver): List<PlayerMatchStateWithName> = map { player ->
    player.withName(resolver[player.playerId])
}
