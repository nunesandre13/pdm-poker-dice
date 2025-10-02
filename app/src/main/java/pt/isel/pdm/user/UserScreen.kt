package pt.isel.pdm.user

import android.annotation.SuppressLint
import pt.isel.pdm.profile.ProfileScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.user.services.UsersServiceMock

@Composable
fun UserScreen(viewModel: UserViewModel, goBack: () -> Unit) {

    val stateUi = viewModel.stateUi.collectAsState().value

    when (stateUi) {

        is UserScreenState.CreatingUser -> UserFormView(config = viewModel.createUserConfiguration)

        is UserScreenState.Idle -> {}

        is UserScreenState.UserLoggIn -> ProfileScreen(onBack = goBack, user = stateUi.user)

        is UserScreenState.UserLoggedOut -> UserFormView(config = viewModel.loginConfiguration)

    }
}

@Composable
@Preview
fun UserScreenPreview() {
    UserScreen(viewModel = UserViewModel(UsersServiceMock()), goBack = {})
}