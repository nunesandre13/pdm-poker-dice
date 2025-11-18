package pt.isel.pdm.match.viewModels.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pt.isel.pdm.match.viewModels.MatchStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.match.viewModels.interfaces.RollingActions

@Composable
fun MatchScreen(
    matchViewModel: MatchViewModel,
    navController: NavHostController = rememberNavController()
) {
    val uiState by matchViewModel.stateUi.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        val newRoute: MatchRoute = when (uiState) {
            is MatchStateUi.MyTurnState -> MatchRoute.MyTurn
            is MatchStateUi.OtherPlayerTurn -> MatchRoute.OtherPlayerTurn
            is MatchStateUi.BettingState -> MatchRoute.Betting
            is MatchStateUi.Idle -> MatchRoute.Idle
        }

        navController.navigate(newRoute) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = MatchRoute.Idle
    ) {
        composable<MatchRoute.Idle> {

        }
        composable<MatchRoute.MyTurn> {

            val rollingActions: RollingActions = matchViewModel

        }
        composable<MatchRoute.OtherPlayerTurn> {

        }
        composable<MatchRoute.Betting> {
            val bettingActions: BettingActions = matchViewModel
        }
    }
}
