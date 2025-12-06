package pt.isel.pdm.user.services


import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.AuthenticatedUser
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.User
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.domain.state.UserError

import pt.isel.pdm.utils.OutCome

interface UserServices {

    val currentUser: StateFlow<User?>
    fun getCurrentUser(): User?
    suspend fun login(user: UserLogin): OutCome<User, UserError>
    suspend fun logout(): OutCome<Unit, UserError>
    suspend fun createUser(user: UserCreate,inviteCode: InviteCode): OutCome<User, UserError>

    suspend fun inviteCode(user: AuthenticatedUser): InviteCode

}
