package pt.isel.pdm.navigation.screens


import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens
import pt.isel.pdm.profile.ui.ProfileScreen
import pt.isel.pdm.profile.viewmodel.ProfileViewModel

fun NavGraphBuilder.profile(
    appConfiguration: DependenciesContainer,
    onNavigation: (NavigationEvent) -> Unit
) {
    composable<Screens.Profile> { backStackEntry ->
        val profileVm: ProfileViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = ProfileViewModel.factory(appConfiguration.userServices)
        )
        ProfileScreen(
            viewModel = profileVm,
            onLogOut = { onNavigation(NavigationEvent.Navigate(Screens.Home)) },
            onBack = { onNavigation(NavigationEvent.Navigate(Screens.Title)) }
        )
    }
}
