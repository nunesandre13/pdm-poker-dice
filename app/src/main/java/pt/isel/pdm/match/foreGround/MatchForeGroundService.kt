package pt.isel.pdm.match.foreGround

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.core.app.NotificationCompat
import com.example.chelasmulti_playerpokerdice.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import pt.isel.pdm.DeepLinks
import pt.isel.pdm.MainActivity
import pt.isel.pdm.configuration.DependenciesContainer
import pt.isel.pdm.domain.Match
import pt.isel.pdm.domain.MatchId
import pt.isel.pdm.domain.MatchStatus
import pt.isel.pdm.domain.RawMatch
import pt.isel.pdm.domain.RoundState
import pt.isel.pdm.domain.events.MatchResponse
import pt.isel.pdm.domain.toPlayerInfo
import pt.isel.pdm.utils.onOutCome
import kotlin.time.Duration.Companion.seconds

class MatchForegroundService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private val appConfiguration by lazy { (application as DependenciesContainer) }
    private val matchService by lazy { appConfiguration.matchService }
    private val localPlayerId by lazy { appConfiguration.userServices.currentUser.value?.toPlayerInfo() }

    companion object {
        const val CHANNEL_ID = "match_service_channel"
        const val NOTIFICATION_ID = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("Waiting for the game to Start")
        startForeground(
            NOTIFICATION_ID,
            notification,
            FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
        matchService.matchIdState.value?.let { startListeningToMatch() } ?: stopSelf()
        return START_STICKY
    }

    private fun startListeningToMatch() {
        serviceScope.launch {
            val matchId = matchService.matchIdState.value ?: return@launch
            matchService.getMatchUpdate(MatchId(matchId)).collect { outcome ->
                outcome.onOutCome(
                    onSuccess = { matchResponse ->
                        val message = determineNotificationMessage(
                            (matchResponse as? MatchResponse.NewMatch)?.newMatch
                        )
                        updateNotification(message)
                        if (matchResponse is MatchResponse.MatchEnded) {
                            serviceScope.launch {
                                delay(5.seconds)
                                stopSelf()
                            }
                        }
                    },
                    onFailure = {}
                )
            }
        }
    }

    private fun determineNotificationMessage(match: RawMatch?): String {
        return when {
            match?.matchStatus == MatchStatus.FINISHED -> "Match Ended Go See the results."
            (match?.actualRound?.state as? RoundState.Rolling)?.turn?.playerId == localPlayerId?.id -> "Your Turn To Player!!!!"
            match?.actualRound?.state is RoundState.Betting -> "Betting Phase is Going"
            else -> "Match OnGoing"
        }
    }

    private fun updateNotification(contentText: String) {
        val notification = createNotification(contentText)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotification(contentText: String): Notification {
        val matchId = matchService.matchIdState.value
        val deepLinkUri = if (matchId != null) {
            DeepLinks.createMatchUri(matchId)
        } else {
            DeepLinks.createLobbyUri()
        }
        val intent = Intent(
            Intent.ACTION_VIEW,
            deepLinkUri,
            this,
            MainActivity::class.java
        )

        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Poker Dice Match")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ficha)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Match Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}