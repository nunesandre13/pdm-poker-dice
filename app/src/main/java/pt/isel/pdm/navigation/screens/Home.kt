package pt.isel.pdm.navigation.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.navigation.NavigationEvent
import pt.isel.pdm.navigation.Screens
import pt.isel.pdm.navigation.composableWithOrientation
import pt.isel.pdm.user.ui.UserScreen
import pt.isel.pdm.user.viewmodel.UserViewModel

fun NavGraphBuilder.home(
    appConfiguration: DependenciesContainer,
    onNavigate: (NavigationEvent) -> Unit
){
    composableWithOrientation<Screens.Home> { backStackEntry ->
        val userVm: UserViewModel = viewModel(
            viewModelStoreOwner = backStackEntry,
            factory = UserViewModel.factory(appConfiguration.userServices)
        )
        UserScreen(
            viewModel = userVm,
            onTitleScreen = { onNavigate(NavigationEvent.Title) }
        )
    }
}