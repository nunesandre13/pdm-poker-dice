package pt.isel.pdm.domain

import kotlinx.collections.immutable.ImmutableList

data class DicesHand(
    val dices: ImmutableList<DiceFace>,
)