package pt.isel.pdm.user.ui.create

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.domain.toEmail
import pt.isel.pdm.domain.toName
import pt.isel.pdm.domain.toPassword
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.ui.login.presentError

data class CreateState(
    val topBarConfig: TopBarConfig,
    val name: NameInput?,
    val email: EmailInput?,
    val password: PasswordInput?,
    val showPassword: Boolean,
    val emailError: Boolean,
    val passwordError: Boolean
)
data class CreateActions(
    val onNameChange: (NameInput) -> Unit,
    val onEmailChange: (EmailInput) -> Unit,
    val onPasswordChange: (PasswordInput) -> Unit,
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
    onCreateUser: (UserCreate) -> Unit,
    createView: CreateView,
    topBarConfig: TopBarConfig,
) {
    var name by remember { mutableStateOf<NameInput?>(null) }
    var email by remember { mutableStateOf<EmailInput?>(null) }
    var password by remember { mutableStateOf<PasswordInput?>(null) }
    var showPassword by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val state = CreateState(
        topBarConfig = topBarConfig,
        name = name,
        email = email,
        password = password,
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
        onShowPassword = { showPassword = !showPassword },
        onCreateUser = {
            if (name != null && email != null && password != null) {
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
                onCreateUser(UserCreate(myName, myEmail, myPassword))
            }
        }
    )
    createView(state, actions)
}

