package pt.isel.pdm.domain.state

import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.State
import pt.isel.pdm.domain.User

sealed class ProfileScreenState: State {
    data object Idle: ProfileScreenState()
    data class OnProfileView(val user: User): ProfileScreenState()
    data object LoggedOut: ProfileScreenState()
}
sealed class ProfileError(override val message: String?): DomainError {
    data object NoError: ProfileError(null)
    data object LogoutError: ProfileError("Error doing the logout")
}