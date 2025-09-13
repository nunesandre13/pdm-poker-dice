package com.example.chelasmulti_playerpokerdice.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chelasmulti_playerpokerdice.Screens
import com.example.chelasmulti_playerpokerdice.screens.AboutScreen
import com.example.chelasmulti_playerpokerdice.screens.TitleScreen

@Composable
fun RootApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.START_SCREEN.route) {
        composable(Screens.START_SCREEN.route) {
            TitleScreen{
                navController.navigate(Screens.ABOUT.route)
            }
       }
        composable(Screens.ABOUT.route) {
            AboutScreen()
        }
    }
}
