package pt.isel.pdm.match.viewModels.interfaces

import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.Match

interface MatchStateProvider {
    val matchState: StateFlow<Match>
}