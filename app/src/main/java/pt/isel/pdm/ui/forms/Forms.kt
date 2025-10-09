package pt.isel.pdm.ui.forms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import pt.isel.pdm.domain.Email
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.Password
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.ui.clickable.ClickableIcon

@Composable
fun EmailForm(
    email: EmailInput?,
    onEmailChange: (EmailInput) -> Unit
) {
    OutlinedTextField(
        value = email?.email ?: "",
        onValueChange = { newValue ->
            onEmailChange(EmailInput(newValue))
        },
        label = { Text("Email") },
        placeholder = { Text("nome@exemplo.com") }
    )
}

@Composable
fun PasswordForm(
    password: PasswordInput?,
    onPasswordChange: (PasswordInput) -> Unit,
    showPassword: Boolean,
    onShowPasswordChange: () -> Unit
) {
    OutlinedTextField(
        value = password?.passwordInput ?: "",
        onValueChange = { newValue ->
            onPasswordChange(PasswordInput(newValue))
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        label = { Text("Password") },
        trailingIcon = {
            ClickableIcon(
                icon = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                contentDescription = if (showPassword) "Show" else "NotShow",
                onClick = onShowPasswordChange
            )
        }
    )
}


@Composable
fun NamerForm(
    name: NameInput?,
    onNameChange: (NameInput) -> Unit
) {
    OutlinedTextField(
        value = name?.name ?: "",
        onValueChange = { newValue ->
            onNameChange(NameInput(newValue))
        },
        label = { Text("Name") }
    )
}

