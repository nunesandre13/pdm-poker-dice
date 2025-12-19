package pt.isel.pdm.match.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.domain.MatchStatus
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.match.innerComposable.RoundScreen
import pt.isel.pdm.match.repository.RepositoryMatchMock
import pt.isel.pdm.match.services.MatchServiceImp
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.MatchStateUi
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.utils.ViewModelBase

@Composable
fun MatchScreen(
    matchViewModel: MatchViewModel,
    onMatchEnded: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    var showMatchDetails by remember { mutableStateOf(false) }

    val pokerTableContent = remember(matchViewModel, navController) {
        movableContentOf {
            RoundScreen(matchViewModel = matchViewModel)
        }
    }

    // so para teste
    LaunchedEffect(Unit) {
        matchViewModel.matchState.collect { state ->
            if (state is MatchState.ActualMatch && state.match.matchStatus == MatchStatus.FINISHED) {
                onMatchEnded()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(R.color.table_bg_light),
                        colorResource(R.color.table_bg_dark)
                    )
                )
            )
    ) {
        if (showMatchDetails) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.weight(0.2f)
                        .fillMaxHeight()
                        .background(Color.Cyan)
                )
                Box(
                    modifier = Modifier.weight(0.8f)
                        .fillMaxHeight()) {
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
                .padding(1.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text(
                text = if (showMatchDetails) "Close Details" else "Open Details",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Other Player's Turn Test")
@Composable
fun MatchScreenPreviewOtherTurn() {
    val fakeViewModel = MatchViewModel(
        ViewModelBase(MatchStateUi.OtherPlayerTurn, MatchError.SomeError),
        MatchServiceImp(RepositoryMatchMock()),
        UsersServiceMock(),
        1234
    )
    MatchScreen(matchViewModel = fakeViewModel, {},navController = rememberNavController())
}

