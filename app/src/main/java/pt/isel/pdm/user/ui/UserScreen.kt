package pt.isel.pdm.user.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.viewmodel.UserError
import pt.isel.pdm.user.viewmodel.UserScreenState
import pt.isel.pdm.user.viewmodel.UserViewModel
import pt.isel.pdm.user.services.UsersServiceMock
import pt.isel.pdm.user.ui.create.CreateUserScreen
import pt.isel.pdm.user.ui.create.ViewUserCreateStateFull
import pt.isel.pdm.user.ui.login.LoginScreen
import pt.isel.pdm.user.ui.login.ViewUserLoginStateFull
import pt.isel.pdm.utils.ViewModelBase

@Composable
fun UserScreen(viewModel: UserViewModel, onTitleScreen: () -> Unit) {
    UserScreenContent(viewModel,onTitleScreen)
    UserScreenError(viewModel)
}

@Composable
fun UserScreenContent(viewModel: UserViewModel, onTitleScreen: () -> Unit) {

    val stateUi = viewModel.stateUi.collectAsState().value

    when (stateUi) {

        is UserScreenState.CreatingUser -> ViewUserCreateStateFull(
            onCreateUser = { viewModel.createUser(it) },
            topBarConfig = TopBarConfig.WithBack("Create User") {
                viewModel.navigateTo(
                    UserScreenState.UserLoggedOut
                )
            },
            createView = { state, actions ->
                CreateUserScreen(
                    topBarConfig = state.topBarConfig,
                    email = state.email,
                    onEmailChange = actions.onEmailChange,
                    userName = state.name,
                    onUserNameChange = actions.onNameChange,
                    password = state.password,
                    onPasswordChange = actions.onPasswordChange,
                    showPassword = state.showPassword,
                    onShowPassword = actions.onShowPassword,
                    onCreateUser = actions.onCreateUser
                )
            }
        )

        is UserScreenState.Idle -> {}

        is UserScreenState.UserLoggIn -> onTitleScreen()

        is UserScreenState.UserLoggedOut -> ViewUserLoginStateFull(
            onLogin = { viewModel.login(it) },
            onSignUp = { viewModel.navigateTo(UserScreenState.CreatingUser) },
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
                    onSignUp = actions.onSignUp,
                    emailError = state.emailError
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
        UserViewModel(
            UsersServiceMock(),
            viewModelBase = ViewModelBase(UserScreenState.Idle, UserError.NoError)
        ),
        onTitleScreen = {}
    )
}