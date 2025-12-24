package pt.isel.pdm.dto.round

import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.match.DiceFace
import pt.isel.pdm.domain.match.DicesHand

@Serializable
data class HandIn(
    val dices: List<String>,
) {
    fun toDomain(): DicesHand = DicesHand(dices.map { DiceFace.valueOf(it) }.toImmutableList())
}