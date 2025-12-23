package pt.isel.pdm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import pt.isel.pdm.orientation.OrientationType
import pt.isel.pdm.orientation.ScreenOrientation
import kotlin.reflect.KClass

inline fun <reified T : Any> NavGraphBuilder.composableWithOrientation(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable (NavBackStackEntry) -> Unit
) {
    val orientationType = T::class.findOrientation()
    composable<T>(deepLinks = deepLinks) { backStackEntry ->
        orientationType.HandleScreenOrientation()
        content(backStackEntry)
    }
}

fun KClass<*>.findOrientation(): OrientationType {
    val annotation = this.java.getAnnotation(ScreenOrientation::class.java)
    return annotation?.orientation ?: OrientationType.FREE
}
