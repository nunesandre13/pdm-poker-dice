package pt.isel.pdm.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.ui.DefaultBackGround
import pt.isel.pdm.ui.EmailForm
import pt.isel.pdm.ui.Handlers.TopBarConfig
import pt.isel.pdm.ui.NamerForm
import pt.isel.pdm.ui.PasswordForm
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme
import java.lang.IllegalStateException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(topBarConfig: TopBarConfig ,createUser: (userName: Name, email: Email, password: Password) -> Unit) {
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
    )
}

@Composable
@Preview
fun CreateUserScreenPreview() {
    ChelasMultiPlayerPokerDiceTheme {
        CreateUserScreen (TopBarConfig.Simple("Example")){ _, _, _ -> }
    }
}