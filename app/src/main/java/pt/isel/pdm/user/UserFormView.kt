package pt.isel.pdm.user

import androidx.compose.runtime.Composable

@Composable
fun UserFormView(config: UserFormsConfiguration) {
    when (config) {
        is UserFormsConfiguration.CreateUserForm -> CreateUserScreen(
            topBarConfig = config.topBarConfig,
            email = config.email,
            onEmailChange = config.onEmailChange,
            userName = config.name,
            onUserNameChange = config.onNameChange,
            password = config.password,
            onPasswordChange = config.onPasswordChange,
            showPassword = config.showPassword,
            onShowPassword = config.onShowPassword,
            onCreateUser = {
                config.onCreateUser()
            }
        )
        is UserFormsConfiguration.LoginForm -> LoginScreen(
            email = config.email,
            password = config.password,
            onEmailChange = config.onEmailChange,
            onPasswordChange = config.onPasswordChange,
            showPassword = config.showPassword,
            onShowPassword = config.onShowPassword,
            login = { config.onLogin() },
            onSignUp = { config.onSignUp() }
        )
    }
}
