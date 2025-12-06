package pt.isel.pdm.ui.forms

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.InviteInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.ui.clickable.ClickableIcon

@Composable
fun EmailForm(
    email: EmailInput?,
    error: Boolean,
    onEmailChange: (EmailInput) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = email?.email ?: "",
        onValueChange = { newValue ->
            onEmailChange(EmailInput(newValue))
        },
        label = { Text("Email") },
        placeholder = { Text("nome@exemplo.com") },
        isError = email == null || error,
        supportingText = { if (error) Text("Email Invalid", color = MaterialTheme.colorScheme.error) },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun EmailFormPreview() {
    EmailForm(
        email = EmailInput("testtetete"),
        error = false,
        onEmailChange = {}
    )
}

@Composable
fun PasswordForm(
    password: PasswordInput?,
    onPasswordChange: (PasswordInput) -> Unit,
    showPassword: Boolean,
    onShowPasswordChange: () -> Unit,
    error: Boolean,
    modifier: Modifier = Modifier
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
        },
        isError = password == null || error,
        supportingText = { if (error) Text("Password Invalid", color = MaterialTheme.colorScheme.error) },
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun NamerForm(
    name: NameInput?,
    onNameChange: (NameInput) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = name?.name ?: "",
        onValueChange = { newValue -> onNameChange(NameInput(newValue)) },
        isError = name == null,
        label = { Text("Name") },
        modifier = modifier.fillMaxWidth()
    )
}


@Composable
fun InviteForm(
    invite: InviteInput?,
    onInviteCodeChange: (InviteInput) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = invite ?: "",
        onValueChange = { newValue ->
            onInviteCodeChange(InviteInput(newValue))
        },
        label = { Text("Invite") },
        modifier = modifier.fillMaxWidth()
    )
}
