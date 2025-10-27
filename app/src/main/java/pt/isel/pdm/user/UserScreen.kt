package pt.isel.pdm.user

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.services.UsersServiceMock

@Composable
fun UserScreen(viewModel: UserViewModel, onTitleScreen: () -> Unit) {
    UserScreenContent(viewModel,onTitleScreen)
    UserScreenError(viewModel)
}

@Composable
fun UserScreenContent(viewModel: UserViewModel,onTitleScreen: () -> Unit) {

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
            {viewModel.createUser()}
        )

        is UserScreenState.Idle -> {}

        is UserScreenState.UserLoggIn -> onTitleScreen()

        is UserScreenState.UserLoggedOut -> ViewUserLoginStateFull(
            onLogin = { viewModel.login(it) },
            topBarConfig = TopBarConfig.Simple("Login"),
            loginView = { state, actions ->
                LoginScreen(
                    topBarConfig = state.topBarConfig,
                    email = state.email,
                    password = state.password,
                    onEmailChange = actions.onEmailChange,
                    onPasswordChange = actions.onPasswordChange,
                    showPassword = state.showPassword,
                    onShowPassword = actions.onShowPassword,
                    login = actions.onLogin,
                    onSignUp = actions.onSignUp
                )
            }
        )
    }
}


@Composable
private fun UserScreenError(viewModel: UserViewModel) {
    when (val stateError = viewModel.errorState.collectAsState().value) {
        is UserError.NoError -> {}
        is UserError.ErrorLogin -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
        is UserError.ErrorCreateUser -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
        UserError.UserNotFound -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun UserScreenPreview() {
    UserScreen(
        UserViewModel(UsersServiceMock()),
        onTitleScreen = {}
    )
}