package pt.isel.pdm.navigation.screens



import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens

fun NavGraphBuilder.title(
    onNavigation: (NavigationEvent) -> Unit
) {
    composable<Screens.Title> {
        TitleScreen(
            onAboutClick = { onNavigation(NavigationEvent.Navigate(Screens.About)) },
            onProfileClick = { onNavigation(NavigationEvent.Navigate(Screens.Profile)) },
            onStartMatchClick = { onNavigation(NavigationEvent.Navigate(Screens.StartMatch)) }
        )
    }
}
