package pt.isel.pdm.profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import pt.isel.pdm.domain.state.ProfileError
import pt.isel.pdm.domain.state.ProfileScreenState
import pt.isel.pdm.profile.viewmodel.ProfileViewModel
import pt.isel.pdm.ui.errorPresentation.ErrorPopUp

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onLogOut: () -> Unit) {
    ProfileScreenContent(viewModel, onLogOut)
    ProfileScreenError(viewModel)
}

@Composable
fun  ProfileScreenContent(viewModel: ProfileViewModel, onLogOut: () -> Unit) {
    when (val stateUi = viewModel.stateUi.collectAsState().value) {
        ProfileScreenState.Idle -> {}
        ProfileScreenState.LoggedOut -> onLogOut()
        is ProfileScreenState.OnProfileView -> ProfileView(
            user = stateUi.user,
            onBack = {},
            onLogOut={ viewModel.logout() },
            onGenerateInviteCode = {viewModel.generateInviteCode() }
        )
    }
}

@Composable
private fun ProfileScreenError(viewModel: ProfileViewModel) {
    when (val stateError = viewModel.errorState.collectAsState().value) {
        ProfileError.LogoutError, ProfileError.InviteCodeError -> ErrorPopUp(stateError){
            viewModel.dismissError()
        }
        ProfileError.NoError -> {}
    }
}
