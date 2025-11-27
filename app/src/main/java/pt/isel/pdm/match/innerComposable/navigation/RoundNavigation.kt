package pt.isel.pdm.match.innerComposable.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.screens.MatchRoute
import pt.isel.pdm.match.viewModels.MatchStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel

@Composable
fun RoundNavigation(matchViewModel: MatchViewModel, navController: NavHostController, playersPosition: PlayerRegistry){
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

    NavHost(navController = navController, startDestination = MatchRoute.Idle) {
        idle()
        otherPlayerTurn(matchViewModel,playersPosition)
        myTurn(matchViewModel,playersPosition)
        betting(matchViewModel,playersPosition)
    }

}
