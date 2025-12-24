package pt.isel.pdm.user.services


import kotlinx.coroutines.flow.StateFlow
import pt.isel.pdm.domain.user.InviteCode
import pt.isel.pdm.domain.user.User
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.dto.user.UserCreateTokenOutputModel
import pt.isel.pdm.dto.user.UserInput

import pt.isel.pdm.utils.OutCome

interface UserServices {

    val currentUser: StateFlow<User?>
    fun getCurrentUser(): User?

    suspend fun restoreSession(): Boolean
    suspend fun login(user: UserCreateTokenInputModel): OutCome<UserCreateTokenOutputModel, UserError>
    suspend fun logout(): OutCome<Unit, UserError>
    suspend fun createUser(user: UserInput,inviteCode: InviteCode): OutCome<User, UserError>
    suspend fun inviteCode(): InviteCode

}
