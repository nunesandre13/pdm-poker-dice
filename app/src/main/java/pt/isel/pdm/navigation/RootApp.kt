package pt.isel.pdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.Screens
import pt.isel.pdm.screens.AboutScreen
import pt.isel.pdm.screens.TitleScreen

@Composable
fun RootApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.HOME_SCREEN.route) {
        composable(Screens.HOME_SCREEN.route) {
            TitleScreen{
                navController.navigate(Screens.ABOUT.route)
            }
       }
        composable(Screens.ABOUT.route) {
            AboutScreen()
        }
    }
}
