package pt.isel.pdm.match.viewModels.interfaces

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.PlayerInfo
import pt.isel.pdm.domain.RawRound
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.state.Round

interface RoundStateProvider {
    val roundState: StateFlow<Round?>
    val player: StateFlow<PlayerInfo?>
}