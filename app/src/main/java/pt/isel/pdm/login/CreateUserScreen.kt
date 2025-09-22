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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.ui.ColumnScaffold
import pt.isel.pdm.ui.EmailForm
import pt.isel.pdm.ui.NamerForm
import pt.isel.pdm.ui.PasswordForm
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme
import java.lang.IllegalStateException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateUserScreen(onBack : () -> Unit ,createUser: (userName: Name, email: Email, password: Password) -> Unit) {
    var email by remember { mutableStateOf<Email?>(null) }
    var userName by remember { mutableStateOf<Name?>(null) }
    var password by remember { mutableStateOf<Password?>(null) }
    var showPassword by remember { mutableStateOf(false) }

    ColumnScaffold( topBar = {
        TopAppBar(
            title = { Text("Profile") },
            navigationIcon = {
                IconButton(onClick = { onBack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Voltar ao Menu"
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    },
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
            Spacer(modifier = Modifier.height(16.dp))
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
        }
    )
}

@Composable
@Preview
fun CreateUserScreenPreview() {
    ChelasMultiPlayerPokerDiceTheme {
        CreateUserScreen ({}){ _, _, _ -> }
    }
}