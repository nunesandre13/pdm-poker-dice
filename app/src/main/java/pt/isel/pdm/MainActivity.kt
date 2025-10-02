package pt.isel.pdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.configuration.MockConfiguration
import pt.isel.pdm.navigation.RootApp
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme

class MainActivity : ComponentActivity() {
    val appConfiguration by lazy { (application as DependenciesContainer) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChelasMultiPlayerPokerDiceTheme {
                RootApp(appConfiguration)
            }
        }
    }
}
