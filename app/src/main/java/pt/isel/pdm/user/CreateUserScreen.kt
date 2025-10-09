package pt.isel.pdm.user

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.forms.EmailForm
import pt.isel.pdm.ui.forms.NamerForm
import pt.isel.pdm.ui.forms.PasswordForm
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme
import pt.isel.pdm.ui.topBar.TopBarConfig
import java.lang.IllegalStateException
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
    onCreateUser: () -> Unit = {}
) {
    DefaultBackGround(
        {
            NamerForm(name = userName, onNameChange = onUserNameChange)
        },
        {
            EmailForm(email = email, onEmailChange = onEmailChange)
        },
        {
            PasswordForm(
                password = password,
                onPasswordChange = onPasswordChange,
                showPassword = showPassword,
                onShowPasswordChange = onShowPassword
            )
        },
        {
            Button(onClick = {
                onCreateUser()
            }) {
                Text(text = "Criar Conta")
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
        CreateUserScreen (TopBarConfig.Simple("Example")){
            UserCreate(
                name = Name("Example"),
                email = Email(""),
                password = Password(""),
            )
        }
    }
}