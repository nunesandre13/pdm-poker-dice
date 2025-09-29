package pt.isel.pdm.user

import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.ui.topBar.TopBarConfig


sealed interface UserFormsConfiguration {
    val topBarConfig: TopBarConfig
    data class LoginForm(
        override val topBarConfig: TopBarConfig,
        val email: Email?,
        val onEmailChange: (Email) -> Unit,
        val password: Password?,
        val onPasswordChange: (Password) -> Unit,
        val showPassword: Boolean,
        val onShowPassword: () -> Unit,
        val onLogin: () -> Unit
    ) : UserFormsConfiguration

    data class CreateUserForm(
        override val topBarConfig: TopBarConfig,
        val email: Email?,
        val onEmailChange: (Email) -> Unit,
        val password: Password?,
        val onPasswordChange: (Password) -> Unit,
        val showPassword: Boolean,
        val onShowPassword: () -> Unit,
        val name: Name?,
        val onNameChange: (Name) -> Unit,
        val onCreateUser: () -> Unit
    ) : UserFormsConfiguration
}