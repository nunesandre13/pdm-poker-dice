package pt.isel.pdm.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screens {
    @Serializable
    data object Home : Screens

    @Serializable
    data object Title : Screens

    @Serializable
    data object About : Screens

    @Serializable
    data object Profile : Screens

    @Serializable
    data object StartMatch : Screens

    @Serializable
    data object CreateLobby : Screens

    @Serializable
    data class Match(val matchId: Int): Screens
}
