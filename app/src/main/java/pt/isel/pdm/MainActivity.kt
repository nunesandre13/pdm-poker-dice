package pt.isel.pdm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.match.foreGround.GrantPermission
import pt.isel.pdm.match.foreGround.MatchForegroundService
import pt.isel.pdm.match.foreGround.enableForegroundService
import pt.isel.pdm.navigation.RootApp
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme

class MainActivity : ComponentActivity() {
    val appConfiguration by lazy { (application as DependenciesContainer) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChelasMultiPlayerPokerDiceTheme {
                GrantPermission()
                RootApp(appConfiguration)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        stopService(Intent(this, MatchForegroundService::class.java))
    }

    override fun onStop() {
        super.onStop()
        enableForegroundService(this)
    }
}
