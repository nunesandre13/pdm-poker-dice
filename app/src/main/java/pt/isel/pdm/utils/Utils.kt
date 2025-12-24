package pt.isel.pdm.utils

import kotlinx.coroutines.flow.MutableStateFlow
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.match.PlayerMatchState
import pt.isel.pdm.domain.match.PlayerMatchStateWithName
import pt.isel.pdm.domain.state.PlayerRoundStateWithName
import pt.isel.pdm.domain.match.withName

suspend fun <T> runOperation(defaultValue: T, operation: suspend () -> T?) : T {
    return operation() ?: defaultValue
}

inline fun presentError(onError: () -> Nothing): Nothing {
    onError()
}


fun List<PlayerMatchState>.mapping(resolver: PlayerNameResolver): List<PlayerMatchStateWithName> = map { player ->
    player.withName(resolver[player.playerId])
}


fun <T> MutableStateFlow<T>.updateIf(expect: T, newValue: T): Boolean {
    if (this.value == expect) {
        this.value = newValue
        return true
    }
    return false
}

fun List<PlayerRoundStateWithName>.findMe(myId: PlayerId?): PlayerRoundStateWithName? =
    this.find { it.playerId == myId }