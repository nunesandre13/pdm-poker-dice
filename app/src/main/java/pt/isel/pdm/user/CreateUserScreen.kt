package pt.isel.pdm.user

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.forms.EmailForm
import pt.isel.pdm.ui.forms.NamerForm
import pt.isel.pdm.ui.forms.PasswordForm
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme
import pt.isel.pdm.ui.topBar.TopBarConfig
import java.lang.IllegalStateException
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(
    topBarConfig: TopBarConfig,
    email: Email? = null,
    onEmailChange: (Email) -> Unit = {},
    userName: Name? = null,
    onUserNameChange: (Name) -> Unit = {},
    password: Password? = null,
    onPasswordChange: (Password) -> Unit = {},
    showPassword: Boolean = false,
    onShowPassword: () -> Unit = {},
    onCreateUser: (user: UserCreate) -> Unit = {}
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
                onCreateUser(
                    UserCreate(
                        name = userName ?: throw IllegalStateException(),
                        email = email ?: throw IllegalStateException(),
                        password = password ?: throw IllegalStateException()
                    )
                )
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