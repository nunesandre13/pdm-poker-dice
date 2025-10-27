package pt.isel.pdm.user

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.forms.EmailForm
import pt.isel.pdm.ui.forms.PasswordForm
import pt.isel.pdm.ui.topBar.TopBarConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    topBarConfig: TopBarConfig,
    email: EmailInput? = null,
    password: PasswordInput? = null,
    onEmailChange: (EmailInput) -> Unit = {},
    onPasswordChange: (PasswordInput) -> Unit = {},
    showPassword: Boolean = false,
    onShowPassword : () -> Unit = {},
    login: () -> Unit = {},
    onSignUp: () -> Unit = {},
) {
    DefaultBackGround(

        {
            EmailForm(email = email, onEmailChange = onEmailChange)
        },
        {
            PasswordForm(
                password = password,
                onPasswordChange = onPasswordChange,
                showPassword = showPassword,
                onShowPasswordChange = { onShowPassword() }
            )
        },
        {
            Button(onClick = {
                login()
            }) {
                Text(text = "Login")
            }
            Button(onClick = {
                onSignUp()
            }) {
                Text(text = "Sign up")
            }

        },
        topBarConfig=topBarConfig,
        modifier = Modifier

    )
}


@Composable
@Preview
fun LoginScreenPreview() {
    LoginScreen(
        topBarConfig = TopBarConfig.WithBack("Login", {}),
        email = EmailInput("ola"),
        password = PasswordInput("ola"),
        onEmailChange = {},
        onPasswordChange = {},
        showPassword = true,
        onShowPassword = {},
        login = {},
        onSignUp = {})
}