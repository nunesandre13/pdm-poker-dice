package pt.isel.pdm.match.foreGround

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class MatchLifecycleObserver(private val context: Context) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        context.stopService(Intent(context, MatchForegroundService::class.java))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStop(owner: LifecycleOwner) {
        enableForegroundService(context)
    }
}