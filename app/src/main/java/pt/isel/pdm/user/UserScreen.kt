package pt.isel.pdm.user

import android.annotation.SuppressLint
import pt.isel.pdm.profile.ProfileScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.services.UsersServiceMock

@Composable
fun UserScreen(viewModel: UserViewModel,onTitleScreen: () -> Unit) {

    val stateUi = viewModel.stateUi.collectAsState().value
    val name = viewModel.name.collectAsState().value
    val password = viewModel.password.collectAsState().value
    val showPassword = viewModel.showPassword.collectAsState().value
    val email = viewModel.email.collectAsState().value

    when (stateUi) {

        is UserScreenState.CreatingUser -> CreateUserScreen(
            TopBarConfig.Simple("Login"),
            email,
            {viewModel.onEmailChange(it)},
            name,
            {viewModel.onNameChange(it)},
            password,
            {viewModel.onPasswordChange(it)},
            showPassword,
            {viewModel.onShowPassword()},
            {viewModel.createUser(it)}
        )

        is UserScreenState.Idle -> {}

        is UserScreenState.UserLoggIn -> onTitleScreen()

        is UserScreenState.UserLoggedOut -> LoginScreen(
            email,
            password,
            onEmailChange = { viewModel.onEmailChange(it) },
            onPasswordChange = { viewModel.onPasswordChange(it) },
            showPassword = showPassword,
            onShowPassword = { viewModel.onShowPassword() },
            onBack = {},
            login = { viewModel.login(it) },
            onSignUp = { viewModel.navigateTo(UserScreenState.CreatingUser) }
        )
    }
}

@Composable
@Preview
fun UserScreenPreview() {
    UserScreen(viewModel = UserViewModel(UsersServiceMock()),{})
}