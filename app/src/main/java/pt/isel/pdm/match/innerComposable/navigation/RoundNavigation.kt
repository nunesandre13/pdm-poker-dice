package pt.isel.pdm.match.innerComposable.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import pt.isel.pdm.match.innerComposable.PlayerRegistry
import pt.isel.pdm.match.screens.RoundRoute
import pt.isel.pdm.match.viewModels.InnerRoute
import pt.isel.pdm.match.viewModels.MatchGlobalStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel

@Composable
fun RoundNavigation(matchViewModel: MatchViewModel, navController: NavHostController, playersPosition: PlayerRegistry){
    val uiState by matchViewModel.innerNavigation.collectAsStateWithLifecycle()
    LaunchedEffect(uiState) {
        val newRoute: RoundRoute = when (uiState) {
            is InnerRoute.MyTurnState -> RoundRoute.MyTurn
            is InnerRoute.OtherPlayerTurn -> RoundRoute.OtherPlayerTurn
            is InnerRoute.BettingState -> RoundRoute.Betting
            is InnerRoute.Idle -> RoundRoute.Idle
            is InnerRoute.Finished -> RoundRoute.Finished
        }
        navController.navigate(newRoute) {
            popUpTo(navController.graph.startDestinationId) { inclusive = true }
            launchSingleTop = true
        }
    }

    NavHost(navController = navController, startDestination = RoundRoute.Idle) {
        idle()
        otherPlayerTurn(matchViewModel,playersPosition)
        myTurn(matchViewModel,playersPosition)
        betting(matchViewModel,playersPosition)
        finished()
    }

}
