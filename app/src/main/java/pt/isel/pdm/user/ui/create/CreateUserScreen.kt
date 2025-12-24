package pt.isel.pdm.user.ui.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.chelasmulti_playerpokerdice.R
import pt.isel.pdm.domain.user.InviteCode
import pt.isel.pdm.domain.inputs.EmailInput
import pt.isel.pdm.domain.inputs.InviteInput
import pt.isel.pdm.domain.inputs.NameInput
import pt.isel.pdm.domain.inputs.PasswordInput
import pt.isel.pdm.ui.background.DefaultBackGround
import pt.isel.pdm.ui.forms.EmailForm
import pt.isel.pdm.ui.forms.InviteForm
import pt.isel.pdm.ui.forms.NamerForm
import pt.isel.pdm.ui.forms.PasswordForm
import pt.isel.pdm.ui.topBar.TopBarConfig

@Composable
fun CreateUserScreen(
    topBarConfig: TopBarConfig,
    email: EmailInput? = null,
    onEmailChange: (EmailInput) -> Unit = {},
    userName: NameInput? = null,
    onUserNameChange: (NameInput) -> Unit = {},
    password: PasswordInput? = null,
    inviteCode: InviteInput? =null,
    onInviteChange:(InviteInput)-> Unit = {},
    onPasswordChange: (PasswordInput) -> Unit = {},
    showPassword: Boolean = false,
    onShowPassword: () -> Unit = {},
    onCreateUser: () -> Unit = {},
    emailError: Boolean = false,
    passwordError: Boolean = false
) {
    DefaultBackGround(
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text =  stringResource(R.string.createAccout),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.createAccoutFields),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(18.dp))
                NamerForm(name = userName, onNameChange = onUserNameChange)
                Spacer(modifier = Modifier.height(12.dp))
                EmailForm(email = email, emailError, onEmailChange = onEmailChange)
                Spacer(modifier = Modifier.height(12.dp))

            }
        },
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PasswordForm(
                    password = password,
                    onPasswordChange = onPasswordChange,
                    showPassword = showPassword,
                    onShowPasswordChange = onShowPassword,
                    error = passwordError
                )
                InviteForm(invite = inviteCode, onInviteCodeChange = onInviteChange)
            }
        },
        {
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {onCreateUser() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(text = stringResource(R.string.createAccout))
                    }
                }
            }
        },
        topBarConfig = topBarConfig,
        modifier = Modifier
    )
}

@Composable
@Preview
fun CreateUserScreenPreview() {
    CreateUserScreen(
        topBarConfig = TopBarConfig.Simple("Create"),
        email = EmailInput("hello@ex.com"),
        userName = NameInput("User"),
        password = PasswordInput("password"),
        onEmailChange = {},
        onUserNameChange = {},
        onPasswordChange = {},
        showPassword = false,
        onShowPassword = {},
        onCreateUser = {},
        emailError = false,
        passwordError = false,
        inviteCode = InviteInput("")
    )
}
