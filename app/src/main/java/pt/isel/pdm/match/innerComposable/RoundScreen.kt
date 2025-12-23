package pt.isel.pdm.match.innerComposable

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import pt.isel.pdm.domain.DiceFace
import pt.isel.pdm.domain.MatchId
import pt.isel.pdm.domain.PlayerId
import pt.isel.pdm.domain.RawMatch
import pt.isel.pdm.domain.RoundId
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.state.MatchError
import pt.isel.pdm.match.innerComposable.navigation.RoundNavigation
import pt.isel.pdm.match.services.MatchServices
import pt.isel.pdm.match.ui.playerLayouts.MakeLayout
import pt.isel.pdm.match.ui.playerView.BasePlayerView
import pt.isel.pdm.match.ui.table.PokerTableSurface
import pt.isel.pdm.match.viewModels.MatchGlobalStateUi
import pt.isel.pdm.match.viewModels.MatchState
import pt.isel.pdm.match.viewModels.MatchViewModel
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.utils.OutCome
import pt.isel.pdm.utils.PlayersNameCache
import pt.isel.pdm.utils.Success
import pt.isel.pdm.utils.ViewModelBase

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RoundScreenPreview() {

    val matchServiceMock = object : MatchServices {
        override fun getMatchUpdate(matchId: MatchId) =
            emptyFlow<OutCome<MatchResponse, MatchError>>()

        override suspend fun rollDice(playerId: PlayerId, roundId: RoundId, dices: List<DiceFace>) =
            Success(Unit)

        override suspend fun setHand(playerId: PlayerId, roundId: RoundId) =
            Success(Unit)

        override suspend fun raiseAnte(playerId: PlayerId, roundId: RoundId, ante: Int) =
            Success(Unit)

        override suspend fun passTurn(playerId: PlayerId, roundId: RoundId) =
            Success(Unit)

        override suspend fun call(playerId: PlayerId, roundId: RoundId) =
            Success(Unit)

        override suspend fun fold(playerId: PlayerId, roundId: RoundId) =
            Success(Unit)

        override suspend fun leaveMatch(match: RawMatch): OutCome<Unit, MatchError> =
            Success(Unit)

        override val matchIdState: StateFlow<Int?>
            get() = kotlinx.coroutines.flow.MutableStateFlow(null)
    }

    val userServiceMock = UsersServiceMock()
    val nameCacheMock = PlayersNameCache()
    val viewModel = MatchViewModel(
        viewModelBase = ViewModelBase(
            MatchGlobalStateUi.Elapsed,
            MatchError.SomeError
        ),
        matchServices = matchServiceMock,
        playersNameCache = nameCacheMock,
        userRepository = userServiceMock,
        matchId = 123
    )


    MaterialTheme {
        RoundScreen(matchViewModel = viewModel)
    }
}