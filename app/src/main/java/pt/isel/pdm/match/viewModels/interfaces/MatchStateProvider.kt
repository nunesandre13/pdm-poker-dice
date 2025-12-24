package pt.isel.pdm.match.viewModels.interfaces

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.user.PlayerInfo
import pt.isel.pdm.match.viewModels.MatchState

interface MatchStateProvider {
    val matchState: StateFlow<MatchState>
    val player: StateFlow<PlayerInfo?>
}