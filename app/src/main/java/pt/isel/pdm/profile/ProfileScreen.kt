package pt.isel.pdm.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp
import pt.isel.pdm.ui.topBar.TopBarConfig
import pt.isel.pdm.user.CreateUserScreen
import pt.isel.pdm.user.LoginScreen
import pt.isel.pdm.user.UserError
import pt.isel.pdm.user.UserScreenState
import pt.isel.pdm.user.UserViewModel
import pt.isel.pdm.user.ViewUserCreateStateFull
import pt.isel.pdm.user.ViewUserLoginStateFull

@Composable
fun  ProfileScreenContent(viewModel: ProfileViewModel,onTitleScreen: () -> Unit) {

    val stateUi = viewModel.stateUi.collectAsState().value

    when (stateUi) {

        is UserScreenState.CreatingUser -> ViewUserCreateStateFull(
            onCreateUser = {viewModel.createUser(it)},
            topBarConfig = TopBarConfig.WithBack("Create User") {
                viewModel.navigateTo(
                    UserScreenState.UserLoggedOut
                )
            },
            createView = { state, actions->
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
            onSignUp = {viewModel.navigateTo(UserScreenState.CreatingUser)},
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
private fun ProfileScreenError(viewModel: ProfileViewModel) {
    when (val stateError = viewModel.errorState.collectAsState().value) {
        UserError.ErrorCreateUser -> TODO()
        UserError.ErrorLogin -> TODO()
        UserError.NoError -> TODO()
        UserError.UserNotFound -> TODO()
    }
}
