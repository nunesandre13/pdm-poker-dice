package pt.isel.pdm.match.viewModels.screens

import kotlinx.serialization.Serializable

@Serializable
sealed interface MatchRoute {
    @Serializable
    data object Idle : MatchRoute

    @Serializable
    data object MyTurn : MatchRoute

    @Serializable
    data object OtherPlayerTurn : MatchRoute

    @Serializable
    data object Betting : MatchRoute
}
