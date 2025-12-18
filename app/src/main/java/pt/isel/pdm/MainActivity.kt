package pt.isel.pdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ProcessLifecycleOwner
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.match.foreGround.GrantPermission
import pt.isel.pdm.match.foreGround.MatchLifecycleObserver
import pt.isel.pdm.navigation.RootApp
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme

class MainActivity : ComponentActivity() {
    val appConfiguration by lazy { (application as DependenciesContainer) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(MatchLifecycleObserver(this))
        enableEdgeToEdge()
        setContent {
            ChelasMultiPlayerPokerDiceTheme {
                GrantPermission()
                RootApp(appConfiguration)
            }
        }
    }
}
