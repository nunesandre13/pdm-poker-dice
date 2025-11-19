package pt.isel.pdm.match.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chelasmulti_playerpokerdice.R
import kotlinx.coroutines.flow.MutableStateFlow
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.match.repository.RepositoryMatchMock
import pt.isel.pdm.match.services.MatchServiceImp
import pt.isel.pdm.match.ui.table.PokerTableSurface
import pt.isel.pdm.match.viewModels.MatchStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.match.viewModels.interfaces.BettingActions
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.utils.ViewModelBase

@Composable
fun MatchScreen(
    matchViewModel: MatchViewModel,
    navController: NavHostController = rememberNavController()
) {
    var showMatchDetails by remember { mutableStateOf(false) }

    val pokerTableContent = remember(matchViewModel, navController) {
        movableContentOf {
            PokerTableBase(matchViewModel = matchViewModel, navController)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(brush = Brush.linearGradient(
        colors = listOf(
            colorResource(R.color.table_bg_light),
            colorResource(R.color.table_bg_dark))))) {
        if (showMatchDetails) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(0.2f).fillMaxHeight()
                    .background(Color.Cyan)) {

                }
                Box(modifier = Modifier.weight(0.8f).fillMaxHeight()) {
                    pokerTableContent()
                }
            }
        } else {
            pokerTableContent()
        }

        Button(
            onClick = { showMatchDetails = !showMatchDetails },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text(if (showMatchDetails) "Fechar detalhes" else "Mostrar detalhes")
        }
    }
}

@Composable
private fun PokerTableBase(matchViewModel: MatchViewModel,navController: NavHostController ){
    Box(modifier =
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        PokerTableSurface(
            modifier = Modifier
                .fillMaxWidth(0.78f)
                .aspectRatio(2f)
        )
        {
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


                }
                composable<MatchRoute.OtherPlayerTurn> {

                }
                composable<MatchRoute.Betting> {
                    val bettingActions: BettingActions = matchViewModel
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MatchScreenPreview() {
    val fakeViewModel = object : MatchViewModel(ViewModelBase(MatchStateUi.Idle, MatchError.SomeError),
        MatchServiceImp(RepositoryMatchMock()), UsersServiceMock(),1234){
        override val stateUi = MutableStateFlow<MatchStateUi>(MatchStateUi.Idle)
    }
    MatchScreen(matchViewModel = fakeViewModel, navController = rememberNavController())
}
