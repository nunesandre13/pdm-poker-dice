package pt.isel.pdm.login

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
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.forms.EmailForm
import pt.isel.pdm.ui.forms.NamerForm
import pt.isel.pdm.ui.forms.PasswordForm
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme
import pt.isel.pdm.ui.topBar.TopBarConfig
import java.lang.IllegalStateException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(topBarConfig: TopBarConfig, createUser: (userName: Name, email: Email, password: Password) -> Unit) {
    var email by remember { mutableStateOf<Email?>(null) }
    var userName by remember { mutableStateOf<Name?>(null) }
    var password by remember { mutableStateOf<Password?>(null) }
    var showPassword by remember { mutableStateOf(false) }

    DefaultBackGround(
        {
            NamerForm(name = userName) { userName = it }
        },
        {
            EmailForm(email = email) { email = it }
        },
        {
            PasswordForm(
                password,
                { password = it },
                showPassword,
                { showPassword = !showPassword }
            )
        },
        {
            Button(onClick = {
                createUser(
                    userName ?: throw IllegalStateException(),
                    email ?: throw IllegalStateException(),
                    password ?: throw IllegalStateException()
                )
            }) {
                Text(text = "Criar Conta")
            }
        },
        topBarConfig = topBarConfig
        ,
        modifier = Modifier
    )
}

@Composable
@Preview
fun CreateUserScreenPreview() {
    ChelasMultiPlayerPokerDiceTheme {
        CreateUserScreen (TopBarConfig.Simple("Example")){ _, _, _ -> }
    }
}