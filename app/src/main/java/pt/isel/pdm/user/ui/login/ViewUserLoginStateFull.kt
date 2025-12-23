package pt.isel.pdm.user.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.domain.toEmail
import pt.isel.pdm.domain.toPassword
import pt.isel.pdm.dto.user.UserCreateTokenInputModel
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.utils.presentError
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview



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
    onLogin: (UserCreateTokenInputModel) -> Unit,
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
                onLogin(UserCreateTokenInputModel(myEmail.email,myPassword.password))
            }
        },
        onSignUp = onSignUp
    )
    loginView(state, actions)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ViewUserLoginStateFullPreview() {
    MaterialTheme {
        ViewUserLoginStateFull(
            onLogin = { /* Simula chamada de login */ },
            onSignUp = { /* Navega para registo */ },
            topBarConfig = TopBarConfig.Simple("Login Test"),
            loginView = { state, actions ->
                LoginScreen(
                    topBarConfig = state.topBarConfig,
                    email = state.email,
                    password = state.password,
                    onEmailChange = actions.onEmailChange,
                    onPasswordChange = actions.onPasswordChange,
                    showPassword = state.showPassword,
                    onShowPassword = actions.onShowPassword,
                    login = actions.onLogin,
                    onSignUp = actions.onSignUp,
                    emailError = state.emailError,
                    passwordError = state.passwordError
                )
            }
        )
    }
}