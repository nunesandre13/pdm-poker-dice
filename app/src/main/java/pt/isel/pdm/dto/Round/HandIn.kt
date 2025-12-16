package pt.isel.pdm.dto.Round

import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.DicesHand

@Serializable
data class HandIn(
    val dices: List<String>,
) {
    fun toDomain(): DicesHand = DicesHand(dices.map { DiceFace.valueOf(it) }.toImmutableList())
}