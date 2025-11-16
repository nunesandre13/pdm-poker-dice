package pt.isel.pdm.navigation

sealed interface NavigationEvent {
    data class Navigate(val screen: Screens): NavigationEvent
    data object Back : NavigationEvent
    data object Title: NavigationEvent
    data object LogOut : NavigationEvent
}