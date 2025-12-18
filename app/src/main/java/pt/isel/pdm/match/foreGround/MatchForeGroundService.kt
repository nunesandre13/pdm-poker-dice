package pt.isel.pdm.match.foreGround

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.chelasmulti_playerpokerdice.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pt.isel.pdm.MainActivity
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.utils.onOutCome

class MatchForegroundService : Service() {
    val appConfiguration by lazy { (application as DependenciesContainer) }

    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private val  matchService by lazy {  appConfiguration.matchService }

    companion object {
        const val CHANNEL_ID = "match_service_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        val matchId = matchService.matchIdState.value
        if (matchId != null && matchId != -1) {
            startListeningToMatch()
        }else {
            stopSelf()
        }
        return START_STICKY
    }

    private fun startListeningToMatch() {
        serviceScope.launch {
            val matchId = matchService.matchIdState.value ?: return@launch
            matchService.getMatchUpdate(matchId).collect { outcome ->
                outcome.onOutCome(
                    onSuccess = { matchResponse ->
                        // Processe a resposta do match
                        // Atualize a notificação ou envie broadcast
                    },
                    onFailure = { error ->
                        // Trate o erro
                    }
                )
            }
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Match em andamento")
            .setContentText("A sincronizar dados do match...")
            .setSmallIcon(R.drawable.ficha)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Match Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
