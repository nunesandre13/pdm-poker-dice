package pt.isel.pdm.match.screens

import kotlinx.serialization.Serializable

@Serializable
sealed interface RoundRoute {
    @Serializable
    data object Idle : RoundRoute

    @Serializable
    data object MyTurn : RoundRoute

    @Serializable
    data object OtherPlayerTurn : RoundRoute

    @Serializable
    data object Betting : RoundRoute

    @Serializable
    data object Finished: RoundRoute
}
