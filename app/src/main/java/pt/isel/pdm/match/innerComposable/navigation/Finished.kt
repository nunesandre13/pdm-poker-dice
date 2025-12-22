package pt.isel.pdm.match.innerComposable.navigation
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.match.innerComposable.screens.finished.FinishedScreen
import pt.isel.pdm.match.screens.RoundRoute
import pt.isel.pdm.match.viewModels.interfaces.RoundStateProvider

fun NavGraphBuilder.finished(roundStateProvider: RoundStateProvider) {
    composable<RoundRoute.Finished> {
        FinishedScreen(roundStateProvider)
    }
}

