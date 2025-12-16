package pt.isel.pdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.navigation.screens.about
import pt.isel.pdm.navigation.screens.home
import pt.isel.pdm.navigation.screens.match
import pt.isel.pdm.navigation.screens.profile
import pt.isel.pdm.navigation.screens.lobby
import pt.isel.pdm.navigation.screens.title

@Composable
fun RootApp(appConfiguration: DependenciesContainer) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Home
    ) {
        home(appConfiguration, navController::onNavigateEvent)
        title(navController::onNavigateEvent)
        about(navController::onNavigateEvent)
        lobby(appConfiguration, navController::onNavigateEvent)
        profile(appConfiguration, navController::onNavigateEvent)
        match(appConfiguration,navController::onNavigateEvent)
    }
}