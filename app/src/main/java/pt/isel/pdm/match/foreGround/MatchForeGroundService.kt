package pt.isel.pdm.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.utils.onOutCome

class MatchForegroundService : Service() {
    val appConfiguration by lazy { (application as DependenciesContainer) }

    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private val  matchService = appConfiguration.matchService
    private var matchId: Int? = null

    companion object {
        const val CHANNEL_ID = "match_service_channel"
        const val NOTIFICATION_ID = 1
        const val EXTRA_MATCH_ID = "match_id"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        matchId = intent?.getIntExtra(EXTRA_MATCH_ID, -1)
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        if (matchId != null) {
            startListeningToMatch()
        }
        return START_STICKY
    }

    private fun startListeningToMatch() {
        serviceScope.launch {
            val matchId = matchId ?: return@launch
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
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Match em andamento")
            .setContentText("A sincronizar dados do match...")
            //.setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_LOW)
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
