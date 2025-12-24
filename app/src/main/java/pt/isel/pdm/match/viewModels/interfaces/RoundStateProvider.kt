package pt.isel.pdm.match.viewModels.interfaces

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.user.PlayerInfo
import pt.isel.pdm.domain.state.Round

interface RoundStateProvider {
    val roundState: StateFlow<Round?>
    val player: StateFlow<PlayerInfo?>
}