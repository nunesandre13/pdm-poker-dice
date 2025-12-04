package pt.isel.pdm.match.foreGround

import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import pt.isel.pdm.services.MatchForegroundService

@Composable
fun RememberForegroundService(
    matchId: Int,
    serviceClass: Class<out Service> = MatchForegroundService::class.java
) {
    val context = LocalContext.current

    DisposableEffect(matchId) {
        val intent = Intent(context, serviceClass).apply {
            putExtra(MatchForegroundService.EXTRA_MATCH_ID, matchId)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

        onDispose {
            context.stopService(Intent(context, serviceClass))
        }
    }
}
