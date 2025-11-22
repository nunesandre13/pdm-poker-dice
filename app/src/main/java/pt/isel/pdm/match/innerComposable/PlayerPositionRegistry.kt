package pt.isel.pdm.match.innerComposable

import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

@Immutable
sealed interface PlayerRegistry {
    fun getBounds(playerId: Int): Rect?

    operator fun get(playerId: Int): Rect? = getBounds(playerId)

    companion object {
        val Empty: PlayerRegistry = EmptyPlayerRegistry
    }
}

class PlayerRegistryBuilder {
    private val positions = mutableMapOf<Int, Rect>()

    fun register(playerId: Int, coordinates: LayoutCoordinates) {
        if (!coordinates.isAttached) return
        val position = coordinates.positionInRoot()
        val size = coordinates.size
        positions[playerId] = Rect(
            offset = position,
            size = Size(size.width.toFloat(), size.height.toFloat())
        )
    }

    fun build(): PlayerRegistry {
        return PlayerRegistryImpl(positions.toImmutableMap())
    }
}

fun Modifier.registerBounds(playerId: Int, registryBuilder: PlayerRegistryBuilder): Modifier =
    this.onGloballyPositioned { coordinates ->
        registryBuilder.register(playerId, coordinates)
    }

private data object EmptyPlayerRegistry : PlayerRegistry {
    override fun getBounds(playerId: Int): Rect? = null
}

private data class PlayerRegistryImpl(
    private val positions: ImmutableMap<Int, Rect>
) : PlayerRegistry {
    override fun getBounds(playerId: Int): Rect? = positions[playerId]
    override fun toString(): String = "PlayerRegistry(count=${positions.size})"
}