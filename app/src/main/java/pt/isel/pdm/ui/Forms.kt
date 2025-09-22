package pt.isel.pdm.ui

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password

@Composable
fun EmailForm(
    email: Email?,
    onEmailChange: (Email) -> Unit
) {
    OutlinedTextField(
        value = email?.email ?: "",
        onValueChange = { newValue ->
            onEmailChange(Email(newValue))
        },
        label = { Text("Email") },
        placeholder = { Text("nome@exemplo.com") }
    )
}

@Composable
fun PasswordForm(
    password: Password?,
    onPasswordChange: (Password) -> Unit,
    showPassword: Boolean,
    onShowPasswordChange: () -> Unit
) {
    MakeRow (
        {
            OutlinedTextField(
                value = password?.password ?: "",
                onValueChange = { newValue ->
                    onPasswordChange(Password(newValue))
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text("Password") }
            )
        },
        {
            Button(onClick = onShowPasswordChange) {
                Text(text = "Show password")
            }
        }
    )
}

@Composable
fun NamerForm(
    name: Name?,
    onNameChange: (Name) -> Unit
) {
    OutlinedTextField(
        value = name?.name ?: "",
        onValueChange = { newValue ->
            onNameChange(Name(newValue))
        },
        label = { Text("Name") }
    )
}

