package pt.isel.pdm.domain.state

import pt.isel.pdm.domain.DomainError
import pt.isel.pdm.domain.State
import pt.isel.pdm.domain.user.User

sealed interface UserScreenState : State {
    data object Idle : UserScreenState
    data object UserLoggedOut : UserScreenState
    data class UserLoggIn(val user: User) : UserScreenState
    data object CreatingUser : UserScreenState

}


sealed class UserError(override val message: String?) : DomainError {
    data object NoError : UserError(null)
    data object UserNotFound : UserError("Users not found")
    data object ErrorLogin : UserError("Login not possible")
    data object ErrorCreateUser : UserError("Create user not possible")
    data class UsersApiError(override val message: String?) : UserError(message)
    data object NetworkError: UserError("NetWorkError")
}
