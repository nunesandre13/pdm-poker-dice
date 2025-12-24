package pt.isel.pdm.domain.match

import kotlinx.collections.immutable.ImmutableList

data class DicesHand(
    val dices: ImmutableList<DiceFace>,
)