package pt.isel.pdm.navigation

import kotlinx.serialization.Serializable
import pt.isel.pdm.orientation.OrientationType
import pt.isel.pdm.orientation.ScreenOrientation

@Serializable
sealed interface Screens {
    @Serializable
    @ScreenOrientation(OrientationType.PORTRAIT)
    data object Home : Screens
    @Serializable
    @ScreenOrientation(OrientationType.PORTRAIT)
    data object Title : Screens

    @Serializable
    @ScreenOrientation(OrientationType.PORTRAIT)
    data object About : Screens

    @Serializable
    @ScreenOrientation(OrientationType.PORTRAIT)
    data object Profile : Screens

    @Serializable
    @ScreenOrientation(OrientationType.PORTRAIT)
    data object Lobby : Screens
    @Serializable
    @ScreenOrientation(OrientationType.LANDSCAPE)
    data class Match(val matchId: Int): Screens
}
