package pt.isel.pdm.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.PlayerInfo

class PlayersNameCache {
    private val _playersCache = MutableStateFlow<Map<PlayerId, Name>>(emptyMap())

    val playersCache: StateFlow<Map<PlayerId, Name>> = _playersCache.asStateFlow()

    fun cachePlayers(players: List<PlayerInfo>) {
        _playersCache.update { currentMap ->
            currentMap + players.associate { it.id to it.name }
        }
    }

    fun clear() {
        _playersCache.value = emptyMap()
    }

    fun getName(id: PlayerId): Name? = _playersCache.value[id]
}

@JvmInline
value class PlayerNameResolver(private val map: Map<PlayerId, Name>) {

    operator fun get(id: PlayerId): Name {
        return map[id] ?: Name("Player: ${id.id}")
    }

    fun contains(id: PlayerId) = map.containsKey(id)
}