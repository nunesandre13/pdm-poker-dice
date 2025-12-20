package pt.isel.pdm.match.innerComposable

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import pt.isel.pdm.match.innerComposable.navigation.RoundNavigation
import pt.isel.pdm.match.ui.playerLayouts.MakeLayout
import pt.isel.pdm.match.ui.playerView.BasePlayerView
import pt.isel.pdm.match.ui.table.PokerTableSurface
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.MatchViewModel

@Composable
fun RoundScreen(matchViewModel: MatchViewModel) {
    val tableSetup by matchViewModel.tableSetup.collectAsState()
    val roundNavController = rememberNavController()
    val registryManager = remember { PlayerRegistryManager() }
    var tableCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    Box(
        modifier = Modifier.fillMaxSize()
            .onGloballyPositioned { tableCoordinates = it },
        contentAlignment = Alignment.Center,
    ) {
        val setup = tableSetup
        if (setup != null){
            PokerTableSurface(modifier = Modifier.fillMaxWidth(0.78f).aspectRatio(2f)) {
                MakeLayout(
                    me = setup.myId,
                    others = setup.opponentsIds,
                    myPlayerComposable = { player, modifier ->
                        BasePlayerView(
                            modifier.registerBounds(player, registryManager, tableCoordinates)
                        )
                    },
                    otherPlayersComposable = { player, modifier ->
                        BasePlayerView(
                            modifier.registerBounds(player, registryManager, tableCoordinates)
                        )
                    }
                )
            }
            logger("something")
            RoundNavigation(
                matchViewModel = matchViewModel,
                navController = roundNavController,
                playersPosition = registryManager.build()
            )
        } else {
            Unit // depois colocar algo
        }
    }
}
fun logger(string: String){
    Log.v("some logger of my app", string)
}