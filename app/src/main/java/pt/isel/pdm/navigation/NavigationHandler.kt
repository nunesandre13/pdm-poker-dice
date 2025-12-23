package pt.isel.pdm.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.onNavigateEvent(event: NavigationEvent) {
    when (event) {
        is NavigationEvent.NavigatePopping -> {
            navigate(event.screen) {
                popUpTo(graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
        is NavigationEvent.Navigate -> navigate(event.screen)
        is NavigationEvent.Back -> popBackStack()
        is NavigationEvent.Title -> navigate(Screens.Title)
        is NavigationEvent.LogOut -> {
            navigate(Screens.Home) {
                popUpTo(graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
}
