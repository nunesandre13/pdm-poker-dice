package pt.isel.pdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.isel.pdm.navigation.RootApp
import pt.isel.pdm.ui.theme.ChelasMultiPlayerPokerDiceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChelasMultiPlayerPokerDiceTheme {
                RootApp()
            }
        }
    }
}
