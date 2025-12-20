package pt.isel.pdm.match.viewModels.interfaces

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.Round
import pt.isel.pdm.domain.User

interface RoundStateProvider {
    val roundState: StateFlow<Round?>
    val player: StateFlow<User?>
}