import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import pt.isel.pdm.match.innerComposable.PlayerRegistryBuilder
import pt.isel.pdm.match.innerComposable.registerBounds
import pt.isel.pdm.match.ui.playerLayouts.MakeLayout
import pt.isel.pdm.match.ui.playerView.BasePlayerView
import pt.isel.pdm.match.ui.table.PokerTableSurface
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.MatchViewModel

@Composable
fun RoundScreen(matchViewModel: MatchViewModel, navController: NavHostController) {

    val matchSetUp by produceState<Pair<Int, List<Int>>?>(initialValue = null, matchViewModel) {
       combine(
            matchViewModel.player,
            matchViewModel.matchState
        ) { player, matchState ->
            val myId = player?.id?.toInt()
            val actualMatch = (matchState as? MatchState.ActualMatch)?.match
            if (myId != null && actualMatch != null) {
                val otherIds = actualMatch.players.map { it.playerId }.filter { it != myId }
                Pair(myId, otherIds)
            } else {
                null
            }
        }
            .filterNotNull()
            .take(1)
            .collect { validSetup ->
                value = validSetup
            }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val playerPositionRegistry = remember { PlayerRegistryBuilder() }

        val actualMatchSetUp = matchSetUp
        if (actualMatchSetUp != null) {
            val (myId, otherIds) = actualMatchSetUp

            PokerTableSurface(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .aspectRatio(2f)
            ) {
                MakeLayout(
                    me = myId,
                    others = otherIds,
                    myPlayerComposable = { player, modifier ->
                        BasePlayerView(
                            modifier
                                .fillMaxSize()
                                .registerBounds(player, playerPositionRegistry)
                        )
                    },
                    otherPlayersComposable = { player, modifier ->
                        BasePlayerView(
                            modifier
                                .fillMaxSize()
                                .registerBounds(player, playerPositionRegistry)
                        )
                    }
                )
            }


            // RoundNavigation(...)
        } else {
            // Opcional: Colocar um Loading Spinner aqui enquanto espera pelos dados
        }
    }
}