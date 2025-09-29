package pt.isel.pdm.login

import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.profile.ProfileScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import pt.isel.pdm.domain.User
import pt.isel.pdm.ui.topBar.TopBarConfig


@Composable
fun UserScreen(viewModel: UserViewModel, goBack: () -> Unit) {
    when (val stateUi = viewModel.stateUi.collectAsState().value) {
        UserScreenState.CreatingUser ->
            CreateUserScreen(
                topBarConfig = TopBarConfig.WithBack(
                    title = "Create Account",
                    onBack = goBack
                ),
                createUser = { name, email, password ->
                    viewModel.createUser(viewModel.user)
                }
            )

        UserScreenState.Idle -> {}


        is UserScreenState.UserLoggIn ->
            ProfileScreen(
               onBack = goBack
            )

        UserScreenState.UserLoggedOut ->
            LoginScreen(
                onBack = goBack,
                login = { email, password ->
                viewModel.login(viewModel.user)
            }
        )

    }
}