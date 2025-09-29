package pt.isel.pdm.user

import pt.isel.pdm.profile.ProfileScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import pt.isel.pdm.domain.Name
import pt.isel.pdm.domain.UserCreate
import pt.isel.pdm.domain.UserLogin
import pt.isel.pdm.ui.topBar.TopBarConfig


@Composable
fun UserScreen(viewModel: UserViewModel, goBack: () -> Unit) {
    val stateUi = viewModel.stateUi.collectAsState().value

    when (stateUi) {
        is UserScreenState.CreatingUser ->
            CreateUserScreen(
                topBarConfig = TopBarConfig.WithBack(
                    title = "Create Account",
                    onBack = goBack
                ),
                createUser = { user ->
                    viewModel.createUser(user)
                }
            )

        is UserScreenState.Idle -> {}

        is UserScreenState.UserLoggIn ->
            ProfileScreen(
               onBack = goBack
            )

        is UserScreenState.UserLoggedOut ->
            LoginScreen(
                onBack = goBack,
                login = { viewModel.login(it) }
        )
    }
}