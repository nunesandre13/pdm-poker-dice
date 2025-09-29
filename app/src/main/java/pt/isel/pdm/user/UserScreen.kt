package pt.isel.pdm.user

import pt.isel.pdm.profile.ProfileScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable
fun UserScreen(viewModel: UserViewModel, goBack: () -> Unit) {

    val stateUi = viewModel.stateUi.collectAsState().value
    when (stateUi) {

        is UserScreenState.CreatingUser -> UserFormView(config = viewModel.createUserConfiguration)

        is UserScreenState.Idle -> {}

        is UserScreenState.UserLoggIn -> ProfileScreen(onBack = goBack)

        is UserScreenState.UserLoggedOut -> UserFormView(config = viewModel.loginConfiguration)

    }
}