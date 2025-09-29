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
    when (viewModel.stateUi.collectAsState().value) {
        UserScreenState.CreatingUser ->
            CreateUserScreen(
                topBarConfig = TopBarConfig.WithBack(
                    title = "Create Account",
                    onBack = goBack
                ),
                createUser = { name, email, password ->
                    viewModel.createUser(
                        UserCreate(
                            name = Name(name.name),
                            email=email,
                            password=password)
                    )
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
                viewModel.login(
                    UserLogin(
                        email = email,
                        password = password,
                    )
                )
            }
        )

    }
}