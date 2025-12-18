package pt.isel.pdm.match.foreGround

import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun RememberForegroundService(
    matchId: Int,
    serviceClass: Class<out Service> = MatchForegroundService::class.java
) {
    val context = LocalContext.current

    // apenas para versoes android 13 + ,pedir ao utilizador para permitir notificacoes,
    // futuramente verificar se a permissao ja foi consedida, estamos a usar uma versao superior
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {

            }
        }

        LaunchedEffect(Unit) {
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    DisposableEffect(matchId) {
        val intent = Intent(context, serviceClass).apply {
            putExtra(MatchForegroundService.EXTRA_MATCH_ID, matchId)
        }
        // verificacao do tipo de android, android 8 + necessita de startForeGrounService
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
