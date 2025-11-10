package pt.isel.pdm.user.ui.login

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
    emailError: Boolean,
    passwordError: Boolean,
    login: () -> Unit = {},
    onSignUp: () -> Unit = {},
) {
    DefaultBackGround(

        {
            EmailForm(email = email, emailError, onEmailChange = onEmailChange)
        },
        {
            PasswordForm(
                password = password,
                onPasswordChange = onPasswordChange,
                showPassword = showPassword,
                onShowPasswordChange = { onShowPassword() },
                error = passwordError
            )
        },
        {
            Button(onClick = {
                    login()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Login")
            }
            Button(
                onClick = {
                    onSignUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.small
            ) {
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
        emailError = false,
        login = {},
        onSignUp = {},
        passwordError = false
    )
}