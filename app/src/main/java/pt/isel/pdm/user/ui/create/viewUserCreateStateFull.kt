package pt.isel.pdm.user.ui.create

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pt.isel.pdm.domain.InviteCode
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.InviteInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.domain.toEmail
import pt.isel.pdm.domain.toInviteCode
import pt.isel.pdm.domain.toName
import pt.isel.pdm.domain.toPassword
import pt.isel.pdm.dto.user.UserInput
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.utils.presentError

data class CreateState(
    val topBarConfig: TopBarConfig,
    val name: NameInput?,
    val email: EmailInput?,
    val password: PasswordInput?,
    val inviteCode: InviteInput?,
    val showPassword: Boolean,
    val emailError: Boolean,
    val passwordError: Boolean
)
data class CreateActions(
    val onNameChange: (NameInput) -> Unit,
    val onEmailChange: (EmailInput) -> Unit,
    val onPasswordChange: (PasswordInput) -> Unit,
    val onInviteCodeChange:(InviteInput) -> Unit,
    val onShowPassword: () -> Unit,

    val onCreateUser: () -> Unit
)

// Typealias para a View
typealias CreateView = @Composable (
    state: CreateState,
    actions: CreateActions
) -> Unit

@Composable
fun ViewUserCreateStateFull(
    onCreateUser: (UserInput, InviteCode) -> Unit,
    createView: CreateView,
    topBarConfig: TopBarConfig,
) {
    var name by remember { mutableStateOf<NameInput?>(null) }
    var email by remember { mutableStateOf<EmailInput?>(null) }
    var password by remember { mutableStateOf<PasswordInput?>(null) }
    var inviteCode by remember { mutableStateOf<InviteInput?>(null) }
    var showPassword by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val state = CreateState(
        topBarConfig = topBarConfig,
        name = name,
        email = email,
        password = password,
        inviteCode = inviteCode,
        showPassword = showPassword,
        emailError = emailError,
        passwordError = passwordError
    )

    val actions = CreateActions(
        onNameChange = { name = it },
        onEmailChange = {
            email = it
            emailError = false },
        onPasswordChange = {
            password = it
            passwordError = false },
        onInviteCodeChange = {
            inviteCode = it
                             },
        onShowPassword = { showPassword = !showPassword },
        onCreateUser = {
            if (name != null && email != null && password != null && inviteCode!= null) {
                val myName = name?.toName() ?: presentError {
                    return@CreateActions
                }
                val myEmail = email?.toEmail() ?: presentError {
                    emailError = true
                    return@CreateActions
                }
                val myPassword = password?.toPassword() ?: presentError {
                    passwordError = true
                    return@CreateActions
                }
                val myInviteCode= inviteCode?.toInviteCode()
                Log.d("USER_DATA_CHECK", "Name: ${myName.name}, Email: ${myEmail.email}, InviteCode: ${myInviteCode!!.code}, Password: ${myPassword.password}")
                onCreateUser(UserInput(myName.name, myEmail.email, myPassword.password), InviteCode(myInviteCode!!.code))
            }
        }
    )
    createView(state, actions)
}

