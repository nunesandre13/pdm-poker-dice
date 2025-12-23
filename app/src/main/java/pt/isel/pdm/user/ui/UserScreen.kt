package pt.isel.pdm.user.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import pt.isel.pdm.domain.state.UserScreenState
import pt.isel.pdm.domain.state.UserError
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.viewmodel.UserViewModel
import pt.isel.pdm.user.ui.create.CreateUserScreen
import pt.isel.pdm.user.ui.create.ViewUserCreateStateFull
import pt.isel.pdm.user.ui.login.LoginScreen
import pt.isel.pdm.user.ui.login.ViewUserLoginStateFull
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.ui.CircularBox
import pt.isel.pdm.user.services.UsersServiceMock
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
            onCreateUser = { userCreated, invite ->
                viewModel.createUser(userCreated,invite)
                           },
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
                    onCreateUser = actions.onCreateUser,
                    emailError = state.emailError,
                    passwordError = state.passwordError,
                    inviteCode = state.inviteCode,
                    onInviteChange = actions.onInviteCodeChange
                )
            }
        )

        is UserScreenState.Idle -> CircularBox()

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
                    emailError = state.emailError,
                    passwordError = state.passwordError
                )
            }
        )
    }
}


@Composable
private fun UserScreenError(viewModel: UserViewModel) {
    when (val stateError = viewModel.errorState.collectAsState().value) {
        is UserError.ErrorLogin, is UserError.NetworkError, is UserError.ErrorCreateUser, is UserError.UserNotFound, is UserError.UsersApiError -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
        is UserError.NoError -> {}
    }
}



@Preview(showBackground = true, showSystemUi = true, name = "Preview Login")
@Composable
fun UserScreenLoginPreview() {
    val userServiceMock = UsersServiceMock()

    // CORREÇÃO: Declarar explicitamente <UserScreenState, UserError>
    val viewModelBase = ViewModelBase<UserScreenState, UserError>(
        initialState = UserScreenState.UserLoggedOut,
        noError = UserError.NoError
    )

    val viewModel = UserViewModel(
        userService = userServiceMock,
        viewModelBase = viewModelBase
    )

    MaterialTheme {
        UserScreen(
            viewModel = viewModel,
            onTitleScreen = { }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserScreenCreatePreview() {
    val userServiceMock = UsersServiceMock()
    val viewModelBase = ViewModelBase<UserScreenState, UserError>(
        initialState = UserScreenState.CreatingUser,
        noError = UserError.NoError
    )

    val viewModel = UserViewModel(
        userService = userServiceMock,
        viewModelBase = viewModelBase
    )

    MaterialTheme {
        UserScreen(
            viewModel = viewModel,
            onTitleScreen = { }
        )
    }
}