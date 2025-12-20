package pt.isel.pdm.match.viewModels.interfaces

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.Player
import pt.isel.pdm.domain.User
import pt.isel.pdm.dto.Player.PlayerInfoIn
import pt.isel.pdm.match.viewModels.MatchState

interface MatchStateProvider {
    val matchState: StateFlow<MatchState>
    val player: StateFlow<User?>
}