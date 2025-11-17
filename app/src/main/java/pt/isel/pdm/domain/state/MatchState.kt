package pt.isel.pdm.domain.state

import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.State


sealed interface MatchScreenState : State {
}

sealed class MatchError(override val message: String?): DomainError {
    data object SomeError: MatchError(null)
    data object InvalidPlay: MatchError(null)
}