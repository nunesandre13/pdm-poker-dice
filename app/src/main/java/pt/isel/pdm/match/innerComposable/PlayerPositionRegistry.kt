package pt.isel.pdm.match.innerComposable

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize


@Immutable
sealed interface PlayerRegistry {
    fun getBounds(playerId: Int): Rect?

    operator fun get(playerId: Int): Rect? = getBounds(playerId)

    companion object {
        val Empty: PlayerRegistry = EmptyPlayerRegistry
    }
}


class PlayerRegistryManager {
    private val _positions = mutableStateMapOf<Int, Rect>()
    val isEmpty get() = _positions.isEmpty()

    val size get() = _positions.size

    fun build(): PlayerRegistry = PlayerRegistryImpl(_positions)
    fun register(playerId: Int, coordinates: LayoutCoordinates, parentCoordinates: LayoutCoordinates?) {
        if (!coordinates.isAttached || parentCoordinates == null || !parentCoordinates.isAttached) return

        val relativePosition = parentCoordinates.localPositionOf(coordinates)
        val newRect = Rect(
            offset = relativePosition,
            size = coordinates.size.toSize()
        )
        if (_positions[playerId] != newRect) {
            _positions[playerId] = newRect
        }
    }
}

fun Modifier.registerBounds(playerId: Int, registryManager: PlayerRegistryManager, parentCoordinates: LayoutCoordinates?): Modifier = this.onGloballyPositioned { coordinates ->
    registryManager.register(playerId, coordinates, parentCoordinates)
}

private data object EmptyPlayerRegistry : PlayerRegistry {
    override fun getBounds(playerId: Int): Rect? = null
}

private data class PlayerRegistryImpl(
    private val positions: Map<Int, Rect>
) : PlayerRegistry {
    override fun getBounds(playerId: Int): Rect? = positions[playerId]
    override fun toString(): String = "PlayerRegistry(count=${positions.size})"
}
