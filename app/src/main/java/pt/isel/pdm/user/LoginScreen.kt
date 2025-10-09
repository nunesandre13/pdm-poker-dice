package pt.isel.pdm.user

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Password
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.column.ColumnScaffold
import pt.isel.pdm.ui.forms.EmailForm
import pt.isel.pdm.ui.forms.PasswordForm
import pt.isel.pdm.ui.topBar.TopBarConfig
import java.lang.IllegalStateException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    topBarConfig: TopBarConfig,
    email: Email? = null,
    password: Password? = null,
    onEmailChange: (Email) -> Unit = {},
    onPasswordChange: (Password) -> Unit = {},
    showPassword: Boolean = false,
    onShowPassword : () -> Unit = {},
    onBack: () -> Unit = {},
    login: (UserLogin) -> Unit = {},
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
                login(
                    UserLogin(
                        email ?: throw IllegalStateException(),
                        password ?: throw IllegalStateException()
                    )
                )
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
        email = Email("ola"),
        password = Password("ola"),
        onEmailChange = {},
        onPasswordChange = {},
        showPassword = true,
        onShowPassword = {},
        onBack = {},
        login = {},
        onSignUp = {})
}