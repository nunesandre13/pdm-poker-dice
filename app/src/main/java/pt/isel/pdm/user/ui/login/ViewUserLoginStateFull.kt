package pt.isel.pdm.user.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.domain.toEmail
import pt.isel.pdm.domain.toPassword
import pt.isel.pdm.ui.topBar.TopBarConfig


data class LoginState(
    val topBarConfig: TopBarConfig,
    val email: EmailInput?,
    val password: PasswordInput?,
    val showPassword: Boolean,
    val emailError: Boolean,
    val passwordError: Boolean
)

data class LoginActions(
    val onEmailChange: (EmailInput) -> Unit,
    val onPasswordChange: (PasswordInput) -> Unit,
    val onShowPassword: () -> Unit,
    val onLogin: () -> Unit,
    val onSignUp: () -> Unit
)

// Novo typealias para a View
typealias LoginView = @Composable (
    state: LoginState,
    actions: LoginActions
) -> Unit

@Composable
fun ViewUserLoginStateFull(
    onLogin: (UserLogin) -> Unit,
    onSignUp: () -> Unit,
    loginView: LoginView,
    topBarConfig: TopBarConfig,
) {
    var email by remember { mutableStateOf<EmailInput?>(null) }
    var password by remember { mutableStateOf<PasswordInput?>(null) }
    var showPassword by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val state = LoginState(
        topBarConfig = topBarConfig,
        email = email,
        password = password,
        showPassword = showPassword,
        emailError = emailError,
        passwordError = passwordError
    )

    val actions = LoginActions(
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onShowPassword = { showPassword = !showPassword },
        onLogin = {
            if (email != null && password != null) {
                val myEmail = email?.toEmail() ?: presentError {
                    emailError = true
                    return@LoginActions
                }
                val myPassword = password?.toPassword() ?: presentError {
                    emailError = true
                    return@LoginActions
                }
                onLogin(UserLogin(myEmail,myPassword))
            }
        },
        onSignUp = onSignUp
    )
    loginView(state, actions)
}


inline fun presentError(onError: () -> Nothing): Nothing {
    onError()
}
