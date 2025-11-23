package pt.isel.pdm.match.innerComposable.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pt.isel.pdm.match.ui.GameScreen
import pt.isel.pdm.match.viewModels.myTurn.MyTurnUiState
import pt.isel.pdm.match.viewModels.myTurn.MyTurnViewModel

@Composable
fun MyTurnScreen(vm: MyTurnViewModel) {

    val uiState by vm.stateUi.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is MyTurnUiState.InitialLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
        is MyTurnUiState.ValidState -> {
            val players = state.round.players

            if (players.isNotEmpty()) {

                val me = players.first()
                val others = players.drop(1)
                GameScreen(
                    me = me,
                    others = others,
                    onRollFinished = {
                        vm.setHand()
                    }
                )
            }
        }
    }
}