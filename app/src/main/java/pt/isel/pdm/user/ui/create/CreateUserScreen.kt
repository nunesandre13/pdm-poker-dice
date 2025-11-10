package pt.isel.pdm.user.ui.create

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.forms.EmailForm
import pt.isel.pdm.ui.forms.NamerForm
import pt.isel.pdm.ui.forms.PasswordForm
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme
import pt.isel.pdm.ui.topBar.TopBarConfig

@Composable
fun CreateUserScreen(
    topBarConfig: TopBarConfig,
    email: EmailInput? = null,
    onEmailChange: (EmailInput) -> Unit = {},
    userName: NameInput? = null,
    onUserNameChange: (NameInput) -> Unit = {},
    password: PasswordInput? = null,
    onPasswordChange: (PasswordInput) -> Unit = {},
    showPassword: Boolean = false,
    onShowPassword: () -> Unit = {},
    onCreateUser: () -> Unit = {},
    emailError: Boolean = false,
    passwordError: Boolean = false
) {
    DefaultBackGround(
        {
            NamerForm(name = userName, onNameChange = onUserNameChange)
        },
        {
            EmailForm(email = email,emailError ,onEmailChange = onEmailChange)
        },
        {
            PasswordForm(
                password = password,
                onPasswordChange = onPasswordChange,
                showPassword = showPassword,
                onShowPasswordChange = onShowPassword,
                error = passwordError
            )
        },
        {
            Button(
                onClick = {
                    onCreateUser()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text(text = "Create Account")
            }
        },
        topBarConfig = topBarConfig,
        modifier = Modifier
    )
}

@Composable
@Preview
fun CreateUserScreenPreview() {
    ChelasMultiPlayerPokerDiceTheme {
        CreateUserScreen (TopBarConfig.Simple("Example"))
    }
}