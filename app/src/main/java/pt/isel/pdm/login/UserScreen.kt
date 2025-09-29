package pt.isel.pdm.login

import pt.isel.pdm.home.TitleScreen
import pt.isel.pdm.profile.ProfileScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier



@Composable
fun UserScreen(viewModel: UserViewModel, goBack: () -> Unit) {
    when (val stateUi = viewModel.stateUi.collectAsState().value) {

        UserScreenState.Idle -> {
            TitleScreen(
                {},{},{}
            )
        }

        UserScreenState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator() // nAO SEU OQ METER
            }
        }

        is UserScreenState.UserLogged -> {
            ProfileScreen {
                viewModel.logout(stateUi.user)
                goBack()
            }
        }

        UserScreenState.UserLoggedOut -> {
            TitleScreen(
                {},{},{}
            )
        }
    }
}
